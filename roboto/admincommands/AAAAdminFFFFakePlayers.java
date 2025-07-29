package dre.elfocrash.roboto.admincommands;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.FFFFakePlayerManager;
import dre.elfocrash.roboto.FFFFakePlayerTaskManager;
import dre.elfocrash.roboto.ai.EEEEnchanterAI;
import dre.elfocrash.roboto.ai.walker.GGGGiranWalkerAI;

import java.util.List;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * @author Elfocrash
 *
 */
public class AAAAdminFFFFakePlayers implements IAdminCommandHandler
{
	private final String ffffakesFolder = "data/html/admin/ffffakeplayers/";
	 
	private static final String[] ADMIN_COMMANDS =
	 {
	     "admin_fffftakecontrol",
	     "admin_ffffreleasecontrol",
	     "admin_ffffakes",
	     "admin_ffffspawnrandom",
	     "admin_ffffdeletefake",
	     "admin_ffffspawnenchanter",
	     "admin_ffffspawnwalker",
	     "admin_tpbot",
	     "admin_deletebot",
	     "admin_refreshbotlists",
	     "admin_gmpanel",
	     "admin_autopanel",
	     "admin_refreshgm",
	     "admin_refreshauto",
	     "admin_gmpage",
	     "admin_autopage",
	     "admin_cleanghosts"
	 };
	
	private String getGMSpawnedBotsPage(int page) {
	    List<String> list = FFFFakePlayerManager.INSTANCE.getLatestGMSpawnedBotNames();
	    int start = (page - 1) * 20;
	    int end = Math.min(start + 20, list.size());
	    StringBuilder sb = new StringBuilder();
	    for (int i = start; i < end; i++) {
	        String name = list.get(i);
	        sb.append("<tr><td><font color=\"00FF00\">" + name + "</font></td></tr>");
	        sb.append("<tr><td><a action=\"bypass admin_tpbot " + name + "\">Teleport</a></td></tr>");
	        sb.append("<tr><td><a action=\"bypass admin_deletebot " + name + "\">Delete</a></td></tr>");
	        sb.append("<tr><td><hr></td></tr>");
	    }
	    return sb.toString();
	}

	private String getAutoSpawnedBotsPage(int page) {
	    List<String> list = FFFFakePlayerManager.INSTANCE.getLatestAutoSpawnedBotNames();
	    int start = (page - 1) * 20;
	    int end = Math.min(start + 20, list.size());
	    StringBuilder sb = new StringBuilder();
	    for (int i = start; i < end; i++) {
	        String name = list.get(i);
	        sb.append("<tr><td><font color=\"00CCCC\">" + name + "</font></td></tr>");
	        sb.append("<tr><td><a action=\"bypass admin_tpbot " + name + "\">Teleport</a></td></tr>");
	        sb.append("<tr><td><a action=\"bypass admin_deletebot " + name + "\">Delete</a></td></tr>");
	        sb.append("<tr><td><hr></td></tr>");
	    }
	    return sb.toString();
	}
	
	    private String getGMSpawnedBotsHTML() {
		        StringBuilder sb = new StringBuilder();
		            for (String name : FFFFakePlayerManager.INSTANCE.getLatestGMSpawnedBotNames()) {
		        	       sb.append("<tr><td><font color=\"00FF00\">" + name + "</font></td></tr>");
		                sb.append("<tr><td><a action=\"bypass admin_tpbot " + name + "\">Teleport</a></td></tr>");
		        	        sb.append("<tr><td><a action=\"bypass admin_deletebot " + name + "\">Delete</a></td></tr>");
		        	       sb.append("<tr><td><hr></td></tr>");

		        }
		       return sb.toString();
		    }

		    private String getAutoSpawnedBotsHTML() {
		        StringBuilder sb = new StringBuilder();
		            for (String name : FFFFakePlayerManager.INSTANCE.getLatestAutoSpawnedBotNames()) {
		        	       sb.append("<tr><td><font color=\"00CCCC\">" + name + "</font></td></tr>");
		        	        sb.append("<tr><td><a action=\"bypass admin_tpbot " + name + "\">Teleport</a></td></tr>");
		        	        sb.append("<tr><td><a action=\"bypass admin_deletebot " + name + "\">Delete</a></td></tr>");
		        	        sb.append("<tr><td><hr></td></tr>");
		        	 
		        }
		        return sb.toString();
		    }
	
