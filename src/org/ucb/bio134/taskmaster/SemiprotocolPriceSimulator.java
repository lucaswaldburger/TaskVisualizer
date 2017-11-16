package org.ucb.bio134.taskmaster;

import java.util.HashMap;

import org.ucb.bio134.taskmaster.model.Tip;
import org.ucb.c5.semiprotocol.ParseSemiprotocol;
import org.ucb.c5.semiprotocol.model.*;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * @author J. Christopher Anderson
 */
public class SemiprotocolPriceSimulator {
    private PriceCalculator priceCalculator = new PriceCalculator();


    public void initiate() throws Exception {

        priceCalculator.initiate();

    }

    public double run(Semiprotocol protocol) throws Exception {

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

    private Tip getTip(double volume) {
        if (volume <= 20) {
            return Tip.P20;
        } else if (volume > 20 && volume <= 200) {
            return Tip.P200;
        } else {
            return Tip.P1000;
        }
    }

    public static void main(String[] args) throws Exception {
        //Read in the example semiprotocol
        String text = FileUtils.readResourceFile("semiprotocol/data/alibaba_semiprotocol.txt");
        ParseSemiprotocol parser = new ParseSemiprotocol();
        parser.initiate();
        Semiprotocol protocol = parser.run(text);

        SemiprotocolPriceSimulator sim = new SemiprotocolPriceSimulator();
        sim.initiate();
        double price = sim.run(protocol);
        System.out.println("$" + price);
    }
}
