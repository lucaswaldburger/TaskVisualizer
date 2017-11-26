package org.ucb.c5.semiprotocol.model;

/**
 *
 * @author J. Christopher Anderson
 */
public class RemoveContainer implements Task {
    private final Container tubeType;
    private final String name;

    public RemoveContainer(String containerName, Container containerType) {
        this.name = containerName;
        this.tubeType = containerType;
    }

    public Container getTubeType() {
        return tubeType;
    }

    public String getName() {
        return name;
    }

    @Override
    public LabOp getOperation() {
        return LabOp.removeContainer;
    }
        
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(getOperation()).append("\t");
        out.append(tubeType);
        return out.toString();
    }
}
