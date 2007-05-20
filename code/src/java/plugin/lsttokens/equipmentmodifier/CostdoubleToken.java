package plugin.lsttokens.equipmentmodifier;

import pcgen.core.EquipmentModifier;
import pcgen.persistence.lst.EquipmentModifierLstToken;

/**
 * Deals with COSTDOUBLE token 
 */
public class CostdoubleToken implements EquipmentModifierLstToken
{

	public String getTokenName()
	{
		return "COSTDOUBLE";
	}

	public boolean parse(EquipmentModifier mod, String value)
	{
		boolean set;
		char firstChar = value.charAt(0);
		if (firstChar == 'y' || firstChar =='Y')
		{
			// 514 abbreviation cleanup
//			if (value.length() > 1 && !value.equalsIgnoreCase("YES"))
//			{
//				Logging.errorPrint("You should use 'YES' or 'NO' as the " + getTokenName());
//				Logging.errorPrint("Abbreviations will fail after PCGen 5.12");
//			}
			set = true;
		}
		else 
		{
			// 514 abbreviation cleanup
//			if (firstChar != 'N' && firstChar != 'n'
//				&& !value.equalsIgnoreCase("NO"))
//			{
//				Logging.errorPrint("You should use 'YES' or 'NO' as the "
//						+ getTokenName());
//				Logging.errorPrint("Abbreviations will fail after PCGen 5.12");
//			}
			set = false;
		}
		mod.setCostDouble(set);
		return true;
	}
}
