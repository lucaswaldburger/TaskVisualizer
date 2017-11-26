package org.ucb.bio134.taskvisualizer.model;

/**
 *
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class DeckConfig implements Config{

    private final int xoffset = 5;
    private final int yoffset = 5;
    private final int numRows = 2;
    private final int numCols = 2;
    private final int width = 400 * numCols;
    private final int height = 200 * numRows + yoffset;
    private final int wellheight = height/numRows;
    private final int wellwidth = width/numCols;

    private static DeckConfig config;

    public DeckConfig() {
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
//
//    public static int calcPixels(Double inCm) {
//        double pixPerCm = DeckConfig.getInstance().pixels_per_cm;
//        double pix = inCm * pixPerCm / 1.3;
//        return (int) Math.floor(pix);
//    }
//
//    public static void main(String[] args) {
//        DeckConfig conf = DeckConfig.getInstance();
//        System.out.println("pixels_per_cm: " + conf.pixels_per_cm);
//        System.out.println("monitor_width_in_cm: " + conf.monitor_width);
//        System.out.println("monitor_height_in_cm: " + conf.monitor_height);
//    }
//}