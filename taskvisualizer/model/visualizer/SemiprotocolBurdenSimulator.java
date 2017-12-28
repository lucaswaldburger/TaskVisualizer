package org.ucb.bio134.taskvisualizer.model.visualizer;

import java.util.HashMap;
import java.util.HashSet;

import org.ucb.c5.semiprotocol.model.*;
import org.ucb.c5.semiprotocol.model.Semiprotocol;

/**
 * Simulates the human burden for a Semiprotocol. This version has been tweaked slightly from the
 * original version to give the human burden associated with each Task of the protocol, rather
 * than the protocol as a whole.
 *
 * @author Tong Zhang
 * @author Lucas M. Waldburger
 */

public class SemiprotocolBurdenSimulator {

    private BurdenCalculator burdenCalculator;
    private HashSet<Reagent> buffer;
    private HashSet<Reagent> enzyme;
    private int currentBurden;

    /**
     * Instantiates class variables
     *
     * @throws Exception issue instantiating variables
     */
    public void initiate() throws Exception {
        burdenCalculator  = new BurdenCalculator();
        burdenCalculator.initiate();

        buffer = new HashSet<>();
        enzyme = new HashSet<>();

        buffer.add(Reagent.T4_DNA_Ligase_Buffer_10x);
        buffer.add(Reagent.NEB_Buffer_2_10x);
        buffer.add(Reagent.NEB_Buffer_3_10x);
        buffer.add(Reagent.NEB_Buffer_4_10x);
        buffer.add(Reagent.Q5_Polymerase_Buffer_5x);
        buffer.add(Reagent.dNTPs_2mM);

        enzyme.add(Reagent.phusion);
        enzyme.add(Reagent.DpnI);
        enzyme.add(Reagent.Q5_polymerase);
        enzyme.add(Reagent.BamHI);
        enzyme.add(Reagent.BglII);
        enzyme.add(Reagent.BsaI);
        enzyme.add(Reagent.BsmBI);
        enzyme.add(Reagent.T4_DNA_ligase);
        enzyme.add(Reagent.SpeI);
        enzyme.add(Reagent.XhoI);
        enzyme.add(Reagent.XbaI);
        enzyme.add(Reagent.PstI);
        enzyme.add(Reagent.Hindiii);
    }

    /**
     * Updates the HashMaps containing the human burden count
     *
     * @param step current task in the Semiprotocol
     * @return total human burden up to the current step
     * @throws Exception error adding element to burden count HashMap
     */
    public int run(Task step) throws Exception {
        HashMap<Burden, Integer> burdenCount = new HashMap<>();
            switch (step.getOperation()) {
                case addContainer:
                    burdenCount.put(Burden.add_label_plate_tube, 1);
                    AddContainer a = (AddContainer) step;
                    if (a.getTubetype() == Container.pcr_plate_96) {
                        burdenCount.put(Burden.seal_plate, 1);
                    }
                    break;

                case dispense:
                    burdenCount.put(Burden.uncap_eppen_tube, 1);
                    burdenCount.put(Burden.cap_eppen_tube, 1);

                    Dispense d = (Dispense) step;
                    if (d.getReagent() == Reagent.water) {
                        burdenCount.put(Burden.dispense_water, 1);
                    } else if (buffer.contains(d.getReagent())) {
                        burdenCount.put(Burden.dispense_buffer, 1);
                    } else if (enzyme.contains(d.getReagent())) {
                        burdenCount.put(Burden.dispense_enzyme, 1);
                    }

                    burdenCount.put(Burden.uncap_eppen_tube, 1);
                    burdenCount.put(Burden.cap_eppen_tube, 1);
                    break;

                case transfer:
                    burdenCount.put(Burden.uncap_eppen_tube, 2);
                    burdenCount.put(Burden.cap_eppen_tube, 2);

                    burdenCount.put(Burden.single_tip_transfer, 1);
                    break;

                case multichannel:
                    burdenCount.put(Burden.multichannel_transfer, 1);
                    break;

                default:
                    System.out.println("unknown operations");
                    break;
            }
        currentBurden += burdenCalculator.run(burdenCount);
        return currentBurden;
        }

    /**
     * Calculates the total human burden associated with an entire Semiprotocol
     *
     * @param protocol the entire Semiprotocol
     * @return total human burden
     * @throws Exception error adding element to burden count HashMap
     */
    public int run(Semiprotocol protocol) throws Exception {
        HashMap<Burden, Integer> burdenCount = new HashMap<>();

        for (Task task: protocol.getSteps()) {
            switch (task.getOperation()) {
                case addContainer:
                    burdenCount.put(Burden.add_label_plate_tube, 1);
                    AddContainer a = (AddContainer) task;
                    if (a.getTubetype() == Container.pcr_plate_96) {
                        burdenCount.put(Burden.seal_plate, 1);
                    }
                    break;

                case dispense:
                    burdenCount.put(Burden.uncap_eppen_tube, 1);
                    burdenCount.put(Burden.cap_eppen_tube, 1);

                    Dispense d = (Dispense) task;
                    if (d.getReagent() == Reagent.water) {
                        burdenCount.put(Burden.dispense_water, 1);
                    } else if (buffer.contains(d.getReagent())) {
                        burdenCount.put(Burden.dispense_buffer, 1);
                    } else if (enzyme.contains(d.getReagent())) {
                        burdenCount.put(Burden.dispense_enzyme, 1);
                    }


                    burdenCount.put(Burden.uncap_eppen_tube, 1);
                    burdenCount.put(Burden.cap_eppen_tube, 1);
                    break;

                case transfer:
                    burdenCount.put(Burden.uncap_eppen_tube, 2);
                    burdenCount.put(Burden.cap_eppen_tube, 2);

                    burdenCount.put(Burden.single_tip_transfer, 1);
                    break;

                case multichannel:
                    burdenCount.put(Burden.multichannel_transfer, 1);
                    break;

                default:
                    System.out.println("unknown operations");
                    break;
            }
        }

        return burdenCalculator.run(burdenCount);
    }

    public int getCurrentBurden() {
        return currentBurden;
    }
}
