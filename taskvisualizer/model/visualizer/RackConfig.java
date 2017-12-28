package org.ucb.bio134.taskvisualizer.model.visualizer;

import org.ucb.bio134.taskvisualizer.view.View;

/**
 * Defines the configuration of the rack within the workspace.
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */

public class RackConfig implements Config {
    private final int wellWidth = 50;
    private final int wellHeight = 50;
    private final int numRows = 2;
    private final int numCols = 1;
    private final int width = View.plateWidth * numCols;
    private final int height = View.plateHeight * numRows;


    private static RackConfig config;

    private RackConfig() {
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
