package dre.elfocrash.roboto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.sql.PlayerInfoTable;

public enum FFFFakePlayerNameManager {
	INSTANCE;
	
	public static final Logger _log = Logger.getLogger(FFFFakePlayerNameManager.class.getName());
	private List<String> _ffffakePlayerNames;
	private List<String> _gmFakePlayerNames = new ArrayList<>();
	private int _gmIndex = 0;
	
	public void initialise() {
		loadWordlist();
		loadGMWordlist();
	}
	
	public String getRandomAvailableName() {
		String name = getRandomNameFromWordlist();
		
		while(nameAlreadyExists(name)) {
			name = getRandomNameFromWordlist();
		}
		
		return name;
	}
	
	private String getRandomNameFromWordlist() {
		return _ffffakePlayerNames.get(Rnd.get(0, _ffffakePlayerNames.size() - 1));
	}
	   public String getRandomAvailableGMName() {
		       if (_gmFakePlayerNames.isEmpty())
		           return "GM_Bot_" + Rnd.get(1000, 9999);
		
		       String name = _gmFakePlayerNames.get(Rnd.get(_gmFakePlayerNames.size()));
		       int attempts = 0;
		       while (nameAlreadyExists(name) && attempts < 100) {
		           name = _gmFakePlayerNames.get(Rnd.get(_gmFakePlayerNames.size()));
		           attempts++;
		       }
		       return name;

		   }
	
	public List<String> getFFFFakePlayerNames() {
		return _ffffakePlayerNames;
	}
	
	private void loadWordlist()
    {
        try(LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader(new File("./data/ffffakenamewordlist.txt"))));)
        {
            String line;
            ArrayList<String> playersList = new ArrayList<String>();
            while((line = lnr.readLine()) != null)
            {
                if(line.trim().length() == 0 || line.startsWith("#"))
                    continue;
                playersList.add(line);
            }
            _ffffakePlayerNames = playersList;
            _log.log(Level.INFO, String.format("Loaded %s ffffake player names.", _ffffakePlayerNames.size()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
	
	   private void loadGMWordlist()
	   {
	       try(LineNumberReader lnr = new LineNumberReader(new BufferedReader(new FileReader(new File("./data/fakebots/gm-bot-ffff.txt"))));)
	       {
	           String line;
	           while((line = lnr.readLine()) != null)
	           {
	               if(line.trim().isEmpty() || line.startsWith("#"))
	                   continue;
	               _gmFakePlayerNames.add(line);
	           }
	           _log.log(Level.INFO, String.format("Loaded %s GM bot names.", _gmFakePlayerNames.size()));
	       }
	       catch(Exception e)
	       {
	           e.printStackTrace();
	       }
	   }
	
	private boolean nameAlreadyExists(String name) {
		return PlayerInfoTable.getInstance().getPlayerObjectId(name) > 0;
	}
}
