# TaskVisualizer

TaskVisualizer is a simulator to visualize the Semiprotocols from the TaskMaster 2.0. Unlike the PipetteAid, this will feature a PCR and tube rack and will verify the sequence of events from a semiprotocol by handling well volume and tubes already present in the workspace. TaskVisualizer will significantly reduce the time required to debug the results from TaskMaster 2.0 as well as future TaskMaster assignments.

## Unlike PipetteAid
* Additional functionality
  * Play
  * Simulate
  * End
* Features a complete workspace composed of
  * Tube rack
  * PCR rack
  * Deck (2x2 PCR plates)
* Allows access to contents of each well using MouseOver
* Keeps track of
  * Price at the current stage of the Semiprotocol
  * Human burden at the fcurrent stage of the Semiprotocl
  * Current pipette being used for Transfer and Dispense steps
  
## Addresses Confusion from TaskMaster 1.0
* User can view changes to the entire workspace
* Tube volume is considered to throw Exception when removing volume that results in a negative volume, or adding volume that exceeds the maximum volume for the specified Container
* Since user implementation of TaskMaster varied, it was challenging to write robust tests that applied to all cases. Therefore, TaskVisualizer serves as a visual guide to addressing errors in Semiprotocols generated by the TaskMaster.

## Workspace Representation
* PCR plates and rack
  * 8 rows x 12 columns
* Tube rack
  * 6 rows x 8 columns (default)
  * 8 rows x 12 columns
* Deck is empty until Semiprotocol specifies the addition of a plate

## Challenges
* No experience using Model-View-Controller (MVC) pattern for implementing user interfaces
* Sticking to the MVC framework
* MouseOver functionality
* Updating Well color, contents, and displayed volume
* Making implementation functional without drastically changing the Semiprotocol structure

## Next Steps
- [ ] For Transfer steps, add arrow specifying source to destination
- [ ] Work on a visual implementation of Multichannel
- [ ] Add tabs: to enable multiple tube and PCR racks
- [ ] Revise MouseOver: allow user to select well by clicking and sum the contents for multiple selected wells

## Authors

* **Lucas M. Waldburger** *TaskVisualizer MVC framework*
* **Tong Zhang** *TaskMaster 2.0 and Burden calculator*
* **J. Christopher Anderson** *PipetteAid framework as a reference*

## Acknowledgments

* Professor Anderson and Pawel Gniewek for feedback and assistance

## License

MIT
