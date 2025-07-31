package dre.elfocrash.roboto.helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads categorized PM reply phrases from a text file.
 * File format:
 * [category]
 * phrase
 * phrase
 */
public class FFFFakePMReplyLoader {
    
    public static Map<String, List<String>> loadReplies(String filePath) {
        Map<String, List<String>> replies = new HashMap<>();
        String currentTag = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty())
                    continue;

                if (line.startsWith("[") && line.endsWith("]")) {
                    currentTag = line.substring(1, line.length() - 1).toLowerCase();
                    replies.putIfAbsent(currentTag, new ArrayList<>());
                } else if (currentTag != null) {
                    replies.get(currentTag).add(line);
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to load PM replies: " + e.getMessage());
        }

        return replies;
    }
}
