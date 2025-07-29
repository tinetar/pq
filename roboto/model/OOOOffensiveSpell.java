package dre.elfocrash.roboto.model;

public class OOOOffensiveSpell extends BBBBotSkill {	
	
	public OOOOffensiveSpell (int skillId, SSSSpellUsageCondition condition, int conditionValue, int priority) {
		super(skillId, condition, conditionValue, priority);
	}
	
	public OOOOffensiveSpell (int skillId, int priority) {
		super(skillId, SSSSpellUsageCondition.NONE, 0, priority);
	}
	
	public OOOOffensiveSpell (int skillId) {
		super(skillId);
	}		
}
