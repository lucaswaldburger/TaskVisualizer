package org.ucb.bio134.taskvisualizer.model.visualizer;

/**
 * Defines the configuration of the deck within the workspace.
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class DeckConfig implements Config {
    private final int numRows = 2;
    private final int numCols = 2;
    private final int width = 400 * numCols;
    private final int height = 200 * numRows;
    private final int wellHeight = height / numRows;
    private final int wellWidth = width / numCols;

    private static DeckConfig config;

    private DeckConfig() {
    }

    public static DeckConfig getInstance() {
        if (config != null) {
            return config;
        } else {
            config = new DeckConfig();
        }

        return config;
    }

    @Override
    public int getWellWidth() {
        return wellWidth;
    }

    @Override
    public int getWellHeight() {
        return wellHeight;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getNumRows() {
        return numRows;
    }

    @Override
    public int getNumCols() {
        return numCols;
    }
}