	   private void tryTeleportToBot(Player gm, String botName) {
		        for (FFFFakePlayer bot : FFFFakePlayerManager.INSTANCE.getFFFFakePlayers()) {
		            if (bot.getName().equalsIgnoreCase(botName)) {
		                gm.teleToLocation(bot.getX(), bot.getY(), bot.getZ(), 0);
		                gm.sendMessage("Teleported to bot: " + botName);
		                return;
		            }
		        }
		        gm.sendMessage("Bot '" + botName + "' not found.");
		    }
		
		    private void tryDeleteBotByName(Player gm, String botName) {
		        for (FFFFakePlayer bot : FFFFakePlayerManager.INSTANCE.getFFFFakePlayers()) {
		            if (bot.getName().equalsIgnoreCase(botName)) {
		                bot.despawnPlayer();
		                gm.sendMessage("Deleted bot: " + botName);
		                return;
		            }
		        }
		        gm.sendMessage("Bot '" + botName + "' not found.");
		    }
	
	private static int classDesired = 0;
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private void showFakeDashboard(Player activeChar) {
	    final NpcHtmlMessage html = new NpcHtmlMessage(0);
	    html.setFile(ffffakesFolder + "index.htm");
	    html.replace("%ffffakecount%", FFFFakePlayerManager.INSTANCE.getFFFFakePlayersCount());
	    html.replace("%fffftaskcount%", FFFFakePlayerTaskManager.INSTANCE.getTaskCount());

	    StringBuilder gmBotListBuilder = new StringBuilder();
	    for (String name : FFFFakePlayerManager.INSTANCE.getLatestGMSpawnedBotNames()) {
	        gmBotListBuilder.append("<tr><td>");
	        gmBotListBuilder.append("<font color=\"00FF00\">" + name + "</font>");
	        gmBotListBuilder.append(" • <a action=\"bypass admin_tpbot " + name + "\">Teleport</a>");
	        gmBotListBuilder.append(" • <a action=\"bypass admin_deletebot " + name + "\">Delete</a>");
	        gmBotListBuilder.append("</td></tr>");
	    }
	    html.replace("%gmspawnlist%", gmBotListBuilder.toString());

	    StringBuilder autoBotListBuilder = new StringBuilder();
	    for (String name : FFFFakePlayerManager.INSTANCE.getLatestAutoSpawnedBotNames()) {
	        autoBotListBuilder.append("<tr><td>");
	        autoBotListBuilder.append("<font color=\"00CCCC\">" + name + "</font>");
	        autoBotListBuilder.append(" • <a action=\"bypass admin_tpbot " + name + "\">Teleport</a>");
	        autoBotListBuilder.append(" • <a action=\"bypass admin_deletebot " + name + "\">Delete</a>");
	        autoBotListBuilder.append("</td></tr>");
	    }
	    html.replace("%autospawnlist%", autoBotListBuilder.toString());

	    activeChar.sendPacket(html);
	}

	
	@Override
	public boolean useAdminCommand(String command, Player activeChar)
	{
	    if (command.equals("admin_cleanghosts")) {
	        FFFFakePlayerManager.INSTANCE.cleanGhosts();
	        activeChar.sendMessage("All ghost bots have been cleaned.");
	        return true;
	    } 
	    
		if (command.startsWith("admin_gmpage ")) {
		    int page = Integer.parseInt(command.substring("admin_gmpage ".length()).trim());
		    NpcHtmlMessage html = new NpcHtmlMessage(0);
		    html.setFile(ffffakesFolder + "gmpanel.htm");
		    html.replace("%gmspawnlist%", getGMSpawnedBotsPage(page));
		       int total = FFFFakePlayerManager.INSTANCE.getLatestGMSpawnedBotNames().size();
		       int totalPages = (int) Math.ceil(total / 20.0);		   
		       if (page < 1) page = 1;
		       if (page > totalPages) page = totalPages;
		       String prevBtn = page > 1 ? "<a action=\"bypass admin_gmpage " + (page - 1) + "\">« Prev</a>" : "";
		       String nextBtn = page < totalPages ? "<a action=\"bypass admin_gmpage " + (page + 1) + "\">Next »</a>" : "";
		       html.replace("%prevpage%", prevBtn);
		       html.replace("%nextpage%", nextBtn);
		       html.replace("%pageinfo%", "Page " + page + " of " + totalPages);	     
		    activeChar.sendPacket(html);
		    return true;
		}

		if (command.startsWith("admin_autopage ")) {
		    int page = Integer.parseInt(command.substring("admin_autopage ".length()).trim());
		    NpcHtmlMessage html = new NpcHtmlMessage(0);
		    html.setFile(ffffakesFolder + "autopanel.htm");
		    html.replace("%autospawnlist%", getAutoSpawnedBotsPage(page));
		      int total = FFFFakePlayerManager.INSTANCE.getLatestAutoSpawnedBotNames().size();
		      int totalPages = (int) Math.ceil(total / 20.0);
		       if (page < 1) page = 1;
		       if (page > totalPages) page = totalPages;
		       String prevPageBtn = page > 1 ? "<a action=\"bypass admin_autopage " + (page - 1) + "\">« Prev</a>" : "";
		       String nextPageBtn = page < totalPages ? "<a action=\"bypass admin_autopage " + (page + 1) + "\">Next »</a>" : "";
		       html.replace("%prevpage%", prevPageBtn);
		       html.replace("%nextpage%", nextPageBtn);
		       html.replace("%pageinfo%", "Page " + page + " of " + totalPages);
		    activeChar.sendPacket(html);
		    return true;
		}

		    if (command.equals("admin_refreshgm")) {
		        NpcHtmlMessage html = new NpcHtmlMessage(0);
		        html.setFile(ffffakesFolder + "gmpanel.htm");
		        html.replace("%gmspawnlist%", getGMSpawnedBotsHTML());
		        activeChar.sendPacket(html);
		        return true;
		    }

		    if (command.equals("admin_refreshauto")) {
		        NpcHtmlMessage html = new NpcHtmlMessage(0);
		        html.setFile(ffffakesFolder + "autopanel.htm");
		        html.replace("%autospawnlist%", getAutoSpawnedBotsHTML());
		        activeChar.sendPacket(html);
		        return true;
		    }

		        if (command.equals("admin_gmpanel")) {
			            NpcHtmlMessage html = new NpcHtmlMessage(0);
			            html.setFile(ffffakesFolder + "gmpanel.htm");
			            html.replace("%gmspawnlist%", getGMSpawnedBotsHTML());
			            activeChar.sendPacket(html);
			            return true;
			        }

			        if (command.equals("admin_autopanel")) {
			            NpcHtmlMessage html = new NpcHtmlMessage(0);
			            html.setFile(ffffakesFolder + "autopanel.htm");
			            html.replace("%autospawnlist%", getAutoSpawnedBotsHTML());
			            activeChar.sendPacket(html);
			            return true;
			        }
				
		        if (command.startsWith("admin_tpbot ")) {
		            String botName = command.substring("admin_tpbot ".length()).trim();
		            activeChar.sendMessage(">> Received Teleport for bot: '" + botName + "'");
		            tryTeleportToBot(activeChar, botName);
		            return true;
		       }
		
		        if (command.startsWith("admin_deletebot ")) {
		            String botName = command.substring("admin_deletebot ".length()).trim();
		            boolean success = FFFFakePlayerManager.INSTANCE.deleteFakeBotByName(botName);
		            if (success) {
		                activeChar.sendMessage("ok Bot delete [" + botName + "] deleted successfully.");
		            } else {
		                activeChar.sendMessage("x Bot [" + botName + "] not found.");
		            }
		            return true;
		        }
		
		        if (command.startsWith("admin_refreshbotlists")) {
		            showFakeDashboard(activeChar);
		            return true;
		        }
	
		if (command.startsWith("admin_ffffakes"))
		{
			showFakeDashboard(activeChar);
		}
		
		if(command.startsWith(ADMIN_COMMANDS[4])) {
			
			String subStr = command.substring(ADMIN_COMMANDS[4].length()).trim();
			boolean matchesHtm = command.matches(".*[hH][tT][mM].*");
			if(matchesHtm)
				subStr = subStr.replaceFirst(" [hH][tT][mM]", "");
			
			if(subStr.length() <= 0)
			{
				if(activeChar.getTarget() != null && activeChar.getTarget() instanceof FFFFakePlayer) {
					FFFFakePlayer ffffakePlayer = (FFFFakePlayer)activeChar.getTarget();
					ffffakePlayer.despawnPlayer();
				}
			}else {
				if(subStr.matches(".*[aA][lL][lL].*"))
				{
				FFFFakePlayerManager.INSTANCE.deleteAllFFFFakes();
				}else if(matchesHtm || subStr.length() > 0)
				{
					kazkaVeikiam(subStr, activeChar, false);
				}
			}
			
			if(matchesHtm)
				showFakeDashboard(activeChar);
			return true;
		}
		
		if(command.startsWith("admin_ffffspawnwalker")) {
			if(command.contains(" ")) {
				String locationName = command.split(" ")[1];
				FFFFakePlayer ffffakePlayer = FFFFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ(), classDesired);				
				switch(locationName) {
					case "giran":
						ffffakePlayer.setFFFFakeAi(new GGGGiranWalkerAI(ffffakePlayer));
						ffffakePlayer.setGmSpawned(true);
					break;
				}
				return true;
			}
			
			return true;
		}
		
