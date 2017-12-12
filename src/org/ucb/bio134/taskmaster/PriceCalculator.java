package org.ucb.bio134.taskmaster;

import java.util.HashMap;
import java.util.Map;

import org.ucb.bio134.taskmaster.model.Tip;
import org.ucb.c5.semiprotocol.model.Container;
import org.ucb.c5.semiprotocol.model.Reagent;

/**
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger with peer review from Meital
 */
public class PriceCalculator {
    private HashMap<Reagent, Double> reagentPrice;
    private HashMap<Container, Double> tubePrice;
    private HashMap<Tip, Double> tipPrice;
    private double reagentTotal = 0.0;
    private double tubeTotal = 0.0;
    private double tipTotal = 0.0;

    public void initiate() throws Exception {
        reagentPrice = new HashMap<>();
        reagentPrice.put(Reagent.BsaI, 2.0);
        reagentPrice.put(Reagent.SpeI, 2.0);
        reagentPrice.put(Reagent.DpnI, 2.0);
        reagentPrice.put(Reagent.BsmBI,2.0);
        reagentPrice.put(Reagent.BglII,2.0);
        reagentPrice.put(Reagent.BamHI,2.0);
        reagentPrice.put(Reagent.PstI,2.0);
        reagentPrice.put(Reagent.XhoI,2.0);
        reagentPrice.put(Reagent.XbaI,2.0);
        reagentPrice.put(Reagent.Hindiii,2.0);
        reagentPrice.put(Reagent.BamHI,2.0);

        reagentPrice.put(Reagent.phusion,2.0);
        reagentPrice.put(Reagent.T4_DNA_ligase, 2.0);
        reagentPrice.put(Reagent.T4_DNA_Ligase_Buffer_10x, 0.0);
        reagentPrice.put(Reagent.Q5_polymerase, 2.0);
        reagentPrice.put(Reagent.Q5_Polymerase_Buffer_5x, 0.0);
        reagentPrice.put(Reagent.NEB_Buffer_2_10x, 0.0);
        reagentPrice.put(Reagent.NEB_Buffer_3_10x, 0.0);
        reagentPrice.put(Reagent.NEB_Buffer_4_10x, 0.0);
        reagentPrice.put(Reagent.dNTPs_2mM, 0.347);
        reagentPrice.put(Reagent.water, 0.0);

        reagentPrice.put(Reagent.lb, 0.0);
        reagentPrice.put(Reagent.lb_agar_100ug_ml_amp, 0.0);
        reagentPrice.put(Reagent.lb_agar_100ug_ml_cm, 0.0);
        reagentPrice.put(Reagent.lb_agar_100ug_ml_specto, 0.0);
        reagentPrice.put(Reagent.lb_agar_50ug_ml_kan, 0.0);
        reagentPrice.put(Reagent.lb_amp, 0.0);
        reagentPrice.put(Reagent.lb_cam, 0.0);
        reagentPrice.put(Reagent.lb_specto, 0.0);
        reagentPrice.put(Reagent.lb_agar_noAB, 0.0);
        reagentPrice.put(Reagent.Ec100D_pir_plus, 0.0);
        reagentPrice.put(Reagent.Ec100D_pir116, 0.0);
        reagentPrice.put(Reagent.arabinose_10p, 0.0);
        reagentPrice.put(Reagent.Zymo_5a, 0.0);
        reagentPrice.put(Reagent.zymo_10b, 0.0);
        reagentPrice.put(Reagent.JM109, 0.0);
        reagentPrice.put(Reagent.lb_kan, 0.0);
        reagentPrice.put(Reagent.MC1061, 0.0);
        reagentPrice.put(Reagent.DH10B, 0.0);

        tubePrice = new HashMap<>();
        tubePrice.put(Container.eppendorf_1p5mL, 0.027);
        tubePrice.put(Container.eppendorf_2mL, 0.026);
        tubePrice.put(Container.pcr_tube, 0.03);
        tubePrice.put(Container.pcr_strip, 0.178);
        tubePrice.put(Container.pcr_plate_96, 1.6);

        tipPrice = new HashMap<>();
        tipPrice.put(Tip.P1000, 0.074);
        tipPrice.put(Tip.P200, 0.038);
        tipPrice.put(Tip.P20, 0.026);

    }

    public double run(Map<Reagent, Double> reagentCount,
                      Map<Container, Integer> tubeCount,
                      Map<Tip, Integer> tipCount) throws Exception {
        Reagent reagent;
        Container tube;
        Tip tip;
        Double price;
        Double total = 0.0;

        for (Map.Entry<Reagent, Double> rCPair : reagentCount.entrySet()) {
            reagent = rCPair.getKey();
            Double count = rCPair.getValue();
            price = reagentPrice.get(reagent);
            total += price * count;
            reagentTotal += price * count;

        }

        for (Map.Entry<Container, Integer> tuCPair : tubeCount.entrySet()) {
            tube = tuCPair.getKey();
            Integer count = tuCPair.getValue();
            price = tubePrice.get(tube);
            total += price * count;
            tubeTotal += price * count;
        }

        for (Map.Entry<Tip, Integer> tiCPair : tipCount.entrySet()) {
            tip = tiCPair.getKey();
            Integer count = tiCPair.getValue();
            price = tipPrice.get(tip);
            total += price * count;
        }

        return total;

    }


    public static void main(String[] args) throws Exception {
        PriceCalculator calc = new PriceCalculator();
        calc.initiate();

        //Construct some example data
        Map<Reagent, Double> reagentCount = new HashMap<>();
        reagentCount.put(Reagent.BsaI, 32.0);
        reagentCount.put(Reagent.T4_DNA_Ligase_Buffer_10x, 32.0);
        reagentCount.put(Reagent.T4_DNA_ligase, 16.0);

        Map<Container, Integer> tubeCount = new HashMap<>();
        tubeCount.put(Container.pcr_strip, 3);
        tubeCount.put(Container.pcr_tube, 8);
        tubeCount.put(Container.pcr_plate_96, 1);
        tubeCount.put(Container.eppendorf_1p5mL, 3);
        tubeCount.put(Container.eppendorf_2mL, 2);

        Map<Tip, Integer> tipCount = new HashMap<>();
        tipCount.put(Tip.P20, 86);
        tipCount.put(Tip.P200, 23);
        tipCount.put(Tip.P1000, 6);

        double price = calc.run(reagentCount, tubeCount, tipCount);
        System.out.println("$" + price);
    }
}
