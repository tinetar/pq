package dre.elfocrash.roboto.ai;

import java.util.Collections;
import java.util.List;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.model.HHHHealingSpell;
import dre.elfocrash.roboto.model.OOOOffensiveSpell;
import dre.elfocrash.roboto.model.SSSSupportSpell;

import net.sf.l2j.gameserver.model.ShotType;

/**
 * @author Elfocrash
 *
 */
public class FFFFallbackAI extends CCCCombatAI
{

	public FFFFallbackAI(FFFFakePlayer character)
	{
		super(character);
	}
	
	@Override
	public void thinkAndAct()
	{
		
	}
	
	@Override
	protected ShotType getShotType()
	{
		return ShotType.SOULSHOT;
	}
	
	@Override
	protected List<OOOOffensiveSpell> getOffensiveSpells()
	{
		return Collections.emptyList();
	}
	
	@Override
	protected int[][] getBuffs()
	{
		return new int[0][0];
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
