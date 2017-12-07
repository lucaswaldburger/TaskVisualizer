package org.ucb.bio134.taskvisualizer.model;

/**
 * Defines the configuration of the different types of plates in a workspace.
 *
 * @author J. Christopher Anderson
 * @author Lucas M. Waldburger
 */

public interface Config {
    //Public modifier has been removed since it is redundant
    int getWellWidth();

    int getWellHeight();

    int getWidth();

    int getHeight();

    int getNumRows();

    int getNumCols();
}

