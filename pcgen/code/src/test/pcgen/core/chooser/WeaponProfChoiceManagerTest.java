/*
 * WeaponProfChoiceManagerTest.java
 * Copyright 2005 (C) Andrew Wilson <nuance@sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.     See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on Oct 7, 2005
 *
 * $Author: nuance $ 
 * $Date: 2006-03-26 08:00:03 +0100 (Sun, 26 Mar 2006) $
 * $Revision: 471 $
 *
 */
package pcgen.core.chooser;

import java.lang.reflect.Field;
import java.util.List;

import pcgen.AbstractCharacterTestCase;
import pcgen.base.formula.Formula;
import pcgen.cdom.enumeration.FormulaKey;
import pcgen.cdom.enumeration.StringKey;
import pcgen.core.PObject;
import pcgen.core.PlayerCharacter;
import pcgen.util.TestHelper;

/**
 * {@code WeaponProfChoiceManagerTest} test that the WeaponProfChoiceManager class is functioning correctly.
 *
 * @author Andrew Wilson <nuance@sourceforge.net>
 */

public class WeaponProfChoiceManagerTest extends AbstractCharacterTestCase
{

	/**
	 * Constructs a new {@code WeaponProfChoiceManagerTest}.
	 */
	public WeaponProfChoiceManagerTest()
	{
		// Do Nothing
	}

	/**
	 * Test the constructor 
	 */
	public void test001()
	{
		PObject pObj = new PObject();
		pObj.setName("My PObject");
		pObj.put(StringKey.CHOICE_STRING, "WEAPONPROFS|Foo|Bar|Baz");
		pObj.put(FormulaKey.NUMCHOICES, Formula.ONE);
		is(pObj.getSafe(StringKey.CHOICE_STRING),
			strEq("WEAPONPROFS|Foo|Bar|Baz"));

		PlayerCharacter aPC = getCharacter();

		ChoiceManagerList choiceManager =
				ChooserUtilities.getChoiceManager(pObj, null, aPC);
		is(choiceManager, not(eq(null)), "Found the chooser");

		is(choiceManager.typeHandled(), strEq("WEAPONPROFS"),
			"got expected chooser");

		try
		{
			Class cMClass = choiceManager.getClass();

			Field aField =
					(Field) TestHelper.findField(cMClass, "numberOfChoices");
			is(aField.get(choiceManager), eq(1));

			aField = (Field) TestHelper.findField(cMClass, "choices");
			List choices = (List) aField.get(choiceManager);
			is(choices.size(), eq(3));
			is(choices.get(0), strEq("Foo"));
			is(choices.get(1), strEq("Bar"));
			is(choices.get(2), strEq("Baz"));
		}
		catch (IllegalAccessException e)
		{
			System.out.println(e);
		}
	}

}
