/*
 * Copyright 2007 (C) Tom Parker <thpr@users.sourceforge.net>
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
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 */
package plugin.lsttokens.pcclass;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.FormulaFactory;
import pcgen.cdom.enumeration.FormulaKey;
import pcgen.core.PCClass;
import pcgen.persistence.LoadContext;
import pcgen.persistence.lst.PCClassClassLstToken;
import pcgen.persistence.lst.PCClassLstToken;

/**
 * Class deals with MONNONSKILLHD Token
 */
public class MonnonskillhdToken implements PCClassLstToken,
		PCClassClassLstToken
{

	public String getTokenName()
	{
		return "MONNONSKILLHD";
	}

	public boolean parse(PCClass pcclass, String value, int level)
	{
		pcclass.addBonusList("0|MONNONSKILLHD|NUMBER|" + value);
		return true;
	}

	public boolean parse(LoadContext context, PCClass pcc, String value)
	{
		/*
		 * TODO Trailing PRE legal :P
		 */
		context.obj.put(pcc, FormulaKey.MONSTER_NON_SKILL_HD, FormulaFactory
			.getFormulaFor(value));
		return true;
	}

	public String[] unparse(LoadContext context, PCClass pcc)
	{
		Formula msp =
				context.obj.getFormula(pcc, FormulaKey.MONSTER_NON_SKILL_HD);
		if (msp == null)
		{
			return null;
		}
		return new String[]{msp.toString()};
	}
}
