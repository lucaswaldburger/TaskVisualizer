package org.ucb.bio134.taskvisualizer.model;

/**
 * Types of actions that result in human burden
 *
 * @author Tong Zhang
 */
public enum Burden {

    //uncap container
    uncap_pcr_strip,
    uncap_pcr_tube,
    uncap_eppen_tube,
    unseal_plate,

    //cap container
    seal_plate,
    cap_pcr_tube,
    cap_eppen_tube,
    cap_pcr_strip,

    //transfer
    single_tip_transfer,
    multichannel_transfer,

    //dispense
    dispense_enzyme,
    dispense_buffer,
    dispense_water,

    //add and label containers
    add_label_plate_tube
}
