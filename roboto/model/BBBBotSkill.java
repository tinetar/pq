package dre.elfocrash.roboto.model;

public abstract class BBBBotSkill {
	protected int _skillId;
	protected SSSSpellUsageCondition _condition;
	protected int _conditionValue;
	protected int _priority;
	
	public BBBBotSkill(int skillId, SSSSpellUsageCondition condition, int conditionValue, int priority) {
		_skillId = skillId;
		_condition = condition;
		_conditionValue = conditionValue;
	}

	public BBBBotSkill(int skillId) {
		_skillId = skillId;
		_condition = SSSSpellUsageCondition.NONE;
		_conditionValue = 0;
		_priority = 0;
	}
	
	public int getSkillId() {
		return _skillId;
	}	

	public SSSSpellUsageCondition getCondition(){
		return _condition;
	}
	
	public int getConditionValue() {
		return _conditionValue;
	}
	
	public int getPriority() {
		return _priority;
	}
}
