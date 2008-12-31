/*
 * Copyright 2007 (C) Thomas Parker <thpr@users.sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package plugin.lsttokens.add;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.ChoiceSet;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.base.PersistentChoiceActor;
import pcgen.cdom.base.PersistentTransitionChoice;
import pcgen.cdom.base.TransitionChoice;
import pcgen.cdom.choiceset.ReferenceChoiceSet;
import pcgen.cdom.choiceset.SpellCasterChoiceSet;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.reference.CDOMGroupRef;
import pcgen.core.Globals;
import pcgen.core.PCClass;
import pcgen.core.PlayerCharacter;
import pcgen.core.analysis.BonusAddition;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.TokenUtilities;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMSecondaryToken;
import pcgen.util.Logging;

public class SpellCasterToken extends AbstractToken implements
		CDOMSecondaryToken<CDOMObject>, PersistentChoiceActor<PCClass>
{

	private static final Class<PCClass> PCCLASS_CLASS = PCClass.class;

	public String getParentToken()
	{
		return "ADD";
	}

	private String getFullName()
	{
		return getParentToken() + ":" + getTokenName();
	}

	@Override
	public String getTokenName()
	{
		return "SPELLCASTER";
	}

	public boolean parse(LoadContext context, CDOMObject obj, String value)
	{
		int pipeLoc = value.indexOf(Constants.PIPE);
		Formula count;
		String items;
		if (pipeLoc == -1)
		{
			count = FormulaFactory.ONE;
			items = value;
		}
		else
		{
			String countString = value.substring(0, pipeLoc);
			count = FormulaFactory.getFormulaFor(countString);
			if (count.isStatic() && count.resolve(null, "").doubleValue() <= 0)
			{
				Logging.log(Logging.LST_ERROR, "Count in " + getFullName()
								+ " must be > 0");
				return false;
			}
			items = value.substring(pipeLoc + 1);
		}

		if (isEmpty(items) || hasIllegalSeparator(',', items))
		{
			return false;
		}
		StringTokenizer tok = new StringTokenizer(items, Constants.COMMA);

		boolean foundAny = false;
		boolean foundOther = false;

		List<CDOMReference<PCClass>> groups = new ArrayList<CDOMReference<PCClass>>();
		List<CDOMReference<PCClass>> prims = new ArrayList<CDOMReference<PCClass>>();
		List<String> spelltypes = new ArrayList<String>();
		CDOMGroupRef<PCClass> allRef = context.ref.getCDOMAllReference(PCCLASS_CLASS);
		while (tok.hasMoreTokens())
		{
			String token = tok.nextToken();
			if (Constants.LST_ANY.equalsIgnoreCase(token))
			{
				foundAny = true;
				groups.add(allRef);
			}
			else
			{
				foundOther = true;
				if (token.equals("Arcane") || token.equals("Divine")
						|| token.equals("Psionic"))
				{
					spelltypes.add(token);
				}
				else if (token.startsWith(Constants.LST_TYPE_OLD)
						|| token.startsWith(Constants.LST_TYPE))
				{
					CDOMReference<PCClass> ref = TokenUtilities
							.getTypeReference(context, PCCLASS_CLASS, token
									.substring(5));
					if (ref == null)
					{
						Logging.log(Logging.LST_ERROR, "  Error was encountered while parsing "
										+ getFullName()
										+ ": "
										+ token
										+ " is not a valid reference: " + value);
						return false;
					}
					groups.add(ref);
				}
				else
				{
					prims.add(context.ref
							.getCDOMReference(PCCLASS_CLASS, token));
				}
			}
		}

		if (foundAny && foundOther)
		{
			Logging.log(Logging.LST_ERROR, "Non-sensical " + getFullName()
					+ ": Contains ANY and a specific reference: " + value);
			return false;
		}

		ReferenceChoiceSet<PCClass> grcs = groups.isEmpty() ? null
				: new ReferenceChoiceSet<PCClass>(groups);
		ReferenceChoiceSet<PCClass> prcs = prims.isEmpty() ? null
				: new ReferenceChoiceSet<PCClass>(prims);
		ChoiceSet<PCClass> cs = new SpellCasterChoiceSet(allRef, spelltypes, grcs, prcs);
		PersistentTransitionChoice<PCClass> tc = new PersistentTransitionChoice<PCClass>(
				cs, count);
		tc.setTitle("Spell Caster Class Choice");
		context.getObjectContext().addToList(obj, ListKey.ADD, tc);
		tc.setChoiceActor(this);
		return true;

	}

	public String[] unparse(LoadContext context, CDOMObject obj)
	{
		Changes<PersistentTransitionChoice<?>> grantChanges = context
				.getObjectContext().getListChanges(obj, ListKey.ADD);
		Collection<PersistentTransitionChoice<?>> addedItems = grantChanges
				.getAdded();
		if (addedItems == null || addedItems.isEmpty())
		{
			// Zero indicates no Token
			return null;
		}
		List<String> addStrings = new ArrayList<String>();
		for (TransitionChoice<?> container : addedItems)
		{
			ChoiceSet<?> cs = container.getChoices();
			if (PCCLASS_CLASS.equals(cs.getChoiceClass()))
			{
				Formula f = container.getCount();
				if (f == null)
				{
					context.addWriteMessage("Unable to find " + getFullName()
							+ " Count");
					return null;
				}
				StringBuilder sb = new StringBuilder();
				if (!FormulaFactory.ONE.equals(f))
				{
					sb.append(f).append(Constants.PIPE);
				}
				sb.append(cs.getLSTformat());
				addStrings.add(sb.toString());

				// assoc.getAssociation(AssociationKey.CHOICE_MAXCOUNT);
			}
		}
		return addStrings.toArray(new String[addStrings.size()]);
	}

	public Class<CDOMObject> getTokenClass()
	{
		return CDOMObject.class;
	}

	public void applyChoice(CDOMObject owner, PCClass choice, PlayerCharacter pc)
	{
		PCClass theClass = pc.getClassKeyed(choice.getKeyName());

		if (theClass == null)
		{
			pc.incrementClassLevel(0, choice);
			theClass = pc.getClassKeyed(choice.getKeyName());
		}

		BonusAddition.applyBonus("PCLEVEL|" + theClass.getKeyName() + "|1", "",
				pc, owner, false);

		theClass.setLevel(theClass.getLevel(), pc);
	}

	public boolean allow(PCClass choice, PlayerCharacter pc, boolean allowStack)
	{
		return true;
	}

	public PCClass decodeChoice(String s)
	{
		return Globals.getContext().ref.silentlyGetConstructedCDOMObject(
				PCCLASS_CLASS, s);
	}

	public String encodeChoice(Object choice)
	{
		return ((PCClass) choice).getKeyName();
	}

	public void restoreChoice(PlayerCharacter pc, CDOMObject owner,
			PCClass choice)
	{
		// No action required
	}
}
