package org.ucb.bio134.taskvisualizer.model;

/**
 * This handles configuration information about a 96-well pcr plate. It is a
 * static singleton holding constants currently.
 *
 * Values (and image) derived from:
 * http://www.bio-rad.com/en-ch/sku/hsr9905-hard-shell-96-well-pcr-plates-low-profile-clear-white-barcoded
 *
 * @author J. Christopher Anderson
 */
public class PCRBlockConfig implements BlockConfig {

    private final double xoffset = 1.438;
    private final double yoffset = 1.124;
    private final double subBlockWidth = 0.9;
    private final double subBlockHeight = 0.9;
    private final double width = 12.776;
    private final double height = 8.548;
    private final int numRows = 8;
    private final int numCols = 12;

    private static PCRBlockConfig config;

    private PCRBlockConfig() {
    }

    public static PCRBlockConfig getInstance() {
        if (config != null) {
            return config;
        } else {
            config = new PCRBlockConfig();
        }

        return config;
    }

    @Override
    public double getXoffset() {
        return xoffset;
    }

    @Override
    public double getYoffset() {
        return yoffset;
    }

    @Override
    public double getSubBlockWidth() {
        return subBlockWidth;
    }

    @Override
    public double getSubBlockHeight() {
        return subBlockHeight;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getWidth() {
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
