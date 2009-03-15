/*
 * Copyright 2005 (C) Tom Parker <thpr@users.sourceforge.net>
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
 * Created on June 18, 2005.
 *
 * Current Ver: $Revision$
 * Last Editor: $Author$
 * Last Edited: $Date$
 */
package pcgen.cdom.enumeration;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import pcgen.base.formula.Formula;
import pcgen.base.util.CaseInsensitiveMap;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.ChooseResultActor;
import pcgen.cdom.base.PersistentTransitionChoice;
import pcgen.cdom.base.TransitionChoice;
import pcgen.cdom.content.CampaignURL;
import pcgen.cdom.content.ChangeProf;
import pcgen.cdom.content.KnownSpellIdentifier;
import pcgen.cdom.content.LevelCommandFactory;
import pcgen.cdom.helper.ArmorProfProvider;
import pcgen.cdom.helper.Capacity;
import pcgen.cdom.helper.EqModRef;
import pcgen.cdom.helper.FollowerLimit;
import pcgen.cdom.helper.PointCost;
import pcgen.cdom.helper.ProfProvider;
import pcgen.cdom.helper.Qualifier;
import pcgen.cdom.helper.ShieldProfProvider;
import pcgen.cdom.helper.StatLock;
import pcgen.cdom.helper.WeaponProfProvider;
import pcgen.cdom.list.ClassSkillList;
import pcgen.cdom.modifier.ChangeArmorType;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.core.Ability;
import pcgen.core.ArmorProf;
import pcgen.core.Campaign;
import pcgen.core.DamageReduction;
import pcgen.core.Deity;
import pcgen.core.Description;
import pcgen.core.Domain;
import pcgen.core.Equipment;
import pcgen.core.EquipmentModifier;
import pcgen.core.FollowerOption;
import pcgen.core.Kit;
import pcgen.core.Language;
import pcgen.core.Movement;
import pcgen.core.PCClass;
import pcgen.core.PCStat;
import pcgen.core.PCTemplate;
import pcgen.core.QualifiedObject;
import pcgen.core.Race;
import pcgen.core.ShieldProf;
import pcgen.core.Skill;
import pcgen.core.SpecialAbility;
import pcgen.core.SpecialProperty;
import pcgen.core.SpellProhibitor;
import pcgen.core.SubClass;
import pcgen.core.SubstitutionClass;
import pcgen.core.Vision;
import pcgen.core.WeaponProf;
import pcgen.core.bonus.BonusObj;
import pcgen.core.kit.BaseKit;
import pcgen.persistence.lst.CampaignSourceEntry;
import pcgen.persistence.lst.utils.DeferredLine;

/**
 * @author Tom Parker <thpr@users.sourceforge.net>
 * 
 * This is a Typesafe enumeration of legal List Characteristics of an object. It
 * is designed to act as an index to a specific Object items within a
 * CDOMObject.
 * 
 * ListKeys are designed to store items in a CDOMObject in a type-safe
 * fashion. Note that it is possible to use the ListKey to cast the object to
 * the type of object stored by the ListKey. (This assists with Generics)
 * 
 * @param <T>
 *            The class of object stored by this ListKey.
 */
public final class ListKey<T> {

