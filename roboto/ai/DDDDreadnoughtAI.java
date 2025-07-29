package dre.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.ai.addon.IIIIConsumableSpender;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import dre.elfocrash.roboto.model.HHHHealingSpell;
import dre.elfocrash.roboto.model.OOOOffensiveSpell;
import dre.elfocrash.roboto.model.SSSSpellUsageCondition;
import dre.elfocrash.roboto.model.SSSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

public class DDDDreadnoughtAI extends CCCCombatAI implements IIIIConsumableSpender {
	
	public DDDDreadnoughtAI(FFFFakePlayer character) {
		super(character);
	}

	@Override
	public void thinkAndAct() {		
		super.thinkAndAct();
		setBusyThinking(true);
		applyDefaultBuffs();
		handleShots();
		selfSupportBuffs();
		tryTargetAttackerOrRandomMob(FFFFakeHelpers.getTestTargetClass(), FFFFakeHelpers.getTestTargetRange());		
		tryAttackingUsingFighterOffensiveSkill();
		setBusyThinking(false);
	}

	@Override
	protected ShotType getShotType() {
		return ShotType.SOULSHOT;
	}	
	
	@Override
	protected double changeOfUsingSkill() {
		return 0.2;
	}

	@Override
	protected List<OOOOffensiveSpell> getOffensiveSpells() {
		List<OOOOffensiveSpell> _offensiveSpells = new ArrayList<>();		
		return _offensiveSpells;
	}
	
	@Override
	protected List<SSSSupportSpell> getSelfSupportSpells() {
		List<SSSSupportSpell> _selfSupportSpells = new ArrayList<>();
		_selfSupportSpells.add(new SSSSupportSpell(440, SSSSpellUsageCondition.MISSINGCP, 1000, 1));
		return _selfSupportSpells;
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FFFFakeHelpers.getFighterBuffs();
	}
	
	@Override
	protected List<HHHHealingSpell> getHealingSpells()
	{		
		return Collections.emptyList();
	}

}
