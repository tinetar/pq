package dre.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.ai.addon.IIIIHealer;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import dre.elfocrash.roboto.model.HHHHealingSpell;
import dre.elfocrash.roboto.model.OOOOffensiveSpell;
import dre.elfocrash.roboto.model.SSSSupportSpell;

import net.sf.l2j.commons.concurrent.ThreadPool;

import net.sf.l2j.gameserver.data.xml.MapRegionData;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;

/**
 * @author Elfocrash
 *
 */
public class CCCCardinalAI extends CCCCombatAI implements IIIIHealer
{
    /**
     * Called when this bot receives a private message from a player.
     */
    public void onPrivateMessage(String fromPlayerName, String messageText) {
        _ffffakePlayer.onPrivateMessageReceived(fromPlayerName, messageText);
    }

    public CCCCardinalAI(FFFFakePlayer character)
    {
        super(character);
    }
    
    @Override
    public void thinkAndAct() {
    	
    	 _ffffakePlayer.refreshPvpFlagIfInZone();
    	
    	if (_ffffakePlayer.isGmSpawned() && _ffffakePlayer.isDead()) {
    	    _ffffakePlayer.setFakeDeathRespawn(true);
    	    ThreadPool.schedule(() -> _ffffakePlayer.doReviveAndTeleportIfFake(), 3000);
    	    return;
    	}
   	
        if (_ffffakePlayer.isDead() && _ffffakePlayer.isGmSpawned()) {
            ThreadPool.schedule(() -> {
                if (_ffffakePlayer.isDead()) {
                     _ffffakePlayer.doRevive();
                     _ffffakePlayer.teleToLocation(MapRegionData.getInstance().getLocationToTeleport(_ffffakePlayer, MapRegionData.TeleportType.TOWN), 20);
                 }
            }, 3000);
            return;
         }
        
        super.thinkAndAct();
        setBusyThinking(true);
        applyDefaultBuffs();
        handleShots();
        tryTargetingLowestHpTargetInRadius(_ffffakePlayer, FFFFakePlayer.class, FFFFakeHelpers.getTestTargetRange());
        tryHealingTarget(_ffffakePlayer);
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
        return Collections.emptyList();
    }
    
    @Override
    protected List<HHHHealingSpell> getHealingSpells()
    {        
        List<HHHHealingSpell> _healingSpells = new ArrayList<>();
        _healingSpells.add(new HHHHealingSpell(1218, SkillTargetType.TARGET_ONE, 60, 1));        
        _healingSpells.add(new HHHHealingSpell(1217, SkillTargetType.TARGET_ONE, 60, 3));
        return _healingSpells; 
    }
    
    @Override
    protected int[][] getBuffs()
    {
        return FFFFakeHelpers.getMageBuffs();
    }    

    @Override
    protected List<SSSSupportSpell> getSelfSupportSpells() {
        return Collections.emptyList();
    }
}
