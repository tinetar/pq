package dre.elfocrash.roboto.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import dre.elfocrash.roboto.FFFFakePlayer;
import dre.elfocrash.roboto.FFFFakePlayerNameManager;
import dre.elfocrash.roboto.ai.AAAAdventurerAI;
import dre.elfocrash.roboto.ai.AAAArchmageAI;
import dre.elfocrash.roboto.ai.CCCCardinalAI;
import dre.elfocrash.roboto.ai.DDDDominatorAI;
import dre.elfocrash.roboto.ai.DDDDreadnoughtAI;
import dre.elfocrash.roboto.ai.DDDDuelistAI;
import dre.elfocrash.roboto.ai.FFFFakePlayerAI;
import dre.elfocrash.roboto.ai.FFFFallbackAI;
import dre.elfocrash.roboto.ai.GGGGhostHunterAI;
import dre.elfocrash.roboto.ai.GGGGhostSentinelAI;
import dre.elfocrash.roboto.ai.GGGGrandKhavatariAI;
import dre.elfocrash.roboto.ai.MMMMoonlightSentinelAI;
import dre.elfocrash.roboto.ai.MMMMysticMuseAI;
import dre.elfocrash.roboto.ai.SSSSaggitariusAI;
import dre.elfocrash.roboto.ai.SSSSoultakerAI;
import dre.elfocrash.roboto.ai.SSSStormScreamerAI;
import dre.elfocrash.roboto.ai.TTTTitanAI;
import dre.elfocrash.roboto.ai.WWWWindRiderAI;

import net.sf.l2j.Config;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.sql.PlayerInfoTable;
import net.sf.l2j.gameserver.data.xml.PlayerData;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.appearance.PcAppearance;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.template.PlayerTemplate;
import net.sf.l2j.gameserver.model.base.ClassId;
import net.sf.l2j.gameserver.model.base.ClassRace;
import net.sf.l2j.gameserver.model.base.Experience;
import net.sf.l2j.gameserver.model.base.Sex;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.location.Location;

public class FFFFakeHelpers {

    public static FFFFakePlayer createRandomFFFFakePlayer(boolean gmMode, int... classs) {
        int objectId = IdFactory.getInstance().getNextId();
        String accountName = gmMode ? "GMSpawned" : "AutoPilot";
        String playerName = gmMode
                ? FFFFakePlayerNameManager.INSTANCE.getRandomAvailableGMName()
                : FFFFakePlayerNameManager.INSTANCE.getRandomAvailableName();

        ClassId classId = getClassFromNum(classs.length > 0 ? classs[0] : 0);
        final PlayerTemplate template = PlayerData.getInstance().getTemplate(classId);
        PcAppearance app = getRandomAppearance(template.getRace());
        FFFFakePlayer player = new FFFFakePlayer(objectId, template, accountName, app);
        
        player.setGmSpawned(gmMode); //

        player.setName(playerName);
        player.setAccessLevel(Config.DEFAULT_ACCESS_LEVEL);
        PlayerInfoTable.getInstance().addPlayer(objectId, accountName, playerName, player.getAccessLevel().getLevel());
        player.setBaseClass(player.getClassId());
        setLevel(player, 81);
        player.rewardSkills();
        giveArmorsByClass(player, true);
        giveWeaponsByClass(player, true);
        player.heal();
        return player;
    }

    public static int[][] getFighterBuffs() {
        return new int[][] {
            { 264, 1 },
            { 265, 1 },
            { 266, 1 },
            { 267, 1 },
            { 268, 1 },
            { 269, 1 },
            { 270, 1 },
            { 271, 1 },
            { 272, 1 },
            { 273, 1 },
            { 274, 1 },
            { 275, 1 },
            { 276, 1 },
            { 277, 1 },
            { 304, 1 },
            { 305, 1 },
            { 306, 1 },
            { 307, 1 },
            { 308, 1 },
            { 309, 1 },
            { 310, 1 },
            { 311, 1 },
            { 1363, 1 },
            { 1068, 3 },
            { 1182, 3 },
            { 1189, 3 },
            { 1191, 3 },
            { 1204, 2 },
            { 1240, 3 },
            { 1242, 3 },
            { 1243, 6 },
            { 1259, 4 },
            { 1392, 3 },
            { 1393, 3 },
            { 1352, 1 },
            { 1353, 1 },
            { 1354, 1 },
            { 1036, 2 },
            { 1040, 3 },
            { 1043, 1 },
            { 1045, 6 },
            { 1048, 6 },
            { 1059, 3 },
            { 1062, 2 },
            { 1077, 3 },
            { 1078, 6 },
            { 1085, 3 },
            { 1086, 2 },
            { 1087, 3 },
            { 4703, 13 },
            { 4699, 13 },
            { 7265, 1 },
        };
    }

