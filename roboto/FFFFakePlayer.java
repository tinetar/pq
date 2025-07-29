package dre.elfocrash.roboto;

import java.util.logging.Level;

import dre.elfocrash.roboto.ai.FFFFakePlayerAI;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import dre.elfocrash.roboto.helpers.FFFFakePMResponder;

import net.sf.l2j.gameserver.data.SkillTable.FrequentSkill;
import net.sf.l2j.gameserver.data.manager.CursedWeaponManager;
import net.sf.l2j.gameserver.data.xml.AdminData;
import net.sf.l2j.gameserver.data.xml.MapRegionData;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.instancemanager.SevenSigns;
import net.sf.l2j.gameserver.instancemanager.SevenSigns.CabalType;
import net.sf.l2j.gameserver.instancemanager.SevenSigns.SealType;
import net.sf.l2j.gameserver.model.L2Effect;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillTargetType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.appearance.PcAppearance;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.model.actor.instance.StaticObject;
import net.sf.l2j.gameserver.model.actor.template.PlayerTemplate;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.entity.Siege.SiegeSide;
import net.sf.l2j.gameserver.model.group.Party.MessageType;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.model.pledge.ClanMember;
import net.sf.l2j.gameserver.model.zone.ZoneId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillSiegeFlag;
import net.sf.l2j.gameserver.templates.skills.L2SkillType;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

/**
 * @author Elfocrash
 *
 */
public class FFFFakePlayer extends Player
{
	    private boolean _fakeDeathRespawn = false;

	    public void setFakeDeathRespawn(boolean value) {
	        _fakeDeathRespawn = value;
	    }
	
	    public boolean isFakeDeathRespawn() {
	        return _fakeDeathRespawn;
	    }
	
	    public void doReviveAndTeleportIfFake() {
	        doRevive();
	        if (isFakeDeathRespawn()) {
	            teleToLocation(MapRegionData.getInstance().getLocationToTeleport(this, MapRegionData.TeleportType.TOWN), 20);
	            setFakeDeathRespawn(false);
	        }
	    }
	    
	// Shared responder instance
   private static final FFFFakePMResponder _pmResponder = new FFFFakePMResponder();	
       /**
        * Called when this bot receives a private message.
        * Stores last sender and message, then responds.
        */
       public void onPrivateMessageReceived(String senderName, String messageText) {
           setLastPrivateMessage(senderName, messageText);
           _pmResponder.handleIncomingPM(this, senderName, messageText);
       }
   	
	private FFFFakePlayerAI _ffffakeAi;	
	private boolean _underControl = false;
	private String _lastPMText = "";
	private String _lastPMFrom = "";
	private boolean _gmSpawned = false;
	
	public void setGmSpawned(boolean value)
		{
			_gmSpawned = value;
		}
	
		public boolean isGmSpawned()
		{
			return _gmSpawned;
		}
	
	    public void sendPrivateMessage(String receiverName, String messageText) {	    	
	        Player receiver = World.getInstance().getPlayer(receiverName);
	        
	        if (receiver == null || receiver.equals(this)) {
	           return;
	        }
	        receiver.sendPacket(new CreatureSay(getObjectId(), 2, getName(), messageText));
	        sendPacket(new CreatureSay(getObjectId(), 2, "->" + receiver.getName(), messageText));
	    }

	public boolean isUnderControl() {
		return _underControl;
	}
	
	public void setUnderCCCControl(boolean underControl) {
		_underControl = underControl;
	}
	
	protected FFFFakePlayer(int objectId)
	{
		super(objectId);
	}
	
	public FFFFakePlayer(int objectId, PlayerTemplate template, String accountName, PcAppearance app)
	{
		super(objectId, template, accountName, app);
	}
		
	public FFFFakePlayerAI getFFFFakeAi()
	{
		return _ffffakeAi;
	}
	
	public void setFFFFakeAi(FFFFakePlayerAI _ffffakeAi)
	{
		this._ffffakeAi = _ffffakeAi;
	}
	
