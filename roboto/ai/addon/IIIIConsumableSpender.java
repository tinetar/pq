package dre.elfocrash.roboto.ai.addon;

import dre.elfocrash.roboto.FFFFakePlayer;

import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

public interface IIIIConsumableSpender {

	default void handleConsumable(FFFFakePlayer ffffakePlayer, int consumableId) {
		if(ffffakePlayer.getInventory().getItemByItemId(consumableId) != null) {
			if(ffffakePlayer.getInventory().getItemByItemId(consumableId).getCount() <= 20) {
				ffffakePlayer.getInventory().addItem("", consumableId, 20000, ffffakePlayer, null);			
				
			}
		}else {
			ffffakePlayer.getInventory().addItem("", consumableId, 20000, ffffakePlayer, null);
			ItemInstance consumable = ffffakePlayer.getInventory().getItemByItemId(consumableId);
			if(consumable.isEquipable())
				ffffakePlayer.getInventory().equipItem(consumable);
		}
	}
}
