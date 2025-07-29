package dre.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.ai.addon.IIIIConsumableSpender;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import dre.elfocrash.roboto.model.HHHHealingSpell;
import dre.elfocrash.roboto.model.OOOOffensiveSpell;
import dre.elfocrash.roboto.model.SSSSupportSpell;
import dre.elfocrash.roboto.pm.PMReplyHelper;

import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.xml.MapRegionData;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Player;

import alt.elfocrash.roboto.helpers.FFFFFakeHelpers;

public class DDDDuelistAI extends CCCCombatAI implements IIIIConsumableSpender {

    private long _lastPvPTime = 0;
    private boolean _inAutoDefend = false;
    private long _lastActTime = 0;
    private long _lastTargetSwitchTime = 0;
    private long _lastCastDelayTime = 0;
    private long _lastPostKillDelayTime = 0;
    private int _pvpFailCounter = 0;
    private static final int MAX_PVP_FAILS = 8;

    public DDDDuelistAI(FFFFakePlayer character) {
        super(character);
    }

    public void onPrivateMessage(String fromPlayerName, String messageText) {
        _ffffakePlayer.onPrivateMessageReceived(fromPlayerName, messageText);
    }

    private void resetAfterPvP() {
        _inAutoDefend = false;
        _lastPvPTime = 0;
        _pvpFailCounter = 0;
        _ffffakePlayer.setTarget(null);
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
        
        long now = System.currentTimeMillis();

        synchronized (_ffffakePlayer) {
            PMReplyHelper.tryReply(_ffffakePlayer);
        }

        if (now - _lastActTime < 300) return;
        _lastActTime = now;

        if (now - _lastTargetSwitchTime < 200) return;

        handleDeath();

        List<Player> alliesUnderAttack = _ffffakePlayer.getKnownType(Player.class).stream()
            .filter(p -> isSameClanOrAlliance(p) && !p.isDead())
            .filter(p -> p.getTarget() instanceof Player)
            .filter(p -> {
                Player attacker = (Player) p.getTarget();
                return attacker != null && !attacker.isDead() && !attacker.isGM()
                    && (attacker.getPvpFlag() > 0 || attacker.getKarma() > 0)
                    && !isSameClanOrAlliance(attacker);
            })
            .collect(Collectors.toList());

        if (!alliesUnderAttack.isEmpty()) {
            Player attacker = (Player) alliesUnderAttack.get(0).getTarget();
            if (_ffffakePlayer.isGmSpawned() && (attacker.isGM() || isSameClanOrAlliance(attacker) || (attacker.getPvpFlag() == 0 && attacker.getKarma() == 0))) return;
            _ffffakePlayer.setTarget(attacker);
            tryAttackingUsingFighterOffensiveSkill();
            _lastPvPTime = now;
            _inAutoDefend = true;
            _lastTargetSwitchTime = now;
            return;
        }

        List<Player> attackers = _ffffakePlayer.getKnownType(Player.class).stream()
            .filter(p -> p.getTarget() == _ffffakePlayer && !p.isDead())
            .filter(p -> _ffffakePlayer.isInsideRadius(p, 1000, true, false))
            .filter(p -> !p.isGM() && !isSameClanOrAlliance(p))
            .filter(p -> p.getPvpFlag() > 0 || p.getKarma() > 0)
            .collect(Collectors.toList());

        if (!attackers.isEmpty()) {
            Creature threat = attackers.get(0);
            if (_ffffakePlayer.isGmSpawned() && (threat.isGM() || (threat instanceof Player && isSameClanOrAlliance((Player) threat)) || (threat instanceof Player && ((Player) threat).getPvpFlag() == 0 && ((Player) threat).getKarma() == 0))) return;
            _ffffakePlayer.setTarget(threat);
            tryAttackingUsingFighterOffensiveSkill();
            _lastPvPTime = now;
            _inAutoDefend = true;
            _lastTargetSwitchTime = now;
            return;
        }

        if (_inAutoDefend) {
            WorldObject target = _ffffakePlayer.getTarget();
            boolean targetKnown = target != null && _ffffakePlayer.getKnownType(WorldObject.class).contains(target);

            if (target == null || !targetKnown) {
                resetAfterPvP();
                return;
            }

            if (target instanceof Player) {
                Player p = (Player) target;
                if (p.isDead() || p.isGM() || isSameClanOrAlliance(p) || (p.getPvpFlag() == 0 && p.getKarma() == 0)) {
                    resetAfterPvP();
                    return;
                }
            }

            if (target instanceof Creature) {
                Creature creatureTarget = (Creature) target;
                if (creatureTarget.isDead()) {
                    resetAfterPvP();
                    return;
                }
                tryAttackingUsingFighterOffensiveSkill();
                _pvpFailCounter++;
                if (_pvpFailCounter >= MAX_PVP_FAILS) {
                    resetAfterPvP();
                    return;
                }
            }

            if (now - _lastPvPTime >= 10000) {
                resetAfterPvP();
            }

            return;
        }

        setBusyThinking(true);

        applyDefaultBuffs();
        handleShots();
        selfSupportBuffs();

        tryTargetAttackerOrRandomMob(FFFFFakeHelpers.getTestTargetClass(), FFFFakeHelpers.getTestTargetRange());

        WorldObject mobTarget = _ffffakePlayer.getTarget();
        if (mobTarget instanceof Creature) {
            Creature mob = (Creature) mobTarget;
            if (!mob.isDead()) {
                if (now - _lastCastDelayTime < Rnd.get(150, 400)) return;
                tryAttackingUsingFighterOffensiveSkill();
                _lastCastDelayTime = now;
            } else {
                if (now - _lastPostKillDelayTime < Rnd.get(500, 1000)) return;
                _lastPostKillDelayTime = now;
            }
        }

        setBusyThinking(false);
    }

    @Override
    protected ShotType getShotType() {
        return ShotType.SOULSHOT;
    }

    @Override
    protected double changeOfUsingSkill() {
        return 0.25;
    }

    @Override
    protected List<OOOOffensiveSpell> getOffensiveSpells() {
        List<OOOOffensiveSpell> offensiveSpells = new ArrayList<>();
        offensiveSpells.add(new OOOOffensiveSpell(440, 10)); // Blade Hurricane
        return offensiveSpells;
    }

    @Override
    protected List<SSSSupportSpell> getSelfSupportSpells() {
        List<SSSSupportSpell> selfBuffs = new ArrayList<>();
        selfBuffs.add(new SSSSupportSpell(139, 1)); // Dance of Warrior
        selfBuffs.add(new SSSSupportSpell(297, 2)); // Dance of Concentration
        return selfBuffs;
    }

    @Override
    protected int[][] getBuffs() {
        return FFFFakeHelpers.getFighterBuffs();
    }

    @Override
    protected List<HHHHealingSpell> getHealingSpells() {
        return Collections.emptyList();
    }
}
