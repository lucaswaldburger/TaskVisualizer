package org.ucb.c5.sequtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Translates a DNA sequence into a Protein sequence
 *
 * @author J. Christopher Anderson
 */
public class Translate {

    private Map<String, String> GeneticCode;

    public void initiate() {
        GeneticCode = new HashMap<>();

        GeneticCode.put("TCA", "S");
        GeneticCode.put("TCC", "S");
        GeneticCode.put("TCG", "S");
        GeneticCode.put("TCT", "S");
        GeneticCode.put("TTC", "F");
        GeneticCode.put("TTT", "F");
        GeneticCode.put("TTA", "L");
        GeneticCode.put("TTG", "L");
        GeneticCode.put("TAC", "Y");
        GeneticCode.put("TAT", "Y");
        GeneticCode.put("TAA", "*");
        GeneticCode.put("TAG", "*");
        GeneticCode.put("TGC", "C");
        GeneticCode.put("TGT", "C");
        GeneticCode.put("TGA", "*");
        GeneticCode.put("TGG", "W");
        GeneticCode.put("CTA", "L");
        GeneticCode.put("CTC", "L");
        GeneticCode.put("CTG", "L");
        GeneticCode.put("CTT", "L");
        GeneticCode.put("CCA", "P");
        GeneticCode.put("CCC", "P");
        GeneticCode.put("CCG", "P");
        GeneticCode.put("CCT", "P");
        GeneticCode.put("CAC", "H");
        GeneticCode.put("CAT", "H");
        GeneticCode.put("CAA", "Q");
        GeneticCode.put("CAG", "Q");
        GeneticCode.put("CGA", "R");
        GeneticCode.put("CGC", "R");
        GeneticCode.put("CGG", "R");
        GeneticCode.put("CGT", "R");
        GeneticCode.put("ATA", "I");
        GeneticCode.put("ATC", "I");
        GeneticCode.put("ATT", "I");
        GeneticCode.put("ATG", "M");
        GeneticCode.put("ACA", "T");
        GeneticCode.put("ACC", "T");
        GeneticCode.put("ACG", "T");
        GeneticCode.put("ACT", "T");
        GeneticCode.put("AAC", "N");
        GeneticCode.put("AAT", "N");
        GeneticCode.put("AAA", "K");
        GeneticCode.put("AAG", "K");
        GeneticCode.put("AGC", "S");
        GeneticCode.put("AGT", "S");
        GeneticCode.put("AGA", "R");
        GeneticCode.put("AGG", "R");
        GeneticCode.put("GTA", "V");
        GeneticCode.put("GTC", "V");
        GeneticCode.put("GTG", "V");
        GeneticCode.put("GTT", "V");
        GeneticCode.put("GCA", "A");
        GeneticCode.put("GCC", "A");
        GeneticCode.put("GCG", "A");
        GeneticCode.put("GCT", "A");
        GeneticCode.put("GAC", "D");
        GeneticCode.put("GAT", "D");
        GeneticCode.put("GAA", "E");
        GeneticCode.put("GAG", "E");
        GeneticCode.put("GGA", "G");
        GeneticCode.put("GGC", "G");
        GeneticCode.put("GGG", "G");
        GeneticCode.put("GGT", "G");
    }

    /**
     * Inputs a DNA sequence and outputs the 0 frame encoded protein
     *
     * @param seq the input DNA sequence
     * @return the encoded protein
     */
    public String run(String seq) {
        String dnaseq = seq.toUpperCase();
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < dnaseq.length(); i += 3) {
            String codon = dnaseq.substring(i, i + 3);
            out.append(GeneticCode.get(codon));
        }
        return out.toString();
    }
}
