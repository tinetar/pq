package dre.elfocrash.roboto.helpers;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Timer;
import java.util.TimerTask;

import dre.elfocrash.roboto.FFFFakePlayer;

/**
 * Responds to private messages sent to fake bots using loaded reply categories.
 * Avoids repeating the same phrase to the same player within 1 hour.
 * Adds a randomized delay (3–10 seconds) before responding.
 */
public class FFFFakePMResponder {

    private final Map<String, List<String>> replyMap;
    private final FFFFakePMMemory memory;

    public FFFFakePMResponder() {
        replyMap = FFFFakePMReplyLoader.loadReplies("data/fakebots/pm_replies_ffff.txt");
        memory = new FFFFakePMMemory();
    }

    /**
     * Handles an incoming PM, searches for matching category, and replies.
     * @param bot The fake bot player instance.
     * @param fromPlayerName The sender's character name.
     * @param incomingText The message sent by the player.
     */
    public void handleIncomingPM(FFFFakePlayer bot, String fromPlayerName, String incomingText) {
        String trigger = findMatchingCategory(incomingText);
        if (trigger == null || !replyMap.containsKey(trigger))
            return;

        List<String> candidates = replyMap.get(trigger);
        List<String> filtered = new ArrayList<>();

        for (String phrase : candidates) {
            if (!memory.alreadyUsed(fromPlayerName, phrase))
                filtered.add(phrase);
        }

        if (filtered.isEmpty())
            return;

        String selected = filtered.get(ThreadLocalRandom.current().nextInt(filtered.size()));

        int delay = ThreadLocalRandom.current().nextInt(3000, 10001); // 3–10 sec
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                bot.sendPrivateMessage(fromPlayerName, selected);
                memory.storeReply(fromPlayerName, selected);
            }
        }, delay);
    }

    /**
     * Finds matching category from reply map based on input text (basic substring matching).
     * @param text Incoming message text.
     * @return matching category name, or null if not found.
     */
    private String findMatchingCategory(String text) {
        text = text.toLowerCase().trim();

        for (String key : replyMap.keySet()) {
            if (text.contains(key))
                return key;
        }

        return null;
    }
}