		if(command.startsWith("admin_ffffspawnenchanter")) {
			FFFFakePlayer ffffakePlayer = FFFFakePlayerManager.INSTANCE.spawnPlayer(activeChar.getX(),activeChar.getY(),activeChar.getZ(), classDesired);
			ffffakePlayer.setGmSpawned(true);
			ffffakePlayer.setFFFFakeAi(new EEEEnchanterAI(ffffakePlayer));
			return true;
		}
		
		// Spawn random
		if (command.startsWith(ADMIN_COMMANDS[3])) {
			
			String subStr = command.substring(ADMIN_COMMANDS[3].length());
			boolean matchesHtm = subStr.matches(".*[hH][tT][mM].*");
			if(matchesHtm)
				subStr = subStr.replaceFirst(" [hH][tT][mM]", "");
			
			kazkaVeikiam(subStr, activeChar, true);

			if(matchesHtm)
				showFakeDashboard(activeChar);
			
			return true;
		}
		
		if (command.startsWith("admin_fffftakecontrol"))
		{
			if(activeChar.getTarget() != null && activeChar.getTarget() instanceof FFFFakePlayer) {
				FFFFakePlayer ffffakePlayer = (FFFFakePlayer)activeChar.getTarget();
				ffffakePlayer.setUnderCCCControl(true);
				activeChar.setPlayerUnderCCCControl(ffffakePlayer);
				activeChar.sendMessage("You are now controlling: " + ffffakePlayer.getName());
				return true;
			}
			
			activeChar.sendMessage("You can only take control of a Fake Player");
			return true;
		}
		if (command.startsWith("admin_ffffreleasecontrol"))
		{
			if(activeChar.isCCCControllingFFFFakePlayer()) {
				FFFFakePlayer ffffakePlayer = activeChar.getPlayerUnderCCCControl();
				activeChar.sendMessage("You are no longer controlling: " + ffffakePlayer.getName());
				ffffakePlayer.setUnderCCCControl(false);
				activeChar.setPlayerUnderCCCControl(null);
				return true;
			}
			
			activeChar.sendMessage("You are not controlling a Fake Player");
			return true;
		}
		return true;
	}
	
	/*
	 * sitas metodas atsirenka kas yra radius ir kas yra count kai ivedi tarp html'o ir paspaudi
	 */
	private static void kazkaVeikiam(String subStr, Player activeChar, boolean spawn) {
		int fakeCount = 1, fakeRadius = 0;
		if(!spawn) {
			fakeCount = -1;
		}

		String[] subStrItems = subStr.trim().split(" ");
		if(subStrItems.length <= 0 || subStrItems.length == 1 && subStrItems[0].equals("#"))
		{
		if(spawn)
			    FFFFakePlayerManager.INSTANCE.spawnPlayers(fakeCount, fakeRadius, activeChar.getX(), activeChar.getY(), activeChar.getZ(), classDesired, true);
			else
				FFFFakePlayerManager.INSTANCE.deleteAllFFFFakes(fakeCount, fakeRadius, activeChar.getX(), activeChar.getY());//triname skaiciu
			
			return;
		}else if(subStrItems.length == 2 && subStrItems[0].equals("#")) {
			try {
				fakeRadius = Integer.parseInt(subStrItems[1]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the radius in wrong format '" + subStrItems[1] + "'");
			}
		}else if(subStrItems.length == 2 && subStrItems[1].equals("#")) {
			try {			
			
				fakeCount = Integer.parseInt(subStrItems[0]);
			}catch (NumberFormatException e) {
				
				activeChar.sendMessage("You've entered the count in a wrong format '" + subStrItems[0] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[0].equals("#")) {
			try {			
				fakeRadius = Integer.parseInt(subStrItems[1]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the radius in wrong format '" + subStrItems[1] + "'");
			}
			try {
				classDesired = Integer.parseInt(subStrItems[2]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered invalid class id '" + subStrItems[2] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[1].equals("#")) {
			try {			
				
				fakeCount = Integer.parseInt(subStrItems[0]);
			}catch (NumberFormatException e) {
				
				activeChar.sendMessage("You've entered the count in a wrong format '" + subStrItems[0] + "'");
			}
			try {
				classDesired = Integer.parseInt(subStrItems[2]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered invalid class id '" + subStrItems[2] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[2].equals("#")) {
			try {
				fakeCount = Integer.parseInt(subStrItems[0]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the count in a wrong format '" + subStrItems[0] + "'");
			}
			try {			
				fakeRadius = Integer.parseInt(subStrItems[1]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the radius in wrong format '" + subStrItems[1] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[0].equals("#") && subStrItems[1].equals("#")) {
			try {
				classDesired = Integer.parseInt(subStrItems[2]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered invalid class id '" + subStrItems[2] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[0].equals("#") && subStrItems[2].equals("#")) {
			try {			
				fakeRadius = Integer.parseInt(subStrItems[1]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the radius in wrong format '" + subStrItems[1] + "'");
			}
		}else if(subStrItems.length == 3 && subStrItems[1].equals("#") && subStrItems[2].equals("#")) {
			try {
				fakeCount = Integer.parseInt(subStrItems[0]);
			}catch (NumberFormatException e) {
				activeChar.sendMessage("You've entered the count in a wrong format '" + subStrItems[0] + "'");
			}
		}
		else
		{
			if(subStrItems.length > 0 && subStrItems[0].length() > 0)
			{
				try {			
					
					fakeCount = Integer.parseInt(subStrItems[0]);
				}catch (NumberFormatException e) {
					
					activeChar.sendMessage("You have entered the count in a wrong format '" + subStrItems[0] + "'");
				}
			}
			
			if(subStrItems.length > 1)
			{
				//int index = subStrItems.length > 3 ? 3 : 1;//mes juk norim kad eitu ir paprastai ivest komanda
				//pvz //spawnrandom 10 100
				int index = 1;
				if(subStrItems[index].length() > 0) {
					try {
						fakeRadius = Integer.parseInt(subStrItems[index]);
					}catch (NumberFormatException e) {
						activeChar.sendMessage("You have entered the radius in a wrong format '" + subStrItems[index] + "'");
					}
				}
			}
			
			if(subStrItems.length > 2)
			{
				//int index = subStrItems.length > 3 ? 3 : 1;//mes juk norim kad eitu ir paprastai ivest komanda
				//pvz //spawnrandom 10 100
				int index = 2;
				if(subStrItems[index].length() > 0) {
					try {
						fakeRadius = Integer.parseInt(subStrItems[index]);
					}catch (NumberFormatException e) {
						activeChar.sendMessage("You have entered the radius in a wrong format '" + subStrItems[index] + "'");
					}
				}
			}
		}
		
		     if (spawn) {
		         FFFFakePlayerManager.INSTANCE.spawnPlayers(
		             fakeCount,
		             fakeRadius,
		             activeChar.getX(),
		             activeChar.getY(),
		             activeChar.getZ(),
		             classDesired,
		             true
		         );
		        activeChar.sendMessage(
		             "Spawned " + fakeCount + " bot(s) at location "
		             + activeChar.getX() + ";" + activeChar.getY() + ";" + activeChar.getZ()
		         );
		    } else {
		         FFFFakePlayerManager.INSTANCE.deleteAllFFFFakes(
		            fakeCount,
		             fakeRadius,
		             activeChar.getX(),
		             activeChar.getY()
		         );
		     }
	}
}
