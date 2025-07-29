package dre.elfocrash.roboto.ai;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dre.elfocrash.roboto.FFFFakePlayer;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.serverpackets.MoveToLocation;
import net.sf.l2j.gameserver.network.serverpackets.MoveToPawn;
import net.sf.l2j.gameserver.network.serverpackets.StopMove;
import net.sf.l2j.gameserver.network.serverpackets.StopRotation;
import net.sf.l2j.gameserver.network.serverpackets.TeleportToLocation;
import net.sf.l2j.gameserver.templates.skills.L2EffectType;

/**
 * @author Elfocrash
 *
 */
public abstract class FFFFakePlayerAI
{
	protected final FFFFakePlayer _ffffakePlayer;		
	protected volatile boolean _clientMoving;
	protected volatile boolean _clientAutoAttacking;
	private long _moveToPawnTimeout;
	protected int _clientMovingToPawnOffset;	
	protected boolean _isBusyThinking = false;
	protected int iterationsOnDeath = 0;
	private final int toVillageIterationsOnDeath = 5;
	
	public FFFFakePlayerAI(FFFFakePlayer character)
	{
		_ffffakePlayer = character;
		setup();
		applyDefaultBuffs();
	}
	
	public void setup() {
		_ffffakePlayer.setIsRunning(true);
	}
	
