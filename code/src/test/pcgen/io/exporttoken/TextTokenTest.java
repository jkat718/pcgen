/*
 * TextTokenTest.java
 * Copyright 2006 (C) James Dempsey <jdempsey@users.sourceforge.net>
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
 * Created on Oct 17, 2006
 *
 * $Id: VarTokenTest.java 201 2006-03-14 23:19:50Z nuance $
 *
 */
package pcgen.io.exporttoken;

import junit.framework.Test;
import junit.framework.TestSuite;
import pcgen.AbstractCharacterTestCase;
import pcgen.core.PlayerCharacter;
import pcgen.io.ExportHandler;
import plugin.exporttokens.TextToken;

/**
 * <code>TextTokenTest</code> tests the functioning of the TEXT 
 * token processing code. 
 *
 * Last Editor: $Author: nuance $
 * Last Edited: $Date: 2006-03-15 10:19:50 +1100 (Wed, 15 Mar 2006) $
 *
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision: 201 $
 */
public class TextTokenTest extends AbstractCharacterTestCase
{

	/**
	 * Quick test suite creation - adds all methods beginning with "test"
	 * @return The Test suite
	 */
	public static Test suite()
	{
		return new TestSuite(TextTokenTest.class);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		PlayerCharacter character = getCharacter();
		character.setName("The Vitamins are in my Fresh Brussels Sprouts");
	}

	/**
	 * Test the output for positive numbers with fractions.
	 */
	public void testTextFormatting()
	{
		TextToken tok = new TextToken();
		ExportHandler eh = new ExportHandler(null);

		assertEquals("TEXT.LOWER.NAME",
			"the vitamins are in my fresh brussels sprouts", tok
				.getToken("TEXT.LOWER.NAME", getCharacter(), eh));
		assertEquals("TEXT.UPPER.NAME",
			"THE VITAMINS ARE IN MY FRESH BRUSSELS SPROUTS", tok
				.getToken("TEXT.UPPER.NAME", getCharacter(), eh));
//		assertEquals("TEXT.SENTANCE.NAME",
//			"The vitamins are in my fresh brussels sprouts", tok
//				.getToken("TEXT.SENTANCE.NAME", getCharacter(), eh));
//		assertEquals("TEXT.TITLE.NAME",
//			"The Vitamins Are In My Fresh Brussels Sprouts", tok
//				.getToken("TEXT.TITLE.NAME", getCharacter(), eh));
	}

	/**
	 * Test the output for negative numbers with fractions.
	 */
	public void testNumSuffix()
	{
		TextToken tok = new TextToken();
		ExportHandler eh = new ExportHandler(null);
		PlayerCharacter character = getCharacter();
		
		character.setAge(1);
		assertEquals("Suffix 1", "st", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(2);
		assertEquals("Suffix 2", "nd", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(3);
		assertEquals("Suffix 3", "rd", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(4);
		assertEquals("Suffix 4", "th", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(11);
		assertEquals("Suffix 11", "th", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(12);
		assertEquals("Suffix 12", "th", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(13);
		assertEquals("Suffix 13", "th", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(14);
		assertEquals("Suffix 14", "th", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(21);
		assertEquals("Suffix 21", "st", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(22);
		assertEquals("Suffix 22", "nd", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(23);
		assertEquals("Suffix 23", "rd", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(24);
		assertEquals("Suffix 24", "th", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
		character.setAge(133);
		assertEquals("Suffix 133", "rd", tok.getToken("TEXT.NUMSUFFIX.AGE",
			getCharacter(), eh));
	}

	/**
	 * Test the output for negative numbers with fractions.
	 */
	public void testNumSuffixDirect()
	{
		TextToken tok = new TextToken();
		ExportHandler eh = new ExportHandler(null);
		
		assertEquals("Suffix 1", "st", tok.getToken("TEXT.NUMSUFFIX.1",
			getCharacter(), eh));
		assertEquals("Suffix 2", "nd", tok.getToken("TEXT.NUMSUFFIX.2",
			getCharacter(), eh));
		assertEquals("Suffix 3", "rd", tok.getToken("TEXT.NUMSUFFIX.3",
			getCharacter(), eh));
		assertEquals("Suffix 4", "th", tok.getToken("TEXT.NUMSUFFIX.4",
			getCharacter(), eh));
		assertEquals("Suffix 12", "th", tok.getToken("TEXT.NUMSUFFIX.12",
			getCharacter(), eh));
		assertEquals("Suffix 133", "rd", tok.getToken("TEXT.NUMSUFFIX.133",
			getCharacter(), eh));
	}


}