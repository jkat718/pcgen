/*
 * Copyright (c) 2008 Tom Parker <thpr@users.sourceforge.net>
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */
package plugin.lsttokens.equipmentmodifier;

import java.util.StringTokenizer;

import pcgen.base.lang.StringUtil;
import pcgen.cdom.base.Constants;
import pcgen.cdom.enumeration.ListKey;
import pcgen.core.EquipmentModifier;
import pcgen.rules.context.Changes;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;

/**
 * Deals with ITYPE token
 */
public class ItypeToken extends AbstractToken implements
		CDOMPrimaryToken<EquipmentModifier>
{

	@Override
	public String getTokenName()
	{
		return "ITYPE";
	}

	public boolean parse(LoadContext context, EquipmentModifier mod,
			String value)
	{
		if (isEmpty(value) || hasIllegalSeparator('.', value))
		{
			return false;
		}
		context.getObjectContext().removeList(mod, ListKey.ITEM_TYPES);

		StringTokenizer tok = new StringTokenizer(value, Constants.DOT);
		while (tok.hasMoreTokens())
		{
			context.getObjectContext().addToList(mod, ListKey.ITEM_TYPES,
					tok.nextToken());
		}
		return true;
	}

	public String[] unparse(LoadContext context, EquipmentModifier mod)
	{
		Changes<String> changes = context.getObjectContext().getListChanges(
				mod, ListKey.ITEM_TYPES);
		if (changes == null || changes.isEmpty())
		{
			return null;
		}
		return new String[] { StringUtil
				.join(changes.getAdded(), Constants.DOT) };
	}

	public Class<EquipmentModifier> getTokenClass()
	{
		return EquipmentModifier.class;
	}
}
