package dre.elfocrash.roboto.pm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class PMReplyManager {
    private static final Map<String, List<String>> _replies = new HashMap<>();
    private static final List<String> _genericReplies = new ArrayList<>();
    private static final Random _random = new Random();

    static {
        loadReplies();
    }

    public static String getReply(String senderName, String receivedText) {
        if (receivedText == null || receivedText.trim().isEmpty())
            return null;

        String normalized = receivedText.trim().toLowerCase();

        //  Εύρεση βάσει λέξης–κλειδιού
        for (Map.Entry<String, List<String>> entry : _replies.entrySet()) {
            String keyword = entry.getKey().toLowerCase();
            if (normalized.startsWith(keyword) || normalized.contains(keyword)) {
                List<String> options = entry.getValue();
                return options.get(_random.nextInt(options.size()));
            }
        }

        // Fallback σε generic απάντηση
        if (!_genericReplies.isEmpty())
            return _genericReplies.get(_random.nextInt(_genericReplies.size()));

        return null;
    }

    private static void loadReplies() {
    	File file = new File("data/fakebots/pm_replies_ffff.txt");
        if (!file.exists())
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String currentKeyword = null;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                //  Σχόλια ή κενές γραμμές
                if (line.isEmpty() || line.startsWith("//") || line.startsWith("#"))
                    continue;

                //  Νέα λέξη–κλειδί
                if (line.startsWith("[") && line.endsWith("]")) {
                    currentKeyword = line.substring(1, line.length() - 1).toLowerCase();
                    _replies.putIfAbsent(currentKeyword, new ArrayList<>());
                } else {
                    if (currentKeyword == null) {
                        _genericReplies.add(line);
                    } else {
                        _replies.get(currentKeyword).add(line);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Could not load pm replies: " + e.getMessage());
        }
    }
}
