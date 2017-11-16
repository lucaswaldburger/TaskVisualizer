package org.ucb.bio134.taskmaster;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ucb.c5.constructionfile.ParseConstructionFile;
import org.ucb.c5.constructionfile.model.*;
import org.ucb.c5.semiprotocol.model.*;
import org.ucb.c5.utils.FileUtils;

/**
 *
 * Inputs a bag of Construction Files and calculates the Semiprotocols
 * to implement them efficiently
 *
 * Run's output:
 *
 * Semiprotocol[0] =  the PCR Semiprotocol
 * Semiprotocol[1] =  the Digestion Semiprotocol
 * Semiprotocol[2] =  the Ligation Semiprotocol
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */
public class TaskDesigner {
    private List<PCR> pcrs;
    private List<Ligation> ligations;
    private List<Digestion> digestions;
    private List<Task> pcrSteps;
    private List<Task> ligationSteps;
    private List<Task> digestionSteps;
    private List<String> containerLocations;
    private List<String> deckLocations;
    private Map<String,Double> dilutionInventory;
    private Map<String,Double> oligoInventory;
    private Map<String,String> tubeLocation;
    private int deckIndex;
    private int containerIndex;
    private int deckCount;
    private String mastermix;

    public void initiate() throws Exception {
        //Instantiates class variables
        pcrs = new ArrayList<>();
        ligations = new ArrayList<>();
        digestions = new ArrayList<>();

        pcrSteps = new ArrayList<>();
        ligationSteps = new ArrayList<>();
        digestionSteps = new ArrayList<>();

        containerLocations = new ArrayList<>();
        deckLocations = new ArrayList<>();
        tubeLocation = new HashMap<>();

        dilutionInventory = new HashMap<>();
        oligoInventory = new HashMap<>();

        deckIndex = 0;
        deckCount = 1;
        mastermix = "mastermix";

        String[] rows = {"A","B","C","D","E","F","G","H"};
        int columns = 12;
        for (String row: rows){
            for (int i = 1; i <= columns; i++) {
                deckLocations.add(row+i);
            }
        }
    }

    private String getDeckLocation(String tubeName) {
        if (deckIndex >= deckLocations.size()) {
            deckIndex = 0;
            deckCount++;
        }
        if (!tubeLocation.containsKey(tubeName)) {
            String location = "deck_" + deckCount + "_" + deckLocations.get(deckIndex);
            tubeLocation.put(tubeName,location);
            deckIndex++;
        }
        return tubeLocation.get(tubeName);
    }