    public static int[][] getMageBuffs() {
        return new int[][] {
            { 264, 1 },
            { 265, 1 },
            { 266, 1 },
            { 267, 1 },
            { 268, 1 },
            { 269, 1 },
            { 270, 1 },
            { 271, 1 },
            { 272, 1 },
            { 273, 1 },
            { 274, 1 },
            { 275, 1 },
            { 276, 1 },
            { 277, 1 },
            { 304, 1 },
            { 305, 1 },
            { 306, 1 },
            { 307, 1 },
            { 308, 1 },
            { 309, 1 },
            { 310, 1 },
            { 311, 1 },
            { 1363, 1 },
            { 1068, 3 },
            { 1182, 3 },
            { 1189, 3 },
            { 1191, 3 },
            { 1204, 2 },
            { 1240, 3 },
            { 1242, 3 },
            { 1243, 6 },
            { 1259, 4 },
            { 1392, 3 },
            { 1393, 3 },
            { 1352, 1 },
            { 1353, 1 },
            { 1354, 1 },
            { 1036, 2 },
            { 1040, 3 },
            { 1043, 1 },
            { 1045, 6 },
            { 1048, 6 },
            { 1059, 3 },
            { 1062, 2 },
            { 1077, 3 },
            { 1078, 6 },
            { 1085, 3 },
            { 1086, 2 },
            { 1087, 3 },
            { 4703, 13 },
            { 4699, 13 },
            { 7265, 1 },
        };
    }

    public static Class<? extends Creature> getTestTargetClass() {
        return Monster.class;
    }

    public static int getTestTargetRange() {
        return 2000;
    }

    public static FFFFakePlayer createRandomFFFFakePlayer(int... classs) {
        int objectId = IdFactory.getInstance().getNextId();
        String accountName = "AutoPilot";
        String playerName = FFFFakePlayerNameManager.INSTANCE.getRandomAvailableName();

        ClassId classId = getClassFromNum(classs.length > 0 ? classs[0] : 0);

        final PlayerTemplate template = PlayerData.getInstance().getTemplate(classId);
        PcAppearance app = getRandomAppearance(template.getRace());
        FFFFakePlayer player = new FFFFakePlayer(objectId, template, accountName, app);

        player.setName(playerName);
        player.setAccessLevel(Config.DEFAULT_ACCESS_LEVEL);
        PlayerInfoTable.getInstance().addPlayer(objectId, accountName, playerName, player.getAccessLevel().getLevel());
        player.setBaseClass(player.getClassId());
        setLevel(player, 81);
        player.rewardSkills();
        giveArmorsByClass(player, true);
        giveWeaponsByClass(player, true);
        player.heal();
        return player;
    }

