package org.ucb.bio134.taskvisualizer.model;

/**
 * This handles configuration information about a 6x8 tube plate. It is a
 * static singleton holding constants currently.
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class TubePlateConfig implements Config {
    private final int width = 400;
    private final int height = 200;
    private final int numRows = 6;
    private final int numCols = 8;
    private final int wellHeight = height/numRows;
    private final int wellWidth = width/numCols;

    private static TubePlateConfig config;

    private TubePlateConfig() {
    }

    public static TubePlateConfig getInstance() {
        if (config != null) {
            return config;
        } else {
            config = new TubePlateConfig();
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
