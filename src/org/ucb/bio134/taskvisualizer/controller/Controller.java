package org.ucb.bio134.taskvisualizer.controller;

import org.ucb.bio134.taskvisualizer.view.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
//    private final Semiprotocol protocol;
    private final View view;
    private int currPos = 0;



    public Controller(View view) {
        this.view = view;
        view.initiate();

        view.getNextBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
//                    goNext();
                    System.out.println("Mouse go next");
                } catch (Exception ex) {
                    System.err.println("Error going forward");
                    ex.printStackTrace();
                }
            }
        });

    }
//    private void goNext() throws Exception {
//        Task task = protocol.getSteps().get(currPos);
//        switch(task.getOperation()) {
//            case addContainer:
//                AddContainer addcon = (AddContainer) task;
//                Pair<Integer, Integer> position = Well.parseWellLabel(addcon.getLocation());
//                deck.addPlate(addcon.getName(), position.getKey(), position.getValue());
//                //Update the view
//                view.resetAll();
//                view.addTube(addcon.getName(), position.getKey(), position.getValue());
//                break;
//            case removeContainer:
//                System.out.println("Remove container");
//                RemoveContainer rc = (RemoveContainer) task;
//                deck.removePlate(rc.getContainer());
//                break;
//            case transfer:
//                Transfer tfer = (Transfer) task;
//                Well srcwell = deck.getWell(tfer.getSource());
//                srcwell.removeVolume(tfer.getVolume());
//                Well dstwell = deck.getWell(tfer.getDest());
//                dstwell.addVolume(tfer.getVolume());
//
//                //Update the view
//                view.resetAll();
//                String srcLocation = tfer.getSource();
//                System.out.println("srcLocation" + srcLocation);
//                Pair<Integer,Integer> srcPlatePos = deck.getPlatePos(srcLocation);
//                Pair<Integer,Integer> srcWell = Well.parseWellLabel(tfer.getSource());
//
//                System.out.println("srcPlatePos" + srcPlatePos);
//                System.out.println("srcWell" + srcWell);
//
//                //Color color, int plateRow, int plateCol, int wellRow, int wellCol
//                view.colorWell(Color.white, srcPlatePos.getKey(), srcPlatePos.getValue(), srcWell.getKey(), srcWell.getValue());
//
//                Pair<Integer,Integer> dstPlatePos = deck.getPlatePos(tfer.getDest());
//                Pair<Integer,Integer> dstWell = Well.parseWellLabel(tfer.getDest());
//
//                view.colorWell(Color.green, dstPlatePos.getKey(), dstPlatePos.getValue(), dstWell.getKey(), dstWell.getValue());
//                break;
//            case multichannel:
//                Multichannel multi = (Multichannel) task;
//                break;
//            case dispense:
//                Dispense disp = (Dispense) task;
//                break;
//
//
//
//
//
//
//
//
//
//        }
//
//
//    }
    public static void main(String[] args) throws Exception {
        View view = new View();
        Controller controller = new Controller(view);
        view.colorWell(Color.green,1,1);

    }
}
