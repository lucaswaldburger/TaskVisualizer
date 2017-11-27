package org.ucb.bio134.taskvisualizer.model;

/**
 * Specifies whether the Container will belong to a pcr or tube type plate. This allows for verification
 * that a pcr tube is not added to the tube rack and visa versa.
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */

public enum ContainerType {
    PCR,
    TUBE
}
