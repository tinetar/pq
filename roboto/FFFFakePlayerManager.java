package dre.elfocrash.roboto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import dre.elfocrash.roboto.helpers.FFFFakeHelpers;
import net.sf.l2j.commons.concurrent.ThreadPool;
import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.data.xml.MapRegionData.TeleportType;
import net.sf.l2j.gameserver.instancemanager.CastleManager;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.model.entity.Castle;
import net.sf.l2j.gameserver.model.entity.Siege;
import net.sf.l2j.gameserver.model.entity.Siege.SiegeSide;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.pledge.ClanMember;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public enum FFFFakePlayerManager {
    INSTANCE;

    private static final Logger _log = Logger.getLogger(FFFFakePlayerManager.class.getName());

    private static final List<Integer> FAKE_CLAN_IDS = FFFFakeHelpers.loadClanIdList("data/fakebots/clan-list-bot-farm-ffff.txt");
    private static final List<Location> SPAWN_LOCATIONS = FFFFakeHelpers.loadSpawnLocations("data/fakebots/spawn-list-bot-farm-ffff.txt");
    private static final List<Player> ACTIVE_FAKE_PLAYERS = new CopyOnWriteArrayList<>();

    // Περιέχει bots που δημιουργήθηκαν με εντολή του admin
    private final List<String> gmSpawnedBotNames = new ArrayList<>();

    // Περιέχει bots που δημιουργήθηκαν αυτόματα
    private final List<String> autoSpawnedBotNames = new ArrayList<>();

    private static int _fakeClanTableId = 0;
    private static final int MAX_BOTS = 50;
    private static final int BOTS_PER_CYCLE = 5;
    private static final long START_DELAY = 60 * 60 * 1000L; // 1 ώρα

    public List<String> getLatestGMSpawnedBotNames() {
        return gmSpawnedBotNames;
    }

    public List<String> getLatestAutoSpawnedBotNames() {
        return autoSpawnedBotNames;
    }

    public void initialise() {
        FFFFakePlayerNameManager.INSTANCE.initialise();
        FFFFakePlayerTaskManager.INSTANCE.initialise();
        ThreadPool.schedule(() -> startBotSystem(), START_DELAY);
        ThreadPool.scheduleAtFixedRate(() -> {
            INSTANCE.cleanGhosts();
        }, 5 * 60 * 60 * 1000L, 5 * 60 * 60 * 1000L);
    }

    private void startBotSystem() {
        _log.info("Bot system starting after 1-hour delay.");
        spawnInitialBots();
        scheduleBotCycle();
    }

    private void spawnInitialBots() {
        _log.info("Spawning first 5 fake players...");
        spawnFakeBots(5);

        _log.info("Scheduling gradual bot spawn every 5 minutes until MAX_BOTS (" + MAX_BOTS + ")...");
        ThreadPool.scheduleAtFixedRate(() -> {
            if (ACTIVE_FAKE_PLAYERS.size() >= MAX_BOTS) {
                _log.info("Reached MAX_BOTS (" + MAX_BOTS + "), stopping gradual spawn.");
                return;
            }
            int toSpawn = Math.min(5, MAX_BOTS - ACTIVE_FAKE_PLAYERS.size());
            spawnFakeBots(toSpawn);
        }, 5 * 60 * 1000L, 5 * 60 * 1000L);
    }

    private void spawnFakeBots(int count) {
        for (int i = 0; i < count; i++) {
            Location base = Rnd.get(SPAWN_LOCATIONS);
            Location loc = FFFFakeHelpers.getOffsetLocation(base, 250);
            int classId = getRandomClass();
            FFFFakePlayer bot = spawnPlayer(loc.getX(), loc.getY(), loc.getZ(), classId);
            if (!isAllowedClass(classId)) {
                _log.info("Skipped bot with disallowed classId=" + classId);
                bot.deleteMe();
                continue;
            }
            bot.assignDefaultAI();
            ACTIVE_FAKE_PLAYERS.add(bot);
            autoSpawnedBotNames.add(bot.getName());
            _log.info("Spawned bot [" + bot.getName() + "] classId=" + classId + " at " + loc);
        }
    }

    private boolean isAllowedClass(int classId) {
        return List.of(112, 109, 114, 117, 118, 92, 93, 88).contains(classId);
    }

    private void scheduleBotCycle() {
        long interval = Rnd.get(20, 30) * 60 * 1000L;
        _log.info("Scheduling bot cycle every " + (interval / 60000) + " minutes.");

        ThreadPool.scheduleAtFixedRate(() -> {
            _log.info("Bot cycle triggered: removing and spawning " + BOTS_PER_CYCLE + " bots.");

            int removed = 0;
            Iterator<Player> it = ACTIVE_FAKE_PLAYERS.iterator();
            while (it.hasNext() && removed < BOTS_PER_CYCLE) {
                Player bot = it.next();
                if (!autoSpawnedBotNames.contains(bot.getName())) {
                    continue; // skip GM bots
                }
                _log.info("Deleting bot [" + bot.getName() + "]");
                bot.deleteMe();
                it.remove();
                autoSpawnedBotNames.remove(bot.getName());
                removed++;
            }

            for (int i = 0; i < BOTS_PER_CYCLE; i++) {
                if (ACTIVE_FAKE_PLAYERS.size() >= MAX_BOTS)
                    break;
                Location base = Rnd.get(SPAWN_LOCATIONS);
                Location loc = FFFFakeHelpers.getOffsetLocation(base, 250);
                int classId = getRandomClass();
                FFFFakePlayer bot = spawnPlayer(loc.getX(), loc.getY(), loc.getZ(), classId);
                bot.assignDefaultAI();
                ACTIVE_FAKE_PLAYERS.add(bot);
                autoSpawnedBotNames.add(bot.getName());
                _log.info("Spawned bot [" + bot.getName() + "] classId=" + classId + " at " + loc);
            }
        }, interval, interval);
    }

    private int getRandomClass() {
        List<Integer> classIds = Arrays.asList(112, 109, 114, 117, 118, 92, 93, 88);
        return Rnd.get(classIds);
    }

    public FFFFakePlayer spawnPlayer(int x, int y, int z, int classDesired) {
        FFFFakePlayer activeChar = FFFFakeHelpers.createRandomFFFFakePlayer(classDesired);
        World.getInstance().addPlayer(activeChar);

        Clan clan = null;
        while (clan == null) {
            clan = ClanTable.getInstance().getClan(FAKE_CLAN_IDS.get(_fakeClanTableId++ % FAKE_CLAN_IDS.size()));
        }

        clan.addClanMember(activeChar);
        activeChar.setClan(clan);
        activeChar.setPledgeClass(ClanMember.calculatePledgeClass(activeChar));
        activeChar.setClanPrivileges(clan.getRankPrivs(activeChar.getPowerGrade()));

        handlePlayerClanOnSpawn(activeChar);

        activeChar.spawnMe(x, y, z);
        activeChar.onPlayerEnter();
        activeChar.heal();

        return activeChar;
    }

    private static void handlePlayerClanOnSpawn(FFFFakePlayer activeChar) {
        final Clan clan = activeChar.getClan();
        if (clan != null) {
            clan.getClanMember(activeChar.getObjectId()).setPlayerInstance(activeChar);

            final SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_LOGGED_IN).addCharName(activeChar);
            final PledgeShowMemberListUpdate update = new PledgeShowMemberListUpdate(activeChar);

            for (Player member : clan.getOnlineMembers()) {
                if (member == activeChar)
                    continue;
                member.sendPacket(msg);
                member.sendPacket(update);
            }

            for (Castle castle : CastleManager.getInstance().getCastles()) {
                final Siege siege = castle.getSiege();
                if (!siege.isInProgress())
                    continue;

                final SiegeSide type = siege.getSide(clan);
                if (type == SiegeSide.ATTACKER)
                    activeChar.setSiegeState((byte) 1);
                else if (type == SiegeSide.DEFENDER || type == SiegeSide.OWNER)
                    activeChar.setSiegeState((byte) 2);
            }
        }
    }

    public int getFFFFakePlayersCount() {
        return ACTIVE_FAKE_PLAYERS.size();
    }

    // Σβήνει μόνο τα auto-spawned bots
    public void deleteAllFFFFakes() {
        Iterator<Player> it = ACTIVE_FAKE_PLAYERS.iterator();
        while (it.hasNext()) {
            Player bot = it.next();
            if (!autoSpawnedBotNames.contains(bot.getName())) {
                continue;
            }
            _log.info("Deleting auto bot [" + bot.getName() + "]");
            bot.deleteMe();
            it.remove();
            autoSpawnedBotNames.remove(bot.getName());
        }
    }

    public boolean deleteFakeBotByName(String name) {
        for (Player bot : ACTIVE_FAKE_PLAYERS) {
            if (bot.getName().equalsIgnoreCase(name)) {
                bot.deleteMe();
                ACTIVE_FAKE_PLAYERS.remove(bot);
                gmSpawnedBotNames.remove(name);
                autoSpawnedBotNames.remove(name);
                _log.info("Deleted bot [" + name + "] fully.");
                return true;
            }
        }
        _log.warning("Bot [" + name + "] not found for deletion.");
        return false;
    }

    public void deleteAllFFFFakes(int count, int radius, int x, int y) {
        int deleted = 0;
        List<Player> toRemove = new ArrayList<>();
        for (Player bot : ACTIVE_FAKE_PLAYERS) {
            if (deleted >= count)
                break;

            if (!autoSpawnedBotNames.contains(bot.getName()))
                continue;

            double dx = bot.getX() - x;
            double dy = bot.getY() - y;
            if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                _log.info("Deleting bot [" + bot.getName() + "] within radius.");
                bot.deleteMe();
                toRemove.add(bot);
                autoSpawnedBotNames.remove(bot.getName());
                deleted++;
            }
        }
        ACTIVE_FAKE_PLAYERS.removeAll(toRemove);
    }

    public void spawnPlayers(int count, int radius, int x, int y, int z, int classId, boolean gmMode) {
        for (int i = 0; i < count; i++) {
            int offsetX = Rnd.get(-radius, radius);
            int offsetY = Rnd.get(-radius, radius);
            Location loc = new Location(x + offsetX, y + offsetY, z);

            FFFFakePlayer bot = gmMode
                ? FFFFakeHelpers.createRandomFFFFakePlayer(true, classId)
                : FFFFakeHelpers.createRandomFFFFakePlayer(false, classId);

            World.getInstance().addPlayer(bot);

            Clan clan = null;
            while (clan == null) {
                clan = ClanTable.getInstance().getClan(FAKE_CLAN_IDS.get(_fakeClanTableId++ % FAKE_CLAN_IDS.size()));
            }

            clan.addClanMember(bot);
            bot.setClan(clan);
            bot.setPledgeClass(ClanMember.calculatePledgeClass(bot));
            bot.setClanPrivileges(clan.getRankPrivs(bot.getPowerGrade()));
            handlePlayerClanOnSpawn(bot);
            bot.spawnMe(loc.getX(), loc.getY(), loc.getZ());
            bot.onPlayerEnter();
            bot.heal();
            bot.assignDefaultAI();
            ACTIVE_FAKE_PLAYERS.add(bot);

            if (gmMode)
                gmSpawnedBotNames.add(bot.getName());
            else
                autoSpawnedBotNames.add(bot.getName());

            _log.info("Spawned " + (gmMode ? "GM " : "") + "bot [" + bot.getName() + "] (classId=" + classId + ") at " + loc);
        }
    }

    public void cleanGhosts() {
        int removed = 0;
        Iterator<Player> iter = ACTIVE_FAKE_PLAYERS.iterator();
        while (iter.hasNext()) {
            Player p = iter.next();
            if (!p.isOnline() || p.isTeleporting()) {
                iter.remove();
                gmSpawnedBotNames.remove(p.getName());
                autoSpawnedBotNames.remove(p.getName());
                removed++;
            }
        }
        if (removed > 0)
            _log.info("GhostCleaner: Removed " + removed + " ghost bots.");
    }

    @SuppressWarnings("unchecked")
    public List<FFFFakePlayer> getFFFFakePlayers() {
        return (List<FFFFakePlayer>) (List<?>) ACTIVE_FAKE_PLAYERS;
    }
}