    private Semiprotocol optimizePCR() {
        //Short circuit in case there are no PCR reactions
        if (pcrs.isEmpty()) {
            return new Semiprotocol(pcrSteps);
        }

        // Instantiate function variables
        String masterMixTube = "PCR_mastermix";
        int mastermixCount = 1;
        int totalPCR = pcrs.size();
        containerLocations = new ArrayList<>();
        containerIndex = 0;

        // Select container for PCR reactions
        if (totalPCR < 8) {
            pcrSteps.add(new AddContainer(Container.pcr_tube, "pcr",
                    "bench", true));
            for (int i = 1; i <= 8; i++) {
                containerLocations.add("tube_" + i);
            }
        } else if (totalPCR > 8 && totalPCR < 96){
            for (int i = 1; i <= Math.ceil(totalPCR/8.0); i++) {
                pcrSteps.add(new AddContainer(Container.pcr_strip, "pcr",
                        "bench", true));
                for (int j = 1; j <= 8; j++) {
                    containerLocations.add("strip_" + i + "_tube_" + j);
                }
            }
        } else {
            String[] rows = {"A","B","C","D","E","F","G","H"};
            for (int i = 1; i <= Math.ceil(totalPCR/96.0); i++) {
                pcrSteps.add(new AddContainer(Container.pcr_plate_96, "pcr",
                        "bench", true));
                for (String row: rows){
                    for (int j = 1; j <= 12; j++) {
                        containerLocations.add("plate_" + i + "_" + row+j);
                    }
                }
            }
        }

        /* Since the total mastermix volume for one PCR reaction is 44 * 1.1, then an eppendorf 1.5 mL with maximum
        of 1400 uL should fit mastermix for approx. 28 reactions. Likewise, an eppendorf 2 mL with maximum of 1900 uL
        should fit mastermix for approx. 39 reactions
        */
        while (totalPCR > 0) {
            // Make the PCR mastermix with all reagents except for the oligos and template DNA
            masterMixTube = "PCR_mastermix_" + mastermixCount;
            if (totalPCR > 39) {
                pcrSteps.add(new AddContainer(Container.eppendorf_2mL, masterMixTube,
                        "tube/E2mL" , true));
                pcrSteps.add(new Dispense(Reagent.water, masterMixTube, 28.5 * 39 * 1.10));
                pcrSteps.add(new Dispense(Reagent.Q5_Polymerase_Buffer_5x,
                        masterMixTube, 10.0 * 39 * 1.10));
                pcrSteps.add(new Dispense(Reagent.dNTPs_2mM,
                        masterMixTube, 5.0 * 39 * 1.10));
                pcrSteps.add(new Dispense(Reagent.Q5_polymerase,
                        masterMixTube, 0.5 * 39 * 1.10));
                totalPCR -= 39;
            } else  if (totalPCR > 28){
                pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, masterMixTube,
                        "tube/E1.5mL" , true));
                pcrSteps.add(new Dispense(Reagent.water, masterMixTube, 28.5 * 28 * 1.10));
                pcrSteps.add(new Dispense(Reagent.Q5_Polymerase_Buffer_5x,
                        masterMixTube, 10.0 * 28 * 1.10));
                pcrSteps.add(new Dispense(Reagent.dNTPs_2mM,
                        masterMixTube, 5.0 * 28 * 1.10));
                pcrSteps.add(new Dispense(Reagent.Q5_polymerase,
                        masterMixTube, 0.5 * 28 * 1.10));
                totalPCR -= 28;
            } else {
                pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, masterMixTube,
                        "tube/E1.5mL" , false));
                pcrSteps.add(new Dispense(Reagent.water, masterMixTube, 28.5 * totalPCR * 1.10));
                pcrSteps.add(new Dispense(Reagent.Q5_Polymerase_Buffer_5x,
                        masterMixTube, 10.0 * totalPCR * 1.10));
                pcrSteps.add(new Dispense(Reagent.dNTPs_2mM,
                        masterMixTube, 5.0 * totalPCR * 1.10));
                pcrSteps.add(new Dispense(Reagent.Q5_polymerase,
                        masterMixTube, 0.5 * totalPCR * 1.10));
                totalPCR = 0;
            }
            mastermixCount++;
        }

        // Add oligos and template for each PCR reaction
        for (PCR pcr : pcrs) {
            String primer1 = pcr.getOligo1();
            String primer2 = pcr.getOligo2();
            String template = pcr.getTemplate();
            String primer1Tube = primer1 + "_tube";
            String primer2Tube = primer2 + "_tube";
            String templateTube = template + "_tube";
            String primer1Dilution = "10uM_" + primer1;
            String primer2Dilution = "10uM_" + primer2;
            String templateDilution = "10uM_" + template;

            // Check oligo inventory for primer 1
            if (!oligoInventory.containsKey(primer1)) {
                pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL,primer1Tube,getDeckLocation(primer1Tube),false));
                pcrSteps.add(new Dispense(Reagent.water, getDeckLocation(primer1Tube), 300.0));
                oligoInventory.put(primer1,300.0);
            }

            // Check oligo inventory for primer 1
            if (!oligoInventory.containsKey(primer2)) {
                pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL,primer2Tube,getDeckLocation(primer2Tube),false));
                pcrSteps.add(new Dispense(Reagent.water, getDeckLocation(primer2Tube), 300.0));
                oligoInventory.put(primer2 ,300.0);
            }

            // Check dilution inventory for primer 1 dilution
            if (!dilutionInventory.containsKey(primer1Dilution)) {
                pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, primer1Dilution,
                        getDeckLocation(primer1Dilution), true));
                pcrSteps.add(new Dispense(Reagent.water, getDeckLocation(primer1Dilution),
                        90));
                pcrSteps.add(new Transfer(primer1Tube, getDeckLocation(primer1Dilution), 10));
                oligoInventory.replace(primer1, oligoInventory.get(primer1) - 10);
                dilutionInventory.put(primer1Dilution,100.0);
            } else {
                pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, primer1Dilution,
                        getDeckLocation(primer1Dilution), true));
            }

            // Check dilution inventory for primer 2 dilution
            if (!dilutionInventory.containsKey(primer2Dilution)) {
                pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, primer2Dilution,
                        getDeckLocation(primer2Dilution), true));
                pcrSteps.add(new Dispense(Reagent.water, getDeckLocation(primer2Dilution), 90));
                pcrSteps.add(new Transfer(primer2Tube, getDeckLocation(primer2Dilution), 10));
                oligoInventory.replace(primer2,oligoInventory.get(primer2) - 10);
                dilutionInventory.put(primer2Dilution,100.0);
            } else {
                pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, primer2Dilution,
                        getDeckLocation(primer2Dilution), true));
            }

            // Check dilution inventory for template DNA dilution
            if (!dilutionInventory.containsKey(templateDilution)) {
                pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, templateDilution,
                        getDeckLocation(templateDilution), true));
                pcrSteps.add(new Dispense(Reagent.water, getDeckLocation(templateDilution),
                        9.5));
                pcrSteps.add(new Transfer(templateTube, getDeckLocation(templateDilution), 0.5));
                dilutionInventory.put(templateDilution,10.0);
            } else {
                pcrSteps.add(new AddContainer(Container.eppendorf_1p5mL, templateDilution,
                        getDeckLocation(templateDilution), true));
            }

            // Add diluted oligos and template plasmid to the pcr tube
            pcrSteps.add(new Transfer(mastermix, containerLocations.get(containerIndex), 44));
            pcrSteps.add(new Transfer(primer1Dilution, containerLocations.get(containerIndex), 2.5));
            dilutionInventory.replace(primer1Dilution, dilutionInventory.get(primer1Dilution) - 2.5);
            pcrSteps.add(new Transfer(primer2Dilution, containerLocations.get(containerIndex), 2.5));
            dilutionInventory.replace(primer2Dilution, dilutionInventory.get(primer2Dilution) - 2.5);
            pcrSteps.add(new Transfer(templateDilution, containerLocations.get(containerIndex), 1));
            dilutionInventory.replace(templateDilution, dilutionInventory.get(templateDilution) - 1);
            containerIndex++;
        }

        return new Semiprotocol(pcrSteps);
    }

    private Semiprotocol optimizeLigation() {
        //Short circuit in case there are no ligation reactions
        if (ligations.isEmpty()) {
            return new Semiprotocol(ligationSteps);
        }

        // Instantiate function variables
        String masterMixTube;
        String destination = "Experiment";
        containerLocations = new ArrayList<>();
        containerIndex = 0;
        int totalLigations = ligations.size();

        // Select container for ligation reactions
        Container destinationType = null;
        if (totalLigations < 8) {
            ligationSteps.add(new AddContainer(Container.pcr_tube, "pcr_tube",
                    "bench", true));
            for (int i = 1; i <= 8; i++) {
                containerLocations.add("tube_" + i);
            }
        } else if (totalLigations > 8 && totalLigations < 96){
            for (int i = 1; i <= Math.ceil(totalLigations/8.0); i++) {
                ligationSteps.add(new AddContainer(Container.pcr_strip, "pcr_strip",
                        "bench", true));
                for (int j = 1; j <= 8; j++) {
                    containerLocations.add("strip_" + i + "_tube_" + j);
                }
            }
        } else {
            String[] rows = {"A","B","C","D","E","F","G","H"};
            for (int i = 1; i <= Math.ceil(totalLigations/96.0); i++) {
                ligationSteps.add(new AddContainer(Container.pcr_plate_96, "pcr_plate",
                        "bench", true));
                for (String row: rows){
                    for (int j = 1; j <= 12; j++) {
                        containerLocations.add("plate_" + i + "_" + row+j);
                    }
                }
            }
        }
        int mastermixCount = 1;
        while (totalLigations > 0) {
            // Make the ligation mastermix
            masterMixTube = "ligation_mastermix_" + mastermixCount;
            if (totalLigations > 39) {
                ligationSteps.add(new AddContainer(Container.eppendorf_2mL,
                        masterMixTube, masterMixTube + "/E2mL", true));
                ligationSteps.add(new Dispense(Reagent.water, masterMixTube, 1.1 * 7.5 *
                        39));
                ligationSteps.add(new Dispense(Reagent.T4_DNA_Ligase_Buffer_10x,
                        masterMixTube, 1.1 * 1.0 * 39));
                ligationSteps.add(new Dispense(Reagent.T4_DNA_ligase, masterMixTube, 1.1
                        * 0.5 * 39));
                totalLigations -= 39;
            } else if (totalLigations > 28) {
                ligationSteps.add(new AddContainer(Container.eppendorf_1p5mL,
                        masterMixTube, masterMixTube + "/E1.5mL", true));
                ligationSteps.add(new Dispense(Reagent.water, masterMixTube, 1.1 * 7.5 *
                        28));
                ligationSteps.add(new Dispense(Reagent.T4_DNA_Ligase_Buffer_10x,
                        masterMixTube, 1.1 * 1.0 * 28));
                ligationSteps.add(new Dispense(Reagent.T4_DNA_ligase, masterMixTube, 1.1
                        * 0.5 * 28));
                totalLigations -= 28;
            } else {
                ligationSteps.add(new AddContainer(Container.eppendorf_1p5mL,
                        masterMixTube, masterMixTube + "/E1.5mL", true));
                ligationSteps.add(new Dispense(Reagent.water, masterMixTube, 1.1 * 7.5 *
                        totalLigations));
                ligationSteps.add(new Dispense(Reagent.T4_DNA_Ligase_Buffer_10x,
                        masterMixTube, 1.1 * 1.0 * totalLigations));
                ligationSteps.add(new Dispense(Reagent.T4_DNA_ligase, masterMixTube, 1.1
                        * 0.5 * totalLigations));
                totalLigations = 0;
            }
            mastermixCount++;
        }

        for (Ligation lig : ligations) {
            ligationSteps.add(new Transfer(mastermix, containerLocations.get(containerIndex), 9.0));
            for (String fragment : lig.getFragments()) {
                ligationSteps.add(new Transfer(fragment,containerLocations.get(containerIndex),1.0));
            }
            containerIndex++;
        }
        return new Semiprotocol(ligationSteps);
    }

    private Semiprotocol optimizeDigestion() {
        //Short circuit in the case there are no digestion reactions
        if (digestions.isEmpty()) {
            return new Semiprotocol(digestionSteps);
        }

        //Instantiate function variables
        String masterMixTube;
        double volPerDigestation = 9.0;
        containerLocations = new ArrayList<>();
        containerIndex = 0;
        int totalDigestions = digestions.size();

        // Select container for digestion reactions
        if (totalDigestions < 8) {
            digestionSteps.add(new AddContainer(Container.pcr_tube, "pcr_tube",
                    "bench", true));
            for (int i = 1; i <= 8; i++) {
                containerLocations.add("tube_" + i);
            }
        } else if (totalDigestions > 8 && totalDigestions < 96){
            for (int i = 1; i <= Math.ceil(totalDigestions/8.0); i++) {
                digestionSteps.add(new AddContainer(Container.pcr_strip, "pcr_strip",
                        "bench", true));
                for (int j = 1; j <= 8; j++) {
                    containerLocations.add("strip_" + i + "_tube_" + j);
                }
            }
        } else {
            String[] rows = {"A","B","C","D","E","F","G","H"};
            for (int i = 1; i <= Math.ceil(totalDigestions/96.0); i++) {
                digestionSteps.add(new AddContainer(Container.pcr_plate_96, "pcr_plate",
                        "bench", true));
                for (String row: rows){
                    for (int j = 1; j <= 12; j++) {
                        containerLocations.add("plate_" + i + "_" + row+j);
                    }
                }
            }
        }
        int mastermixCount = 1;
        while (totalDigestions > 0) {
            // Make the digestion mastermix
            masterMixTube = "digestion_mastermix" + mastermixCount;
            if (totalDigestions > 39) {
                digestionSteps.add(new AddContainer(Container.eppendorf_2mL,masterMixTube, masterMixTube+"/E2mL",true));
                digestionSteps.add(new Dispense(Reagent.water, masterMixTube, 1.1 * 39 * 39));
                digestionSteps.add(new Dispense(Reagent.NEB_Buffer_2_10x, masterMixTube, 1.1*5.0*39));
                digestionSteps.add(new Dispense(Reagent.SpeI, masterMixTube, 1.1*0.5*39));
                digestionSteps.add(new Dispense(Reagent.DpnI, masterMixTube, 1.1*0.5*39));
                totalDigestions -= 39;
            } else if (totalDigestions > 28) {
                digestionSteps.add(new AddContainer(Container.eppendorf_1p5mL,masterMixTube, masterMixTube+"/E1.5mL",true));
                digestionSteps.add(new Dispense(Reagent.water, masterMixTube, 1.1 * 39 * 28));
                digestionSteps.add(new Dispense(Reagent.NEB_Buffer_2_10x, masterMixTube, 1.1*5.0*28));
                digestionSteps.add(new Dispense(Reagent.SpeI, masterMixTube, 1.1*0.5*28));
                digestionSteps.add(new Dispense(Reagent.DpnI, masterMixTube, 1.1*0.5*28));
                totalDigestions -= 28;
            } else {
                digestionSteps.add(new AddContainer(Container.eppendorf_1p5mL,masterMixTube, masterMixTube+"/E1.5mL",true));
                digestionSteps.add(new Dispense(Reagent.water, masterMixTube, 1.1 * 39 * totalDigestions));
                digestionSteps.add(new Dispense(Reagent.NEB_Buffer_2_10x, masterMixTube, 1.1*5.0*totalDigestions));
                digestionSteps.add(new Dispense(Reagent.SpeI, masterMixTube, 1.1*0.5*totalDigestions));
                digestionSteps.add(new Dispense(Reagent.DpnI, masterMixTube, 1.1*0.5*totalDigestions));
                totalDigestions = 0;
            }
            mastermixCount++;
        }

        for (Digestion dig : digestions) {
            String substrate = dig.getSubstrate();
            digestionSteps.add(new Transfer(mastermix,containerLocations.get(containerIndex),volPerDigestation));
            digestionSteps.add(new Transfer(substrate, containerLocations.get(containerIndex),5.0));
            containerIndex++;
        }
        return new Semiprotocol(digestionSteps);
    }

    private void addAssembly(Assembly assembly) {
        int assemblyCount = 1;
        String assemblyTube = "assembly_tube_" + assemblyCount;
        String assemblyTubeLocation = getDeckLocation(assemblyTube);
        digestionSteps.add(new AddContainer(Container.pcr_tube,assembly.getProduct(), assemblyTubeLocation,true));
        digestionSteps.add(new Dispense(Reagent.water,assemblyTubeLocation,5.0));
        digestionSteps.add(new Dispense(Reagent.T4_DNA_Ligase_Buffer_10x,assemblyTubeLocation,1.0));
        digestionSteps.add(new Dispense(Reagent.T4_DNA_ligase,assemblyTubeLocation,0.5));
        digestionSteps.add(new Dispense(Reagent.valueOf(assembly.getEnzyme().toString()),assemblyTubeLocation,0.5));
        for (String fragment : assembly.getFragments()) {
            digestionSteps.add(new Transfer(fragment, assemblyTubeLocation,1.0));
        }
        assemblyCount++;
    }

    public Semiprotocol[] run(List<ConstructionFile> cfiles) throws Exception {
        Semiprotocol[] allSemiProtocols = new Semiprotocol[3];
        for (ConstructionFile cfile : cfiles) {
            for (Step step : cfile.getSteps()) {
                Operation labOp = step.getOperation();
                switch (labOp) {
                    case pcr:
                        PCR pcr = (PCR) step;
                        pcrs.add(pcr);
                        break;
                    case ligate:
                        Ligation ligation = (Ligation) step;
                        ligations.add(ligation);
                        break;
                    case digest:
                        Digestion digestion = (Digestion) step;
                        digestions.add(digestion);
                        break;
                    case assemble:
                        addAssembly((Assembly) step);
                        break;
                }
            }
        }
        allSemiProtocols[0] = optimizePCR();
        allSemiProtocols[1] = optimizeDigestion();
        allSemiProtocols[2] = optimizeLigation();

        return allSemiProtocols;
    }

    public static void main(String[] args) throws Exception {
        ParseConstructionFile parser = new ParseConstructionFile();
        parser.initiate();

        //Read in the bag of construction files
        File dir = new File("insert the path to the protocols");
        List<ConstructionFile> cfiles = new ArrayList<>();
        for(File afile : dir.listFiles()) {
            String data = FileUtils.readFile(afile.getAbsolutePath());
            ConstructionFile constf = parser.run(data);
            cfiles.add(constf);
        }

        //Run the designer
        TaskDesigner designer = new TaskDesigner();
        designer.initiate();

        Semiprotocol[] semis = designer.run(cfiles);

        //Price out the designed protocols
        double totalCost = 0.0;
        SemiprotocolPriceSimulator sim = new SemiprotocolPriceSimulator();
        sim.initiate();

        for(Semiprotocol protocol : semis) {
            double price = sim.run(protocol);
            totalCost += price;
        }
        System.out.println("$" + totalCost);
    }
}