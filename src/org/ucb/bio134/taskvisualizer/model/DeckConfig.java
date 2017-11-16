package org.ucb.bio134.taskvisualizer.model;

public class DeckConfig implements BlockConfig{
    public final double pixels_per_cm = 38; //7.874;

    //Dimensions in units of cm
    public final double monitor_width = 45.72;
    public final double monitor_height = 30.48;

    public final double plate_offset_x = 6;
    public final double plate_offset_y = 1;

    public final double plate_spacing_x = 14;
    public final double plate_spacing_y = 9;

    public final int num_rows = 2;  //Number of plate positions horizontally
    public final int num_cols = 2; //Number of plate positions vertically

    private final double xoffset = 1.438;
    private final double yoffset = 1.124;
    private final double wellwidth = 0.9;
    private final double wellheight = 0.9;
    private final double width = 12.776;
    private final double height = 8.548;
    private final int numRows = 2;
    private final int numCols = 2;

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
    public double getXoffset() {
        return xoffset;
    }

    @Override
    public double getYoffset() {
        return yoffset;
    }

    @Override
    public double getSubBlockWidth() {
        return wellwidth;
    }

    @Override
    public double getSubBlockHeight() {
        return wellheight;
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