	/** FILE_ABILITY_CATEGORY - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_ABILITY_CATEGORY = new ListKey<CampaignSourceEntry>();
	/** FILE_BIO_SET - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_BIO_SET = new ListKey<CampaignSourceEntry>();
	/** FILE_CLASS - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_CLASS = new ListKey<CampaignSourceEntry>();
	/** FILE_COMPANION_MOD - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_COMPANION_MOD = new ListKey<CampaignSourceEntry>();
	/** FILE_COVER - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_COVER = new ListKey<CampaignSourceEntry>();
	/** FILE_DEITY - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_DEITY = new ListKey<CampaignSourceEntry>();
	/** FILE_DOMAIN - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_DOMAIN = new ListKey<CampaignSourceEntry>();
	/** FILE_EQUIP - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_EQUIP = new ListKey<CampaignSourceEntry>();
	/** FILE_EQUIP_MOD - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_EQUIP_MOD = new ListKey<CampaignSourceEntry>();
	/** FILE_ABILITY - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_ABILITY = new ListKey<CampaignSourceEntry>();
	/** FILE_FEAT - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_FEAT = new ListKey<CampaignSourceEntry>();
	/** FILE_KIT - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_KIT = new ListKey<CampaignSourceEntry>();
	/** FILE_LANGUAGE - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_LANGUAGE = new ListKey<CampaignSourceEntry>();
	/** FILE_LST_EXCLUDE - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_LST_EXCLUDE = new ListKey<CampaignSourceEntry>();
	/** FILE_PCC - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_PCC = new ListKey<CampaignSourceEntry>();
	/** FILE_RACE - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_RACE = new ListKey<CampaignSourceEntry>();
	/** FILE_SKILL - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_SKILL = new ListKey<CampaignSourceEntry>();
	/** FILE_SPELL - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_SPELL = new ListKey<CampaignSourceEntry>();
	/** FILE_TEMPLATE - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_TEMPLATE = new ListKey<CampaignSourceEntry>();
	/** FILE_WEAPON_PROF - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_WEAPON_PROF = new ListKey<CampaignSourceEntry>();
	/** GAME_MODE - a ListKey */
	public static final ListKey<String> GAME_MODE = new ListKey<String>();
	/** LICENSE - a ListKey */
	public static final ListKey<String> LICENSE = new ListKey<String>();
	/** LICENSE_FILE - a ListKey */
	public static final ListKey<CampaignSourceEntry> LICENSE_FILE = new ListKey<CampaignSourceEntry>();
	/** FILE_LOGO - a ListKey */
	public static final ListKey<CampaignSourceEntry> FILE_LOGO = new ListKey<CampaignSourceEntry>();
	/** PANTHEON - a ListKey */
	public static final ListKey<Pantheon> PANTHEON = new ListKey<Pantheon>();
	/** RACE_PANTHEON - a ListKey */
	public static final ListKey<String> RACEPANTHEON = new ListKey<String>();
	/** SAVE - a ListKey */
	public static final ListKey<String> SAVE = new ListKey<String>();
	/** SECTION 15 - a ListKey */
	public static final ListKey<String> SECTION_15 = new ListKey<String>();
	/** SPECIAL_ABILITY - a ListKey */
	public static final ListKey<SpecialAbility> SPECIAL_ABILITY = new ListKey<SpecialAbility>();
	/** TEMP_BONUS - a ListKey */
	public static final ListKey<BonusObj> TEMP_BONUS = new ListKey<BonusObj>();
//	/** Key for a list of weapon proficiencies */
//	public static final ListKey<String> WEAPON_PROF = new ListKey<String>();
	public static final ListKey<CampaignSourceEntry> FILE_ARMOR_PROF = new ListKey<CampaignSourceEntry>();
	public static final ListKey<CampaignSourceEntry> FILE_SHIELD_PROF = new ListKey<CampaignSourceEntry>();
	public static final ListKey<CDOMReference<WeaponProf>> DEITYWEAPON = new ListKey<CDOMReference<WeaponProf>>();
	public static final ListKey<CDOMReference<ClassSkillList>> CLASSES = new ListKey<CDOMReference<ClassSkillList>>();
	public static final ListKey<CDOMReference<ClassSkillList>> PREVENTED_CLASSES = new ListKey<CDOMReference<ClassSkillList>>();
	public static final ListKey<RaceSubType> RACESUBTYPE = new ListKey<RaceSubType>();
	public static final ListKey<RaceSubType> REMOVED_RACESUBTYPE = new ListKey<RaceSubType>();
	public static final ListKey<LevelCommandFactory> ADD_LEVEL = new ListKey<LevelCommandFactory>();
	public static final ListKey<String> RANGE = new ListKey<String>();
	public static final ListKey<String> SAVE_INFO = new ListKey<String>();
	public static final ListKey<String> DURATION = new ListKey<String>();
	public static final ListKey<String> COMPONENTS = new ListKey<String>();
	public static final ListKey<String> CASTTIME = new ListKey<String>();
	public static final ListKey<String> SPELL_RESISTANCE = new ListKey<String>();
	public static final ListKey<String> VARIANTS = new ListKey<String>();
	public static final ListKey<String> SPELL_SCHOOL = new ListKey<String>();
	public static final ListKey<String> SPELL_SUBSCHOOL = new ListKey<String>();
	public static final ListKey<String> SPELL_DESCRIPTOR = new ListKey<String>();
	public static final ListKey<String> PROHIBITED_ITEM = new ListKey<String>();
	public static final ListKey<String> ITEM = new ListKey<String>();
	public static final ListKey<Integer> HITDICE_ADVANCEMENT = new ListKey<Integer>();
	public static final ListKey<String> ITEM_TYPES = new ListKey<String>();
	public static final ListKey<CDOMSingleRef<EquipmentModifier>> REPLACED_KEYS = new ListKey<CDOMSingleRef<EquipmentModifier>>();
	public static final ListKey<SpecialProperty> SPECIAL_PROPERTIES = new ListKey<SpecialProperty>();
	public static final ListKey<ChangeArmorType> ARMORTYPE = new ListKey<ChangeArmorType>();
	public static final ListKey<Formula> SPECIALTYKNOWN = new ListKey<Formula>();
	public static final ListKey<Formula> KNOWN = new ListKey<Formula>();
	public static final ListKey<Formula> CAST = new ListKey<Formula>();
	public static final ListKey<QualifiedObject<CDOMSingleRef<Domain>>> DOMAIN = new ListKey<QualifiedObject<CDOMSingleRef<Domain>>>();
	public static final ListKey<CDOMReference<Deity>> DEITY = new ListKey<CDOMReference<Deity>>();
	public static final ListKey<PointCost> SPELL_POINT_COST = new ListKey<PointCost>();
	public static final ListKey<KnownSpellIdentifier> KNOWN_SPELLS = new ListKey<KnownSpellIdentifier>();
	public static final ListKey<SpellProhibitor> SPELL_PROHIBITOR = new ListKey<SpellProhibitor>();
	public static final ListKey<Description> BENEFIT = new ListKey<Description>();
	public static final ListKey<CDOMReference<Language>> AUTO_LANGUAGES = new ListKey<CDOMReference<Language>>();
	public static final ListKey<PCTemplate> LEVEL_TEMPLATES = new ListKey<PCTemplate>();
	public static final ListKey<PCTemplate> REPEATLEVEL_TEMPLATES = new ListKey<PCTemplate>();
	public static final ListKey<PCTemplate> HD_TEMPLATES = new ListKey<PCTemplate>();
	public static final ListKey<CDOMReference<PCTemplate>> TEMPLATE_CHOOSE = new ListKey<CDOMReference<PCTemplate>>();
	public static final ListKey<CDOMReference<PCTemplate>> TEMPLATE_ADDCHOICE = new ListKey<CDOMReference<PCTemplate>>();
	public static final ListKey<CDOMReference<PCTemplate>> TEMPLATE = new ListKey<CDOMReference<PCTemplate>>();
	public static final ListKey<CDOMReference<PCTemplate>> REMOVE_TEMPLATES = new ListKey<CDOMReference<PCTemplate>>();
	public static final ListKey<Vision> VISION_CACHE = new ListKey<Vision>();
	public static final ListKey<PersistentTransitionChoice<?>> ADD = new ListKey<PersistentTransitionChoice<?>>();
	public static final ListKey<CDOMReference<? extends PCClass>> FAVORED_CLASS = new ListKey<CDOMReference<? extends PCClass>>();
	public static final ListKey<Qualifier> QUALIFY = new ListKey<Qualifier>();
	public static final ListKey<DamageReduction> DAMAGE_REDUCTION = new ListKey<DamageReduction>();
	public static final ListKey<PCStat> UNLOCKED_STATS = new ListKey<PCStat>();
	public static final ListKey<StatLock> STAT_LOCKS = new ListKey<StatLock>();
	public static final ListKey<TransitionChoice<Kit>> KIT_CHOICE = new ListKey<TransitionChoice<Kit>>();
	public static final ListKey<Movement> MOVEMENT = new ListKey<Movement>();
	public static final ListKey<FollowerOption> COMPANIONLIST = new ListKey<FollowerOption>();
	public static final ListKey<FollowerLimit> FOLLOWERS = new ListKey<FollowerLimit>();
	public static final ListKey<Description> DESCRIPTION = new ListKey<Description>();
	public static final ListKey<ChangeProf> CHANGEPROF = new ListKey<ChangeProf>();
	public static final ListKey<Equipment> NATURAL_WEAPON = new ListKey<Equipment>();
	public static final ListKey<SpecialAbility> SAB = new ListKey<SpecialAbility>();
	public static final ListKey<SubClass> SUB_CLASS = new ListKey<SubClass>();
	public static final ListKey<SubstitutionClass> SUBSTITUTION_CLASS = new ListKey<SubstitutionClass>();
	public static final ListKey<DeferredLine> SUB_CLASS_LEVEL = new ListKey<DeferredLine>();
	public static final ListKey<CDOMReference<Skill>> SERVES_AS_SKILL = new ListKey<CDOMReference<Skill>>();
	public static final ListKey<CDOMReference<Race>> SERVES_AS_RACE = new ListKey<CDOMReference<Race>>();
	public static final ListKey<CDOMReference<PCClass>> SERVES_AS_CLASS = new ListKey<CDOMReference<PCClass>>();
	public static final ListKey<CDOMReference<Ability>> SERVES_AS_ABILITY = new ListKey<CDOMReference<Ability>>();
	public static final ListKey<ChooseResultActor> CHOOSE_ACTOR = new ListKey<ChooseResultActor>();
	public static final ListKey<WeaponProfProvider> WEAPONPROF = new ListKey<WeaponProfProvider>();
	public static final ListKey<CDOMSingleRef<WeaponProf>> IMPLIED_WEAPONPROF = new ListKey<CDOMSingleRef<WeaponProf>>();
	public static final ListKey<QualifiedObject<CDOMReference<Equipment>>> EQUIPMENT = new ListKey<QualifiedObject<CDOMReference<Equipment>>>();
	public static final ListKey<ArmorProfProvider> AUTO_ARMORPROF = new ListKey<ArmorProfProvider>();
	public static final ListKey<ShieldProfProvider> AUTO_SHIELDPROF = new ListKey<ShieldProfProvider>();
	public static final ListKey<ProfProvider<ArmorProf>> ARMORPROF_CACHE = new ListKey<ProfProvider<ArmorProf>>();
	public static final ListKey<ProfProvider<ShieldProf>> SHIELDPROF_CACHE = new ListKey<ProfProvider<ShieldProf>>();
	public static final ListKey<CDOMReference<Skill>> CCSKILL = new ListKey<CDOMReference<Skill>>();
	public static final ListKey<CDOMReference<Skill>> CSKILL = new ListKey<CDOMReference<Skill>>();
	public static final ListKey<BonusObj> BONUS = new ListKey<BonusObj>();
	public static final ListKey<String> UNARMED_DAMAGE = new ListKey<String>();
	public static final ListKey<Capacity> CAPACITY = new ListKey<Capacity>();
	public static final ListKey<SpellProhibitor> PROHIBITED_SPELLS = new ListKey<SpellProhibitor>();
	public static final ListKey<String> COMMENT = new ListKey<String>();
	public static final ListKey<PersistentTransitionChoice<?>> REMOVE = new ListKey<PersistentTransitionChoice<?>>();
	public static final ListKey<Type> TYPE = new ListKey<Type>();
	public static final ListKey<BaseKit> KIT_TASKS = new ListKey<BaseKit>();
	public static final ListKey<EquipmentModifier> EQMOD = new ListKey<EquipmentModifier>();
	public static final ListKey<CDOMSingleRef<Race>> APPLIED_RACE = new ListKey<CDOMSingleRef<Race>>();
	public static final ListKey<EqModRef> EQMOD_INFO = new ListKey<EqModRef>();
	public static final ListKey<CampaignURL> CAMPAIGN_URL = new ListKey<CampaignURL>();
	public static final ListKey<Qualifier> FORWARDREF = new ListKey<Qualifier>();
	public static final ListKey<Campaign> CAMPAIGN = new ListKey<Campaign>();
	
