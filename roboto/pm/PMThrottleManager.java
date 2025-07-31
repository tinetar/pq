package dre.elfocrash.roboto.pm;

import java.util.HashMap;
import java.util.Map;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PMThrottleManager {
	private static final long REPLY_COOLDOWN = 20 * 60 * 1000; // 20 λεπτά για throttling
	private static final long ENTRY_LIFETIME = 12 * 60 * 60 * 1000; // 12 ώρες για καθαρισμό
    private static final Map<String, Map<String, Long>> _replyMap = new HashMap<>();
    
        //  Scheduled καθαριστής: τρέχει κάθε ώρα
       static {
            ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();
            cleaner.scheduleAtFixedRate(() -> {
                long now = System.currentTimeMillis();
                Iterator<Map.Entry<String, Map<String, Long>>> userIt = _replyMap.entrySet().iterator();
    
                while (userIt.hasNext()) {
                    Map.Entry<String, Map<String, Long>> entry = userIt.next();
                    Map<String, Long> msgMap = entry.getValue();
    
                    msgMap.entrySet().removeIf(e -> now - e.getValue() > ENTRY_LIFETIME);
    
                    if (msgMap.isEmpty()) {
                       userIt.remove(); // Καθαρίστηκε ολόκληρος ο χρήστης
                    }
               }
                System.out.println("[PM DEBUG] Throttle cache cleaned");
            }, 1, 1, TimeUnit.HOURS); // Ξεκινά μετά από 1 ώρα, επαναλαμβάνεται κάθε ώρα
        }

       public static boolean canReply(String sender, String text, String botId) {
        if (sender == null || sender.isEmpty() || text == null || text.isEmpty())
            return false;

        long now = System.currentTimeMillis();
        String key = (botId + ":" + text.trim().toLowerCase()); //  Normalize το κείμενο
        Map<String, Long> msgMap = _replyMap.computeIfAbsent(sender, k -> new HashMap<>());
        msgMap.entrySet().removeIf(e -> now - e.getValue() > ENTRY_LIFETIME); //  Καθαρισμός παλιών εγγραφών

        msgMap.entrySet().removeIf(e -> now - e.getValue() > ENTRY_LIFETIME);
        Long lastTime = msgMap.get(key);

        return lastTime == null || (now - lastTime) >= REPLY_COOLDOWN;
    }

       public static void registerReply(String sender, String text, String botId) {
        if (sender == null || sender.isEmpty() || text == null || text.isEmpty())
            return;

        String key = (botId + ":" + text.trim().toLowerCase()); //  Ίδιο normalization με το canReply
    Map<String, Long> msgMap = _replyMap.computeIfAbsent(sender, k -> new HashMap<>());
    msgMap.put(key, System.currentTimeMillis());
    }
}
