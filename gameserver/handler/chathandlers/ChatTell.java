package net.sf.l2j.gameserver.handler.chathandlers;

import com.elfocrash.roboto.FakePlayer;
//import compvp.elfocrash.roboto.FFakePlayer;
//import dyn.elfocrash.roboto.FFFakePlayer;
//import dre.elfocrash.roboto.FFFFakePlayer;

import net.sf.l2j.gameserver.handler.IChatHandler;
import net.sf.l2j.gameserver.model.BlockList;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;

public class ChatTell implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		2
	};
	
	@Override
	public void handleChat(int type, Player activeChar, String target, String text)
	{
		if (target == null)
			return;
		
		final Player receiver = World.getInstance().getPlayer(target);
		//if (receiver == null || receiver.getClient().isDetached())			
     	//if (receiver == null || receiver.getClient().isDetached() && !(receiver instanceof FakePlayer) && !(receiver instanceof FFakePlayer) && !(receiver instanceof FFFakePlayer) && !(receiver instanceof FFFFakePlayer))		
		//if (receiver == null || receiver != null && receiver.getClient().isDetached() && !(receiver instanceof FakePlayer))
		
		if (receiver == null || (receiver.getClient() != null && receiver.getClient().isDetached() && !(receiver instanceof FakePlayer)))	
		{
			activeChar.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			return;
		}
		
		if (activeChar.equals(receiver))
		{
			activeChar.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}
		
		if (receiver.isInJail() || receiver.isChatBanned())
		{
			activeChar.sendPacket(SystemMessageId.TARGET_IS_CHAT_BANNED);
			return;
		}
		
		if (!activeChar.isGM() && (receiver.isInRefusalMode() || BlockList.isBlocked(receiver, activeChar)))
		{
			activeChar.sendPacket(SystemMessageId.THE_PERSON_IS_IN_MESSAGE_REFUSAL_MODE);
			return;
		}
		
		    //  Ξ‘Ξ½ ΞΏ Ξ±Ο€ΞΏΞ΄Ξ­ΞΊΟ„Ξ·Ο‚ ΞµΞ―Ξ½Ξ±ΞΉ bot β†’ Ξ±Ο€ΞΏΞΈΞ·ΞΊΞµΟΞΏΟ…ΞΌΞµ Ο„ΞΏ PM Ξ³ΞΉΞ± Ξ½Ξ± Ξ±Ο€Ξ±Ξ½Ο„Ξ®ΟƒΞµΞΉ
		    if (receiver instanceof dre.elfocrash.roboto.FFFFakePlayer) {
		        ((dre.elfocrash.roboto.FFFFakePlayer) receiver).setLastPrivateMessage(activeChar.getName(), text);
		        System.out.println("[PM DEBUG] Saved PM for bot '" + receiver.getName() + "' β† From: " + activeChar.getName() + " | Text: " + text);
		    }		
				
		receiver.sendPacket(new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text));
		activeChar.sendPacket(new CreatureSay(activeChar.getObjectId(), type, "->" + receiver.getName(), text));
	}
	
	@Override
	public int[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}
