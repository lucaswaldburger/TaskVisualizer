package org.ucb.bio134.taskvisualizer.model;

import java.util.HashMap;

import org.ucb.c5.utils.FileUtils;

/**
 * Loads the scores associated with each action that results in human burden then calculates
 * the total burden for a given Semiprotocol.
 *
 * @author Tong Zhang
 */
public class BurdenCalculator {
    private HashMap<String, Integer> burdenMap;

    public void initiate() throws Exception {
        String data = FileUtils.readResourceFile("semiprotocol/data/burden.txt");
        String[] lines = data.split("\\r|\\r?\\n");
        burdenMap = new HashMap<>();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] tabs = line.split("\t");
            burdenMap.put(tabs[0], Integer.parseInt(tabs[1]));
        }
    }

    public int run(HashMap<Burden, Integer> burdenCount) throws Exception {
        int totalBurden = 0;
        for (Burden burden : burdenCount.keySet()) {
            if (burdenMap.containsKey(burden.name())) {
                totalBurden += burdenMap.get(burden.name()) * burdenCount.get(burden);
            }
        }
        return totalBurden;
    }
}
