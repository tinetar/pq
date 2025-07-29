package dre.elfocrash.roboto.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * Remembers last used phrase per player and its timestamp, to avoid repetition.
 */
public class FFFFakePMMemory {

    private static class ReplyInfo {
        public final String lastPhrase;
        public final long timestamp;

        public ReplyInfo(String phrase, long time) {
            lastPhrase = phrase;
            timestamp = time;
        }
    }

    private final Map<String, ReplyInfo> memory = new HashMap<>();

    /**
     * Checks if the given phrase has been used recently for this player.
     * @param playerName The name of the player.
     * @param phrase The candidate phrase.
     * @return true if the phrase was used for this player in the last hour.
     */
    public boolean alreadyUsed(String playerName, String phrase) {
        ReplyInfo info = memory.get(playerName);
        if (info == null)
            return false;
        if (!info.lastPhrase.equalsIgnoreCase(phrase))
            return false;

        long now = System.currentTimeMillis();
        return (now - info.timestamp) < 3600_000; // Less than 1 hour
    }

    /**
     * Stores that this phrase was used for a player now.
     * @param playerName The target player.
     * @param phrase The phrase sent.
     */
    public void storeReply(String playerName, String phrase) {
        memory.put(playerName, new ReplyInfo(phrase, System.currentTimeMillis()));
    }

    /**
     * Optional: clears memory older than 1 hour (can be scheduled or on-demand).
     */
    public void clearExpiredEntries() {
        long now = System.currentTimeMillis();
        memory.entrySet().removeIf(entry -> (now - entry.getValue().timestamp) >= 3600_000);
    }
}