	public void assignDefaultAI() {
		try {
			System.out.println("[DEBUG] [" + getName() + "] Assigning AI for classId=" + getClassId());
			setFFFFakeAi(FFFFakeHelpers.getAIbyClassId(getClassId()).getConstructor(FFFFakePlayer.class).newInstance(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	   //  Setter ΟΟ„Ξ±Ξ½ Ξ»Ξ±ΞΌΞ²Ξ¬Ξ½ΞµΟ„Ξ±ΞΉ PM
	public void setLastPrivateMessage(String senderName, String messageText) {
	        _lastPMFrom = senderName;
	        _lastPMText = messageText;
	   }
	
	    //  Getters Ξ³ΞΉΞ± Ο€ΟΟΟƒΞ²Ξ±ΟƒΞ· Ξ±Ο€Ο AI
	   public String getLastPMFrom() {
	        return _lastPMFrom;
	    }
	
	    public String getLastPMText() {
	        return _lastPMText;
	    }	
		
	public boolean checkUseMagicConditions(L2Skill skill, boolean forceUse, boolean dontMove)
	{
		if(skill == null)
			return false;
		
		if (isDead() || isOutOfControl())
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		if(isSkillDisabled(skill))
			return false;
		
		L2SkillType sklType = skill.getSkillType();
		
		if (isFishing() && (sklType != L2SkillType.PUMPING && sklType != L2SkillType.REELING && sklType != L2SkillType.FISHING))
		{
			return false;
		}
		
		if (isInObserverMode())
		{
			abortCast();
			return false;
		}
		
		if (isSitting())
		{
			if (skill.isToggle())
			{
				L2Effect effect = getFirstEffect(skill.getId());
				if (effect != null)
				{
					effect.exit();
					return false;
				}
			}			
			return false;
		}
		
		if (skill.isToggle())
		{
			L2Effect effect = getFirstEffect(skill.getId());
			
			if (effect != null)
			{
				if (skill.getId() != 60)
					effect.exit();
				
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
		}
		
		if (isFakeDeath())
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		WorldObject target = null;
		SkillTargetType sklTargetType = skill.getTargetType();
		Location worldPosition = getCurrentSkillWorldPosition();
		
		if (sklTargetType == SkillTargetType.TARGET_GROUND && worldPosition == null)
		{
			_log.info("WorldPosition is null for skill: " + skill.getName() + ", player: " + getName() + ".");
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		switch (sklTargetType)
		{
			// Target the player if skill type is AURA, PARTY, CLAN or SELF
			case TARGET_AURA:
			case TARGET_FRONT_AURA:
			case TARGET_BEHIND_AURA:
			case TARGET_AURA_UNDEAD:
			case TARGET_PARTY:
			case TARGET_ALLY:
			case TARGET_CLAN:
			case TARGET_GROUND:
			case TARGET_SELF:
			case TARGET_CORPSE_ALLY:
			case TARGET_AREA_SUMMON:
				target = this;
				break;
			case TARGET_PET:
			case TARGET_SUMMON:
				target = getPet();
				break;
			default:
				target = getTarget();
				break;
		}
		
		// Check the validity of the target
		if (target == null)
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		if (target instanceof Door)
		{
			if (!((Door) target).isAutoAttackable(this) // Siege doors only hittable during siege
				|| (((Door) target).isUnlockable() && skill.getSkillType() != L2SkillType.UNLOCK)) // unlockable doors
			{
				sendPacket(SystemMessageId.INCORRECT_TARGET);
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
		}
		
		// Are the target and the player in the same duel?
		if (isInDuel())
		{
			if (target instanceof Playable)
			{
				// Get Player
				Player cha = target.getActingPlayer();
				if (cha.getDuelId() != getDuelId())
				{
					sendPacket(SystemMessageId.INCORRECT_TARGET);
					sendPacket(ActionFailed.STATIC_PACKET);
					return false;
				}
			}
		}
		
		// ************************************* Check skill availability *******************************************
		
		// Siege summon checks. Both checks send a message to the player if it return false.
		if (skill.isSiegeSummonSkill())
		{
			final Siege siege = CastleManager.getInstance().getActiveSiege(this);
			if (siege == null || !siege.checkSide(getClan(), SiegeSide.ATTACKER) || (isInSiege() && isInsideZone(ZoneId.CASTLE)))
			{
				sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_CALL_PET_FROM_THIS_LOCATION));
				return false;
			}
			
			if (SevenSigns.getInstance().isSealValidationPeriod() && SevenSigns.getInstance().getSealOwner(SealType.STRIFE) == CabalType.DAWN && SevenSigns.getInstance().getPlayerCabal(getObjectId()) == CabalType.DUSK)
			{
				sendPacket(SystemMessageId.SEAL_OF_STRIFE_FORBIDS_SUMMONING);
				return false;
			}
		}
		
		// ************************************* Check casting conditions *******************************************
		
		// Check if all casting conditions are completed
		if (!skill.checkCondition(this, target, false))
		{
			// Send ActionFailed to the Player
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		// ************************************* Check Skill Type *******************************************
		
		// Check if this is offensive magic skill
		if (skill.isOffensive())
		{
			if (isInsidePeaceZone(this, target))
			{
				// If Creature or target is in a peace zone, send a system message TARGET_IN_PEACEZONE ActionFailed
				sendPacket(SystemMessageId.TARGET_IN_PEACEZONE);
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
			
			if (isInOlympiadMode() && !isOlympiadStart())
			{
				// if Player is in Olympia and the match isn't already start, send ActionFailed
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
			
			// Check if the target is attackable
			if (!target.isAttackable() && !getAccessLevel().allowPeaceAttack())
			{
				// If target is not attackable, send ActionFailed
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
			
			// Check if a Forced ATTACK is in progress on non-attackable target
			if (!target.isAutoAttackable(this) && !forceUse)
			{
				switch (sklTargetType)
				{
					case TARGET_AURA:
					case TARGET_FRONT_AURA:
					case TARGET_BEHIND_AURA:
					case TARGET_AURA_UNDEAD:
					case TARGET_CLAN:
					case TARGET_ALLY:
					case TARGET_PARTY:
					case TARGET_SELF:
					case TARGET_GROUND:
					case TARGET_CORPSE_ALLY:
					case TARGET_AREA_SUMMON:
						break;
					default: // Send ActionFailed to the Player
						sendPacket(ActionFailed.STATIC_PACKET);
						return false;
				}
			}
			
			// Check if the target is in the skill cast range
			if (dontMove)
			{
				// Calculate the distance between the Player and the target
				if (sklTargetType == SkillTargetType.TARGET_GROUND)
				{
					if (!isInsideRadius(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), (int) (skill.getCastRange() + getCollisionRadius()), false, false))
					{
						// Send a System Message to the caster
						sendPacket(SystemMessageId.TARGET_TOO_FAR);
						
						// Send ActionFailed to the Player
						sendPacket(ActionFailed.STATIC_PACKET);
						return false;
					}
				}
				else if (skill.getCastRange() > 0 && !isInsideRadius(target, (int) (skill.getCastRange() + getCollisionRadius()), false, false))
				{
					// Send a System Message to the caster
					sendPacket(SystemMessageId.TARGET_TOO_FAR);
					
					// Send ActionFailed to the Player
					sendPacket(ActionFailed.STATIC_PACKET);
					return false;
				}
			}
		}
		
		// Check if the skill is defensive
		if (!skill.isOffensive() && target instanceof Monster && !forceUse)
		{
			// check if the target is a monster and if force attack is set.. if not then we don't want to cast.
			switch (sklTargetType)
			{
				case TARGET_PET:
				case TARGET_SUMMON:
				case TARGET_AURA:
				case TARGET_FRONT_AURA:
				case TARGET_BEHIND_AURA:
				case TARGET_AURA_UNDEAD:
				case TARGET_CLAN:
				case TARGET_SELF:
				case TARGET_CORPSE_ALLY:
				case TARGET_PARTY:
				case TARGET_ALLY:
				case TARGET_CORPSE_MOB:
				case TARGET_AREA_CORPSE_MOB:
				case TARGET_GROUND:
					break;
				default:
				{
					switch (sklType)
					{
						case BEAST_FEED:
						case DELUXE_KEY_UNLOCK:
						case UNLOCK:
							break;
						default:
							sendPacket(ActionFailed.STATIC_PACKET);
							return false;
					}
					break;
				}
			}
		}
		
		// Check if the skill is Spoil type and if the target isn't already spoiled
		if (sklType == L2SkillType.SPOIL)
		{
			if (!(target instanceof Monster))
			{
				// Send a System Message to the Player
				sendPacket(SystemMessageId.INCORRECT_TARGET);
				
				// Send ActionFailed to the Player
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
		}
		
		// Check if the skill is Sweep type and if conditions not apply
		if (sklType == L2SkillType.SWEEP && target instanceof Attackable)
		{
			if (((Attackable) target).isDead())
			{
				final int spoilerId = ((Attackable) target).getSpoilerId();
				if (spoilerId == 0)
				{
					// Send a System Message to the Player
					sendPacket(SystemMessageId.SWEEPER_FAILED_TARGET_NOT_SPOILED);
					
					// Send ActionFailed to the Player
					sendPacket(ActionFailed.STATIC_PACKET);
					return false;
				}
				
				if (!isLooterOrInLooterParty(spoilerId))
				{
					// Send a System Message to the Player
					sendPacket(SystemMessageId.SWEEP_NOT_ALLOWED);
					
					// Send ActionFailed to the Player
					sendPacket(ActionFailed.STATIC_PACKET);
					return false;
				}
			}
		}
		
		// Check if the skill is Drain Soul (Soul Crystals) and if the target is a MOB
		if (sklType == L2SkillType.DRAIN_SOUL)
		{
			if (!(target instanceof Monster))
			{
				// Send a System Message to the Player
				sendPacket(SystemMessageId.INCORRECT_TARGET);
				
				// Send ActionFailed to the Player
				sendPacket(ActionFailed.STATIC_PACKET);
				return false;
			}
		}
		
		// Check if this is a Pvp skill and target isn't a non-flagged/non-karma player
		switch (sklTargetType)
		{
			case TARGET_PARTY:
			case TARGET_ALLY: // For such skills, checkPvpSkill() is called from L2Skill.getTargetList()
			case TARGET_CLAN: // For such skills, checkPvpSkill() is called from L2Skill.getTargetList()
			case TARGET_AURA:
			case TARGET_FRONT_AURA:
			case TARGET_BEHIND_AURA:
			case TARGET_AURA_UNDEAD:
			case TARGET_GROUND:
			case TARGET_SELF:
			case TARGET_CORPSE_ALLY:
			case TARGET_AREA_SUMMON:
				break;
			default:
				if (!checkPvpSkill(target, skill) && !getAccessLevel().allowPeaceAttack())
				{
					// Send a System Message to the Player
					sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					
					// Send ActionFailed to the Player
					sendPacket(ActionFailed.STATIC_PACKET);
					return false;
				}
		}
		
		if ((sklTargetType == SkillTargetType.TARGET_HOLY && !checkIfOkToCastSealOfRule(CastleManager.getInstance().getCastle(this), false, skill, target)) || (sklType == L2SkillType.SIEGEFLAG && !L2SkillSiegeFlag.checkIfOkToPlaceFlag(this, false)) || (sklType == L2SkillType.STRSIEGEASSAULT && !checkIfOkToUseStriderSiegeAssault(skill)) || (sklType == L2SkillType.SUMMON_FRIEND && !(checkSummonerStatus(this) && checkSummonTargetStatus(target, this))))
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			abortCast();
			return false;
		}
		
		
		// finally, after passing all conditions
		return true;
	}
	
	public void forceAutoAttack(Creature creature) {
		if(this.getTarget() == null)
			return;
		
		if (isInsidePeaceZone(this, this.getTarget()))
		{
			return;
		}
		
		if (isInOlympiadMode() && getTarget() != null && getTarget() instanceof Playable)
		{
			Player target = getTarget().getActingPlayer();
			if (target == null || (target.isInOlympiadMode() && (!isOlympiadStart() || getOlympiadGameId() != target.getOlympiadGameId())))
			{
				return;
			}
		}
		
		if (getTarget() != null && !getTarget().isAttackable() && !getAccessLevel().allowPeaceAttack())
		{
			return;
		}
		
		if (isConfused())
		{
			return;
		}
		
		// GeoData Los Check or dz > 1000
		if (!GeoEngine.getInstance().canSeeTarget(this,this.getTarget()))
		{
			return;
		}
		
		// Notify AI with ATTACK
		getAI().setIntention(CtrlIntention.ATTACK, this.getTarget());
	}
	
	public synchronized void despawnPlayer()
	{
		try
		{
			// Put the online status to false
			setOnlineStatus(false, true);
			
			// abort cast & attack and remove the target. Cancels movement aswell.
			abortAttack();
			abortCast();
			stopMove(null);
			setTarget(null);
			
			removeMeFromPartyMatch();
			
			if (isFlying())
				removeSkill(FrequentSkill.WYVERN_BREATH.getSkill().getId(), false);
			
			// Stop all scheduled tasks
			stopAllTimers();
			
			// Cancel the cast of eventual fusion skill users on this target.
			for (Creature character : getKnownType(Creature.class))
				if (character.getFusionSkill() != null && character.getFusionSkill().getTarget() == this)
					character.abortCast();
				
			// Stop signets & toggles effects.
			for (L2Effect effect : getAllEffects())
			{
				if (effect.getSkill().isToggle())
				{
					effect.exit();
					continue;
				}
				
				switch (effect.getEffectType())
				{
					case SIGNET_GROUND:
					case SIGNET_EFFECT:
						effect.exit();
						break;
				default:
					break;
				}
			}
			
			// Remove the Player from the world
			decayMe();
			
			// If a party is in progress, leave it
			if (getParty() != null)
				getParty().removePartyMember(this, MessageType.DISCONNECTED);
			
			// If the Player has Pet, unsummon it
			if (getPet() != null)
				getPet().unSummon(this);
			
			// Handle removal from olympiad game
			if (OlympiadManager.getInstance().isRegistered(this) || getOlympiadGameId() != -1)
				OlympiadManager.getInstance().removeDisconnectedCompetitor(this);
			
			// set the status for pledge member list to OFFLINE
			if (getClan() != null)
			{
				ClanMember clanMember = getClan().getClanMember(getObjectId());
				if (clanMember != null)
					clanMember.setPlayerInstance(null);
			}
			
			// deals with sudden exit in the middle of transaction
			if (getActiveRequester() != null)
			{
				setActiveRequester(null);
				cancelActiveTrade();
			}
			
			// If the Player is a GM, remove it from the GM List
			if (isGM())
				AdminData.getInstance().deleteGm(this);
			
			// Check if the Player is in observer mode to set its position to its position before entering in observer mode
			if (isInObserverMode())
				setXYZInvisible(getSavedLocation());
			
			// Oust player from boat
			if (getVehicle() != null)
				getVehicle().oustPlayer(this, true, Location.DUMMY_LOC);
			
			// Update inventory and remove them from the world
			getInventory().deleteMe();
			
			// Update warehouse and remove them from the world
			clearWarehouse();
			
			// Update freight and remove them from the world
			clearFreight();
			clearDepositedFreight();
			
			if (isCursedWeaponEquipped())
				CursedWeaponManager.getInstance().getCursedWeapon(getCursedWeaponEquippedId()).setPlayer(null);
			
			if (getClanId() > 0)
				getClan().broadcastToOtherOnlineMembers(new PledgeShowMemberListUpdate(this), this);
			
			if (isSeated())
			{
				final WorldObject object = World.getInstance().getObject(_throneId);
				if (object instanceof StaticObject)
					((StaticObject) object).setBusy(false);
			}
			
			World.getInstance().removePlayer(this); // force remove in case of crash during teleport
			
			// friends & blocklist update
			notifyFriends(false);
			getBlockList().playerLogout();
		}
		catch (Exception e)
		{
			_log.log(Level.WARNING, "Exception on deleteMe()" + e.getMessage(), e);
		}
	}

	public void heal() {
	    setCurrentCp(getMaxCp());
	    setCurrentHp(getMaxHp());
	    setCurrentMp(getMaxMp());
	}   
	public boolean isSameClanOrAlliance(Player other) {
	    if (other == null) return false;
	    if (getClanId() > 0 && getClanId() == other.getClanId()) return true;
	    if (getAllyId() > 0 && getAllyId() == other.getAllyId()) return true;
	    return false;
	}

	public void refreshPvpFlagIfInZone() {
	    if (!isGmSpawned()) return;
	    if (getPvpFlag() == 0) {
	        setPvpFlag(1);
	    }
	}
}
