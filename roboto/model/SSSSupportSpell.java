package dre.elfocrash.roboto.model;

public class SSSSupportSpell extends BBBBotSkill {

	public SSSSupportSpell(int skillId, SSSSpellUsageCondition condition, int conditionValue, int priority) {
		super(skillId, condition, conditionValue, priority);
	}
	
	public SSSSupportSpell(int skillId, int priority) {
		super(skillId, SSSSpellUsageCondition.NONE, 0, priority);
	}
	
}