    public static void giveArmorsByClass(FFFFakePlayer player, boolean randomlyEnchant) {
        List<Integer> itemIds = new ArrayList<>();
        switch (player.getClassId()) {
            case ARCHMAGE:
            case SOULTAKER:
            case HIEROPHANT:
            case ARCANA_LORD:
            case CARDINAL:
            case MYSTIC_MUSE:
            case ELEMENTAL_MASTER:
            case EVAS_SAINT:
            case STORM_SCREAMER:
            case SPECTRAL_MASTER:
            case SHILLIEN_SAINT:
            case DOMINATOR:
            case DOOMCRYER:
                itemIds = Arrays.asList(2407, 512, 5767, 5779, 9529, 9532, 9531, 9533, 9530);
                break;
            case DUELIST:
            case DREADNOUGHT:
            case PHOENIX_KNIGHT:
            case SWORD_MUSE:
            case HELL_KNIGHT:
            case SPECTRAL_DANCER:
            case EVAS_TEMPLAR:
            case SHILLIEN_TEMPLAR:
            case TITAN:
            case MAESTRO:
                itemIds = Arrays.asList(6373, 6374, 6378, 6375, 6376, 9529, 9532, 9531, 9533, 9530);
                break;
            case SAGGITARIUS:
            case ADVENTURER:
            case WIND_RIDER:
            case MOONLIGHT_SENTINEL:
            case GHOST_HUNTER:
            case GHOST_SENTINEL:
            case FORTUNE_SEEKER:
            case GRAND_KHAVATARI:
                itemIds = Arrays.asList(6379, 6382, 6380, 6381, 9529, 9532, 9531, 9533, 9530);
                break;
            default:
                break;
        }
        for (int id : itemIds) {
            player.getInventory().addItem("Armors", id, 1, player, null);
            ItemInstance item = player.getInventory().getItemByItemId(id);

            if (randomlyEnchant) {
                item.setEnchantLevel(Rnd.get(40, 48)); // π.χ. +6 έως +10
            }

            player.getInventory().equipItemAndRecord(item);
            player.getInventory().reloadEquippedItems();
            player.broadcastCharInfo();
        }
    }

    public static void giveWeaponsByClass(FFFFakePlayer player, boolean randomlyEnchant) {
        List<Integer> itemIds = new ArrayList<>();
        switch (player.getClassId()) {
            case FORTUNE_SEEKER:
            case GHOST_HUNTER:
            case WIND_RIDER:
            case ADVENTURER:
                itemIds = Arrays.asList(10668);
                break;
            case SAGGITARIUS:
            case MOONLIGHT_SENTINEL:
            case GHOST_SENTINEL:
                itemIds = Arrays.asList(10669);
                break;
            case PHOENIX_KNIGHT:
            case SWORD_MUSE:
            case HELL_KNIGHT:
            case EVAS_TEMPLAR:
            case SHILLIEN_TEMPLAR:
                itemIds = Arrays.asList(10666, 6377);
                break;
            case MAESTRO:
                itemIds = Arrays.asList(10668, 6377);
                break;
            case TITAN:
                itemIds = Arrays.asList(6372);
                break;
            case DUELIST:
            case SPECTRAL_DANCER:
                itemIds = Arrays.asList(10673);
                break;
            case DREADNOUGHT:
                itemIds = Arrays.asList(10670);
                break;
            case ARCHMAGE:
            case SOULTAKER:
            case HIEROPHANT:
            case ARCANA_LORD:
            case CARDINAL:
            case MYSTIC_MUSE:
            case ELEMENTAL_MASTER:
            case EVAS_SAINT:
            case STORM_SCREAMER:
            case SPECTRAL_MASTER:
            case SHILLIEN_SAINT:
            case DOMINATOR:
            case DOOMCRYER:
                itemIds = Arrays.asList(6579);
                break;
            case GRAND_KHAVATARI:
                itemIds = Arrays.asList(6371);
                break;
            default:
                break;
        }
        for (int id : itemIds) {
            player.getInventory().addItem("Weapon", id, 1, player, null);
            ItemInstance item = player.getInventory().getItemByItemId(id);
            if (randomlyEnchant)
                item.setEnchantLevel(Rnd.get(40, 48));
            player.getInventory().equipItemAndRecord(item);
            player.getInventory().reloadEquippedItems();
        }
    }

    public static List<ClassId> getThirdClasses() {
        List<ClassId> classes = new ArrayList<>();

        classes.add(ClassId.SOULTAKER);
        classes.add(ClassId.MYSTIC_MUSE);
        classes.add(ClassId.STORM_SCREAMER);
        classes.add(ClassId.TITAN);
        classes.add(ClassId.CARDINAL);
        classes.add(ClassId.GRAND_KHAVATARI);

        return classes;
    }

