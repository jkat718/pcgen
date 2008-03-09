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
package plugin.qualifier.stat;

import java.util.Iterator;
import java.util.Set;

import pcgen.cdom.inst.CDOMStat;
import pcgen.character.CharacterDataStore;
import plugin.qualifier.AbstractQualifierToken;

public class RemoveToken extends AbstractQualifierToken<CDOMStat>
{
	
	public String getTokenName()
	{
		return "REMOVE";
	}

	public Class<CDOMStat> getChoiceClass()
	{
		return CDOMStat.class;
	}

	public Set<CDOMStat> getSet(CharacterDataStore pc)
	{
		Set<CDOMStat> stats = pc.getRulesData().getAll(CDOMStat.class);
		if (stats != null && pcs != null)
		{
			for (Iterator<CDOMStat> it = stats.iterator(); it.hasNext();)
			{
				if (pcs.allow(pc, it.next()))
				{
					it.remove();
				}
			}
		}
		return stats;
	}
}
