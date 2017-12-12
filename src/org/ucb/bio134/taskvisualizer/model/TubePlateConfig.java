package org.ucb.bio134.taskvisualizer.model;

/**
 * This handles configuration information about a 4x6 tube plate. It is a
 * static singleton holding constants currently.
 *
 * Dimensions (in centimeters) from:
 * https://www.fishersci.com/shop/products/nalgene-microcentrifuge-tube-rack-resmer-manufacturing-technology/14809160
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class TubePlateConfig implements Config {

    private final double xoffset = 1.438;
    private final double yoffset = 1.124;
    private final int width = 400;
    private final int height = 200;
    private final int numRows = 8;
    private final int numCols = 12;
    private final int wellheight = height/numRows;
    private final int wellwidth = width/numCols;

    private static TubePlateConfig config;

    public TubePlateConfig() {
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
    public double getXoffset() {
        return xoffset;
    }

    @Override
    public double getYoffset() {
        return yoffset;
    }

    @Override
    public int getSubBlockWidth() {
        return wellwidth;
    }

    @Override
    public int getSubBlockHeight() {
        return wellheight;
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
