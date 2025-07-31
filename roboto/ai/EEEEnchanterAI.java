package dre.elfocrash.roboto.ai;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.helpers.FFFFakeHelpers;

import net.sf.l2j.Config;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Weapon;

public class EEEEnchanterAI extends FFFFakePlayerAI {

	private int _enchantIterations = 0;
	private int _maxEnchant = Config.ENCHANT_MAX_WEAPON;
	private final int iterationsForAction = Rnd.get(3,5);
	
	public EEEEnchanterAI(FFFFakePlayer character) {
		super(character);
	}
	
	@Override
	public void setup() {
		super.setup();
		ItemInstance weapon = _ffffakePlayer.getActiveWeaponInstance();		
		weapon = checkIfWeaponIsExistsEquipped(weapon);		
		weapon.setEnchantLevel(0);
		_ffffakePlayer.broadcastCharInfo();
		
	}

	@Override
	public void thinkAndAct() {		
		
		handleDeath();
		setBusyThinking(true);
		if(_enchantIterations % iterationsForAction == 0) {	
			ItemInstance weapon = _ffffakePlayer.getActiveWeaponInstance();		
			weapon = checkIfWeaponIsExistsEquipped(weapon);			
			double chance = getSuccessChance(weapon);		
						
			int currentEnchantLevel = weapon.getEnchantLevel();
			if(currentEnchantLevel < _maxEnchant || serverHasUnlimitedMax()) {
				if (Rnd.nextDouble() < chance || weapon.getEnchantLevel() < 4) {				
					weapon.setEnchantLevel(currentEnchantLevel + 1);
					_ffffakePlayer.broadcastCharInfo();
				} else {				
					destroyFailedItem(weapon);
				}							
			}
		}
		_enchantIterations++;	
		setBusyThinking(false);
	}
	
	private void destroyFailedItem(ItemInstance weapon) {
		_ffffakePlayer.getInventory().destroyItem("Enchant", weapon, _ffffakePlayer, null);
		_ffffakePlayer.broadcastUserInfo();
		_ffffakePlayer.setActiveEnchantItem(null);
	}
	
	private double getSuccessChance(ItemInstance weapon) {
		double chance = 0d;
		if (((Weapon) weapon.getItem()).isMagical())
			chance = (weapon.getEnchantLevel() > 14) ? Config.ENCHANT_CHANCE_WEAPON_MAGIC_15PLUS : Config.ENCHANT_CHANCE_WEAPON_MAGIC;
		else
			chance = (weapon.getEnchantLevel() > 14) ? Config.ENCHANT_CHANCE_WEAPON_NONMAGIC_15PLUS : Config.ENCHANT_CHANCE_WEAPON_NONMAGIC;
		return chance;
	}
	
	private boolean serverHasUnlimitedMax() {
		return _maxEnchant == 0;
	}
	
	private ItemInstance checkIfWeaponIsExistsEquipped(ItemInstance weapon) {
		if(weapon == null) {
			FFFFakeHelpers.giveWeaponsByClass(_ffffakePlayer, false);
			weapon = _ffffakePlayer.getActiveWeaponInstance();
		}
		return weapon;
	}

	@Override
	protected int[][] getBuffs() {
		return new int[0][0];
	}
}
