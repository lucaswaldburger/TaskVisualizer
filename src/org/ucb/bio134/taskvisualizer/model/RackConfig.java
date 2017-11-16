package org.ucb.bio134.taskvisualizer.model;

public class RackConfig implements BlockConfig{
    private final double xoffset = 1.438;
    private final double yoffset = 1.124;
    private final double subBlockWidth = 0.9;
    private final double subBlockHeight = 0.9;
    private final double width = 200;
    private final double height = 100;
    private final int numRows = 2;
    private final int numCols = 1;

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
