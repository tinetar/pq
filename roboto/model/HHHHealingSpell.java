package dre.elfocrash.roboto.model;

import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;

public class HHHHealingSpell extends BBBBotSkill {
	
	private SkillTargetType _targetType;
	
	public HHHHealingSpell (int skillId, SkillTargetType targetType, SSSSpellUsageCondition condition, int conditionValue, int priority) {
		super(skillId, condition, conditionValue, priority);		
		_targetType = targetType;	
	}
	
	public HHHHealingSpell (int skillId, SkillTargetType targetType, int conditionValue, int priority) {
		super(skillId, SSSSpellUsageCondition.LESSHPPERCENT, conditionValue, priority);
		_targetType = targetType;	
	}
	
	public SkillTargetType getTargetType() {
		return _targetType;
	}
}
