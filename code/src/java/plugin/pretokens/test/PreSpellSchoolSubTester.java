/*
 * PreSpellSchoolSub.java
 * Copyright 2001 (C) Bryan McRoberts <merton_monk@yahoo.com>
 * Copyright 2003 (C) Chris Ward <frugal@purplewombat.co.uk>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	   See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on November 28, 2003
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 *
 */
package plugin.pretokens.test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.SpellSubSchool;
import pcgen.cdom.graph.PCGenGraph;
import pcgen.cdom.graph.PCGraphEdge;
import pcgen.character.CharacterDataStore;
import pcgen.core.PlayerCharacter;
import pcgen.core.prereq.AbstractPrerequisiteTest;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteException;
import pcgen.core.prereq.PrerequisiteTest;
import pcgen.core.spell.Spell;
import pcgen.util.PropertyFactory;

/**
 * @author wardc
 *
 */
public class PreSpellSchoolSubTester extends AbstractPrerequisiteTest implements
		PrerequisiteTest
{

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#passes(pcgen.core.PlayerCharacter)
	 */
	@Override
	public int passes(final Prerequisite prereq, final PlayerCharacter character)
	{
		final String subSchool = prereq.getKey();
		final int requiredLevel = Integer.parseInt(prereq.getSubKey());
		final int requiredNumber = Integer.parseInt(prereq.getOperand());

		final List<Spell> aArrayList =
				character
					.aggregateSpellList(
						"Any", "No-Match", subSchool, "No-Match", requiredLevel, 20); //$NON-NLS-1$ //$NON-NLS-2$

		final int runningTotal =
				prereq.getOperator().compare(aArrayList.size(), requiredNumber);
		return countedTotal(prereq, runningTotal);
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#kindsHandled()
	 */
	public String kindHandled()
	{
		return "SPELL.SUBSCHOOL"; //$NON-NLS-1$
	}

	/* (non-Javadoc)
	 * @see pcgen.core.prereq.PrerequisiteTest#toHtmlString(pcgen.core.prereq.Prerequisite)
	 */
	@Override
	public String toHtmlString(final Prerequisite prereq)
	{
		return PropertyFactory.getFormattedString(
			"PreSpellSchoolSub.toHtml_spell_sub_school", //$NON-NLS-1$
			new Object[]{prereq.getOperator().toDisplayString(),
				prereq.getOperand(), prereq.getKey(), prereq.getSubKey()});
	}

	public int passesCDOM(Prerequisite prereq, CharacterDataStore character) throws PrerequisiteException
	{
		SpellSubSchool school = SpellSubSchool.getConstant(prereq.getKey());
		int requiredLevel = Integer.parseInt(prereq.getSubKey());
		int requiredNumber = Integer.parseInt(prereq.getOperand());

		PCGenGraph activeGraph = character.getActiveGraph();
		// Build a list of all possible spells (innate & known)
		List<Spell> spells = activeGraph.getGrantedNodeList(Spell.class);
		
		Set<Spell> spellSet = new HashSet<Spell>();
		
		for (Spell s : spells)
		{
			List<SpellSubSchool> subSchoolList = s.getListFor(ListKey.SPELL_SUBSCHOOL);
			if (subSchoolList != null && subSchoolList.contains(school))
			{
				List<PCGraphEdge> assocEdges = activeGraph.getInwardEdgeList(s);
				for (PCGraphEdge edge : assocEdges)
				{
					if (edge.getAssociation(AssociationKey.SPELL_LEVEL)
						.intValue() >= requiredLevel)
					{
						spellSet.add(s);
						break;
					}
				}
			}
		}

		int runningTotal =
			prereq.getOperator().compare(spellSet.size(), requiredNumber);
		return countedTotal(prereq, runningTotal);
	}
}
