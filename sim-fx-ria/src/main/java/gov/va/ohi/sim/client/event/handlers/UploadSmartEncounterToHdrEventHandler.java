/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ohi.sim.client.event.handlers;

import gov.va.ohi.sim.client.App;
import gov.va.sim.rest.client.SimRestClient;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import org.ihtsdo.helper.io.FileIO;

/**
 *
 * @author kec
 */
public class UploadSmartEncounterToHdrEventHandler implements EventHandler<ActionEvent> {
   Node ownerNode;

   //~--- constructors --------------------------------------------------------

   public UploadSmartEncounterToHdrEventHandler(Node ownerNode) {
      this.ownerNode = ownerNode;
   }

   //~--- methods -------------------------------------------------------------

   @Override
   public void handle(ActionEvent t) {
      FileChooser chooser = new FileChooser();

      chooser.setInitialDirectory(new File(System.getProperty("user.home")));
      chooser.setTitle("Select SMART Encounter...");

      File selectedFile = chooser.showOpenDialog(App.stage);

      if (selectedFile != null) {
         FileReader fr = null;

         try {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            fr = new FileReader(selectedFile);
            SimRestClient.get().putSmartDocumentHdr(FileIO.readerToString(fr));
         } catch (IOException ex) {
            Logger.getLogger(UploadSmartEncounterEventHandler.class.getName()).log(Level.SEVERE, null, ex);
         } finally {
            try {
               fr.close();
            } catch (IOException ex) {
               Logger.getLogger(UploadSmartEncounterEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      } else {
         System.out.println("No selected file. ");
      }
   }
}