	protected void applyDefaultBuffs() {
	    for (int[] buff : getBuffs()) {
	        try {
	            L2Effect[] effectsArray = _ffffakePlayer.getAllEffects();
	            Map<Integer, L2Effect> activeEffects = new java.util.HashMap<>();

	            if (effectsArray != null) {
	                for (L2Effect effect : effectsArray) {
	                    if (effect != null && effect.getEffectType() == L2EffectType.BUFF) {
	                        int skillId = effect.getSkill().getId();
	                        // avoid overwriting if already present
	                        activeEffects.putIfAbsent(skillId, effect);
	                    }
	                }
	            }

	            if (!activeEffects.containsKey(buff[0])) {
	                SkillTable.getInstance().getInfo(buff[0], buff[1]).getEffects(_ffffakePlayer, _ffffakePlayer);
	            } else {
	                L2Effect effect = activeEffects.get(buff[0]);
	                if ((effect.getPeriod() - effect.getTime()) <= 20) {
	                    SkillTable.getInstance().getInfo(buff[0], buff[1]).getEffects(_ffffakePlayer, _ffffakePlayer);
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	protected void handleDeath() {
		if(_ffffakePlayer.isDead()) {
			if(iterationsOnDeath >= toVillageIterationsOnDeath) {
				toVillageOnDeath();
			}
			iterationsOnDeath++;
			return;
		}
		
		iterationsOnDeath = 0;		
	}
	
	public void setBusyThinking(boolean thinking) {
		_isBusyThinking = thinking;
	}
	
	public boolean isBusyThinking() {
		return _isBusyThinking;
	}
	
	public void teleportToLocation(int x, int y, int z, int randomOffset) {
		_ffffakePlayer.stopMove(null);
		_ffffakePlayer.abortAttack();
		_ffffakePlayer.abortCast();		
		_ffffakePlayer.setIsTeleporting(true);
		_ffffakePlayer.setTarget(null);		
		_ffffakePlayer.getAI().setIntention(CtrlIntention.ACTIVE);		
		if (randomOffset > 0)
		{
			x += Rnd.get(-randomOffset, randomOffset);
			y += Rnd.get(-randomOffset, randomOffset);
		}		
		z += 5;
		_ffffakePlayer.broadcastPacket(new TeleportToLocation(_ffffakePlayer, x, y, z));
		_ffffakePlayer.decayMe();		
		_ffffakePlayer.setXYZ(x, y, z);
		_ffffakePlayer.onTeleported();		
		_ffffakePlayer.revalidateZone(true);
	}
	
	protected void tryTargetAttackerOrRandomMob(Class<? extends Creature> creatureClass, int radius)
	{
		Creature current = (_ffffakePlayer.getTarget() instanceof Creature)
		    ? (Creature)_ffffakePlayer.getTarget()
		    : null;

		if (current != null && !current.isDead()) {
		    return;
		}

		List<Creature> nearby = _ffffakePlayer.getKnownType(creatureClass).stream()
		    .filter(c -> _ffffakePlayer.isInsideRadius(c, radius, true, false))
		    .collect(Collectors.toList());
	    
	    // Προτεραιότητα: mobs που σε στοχεύουν
	    for (Creature npc : nearby)
	    {
	        if (!npc.isDead() && npc.getTarget() == _ffffakePlayer)
	        {
	            _ffffakePlayer.setTarget(npc);
	            return;
	        }
	    }

	    // Αλλιώς, random στόχος
	    List<Creature> valid = nearby.stream()
	        .filter(x -> !x.isDead())
	        .collect(Collectors.toList());

	    if (!valid.isEmpty()) {
	        _ffffakePlayer.setTarget(valid.get(Rnd.get(valid.size())));
	    }
	}
		
	public void castSpell(L2Skill skill) {
		if(!_ffffakePlayer.isCastingNow()) {		
			
			if (skill.getTargetType() == SkillTargetType.TARGET_GROUND)
			{
				if (maybeMoveToPosition((_ffffakePlayer).getCurrentSkillWorldPosition(), skill.getCastRange()))
				{
					_ffffakePlayer.setIsCastingNow(false);
					return;
				}
			}
			else
			{
				if (checkTargetLost(_ffffakePlayer.getTarget()))
				{
					if (skill.isOffensive() && _ffffakePlayer.getTarget() != null)
						_ffffakePlayer.setTarget(null);
					
					_ffffakePlayer.setIsCastingNow(false);
					return;
				}
				
				if (_ffffakePlayer.getTarget() != null)
				{
					if(maybeMoveToPawn(_ffffakePlayer.getTarget(), skill.getCastRange())) {
						return;
					}
				}
				
				if (_ffffakePlayer.isSkillDisabled(skill)) {
					return;
				}					
			}
			
			if (skill.getHitTime() > 50 && !skill.isSimultaneousCast())
				clientStopMoving(null);
			
			_ffffakePlayer.doCast(skill);
		}else {
			_ffffakePlayer.forceAutoAttack((Creature)_ffffakePlayer.getTarget());
		}
	}
	
	protected void castSelfSpell(L2Skill skill) {
		if(!_ffffakePlayer.isCastingNow() && !_ffffakePlayer.isSkillDisabled(skill)) {		
			
			
			if (skill.getHitTime() > 50 && !skill.isSimultaneousCast())
				clientStopMoving(null);
			
			_ffffakePlayer.doCast(skill);
		}
	}
	
	protected void toVillageOnDeath() {
		if (_ffffakePlayer.isDead())
			_ffffakePlayer.doRevive();
		
		_ffffakePlayer.getFFFFakeAi().teleportToLocation(83478, 148173, -3408, 400);
	}
	
	protected void clientStopMoving(SpawnLocation loc)
	{
		if (_ffffakePlayer.isMoving())
			_ffffakePlayer.stopMove(loc);
		
		_clientMovingToPawnOffset = 0;
		
		if (_clientMoving || loc != null)
		{
			_clientMoving = false;
			
			_ffffakePlayer.broadcastPacket(new StopMove(_ffffakePlayer));
			
			if (loc != null)
				_ffffakePlayer.broadcastPacket(new StopRotation(_ffffakePlayer.getObjectId(), loc.getHeading(), 0));
		}
	}
	
	protected boolean checkTargetLost(WorldObject target)
	{
		if (target instanceof Player)
		{
			final Player victim = (Player) target;
			if (victim.isFakeDeath())
			{
				victim.stopFakeDeath(true);
				return false;
			}
		}
		
		if (target == null)
		{
			_ffffakePlayer.getAI().setIntention(CtrlIntention.ACTIVE);
			return true;
		}
		return false;
	}
	
	protected boolean maybeMoveToPosition(Location worldPosition, int offset)
	{
		if (worldPosition == null)
		{
			return false;
		}
		
		if (offset < 0)
			return false;
			
		if (!_ffffakePlayer.isInsideRadius(worldPosition.getX(), worldPosition.getY(), (int) (offset + _ffffakePlayer.getCollisionRadius()), false))
		{
			if (_ffffakePlayer.isMovementDisabled())
				return true;
			
			int x = _ffffakePlayer.getX();
			int y = _ffffakePlayer.getY();
			
			double dx = worldPosition.getX() - x;
			double dy = worldPosition.getY() - y;
			
			double dist = Math.sqrt(dx * dx + dy * dy);
			
			double sin = dy / dist;
			double cos = dx / dist;
			
			dist -= offset - 5;
			
			x += (int) (dist * cos);
			y += (int) (dist * sin);
			
			moveTo(x, y, worldPosition.getZ());
			return true;
		}

		return false;
	}	
	
	protected void moveToPawn(WorldObject pawn, int offset)
	{
		if (!_ffffakePlayer.isMovementDisabled())
		{
			if (offset < 10)
				offset = 10;
			
			boolean sendPacket = true;
			if (_clientMoving && (_ffffakePlayer.getTarget() == pawn))
			{
				if (_clientMovingToPawnOffset == offset)
				{
					if (System.currentTimeMillis() < _moveToPawnTimeout)
						return;
					
					sendPacket = false;
				}
				else if (_ffffakePlayer.isOnGeodataPath())
				{
					if (System.currentTimeMillis() < _moveToPawnTimeout + 1000)
						return;
				}
			}
			
			_clientMoving = true;
			_clientMovingToPawnOffset = offset;
			_ffffakePlayer.setTarget(pawn);
			_moveToPawnTimeout = System.currentTimeMillis() + 1000;
			
			if (pawn == null)
				return;
			
			_ffffakePlayer.moveToLocation(pawn.getX(), pawn.getY(), pawn.getZ(), offset);
			
			if (!_ffffakePlayer.isMoving())
			{
				return;
			}
			
			if (pawn instanceof Creature)
			{
				if (_ffffakePlayer.isOnGeodataPath())
				{
					_ffffakePlayer.broadcastPacket(new MoveToLocation(_ffffakePlayer));
					_clientMovingToPawnOffset = 0;
				}
				else if (sendPacket)
					_ffffakePlayer.broadcastPacket(new MoveToPawn(_ffffakePlayer, pawn, offset));
			}
			else
				_ffffakePlayer.broadcastPacket(new MoveToLocation(_ffffakePlayer));
		}
	}
	
	public void moveTo(int x, int y, int z)	{
		
		if (!_ffffakePlayer.isMovementDisabled())
		{
			_clientMoving = true;
			_clientMovingToPawnOffset = 0;
			_ffffakePlayer.moveToLocation(x, y, z, 0);
			
			_ffffakePlayer.broadcastPacket(new MoveToLocation(_ffffakePlayer));
			
		}
	}
	
	protected boolean maybeMoveToPawn(WorldObject target, int offset) {
		
		if (target == null || offset < 0)
			return false;
		
		offset += _ffffakePlayer.getCollisionRadius();
		if (target instanceof Creature)
			offset += ((Creature) target).getCollisionRadius();
		
		if (!_ffffakePlayer.isInsideRadius(target, offset, false, false))
		{			
			if (_ffffakePlayer.isMovementDisabled())
			{
				if (_ffffakePlayer.getAI().getIntention() == CtrlIntention.ATTACK)
					_ffffakePlayer.getAI().setIntention(CtrlIntention.IDLE);				
				return true;
			}
			
			if (target instanceof Creature && !(target instanceof Door))
			{
				if (((Creature) target).isMoving())
					offset -= 30;
				
				if (offset < 5)
					offset = 5;
			}
			
			moveToPawn(target, offset);
			
			return true;
		}
		
		if(!GeoEngine.getInstance().canSeeTarget(_ffffakePlayer, _ffffakePlayer.getTarget())){
			_ffffakePlayer.setIsCastingNow(false);
			moveToPawn(target, 50);			
			return true;
		}
		
		
		return false;
	}	
	
	public abstract void thinkAndAct(); 
	protected abstract int[][] getBuffs();
}
