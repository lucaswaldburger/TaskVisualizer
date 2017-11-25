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

    /**
     *
     */
    public ContentPanel() {
        setBackground(Color.lightGray);
        setLayout(new GridLayout(2, 1));
        setBorder(BorderFactory.createEmptyBorder(0,10,10,10));

        wellContents = new HashMap<>();

        //Initial display
        JLabel lbl1 = new JLabel("Contents");
        lbl1.setHorizontalTextPosition(JLabel.RIGHT);
        lbl1.setVerticalTextPosition(JLabel.BOTTOM);
        lbl1.setFont(secondary_font);
        add(lbl1);

    }

    public void getContentsFromWell(HashMap<String,Double> contents) {
        this.wellContents = contents;
    }

    public void displayContents() {
        removeAll();

        wellContents = new HashMap<>();
        wellContents.put("Empty", 0.0);
        wellContents.put("Test", 1.0);
        wellContents.put("of", 1.0);
        wellContents.put("scroll", 1.0);
        wellContents.put("pane", 1.0);
        wellContents.put("please", 1.0);
        wellContents.put("work", 1.0);
        wellContents.put(String.valueOf(Reagent.BsaI), 2.0);
        wellContents.put(String.valueOf(Reagent.DpnI), 2.0);
        wellContents.put(String.valueOf(Reagent.SpeI), 2.0);
        wellContents.put(String.valueOf(Reagent.PstI), 2.0);
        wellContents.put(String.valueOf(Reagent.XbaI), 2.0);
        wellContents.put(String.valueOf(Reagent.XhoI), 2.0);
        wellContents.put(String.valueOf(Reagent.zymo_10b), 2.0);
        wellContents.put(String.valueOf(Reagent.water), 2.0);


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
            table.setValueAt(entry.getValue(),row,1);
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
