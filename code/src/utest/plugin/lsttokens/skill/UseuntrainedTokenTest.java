/*
 * Copyright (c) 2007 Tom Parker <thpr@users.sourceforge.net>
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
package plugin.lsttokens.skill;

import org.junit.Test;

import pcgen.cdom.enumeration.ObjectKey;
import pcgen.core.Skill;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.CDOMToken;
import pcgen.persistence.lst.LstObjectFileLoader;
import pcgen.persistence.lst.SkillLoader;
import plugin.lsttokens.testsupport.AbstractTokenTestCase;

public class UseuntrainedTokenTest extends AbstractTokenTestCase<Skill>
{

	static UseuntrainedToken token = new UseuntrainedToken();
	static SkillLoader loader = new SkillLoader();

	@Override
	public Class<Skill> getCDOMClass()
	{
		return Skill.class;
	}

	@Override
	public LstObjectFileLoader<Skill> getLoader()
	{
		return loader;
	}

	@Override
	public CDOMToken<Skill> getToken()
	{
		return token;
	}

	@Test
	public void testInvalidInputString() throws PersistenceLayerException
	{
		internalTestInvalidInputString(null);
		assertNoSideEffects();
	}

	@Test
	public void testInvalidInputStringSet() throws PersistenceLayerException
	{
		assertTrue(parse("YES"));
		assertEquals(Boolean.TRUE, primaryProf.get(ObjectKey.USE_UNTRAINED));
		internalTestInvalidInputString(Boolean.TRUE);
		assertTrue(primaryGraph.isEmpty());
	}

	public void internalTestInvalidInputString(Object val)
		throws PersistenceLayerException
	{
		assertEquals(val, primaryProf.get(ObjectKey.USE_UNTRAINED));
		assertFalse(parse("String"));
		assertEquals(val, primaryProf.get(ObjectKey.USE_UNTRAINED));
		assertFalse(parse("TYPE=TestType"));
		assertEquals(val, primaryProf.get(ObjectKey.USE_UNTRAINED));
		assertFalse(parse("TYPE.TestType"));
		assertEquals(val, primaryProf.get(ObjectKey.USE_UNTRAINED));
		assertFalse(parse("ALL"));
		assertEquals(val, primaryProf.get(ObjectKey.USE_UNTRAINED));
	}

	@Test
	public void testValidInputs() throws PersistenceLayerException
	{
		assertTrue(parse("YES"));
		assertEquals(Boolean.TRUE, primaryProf.get(ObjectKey.USE_UNTRAINED));
		assertTrue(parse("NO"));
		assertEquals(Boolean.FALSE, primaryProf.get(ObjectKey.USE_UNTRAINED));
		//We're nice enough to be case insensitive here...
		assertTrue(parse("YeS"));
		assertEquals(Boolean.TRUE, primaryProf.get(ObjectKey.USE_UNTRAINED));
		assertTrue(parse("Yes"));
		assertEquals(Boolean.TRUE, primaryProf.get(ObjectKey.USE_UNTRAINED));
		assertTrue(parse("No"));
		assertEquals(Boolean.FALSE, primaryProf.get(ObjectKey.USE_UNTRAINED));
	}

	@Test
	public void testRoundRobinDisplay() throws PersistenceLayerException
	{
		runRoundRobin("YES");
	}

	@Test
	public void testRoundRobinExport() throws PersistenceLayerException
	{
		runRoundRobin("NO");
	}
}
