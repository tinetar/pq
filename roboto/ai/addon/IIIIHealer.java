package dre.elfocrash.roboto.ai.addon;

import java.util.List;
import java.util.stream.Collectors;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.ai.CCCCombatAI;
import dre.elfocrash.roboto.model.HHHHealingSpell;

import net.sf.l2j.gameserver.model.actor.Creature;

public interface IIIIHealer {
	
	default void tryTargetingLowestHpTargetInRadius(FFFFakePlayer player, Class<? extends Creature> creatureClass, int radius) {
		if(player.getTarget() == null) {
			List<Creature> targets = player.getKnownTypeInRadius(creatureClass, radius).stream()
			.filter(x->!x.isDead() &&
			(x instanceof FFFFakePlayer ? ((FFFFakePlayer) x).getAllyId() == player.getAllyId() : false))					
			.collect(Collectors.toList());
			
			if(!player.isDead())
				targets.add(player);		
			
			List<Creature> sortedTargets = targets.stream()
				.sorted((x1, x2) -> Double.compare(x1.getCurrentHp(), x2.getCurrentHp()))
				.collect(Collectors.toList());
			
			if(!sortedTargets.isEmpty()) {
				Creature target = sortedTargets.get(0);
				player.setTarget(target);				
			}
		}else {
			if(((Creature)player.getTarget()).isDead())
				player.setTarget(null);
		}	
	}
	
	default void tryHealingTarget(FFFFakePlayer player) {
		
		if(player.getTarget() != null && player.getTarget() instanceof Creature)
		{
			Creature target = (Creature) player.getTarget();
			if(player.getFFFFakeAi() instanceof CCCCombatAI) {
				HHHHealingSpell skill = ((CCCCombatAI)player.getFFFFakeAi()).getRandomAvaiableHealingSpellForTarget();
				if(skill != null) {
					switch(skill.getCondition()){
						case LESSHPPERCENT:
							double currentHpPercentage = Math.round(100.0 / target.getMaxHp() * target.getCurrentHp());
							if(currentHpPercentage <= skill.getConditionValue()) {
								player.getFFFFakeAi().castSpell(player.getSkill(skill.getSkillId()));						
							}						
							break;				
						default:
							break;							
					}
					
				}
			}
		}
	}
}
