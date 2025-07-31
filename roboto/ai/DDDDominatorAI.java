package dre.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import dre.elfocrash.roboto.model.HHHHealingSpell;
import dre.elfocrash.roboto.model.OOOOffensiveSpell;
import dre.elfocrash.roboto.model.SSSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class DDDDominatorAI extends CCCCombatAI
{
	public DDDDominatorAI(FFFFakePlayer character)
	{
		super(character);		
	}
	
	@Override
	public void thinkAndAct() {
		super.thinkAndAct();
		setBusyThinking(true);
		applyDefaultBuffs();		
		handleShots();		
		tryTargetAttackerOrRandomMob(FFFFakeHelpers.getTestTargetClass(), FFFFakeHelpers.getTestTargetRange());		
		tryAttackingUsingMageOffensiveSkill();
		setBusyThinking(false);
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.BLESSED_SPIRITSHOT;
	}
	
	@Override
	protected List<OOOOffensiveSpell> getOffensiveSpells()
	{
		List<OOOOffensiveSpell> _offensiveSpells = new ArrayList<>();
		_offensiveSpells.add(new OOOOffensiveSpell(1245, 1));
		return _offensiveSpells; 
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return FFFFakeHelpers.getMageBuffs();
	}

	@Override
	protected List<HHHHealingSpell> getHealingSpells()
	{		
		return Collections.emptyList();
	}	

	@Override
	protected List<SSSSupportSpell> getSelfSupportSpells() {
		return Collections.emptyList();
	}
}
