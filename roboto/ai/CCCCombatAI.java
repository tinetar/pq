package dre.elfocrash.roboto.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.model.BBBBotSkill;
import dre.elfocrash.roboto.model.HHHHealingSpell;
import dre.elfocrash.roboto.model.OOOOffensiveSpell;
import dre.elfocrash.roboto.model.SSSSupportSpell;

import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.ShotType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.zone.ZoneId;

public abstract class CCCCombatAI extends FFFFakePlayerAI {

    public CCCCombatAI(FFFFakePlayer character) {
        super(character);
    }

    @Override
    public void thinkAndAct() {
        handleDeath();
    }
    
    public void handleDeath() {
        if (!_ffffakePlayer.isDead()) return;

        _ffffakePlayer.setFakeDeathRespawn(true);
        ThreadPool.schedule(() -> _ffffakePlayer.doReviveAndTeleportIfFake(), 3000);
    }
       
    protected void tryAttackingUsingMageOffensiveSkill() {
        if (_ffffakePlayer.getTarget() != null) {
            BBBBotSkill botSkill = getRandomAvailableMageSpellForTarget();
            if (botSkill == null) return;

            L2Skill skill = _ffffakePlayer.getSkill(botSkill.getSkillId());
            if (skill != null) {
                try {
                    castSpell(skill);
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    protected void tryAttackingUsingFighterOffensiveSkill() {
        if (_ffffakePlayer.getTarget() != null && _ffffakePlayer.getTarget() instanceof Creature) {
            Creature target = (Creature) _ffffakePlayer.getTarget();
            _ffffakePlayer.forceAutoAttack(target);

            if (Rnd.nextDouble() < changeOfUsingSkill()) {
                L2Skill skill = getRandomAvailableFighterSpellForTarget();
                if (skill != null) {
                    try {
                        castSpell(skill);
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
        }
    }

    protected int getShotId() {
        int level = _ffffakePlayer.getLevel();
        if (level < 20) return getShotType() == ShotType.SOULSHOT ? 1835 : 3947;
        if (level < 40) return getShotType() == ShotType.SOULSHOT ? 1463 : 3948;
        if (level < 52) return getShotType() == ShotType.SOULSHOT ? 1464 : 3949;
        if (level < 61) return getShotType() == ShotType.SOULSHOT ? 1465 : 3950;
        if (level < 76) return getShotType() == ShotType.SOULSHOT ? 1466 : 3951;
        return getShotType() == ShotType.SOULSHOT ? 1467 : 3952;
    }

    protected int getArrowId() {
        int level = _ffffakePlayer.getLevel();
        if (level < 20) return 17;
        if (level < 40) return 1341;
        if (level < 52) return 1342;
        if (level < 61) return 1343;
        if (level < 76) return 1344;
        return 1345;
    }

    protected void handleShots() {
        if (_ffffakePlayer.getInventory().getItemByItemId(getShotId()) != null) {
            if (_ffffakePlayer.getInventory().getItemByItemId(getShotId()).getCount() <= 20) {
                _ffffakePlayer.getInventory().addItem("", getShotId(), 20000, _ffffakePlayer, null);
            }
        } else {
            _ffffakePlayer.getInventory().addItem("", getShotId(), 20000, _ffffakePlayer, null);
        }

        if (_ffffakePlayer.getAutoSoulShot().isEmpty()) {
            _ffffakePlayer.addAutoSoulShot(getShotId());
            _ffffakePlayer.rechargeShots(true, true);
        }
    }

    public HHHHealingSpell getRandomAvaiableHealingSpellForTarget() {

        List<HHHHealingSpell> spells = getHealingSpells();
        if (spells.isEmpty()) return null;

        Collections.sort(spells, (a, b) -> Integer.compare(a.getPriority(), b.getPriority()));

        BBBBotSkill chosen = waitAndPickAvailablePrioritisedSpell(spells, spells.size());
        return (HHHHealingSpell) chosen;
    }

    protected BBBBotSkill getRandomAvailableMageSpellForTarget() {
        List<OOOOffensiveSpell> spells = new ArrayList<>(getOffensiveSpells());
        Collections.sort(spells, (a, b) -> Integer.compare(a.getPriority(), b.getPriority()));

        return waitAndPickAvailablePrioritisedSpell(spells, spells.size());
    }

    protected L2Skill getRandomAvailableFighterSpellForTarget() {
        List<OOOOffensiveSpell> spellsOrdered = new ArrayList<>(getOffensiveSpells());
        Collections.sort(spellsOrdered, (a, b) -> Integer.compare(a.getPriority(), b.getPriority()));

        for (OOOOffensiveSpell spell : spellsOrdered) {
            int skillId = spell.getSkillId();
            L2Skill skill = _ffffakePlayer.getSkill(skillId);

            if (skill == null) continue;
            if (_ffffakePlayer.checkUseMagicConditions(skill, true, false)) return skill;
        }

        return null;
    }

    private BBBBotSkill waitAndPickAvailablePrioritisedSpell(List<? extends BBBBotSkill> spells, int size) {
        int i = 0;
        while (i < size) {
            BBBBotSkill chosen = spells.get(i);
            _ffffakePlayer.getCurrentSkill().setCtrlPressed(!_ffffakePlayer.getTarget().isInsideZone(ZoneId.PEACE));
            L2Skill skill = _ffffakePlayer.getSkill(chosen.getSkillId());

            if (skill == null) {
                i++;
                continue;
            }

            if (skill.getCastRange() > 0 && !GeoEngine.getInstance().canSeeTarget(_ffffakePlayer, _ffffakePlayer.getTarget())) {
                moveToPawn(_ffffakePlayer.getTarget(), 100);
                return null;
            }

            if (_ffffakePlayer.checkUseMagicConditions(skill, true, false)) {
                return chosen;
            }

            i++;
        }
        return null;
    }

    protected void selfSupportBuffs() {
        List<Integer> activeEffects = new ArrayList<>();
        L2Effect[] effects = _ffffakePlayer.getAllEffects();

        if (effects != null) {
            for (L2Effect effect : effects) {
                if (effect != null && effect.getSkill() != null) {
                    activeEffects.add(effect.getSkill().getId());
                }
            }
        }

        for (SSSSupportSpell buff : getSelfSupportSpells()) {
            if (activeEffects.contains(buff.getSkillId())) continue;

            L2Skill skill = SkillTable.getInstance().getInfo(
                buff.getSkillId(),
                _ffffakePlayer.getSkillLevel(buff.getSkillId())
            );

            if (skill == null || !_ffffakePlayer.checkUseMagicConditions(skill, true, false)) continue;

            switch (buff.getCondition()) {
                case LESSHPPERCENT:
                    double hpPercent = 100.0 * _ffffakePlayer.getCurrentHp() / _ffffakePlayer.getMaxHp();
                    if (hpPercent <= buff.getConditionValue()) castSelfSpell(skill);
                    break;
                case MISSINGCP:
                    if (getMissingCp() >= buff.getConditionValue()) castSelfSpell(skill);
                    break;
                case NONE:
                    castSelfSpell(skill);
                    break;
            }
        }
    }

    private double getMissingCp() {
        return _ffffakePlayer.getMaxCp() - _ffffakePlayer.getCurrentCp();
    }

    protected double changeOfUsingSkill() {
        return 1.0;
    }

    protected boolean isSameClanOrAlliance(Creature target) {
        if (target == null) return false;
        if (_ffffakePlayer.getActingPlayer() == null || target.getActingPlayer() == null) return false;

        Player bot = _ffffakePlayer.getActingPlayer();
        Player other = target.getActingPlayer();

        return bot.getClanId() > 0 && (
               bot.getClanId() == other.getClanId() ||
               (bot.getAllyId() > 0 && bot.getAllyId() == other.getAllyId()));
    }

    // Abstract methods
    protected abstract ShotType getShotType();
    protected abstract List<OOOOffensiveSpell> getOffensiveSpells();
    protected abstract List<HHHHealingSpell> getHealingSpells();
    protected abstract List<SSSSupportSpell> getSelfSupportSpells();
}
