package dre.elfocrash.roboto.pm;

import java.util.concurrent.ThreadLocalRandom;

import net.sf.l2j.commons.concurrent.ThreadPool;

import dre.elfocrash.roboto.FFFFakePlayer;

public class PMReplyHelper {
    public static void tryReply(FFFFakePlayer bot) {
        String pmText = bot.getLastPMText();
        String pmFrom = bot.getLastPMFrom();
        String reply = PMReplyManager.getReply(pmFrom, pmText);

        String botId = bot.getName();

        if (reply != null && PMThrottleManager.canReply(pmFrom, pmText, botId)) {
            long delay = ThreadLocalRandom.current().nextLong(5000, 15001);
            System.out.println("[PM DEBUG] Scheduling PM to '" + pmFrom + "' in " + delay + "ms via '" + botId + "' β†’ Text: '" + reply + "'");

            bot.setLastPrivateMessage("", ""); //  early clear

            ThreadPool.schedule(() -> {
                bot.sendPrivateMessage(pmFrom, reply);
                PMThrottleManager.registerReply(pmFrom, pmText, botId);
            }, delay);
        }
    }
}
