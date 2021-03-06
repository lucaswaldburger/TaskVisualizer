package org.ucb.bio134.taskvisualizer.view.panels;

import org.ucb.bio134.taskmaster.PriceCalculator;
import org.ucb.bio134.taskmaster.SemiprotocolPriceSimulator;
import org.ucb.c5.semiprotocol.model.Reagent;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class ContentPanel extends JPanel {

    private static final Font secondary_font = new Font("Helvetica", 1, 22);
    private static final Font body_font = new Font("Helvetica", 1, 18);
    private static final Font emphasis_font = new Font("Helvetica", 1, 40);
    private HashMap<String, Double> wellContents;
    private SemiprotocolPriceSimulator sps = new SemiprotocolPriceSimulator();
    private PriceCalculator pc = new PriceCalculator();
    private MouseEvent mouseEvent;
    private DecimalFormat dc;

    /**
     *
     */
    public ContentPanel() {
        setBackground(Color.lightGray);
        setLayout(new GridLayout(2, 1));
        setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        dc = new DecimalFormat("0.00");
        wellContents = new HashMap<>();

        //Initial display
        JLabel lbl1 = new JLabel("Contents");
        lbl1.setHorizontalTextPosition(JLabel.RIGHT);
        lbl1.setVerticalTextPosition(JLabel.BOTTOM);
        lbl1.setFont(secondary_font);
        add(lbl1);

    }

    /**
     *
     * @param contents
     */
    public void getContentsFromWell(HashMap<String,Double> contents) {
        this.wellContents = contents;
    }

    /**
     *
     */
    public void displayContents() {
        removeAll();

        JLabel lbl1 = new JLabel("Contents");
        lbl1.setHorizontalTextPosition(JLabel.RIGHT);
        lbl1.setVerticalTextPosition(JLabel.BOTTOM);
        lbl1.setFont(secondary_font);
        add(lbl1);

        JTable table = new JTable(wellContents.size(),2);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        table.setFillsViewportHeight(true);
        int row=0;
        for(Map.Entry<String, Double> entry: wellContents.entrySet()){
            table.setValueAt(entry.getKey(),row,0);
            String formattedVol = dc.format(entry.getValue());
            table.setValueAt(formattedVol,row,1);
            row++;
        }
//        `
        JTableHeader header= table.getTableHeader();
        TableColumnModel colMod = header.getColumnModel();
        TableColumn contentCol = colMod.getColumn(0);
        contentCol.setHeaderValue("Content");
        TableColumn volumeCol = colMod.getColumn(1);
        volumeCol.setHeaderValue("Volume");
        header.repaint();

        add(scrollPane);

        revalidate();
        repaint();
    }
    public void hideContents() {
        removeAll();

        wellContents = new HashMap<>();

        //Initial display
        JLabel lbl1 = new JLabel("Contents");
        lbl1.setHorizontalTextPosition(JLabel.RIGHT);
        lbl1.setVerticalTextPosition(JLabel.BOTTOM);
        lbl1.setFont(secondary_font);
        add(lbl1);

        revalidate();
        repaint();
    }
}