    public static Map<ClassId, Class<? extends FFFFakePlayerAI>> getAllAIs() {
        Map<ClassId, Class<? extends FFFFakePlayerAI>> ais = new HashMap<>();
        ais.put(ClassId.STORM_SCREAMER, SSSStormScreamerAI.class);
        ais.put(ClassId.MYSTIC_MUSE, MMMMysticMuseAI.class);
        ais.put(ClassId.ARCHMAGE, AAAArchmageAI.class);
        ais.put(ClassId.SOULTAKER, SSSSoultakerAI.class);
        ais.put(ClassId.SAGGITARIUS, SSSSaggitariusAI.class);
        ais.put(ClassId.MOONLIGHT_SENTINEL, MMMMoonlightSentinelAI.class);
        ais.put(ClassId.GHOST_SENTINEL, GGGGhostSentinelAI.class);
        ais.put(ClassId.ADVENTURER, AAAAdventurerAI.class);
        ais.put(ClassId.WIND_RIDER, WWWWindRiderAI.class);
        ais.put(ClassId.GHOST_HUNTER, GGGGhostHunterAI.class);
        ais.put(ClassId.DOMINATOR, DDDDominatorAI.class);
        ais.put(ClassId.TITAN, TTTTitanAI.class);
        ais.put(ClassId.CARDINAL, CCCCardinalAI.class);
        ais.put(ClassId.DUELIST, DDDDuelistAI.class);
        ais.put(ClassId.GRAND_KHAVATARI, GGGGrandKhavatariAI.class);
        ais.put(ClassId.DREADNOUGHT, DDDDreadnoughtAI.class);
        return ais;
    }

    public static PcAppearance getRandomAppearance(ClassRace race) {

        Sex randomSex = Rnd.get(1, 2) == 1 ? Sex.MALE : Sex.FEMALE;
        int hairStyle = Rnd.get(0, randomSex == Sex.MALE ? 4 : 6);
        int hairColor = Rnd.get(0, 3);
        int faceId = Rnd.get(0, 2);

        return new PcAppearance((byte) faceId, (byte) hairColor, (byte) hairStyle, randomSex);
    }

    public static void setLevel(FFFFakePlayer player, int level) {
        if (level >= 1 && level <= Experience.MAX_LEVEL) {
            long pXp = player.getExp();
            long tXp = Experience.LEVEL[81];

            if (pXp > tXp)
                player.removeExpAndSp(pXp - tXp, 0);
            else if (pXp < tXp)
                player.addExpAndSp(tXp - pXp, 0);
        }
    }

    public static Class<? extends FFFFakePlayerAI> getAIbyClassId(ClassId classId) {
        Class<? extends FFFFakePlayerAI> ai = getAllAIs().get(classId);
        if (ai == null)
            return FFFFallbackAI.class;

        return ai;
    }

    public static ClassId getClassFromNum(int i) {
        switch (i) {
            case 1:
                return ClassId.SOULTAKER;
            case 2:
                return ClassId.MYSTIC_MUSE;
            case 3:
                return ClassId.STORM_SCREAMER;
            case 4:
                return ClassId.TITAN;
            case 5:
                return ClassId.CARDINAL;
            case 6:
                return ClassId.GRAND_KHAVATARI;
            case 7:
                return ClassId.SAGGITARIUS;
            case 8:
                return ClassId.GHOST_SENTINEL;
            case 9:
                return ClassId.DUELIST;
            default:
                return getThirdClasses().get(Rnd.get(0, getThirdClasses().size() - 1));
        }
    }

    public static Location getOffsetLocation(Location base, int range) {
        int offsetX = Rnd.get(-range, range);
        int offsetY = Rnd.get(-range, range);
        return new Location(base.getX() + offsetX, base.getY() + offsetY, base.getZ());
    }

    public static List<Location> loadSpawnLocations(String filename) {
        List<Location> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
        	 System.out.println("📁 Loading spawn file: " + filename);
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    int z = Integer.parseInt(parts[2]);
                    list.add(new Location(x, y, z));
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load spawn list: " + e.getMessage());
        }
        return list;
    }

    public static List<Integer> loadClanIdList(String filename) {
        List<Integer> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    list.add(Integer.parseInt(line.trim()));
                } catch (NumberFormatException ignored) {
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to load clan IDs: " + e.getMessage());
        }
        return list;
    }

}
