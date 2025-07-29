package net.sf.l2j.gameserver.model.actor.instance;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.gameserver.cache.HtmCache;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class StatusInstance extends Folk
    {
    
    private class PlayerInfo
        {
        public PlayerInfo(int pos,String n, int pvps, int pks ,int ontime, Boolean iso)
            {
            position = pos;
            Nick = n;
            pvpCount = pvps;
            pkCount = pks;
            onlineTime = ontime;
            isOnline = iso;
            }
        
        public int position;
        public String Nick;
        public int pvpCount;
        public int pkCount;
        public int onlineTime;
        public Boolean isOnline;
        }
    
    //delay interval (in minutes):
    private final int delayForCheck = 5;
    
    //number of players to be listed
    private int pvpListCount = 10;
    private int pkListCount = 10;
    private int onlineListCount = 10;

    
    private PlayerInfo [] topPvPList = new PlayerInfo [pvpListCount];
    private PlayerInfo [] topPkList = new PlayerInfo [pkListCount];
    private PlayerInfo [] topOnlineList = new PlayerInfo [onlineListCount];
    
    
    @SuppressWarnings("synthetic-access")
	public StatusInstance(int objectId, NpcTemplate template)
        {
        super(objectId, template);
        ThreadPool.scheduleAtFixedRate(new RefreshAllLists(), 10000, delayForCheck * 60000);
        }
        
    private class RefreshAllLists implements Runnable
        {
        @SuppressWarnings("synthetic-access")
		@Override
		public void run()
            {
            ReloadData();
            }
        }
    
    @SuppressWarnings("resource")
	private void ReloadData()
        {
        try (Connection con = L2DatabaseFactory.getInstance().getConnection())
            {
            PreparedStatement statement = con.prepareStatement("SELECT char_name, pvpkills, online FROM characters ORDER BY pvpkills DESC, char_name ASC LIMIT 10");
            ResultSet result = statement.executeQuery();
            
            //refreshing top pvp list
            int i = 0; //index of array
            
            while (result.next())
                {
                topPvPList[i] = new PlayerInfo(i+1,result.getString("char_name"),result.getInt("pvpkills"),0,0,result.getBoolean("online"));
                i++;
                }
                    
            //refreshing top pk list
            statement = con.prepareStatement("SELECT char_name, pkkills, online FROM characters ORDER BY pkkills DESC, char_name ASC LIMIT 10");
            result = statement.executeQuery();
            
            i = 0; //index of array
            while (result.next())
                {
                topPkList[i] = new PlayerInfo(i+1,result.getString("char_name"),0,result.getInt("pkkills"),0,result.getBoolean("online"));
                i++;
                }
            
            //refreshing top online list
            statement = con.prepareStatement("SELECT char_name, onlinetime, online FROM characters ORDER BY onlinetime DESC, char_name ASC LIMIT 10");
            result = statement.executeQuery();
            
            i = 0; //index of array
            while (result.next())
                {
                topOnlineList[i] = new PlayerInfo(i+1,result.getString("char_name"),0,0,result.getInt("onlinetime"),result.getBoolean("online"));
                i++;
                }
            
            result.close();
            statement.close();
           
            }
        catch (SQLException e)
            {
            _log.log(Level.WARNING, "ranking (status): could not load statistics informations" + e.getMessage(), e);
            }
        }

    @Override
    public void onSpawn()
        {
        ReloadData();
        }

    @Override
     public void showChatWindow(Player player)
         {
        GeneratePvPList(player);
         }
    
    @Override
    public void onBypassFeedback(Player player, String command)
        {
        StringTokenizer st = new StringTokenizer(command, " ");
        String currentCommand = st.nextToken();
    
        if (currentCommand.startsWith("pvplist"))
            {
            GeneratePvPList(player);
            }

        else if (currentCommand.startsWith("pklist"))
            {
            GeneratePKList(player);
            }
        else if (currentCommand.startsWith("onlinelist"))
            {
            GenerateOnlineList(player);
            }
                
        super.onBypassFeedback(player, command);
        }
    
    private void GeneratePvPList(Player p)
        {
        StringBuilder _PVPranking = new StringBuilder();
        for (PlayerInfo player : topPvPList)
            {
            if (player == null) break;

            _PVPranking.append("<table width=\"290\"><tr>");
               _PVPranking.append("<td FIXWIDTH=\"2\" align=\"center\"></td>");        
               _PVPranking.append("<td FIXWIDTH=\"17\" align=\"center\">"+player.position+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"158\" align=\"center\">"+player.Nick+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"90\" align=\"center\">"+player.pvpCount+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"50\" align=\"center\">"+((player.isOnline) ? "<font color=\"00FF00\">ON</font>" : "<font color=\"CC0000\">OFF</font>")+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"2\" align=\"center\"></td>");
               _PVPranking.append("</tr></table>");
               _PVPranking.append("<img src=\"L2UI.Squaregray\" width=\"300\" height=\"1\">");
            }
        
           NpcHtmlMessage html = new NpcHtmlMessage(1);
        html.setFile(getHtmlPath(getNpcId(), 0));
        html.replace("%objectId%", getObjectId());
        html.replace("%pvplist%", _PVPranking.toString());
        p.sendPacket(html);
        }
    
    private void GeneratePKList(Player p)
        {
        StringBuilder _PVPranking = new StringBuilder();
        for (PlayerInfo player : topPkList)
            {
            if (player == null) break;
    
               _PVPranking.append("<table width=\"290\"><tr>");
               _PVPranking.append("<td FIXWIDTH=\"2\" align=\"center\"></td>");        
               _PVPranking.append("<td FIXWIDTH=\"17\" align=\"center\">"+player.position+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"158\" align=\"center\">"+player.Nick+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"90\" align=\"center\">"+player.pkCount+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"50\" align=\"center\">"+((player.isOnline) ? "<font color=\"00FF00\">ON</font>" : "<font color=\"CC0000\">OFF</font>")+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"2\" align=\"center\"></td>");
               _PVPranking.append("</tr></table>");
               _PVPranking.append("<img src=\"L2UI.Squaregray\" width=\"300\" height=\"1\">");
            }
        
           NpcHtmlMessage html = new NpcHtmlMessage(1);
        html.setFile(getHtmlPath(getNpcId(), 2));
        html.replace("%objectId%", getObjectId());
        html.replace("%pklist%", _PVPranking.toString());
        p.sendPacket(html);
        }
    
    private void GenerateOnlineList(Player p)
        {
        StringBuilder _PVPranking = new StringBuilder();
        for (PlayerInfo player : topOnlineList)
            {
            if (player == null) break;
        
               _PVPranking.append("<table width=\"290\"><tr>");
               _PVPranking.append("<td FIXWIDTH=\"2\" align=\"center\"></td>");        
               _PVPranking.append("<td FIXWIDTH=\"17\" align=\"center\">"+player.position+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"158\" align=\"center\">"+player.Nick+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"90\" align=\"center\">"+ConverTime(player.onlineTime)+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"50\" align=\"center\">"+((player.isOnline) ? "<font color=\"00FF00\">ON</font>" : "<font color=\"CC0000\">OFF</font>")+"</td>");
               _PVPranking.append("<td FIXWIDTH=\"2\" align=\"center\"></td>");
               _PVPranking.append("</tr></table>");
               _PVPranking.append("<img src=\"L2UI.Squaregray\" width=\"300\" height=\"1\">");
            }
        
           NpcHtmlMessage html = new NpcHtmlMessage(1);
        html.setFile(getHtmlPath(getNpcId(), 3));
        html.replace("%objectId%", getObjectId());
        html.replace("%onlinelist%", _PVPranking.toString());
        p.sendPacket(html);
        }
    
    @SuppressWarnings("static-method")
	private String ConverTime(long seconds)
        {
        long remainder = seconds;
        int days = (int) remainder / (24*3600);
        remainder = remainder -(days * 3600 * 24);
    
        int hours = (int) (remainder / 3600);
        remainder = remainder -(hours * 3600);
       
        int minutes = (int) (remainder / 60);
        remainder = remainder -(hours * 60);
 
        seconds = remainder;
    
        String timeInText = "";
    
        if (days > 0)
            timeInText = days+"<font color=\"LEVEL\">D</font> ";
        if (hours > 0)
            timeInText = timeInText+ hours+"<font color=\"LEVEL\">H</font> ";
        if (minutes >0)
            timeInText = timeInText+ minutes+"<font color=\"LEVEL\">M</font>";
          
        if (timeInText=="")
            {
            if(seconds>0)
                {
                timeInText = seconds+"<font color=\"LEVEL\">S</font>";
                }
            else
                {
                timeInText = "N/A";
                }
            }
        return timeInText;
        }
    
    @Override
    public String getHtmlPath(int npcId, int val)
        {
        String filename;
        
        if (val == 0)
            filename = "data/html/Status/" + npcId + ".htm";
        else
            filename = "data/html/Status/" + npcId + "-" + val + ".htm";
        
        if (HtmCache.getInstance().isLoadable(filename))
            return filename;
        
        return "data/html/Status/" + npcId + ".htm";
        }
    }
