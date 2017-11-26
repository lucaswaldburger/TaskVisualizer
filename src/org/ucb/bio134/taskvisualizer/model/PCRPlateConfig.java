package org.ucb.bio134.taskvisualizer.model;

/**
 * This handles configuration information about a 96-well pcr plate. It is a
 * static singleton holding constants currently.
 *
 * Dimensions (and image) derived from:
 * http://www.bio-rad.com/en-ch/sku/hsr9905-hard-shell-96-well-pcr-plates-low-profile-clear-white-barcoded
 *
 * TODO: Add 384 well plate configuration
 *
 * Dimensions (and image) derived from:
 * http://www.bio-rad.com/en-us/sku/hsp3805-hard-shell-384-well-pcr-plates-thin-wall-skirted-clear-white
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class PCRPlateConfig implements Config {

    private final double xoffset = 1.438;
    private final double yoffset = 1.124;
    private final int width = 400;
    private final int height = 200;
    private final int numRows = 8;
    private final int numCols = 12;
    private final int wellheight = height/numRows;
    private final int wellwidth = width/numCols;

    private static PCRPlateConfig config;

    public PCRPlateConfig() {
    }

    public static PCRPlateConfig getInstance() {
        if (config != null) {
            return config;
        } else {
            config = new PCRPlateConfig();
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
