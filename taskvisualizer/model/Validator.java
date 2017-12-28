package org.ucb.bio134.taskvisualizer.model;

import org.ucb.c5.semiprotocol.model.Semiprotocol;

import java.util.HashMap;
import java.util.HashSet;

public class Validator {
    private HashSet<String> tubeNames;
    private HashMap<String,HashMap<String, Double>> pcrToContents;
    private HashMap<String,HashMap<String, Double>> ligationToContents;
    private HashMap<String,HashMap<String, Double>> digestionToContents;

    public void initiate() {
        // Get all the wells

        // Determine which are PCR, ligation, or digestion by tube name
    }
    public void run() {

        // Validate PCRs
        // Validate ligations
        // Validate digestions
    }
    public void validatePCR() {

    }
    public void validateLigation() {

    }
    public void validateDigestion() {

    }
}
