package org.ucb.bio134.taskvisualizer.model;

import org.ucb.bio134.taskmaster.model.Tip;
import org.ucb.c5.semiprotocol.ParseSemiprotocol;
import org.ucb.c5.semiprotocol.model.*;
import org.ucb.c5.utils.FileUtils;

import java.util.HashMap;

/**
 * Simulates the price for a Semiprotocol. This version has been tweaked slightly since project 4 to
 * give the cost at each Task of the protocol, rather than the protocol as a whole.
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
     * Instantiates class variables
     *
     * @throws Exception issue instantiating variables
     */
    public void initiate() throws Exception {
        priceCalculator.initiate();
        reagentCount = new HashMap<>();
        containerCount = new HashMap<>();
        tipCount = new HashMap<>();
    }

    /**
     * Updates the HashMaps with reagent, container, and tip counts for a given Task.
     *
     * @param task current task in the Semiprotocol
     * @return price total
     * @throws Exception error identifying LabOp
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
     * Updates the HashMaps with reagent, container, and tip counts for a given Semiprotocol.
     *
     * @param protocol the entire Semiprotocol
     * @return the total price for the Semiprotocol
     * @throws Exception error updating count HashMaps
     */
    public double runProtocol(Semiprotocol protocol) throws Exception {
        HashMap<Reagent, Double> localReagentCount = new HashMap<>();
        HashMap<Container, Integer> localContainerCount = new HashMap<>();
        HashMap<Tip, Integer> localTipCount = new HashMap<>();

        Tip tip;
        Reagent reagent;
        double volume;

        for (Task task : protocol.getSteps() ) {
            LabOp labOp = task.getOperation();
            switch (labOp) {
                case addContainer:
                    AddContainer ac = (AddContainer) task;
                    updateContainerCount(localContainerCount, ac);
                    break;
                case removeContainer:
                    RemoveContainer rc = (RemoveContainer) task;
                    break;
                case transfer:
                    Transfer t = (Transfer) task;
                    tip = getTip(t.getVolume());
                    updateTipCount(localTipCount, tip);
                    break;
                case dispense:
                    Dispense d = (Dispense) task;
                    tip = getTip(d.getVolume());
                    reagent = d.getReagent();
                    volume = d.getVolume();
                    updateTipCount(localTipCount, tip);
                    updateReagentCount(localReagentCount, reagent, volume);
                    break;
                case multichannel:
                    Multichannel m = (Multichannel) task;
                    break;
            }
        }

        return priceCalculator.run(localReagentCount, localContainerCount,
                localTipCount);
    }

    private void updateTipCount(HashMap<Tip, Integer> countMap, Tip t) {
        if (countMap.containsKey(t)) {
            countMap.put(t, countMap.get(t) + 1);
        } else {
            countMap.put(t, 1);
        }
    }

    private void updateReagentCount(HashMap<Reagent, Double> countMap,
                                    Reagent r, double v) {
        if (countMap.containsKey(r)) {
            countMap.put(r, countMap.get(r) + v);
        } else {
            countMap.put(r, v);
        }
    }

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
        return priceCalculator.getReagentTotal();
    }
    public double getTubeTotal(){
        return priceCalculator.getTubeTotal();
    }
    public double getTipTotal() {
        return priceCalculator.getTipTotal();
    }
    public static void main(String[] args) throws Exception {
        //Read in the example semiprotocol
        String text = FileUtils.readResourceFile("semiprotocol/data/mastermix7.txt");
        ParseSemiprotocol parser = new ParseSemiprotocol();
        parser.initiate();
        Semiprotocol protocol = parser.run(text);

        SemiprotocolPriceSimulator sim = new SemiprotocolPriceSimulator();
        sim.initiate();
        double price = sim.runProtocol(protocol);
        System.out.println("$" + price);
        System.out.println(sim.getReagentTotal());
        System.out.println(sim.getTubeTotal());
        System.out.println(sim.getTipTotal());
//        sim.getReagentTotal();
//        sim.getTubeTotal();
//        sim.getTipTotal();

    }
}