	public static final ListKey<Class<?>> DUPES_ALLOWED = new ListKey<Class<?>>();
	public static final ListKey<ObjectKey<?>> REMOVED_OBJECTKEY = new ListKey<ObjectKey<?>>();
	public static final ListKey<CDOMReference<Ability>> FEAT_TOKEN_LIST = new ListKey<CDOMReference<Ability>>();

	//These are case sensitive, please do not change them to upper case
	public static final ListKey<String> HIDDEN_Equipment = new ListKey<String>();
	public static final ListKey<String> HIDDEN_Ability = new ListKey<String>();
	public static final ListKey<String> HIDDEN_Skill = new ListKey<String>();

	private static CaseInsensitiveMap<ListKey<?>> map = null;
	
	static
	{
		buildMap();
	}

	/** Private constructor to prevent instantiation of this class */
	private ListKey() {
		//Only allow instantation here
	}

	public T cast(Object o)
	{
		return (T) o;
	}

	public static <OT> ListKey<OT> getKeyFor(Class<OT> c, String s)
	{
		/*
		 * CONSIDER This is actually not type safe, there is a case of asking
		 * for a String a second time with a different Class that ObjectKey
		 * currently does not handle. Two solutions: One, store this in a
		 * Two-Key map and allow a String to map to more than one ObjectKey
		 * given different output types (considered confusing) or Two, store the
		 * Class and validate that with a an error message if a different class
		 * is requested.
		 */
		ListKey<OT> o = (ListKey<OT>) map.get(s);
		if (o == null)
		{
			o = new ListKey<OT>();
			map.put(s, o);
		}
		return o;
	}

	private static void buildMap()
	{
		map = new CaseInsensitiveMap<ListKey<?>>();
		Field[] fields = ListKey.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			int mod = fields[i].getModifiers();

			if (java.lang.reflect.Modifier.isStatic(mod)
					&& java.lang.reflect.Modifier.isFinal(mod)
					&& java.lang.reflect.Modifier.isPublic(mod))
			{
				try
				{
					Object o = fields[i].get(null);
					if (o instanceof ListKey)
					{
						map.put(fields[i].getName(), (ListKey<?>) o);
					}
				}
				catch (IllegalArgumentException e)
				{
					throw new InternalError();
				}
				catch (IllegalAccessException e)
				{
					throw new InternalError();
				}
			}
		}
	}

	@Override
	public String toString()
	{
		/*
		 * CONSIDER Should this find a way to do a Two-Way Map or something to
		 * that effect?
		 */
		for (Map.Entry<?, ListKey<?>> me : map.entrySet())
		{
			if (me.getValue() == this)
			{
				return me.getKey().toString();
			}
		}
		// Error
		return "";
	}

	public static Collection<ListKey<?>> getAllConstants()
	{
		return new HashSet<ListKey<?>>(map.values());
	}
}
