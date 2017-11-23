package org.ucb.bio134.taskvisualizer.model;

import org.ucb.bio134.taskvisualizer.view.View;

/**
 *
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */

public class RackConfig implements Config {
    private final double xoffset = 5;
    private final double yoffset = 5;
    private final int subBlockWidth = 50;
    private final int subBlockHeight = 50;
    private final int numRows = 2;
    private final int numCols = 1;
    private final int width = View.plateWidth * numCols;
    private final int height = View.plateHeight * numRows;


    private static RackConfig config;

    public RackConfig() {
    }

    public static RackConfig getInstance() {
        if (config != null) {
            return config;
        } else {
            config = new RackConfig();
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
    public int getSubBlockWidth() {
        return subBlockWidth;
    }

    @Override
    public int getSubBlockHeight() {
        return subBlockHeight;
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
