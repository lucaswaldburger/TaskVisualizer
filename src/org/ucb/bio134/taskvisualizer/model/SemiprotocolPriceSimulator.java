package org.ucb.bio134.taskvisualizer.model;

import org.ucb.bio134.taskmaster.model.Tip;
import org.ucb.c5.semiprotocol.ParseSemiprotocol;
import org.ucb.c5.semiprotocol.model.*;
import org.ucb.c5.utils.FileUtils;

import java.util.HashMap;

/**
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class SemiprotocolPriceSimulator {
    private PriceCalculator priceCalculator = new PriceCalculator();
    private HashMap<Reagent, Double> reagentCount;
    private HashMap<Container, Integer> containerCount;
    private HashMap<Tip, Integer> tipCount;

    /**
     *
     * @throws Exception
     */
    public void initiate() throws Exception {
        priceCalculator.initiate();
        reagentCount = new HashMap<>();
        containerCount = new HashMap<>();
        tipCount = new HashMap<>();
    }

    /**
     *
     * @param task
     * @return
     * @throws Exception
     */
    public double run(Task task) throws Exception {
        Tip tip;
        Reagent reagent;
        double volume;

        LabOp labOp = task.getOperation();
        switch (labOp) {
            case addContainer:
                AddContainer ac = (AddContainer) task;
                updateContainerCount(containerCount, ac);
                break;
            case removeContainer:
                RemoveContainer rc = (RemoveContainer) task;
                break;
            case transfer:
                Transfer t = (Transfer) task;
                tip = getTip(t.getVolume());
                updateTipCount(tipCount, tip);
                break;
            case dispense:
                Dispense d = (Dispense) task;
                tip = getTip(d.getVolume());
                reagent = d.getReagent();
                volume = d.getVolume();
                updateTipCount(tipCount, tip);
                updateReagentCount(reagentCount, reagent, volume);
                break;
            case multichannel:
                Multichannel m = (Multichannel) task;
                break;
            }
        return priceCalculator.run(reagentCount, containerCount,
                tipCount);
    }

    /**
     *
     * @param countMap
     * @param t
     */
    private void updateTipCount(HashMap<Tip, Integer> countMap, Tip t) {
        if (countMap.containsKey(t)) {
            countMap.put(t, countMap.get(t) + 1);
        } else {
            countMap.put(t, 1);
        }
    }

    /**
     *
     * @param countMap
     * @param r
     * @param v
     */
    private void updateReagentCount(HashMap<Reagent, Double> countMap,
                                    Reagent r, double v) {
        if (countMap.containsKey(r)) {
            countMap.put(r, countMap.get(r) + v);
        } else {
            countMap.put(r, v);
        }
    }

    /**
     *
     * @param countMap
     * @param ac
     */
    private void updateContainerCount(HashMap<Container, Integer>
                                              countMap, AddContainer ac) {
        Container tube = ac.getTubetype();
        if (ac.isNew()) {
            if (countMap.containsKey(tube)) {
                countMap.put(tube,
                        countMap.get(tube) + 1);
            } else {
                countMap.put(tube, 1);
            }
        }
    }

    /**
     *
     * @param volume
     * @return
     */
    public Tip getTip(double volume) {
        if (volume <= 20) {
            return Tip.P20;
        } else if (volume > 20 && volume <= 200) {
            return Tip.P200;
        } else {
            return Tip.P1000;
        }
    }

    public double getReagentTotal() {
        return priceCalculator.reagentTotal;
    }
    public double getTubeTotal(){
        return priceCalculator.tubeTotal;
    }
    public double getTipTotal() {
        return priceCalculator.tipTotal;
    }
    public static void main(String[] args) throws Exception {
        //Read in the example semiprotocol
        String text = FileUtils.readResourceFile("semiprotocol/data/alibaba_semiprotocol.txt");
        ParseSemiprotocol parser = new ParseSemiprotocol();
        parser.initiate();
        Semiprotocol protocol = parser.run(text);

//        SemiprotocolPriceSimulator sim = new SemiprotocolPriceSimulator();
//        sim.initiate();
//        double price = sim.run(protocol);
//        System.out.println("$" + price);
//        sim.getReagentTotal();
//        sim.getTubeTotal();
//        sim.getTipTotal();

    }
}
