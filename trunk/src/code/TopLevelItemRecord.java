package code;

import java.util.HashSet;
import java.util.Set;

public class TopLevelItemRecord extends SourceItem
{
	protected Set<ModifierRecord> modifiers;
	
	protected TopLevelItemRecord()
	{
		modifiers = new HashSet<ModifierRecord>();
	}
	
	public void addModifier(ModifierRecord mod)
	{
		modifiers.add(mod);
	}
}
