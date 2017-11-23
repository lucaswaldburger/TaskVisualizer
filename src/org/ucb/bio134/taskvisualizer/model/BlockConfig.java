package org.ucb.bio134.taskvisualizer.model;


//public final double pixels_per_cm = 38; //7.874;
//
////Dimensions in units of cm
//public final double monitor_width = 45.72;
//public final double monitor_height = 30.48;
//
//public final double plate_offset_x = 6;
//public final double plate_offset_y = 1;
//
//public final double plate_spacing_x = 14;
//public final double plate_spacing_y = 9;
//
//public final int num_rows = 2;  //Number of plate positions horizontally
//public final int num_cols = 2; //Number of plate positions vertically

//private static DeckConfig config;
//
//private DeckConfig() {
//        }
//
//public static DeckConfig getInstance() {
//        if (config != null) {
//        return config;
//        } else {
//        config = new DeckConfig();
//        }
//
//        return config;
//        }
//
//public static int calcPixels(Double inCm) {
//        double pixPerCm = DeckConfig.getInstance().pixels_per_cm;
//        double pix = inCm * pixPerCm;
//        return (int) Math.floor(pix);
//        }
//
//public static void main(String[] args) {
//        DeckConfig conf = DeckConfig.getInstance();
//        System.out.println("pixels_per_cm: " + conf.pixels_per_cm);
//        System.out.println("monitor_width_in_cm: " + conf.monitor_width);
//        System.out.println("monitor_height_in_cm: " + conf.monitor_height);
//        }
public class BlockConfig implements Config {
    private final double xoffset = 1.438;
    private final double yoffset = 1.124;
    private final int subBlockWidth = 10;
    private final int subBlockHeight = 10;
    private final int width = 12;
    private final int height = 8;
    private final int numRows = 8;
    private final int numCols = 12;

    private static BlockConfig config;

    private BlockConfig() {
    }

    public static BlockConfig getInstance() {
        if (config != null) {
            return config;
        } else {
            config = new BlockConfig();
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
