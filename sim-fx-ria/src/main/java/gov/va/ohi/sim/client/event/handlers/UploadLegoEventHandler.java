
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.ohi.sim.client.event.handlers;

import com.sun.jersey.api.client.UniformInterfaceException;
import gov.va.ohi.sim.client.App;
import gov.va.ohi.sim.fx.activity.ActivityPanel;
import gov.va.sim.rest.client.SimRestClient;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.DirectoryChooser;
import org.ihtsdo.helper.io.FileIO;

/**
 *
 * @author kec
 */
public class UploadLegoEventHandler implements EventHandler<ActionEvent> {
   Node ownerNode;
   ActivityPanel activityPanel;

   //~--- constructors --------------------------------------------------------

   public UploadLegoEventHandler(Node ownerNode, ActivityPanel activityPanel) {
      this.ownerNode = ownerNode;
      this.activityPanel = activityPanel;
   }

   //~--- methods -------------------------------------------------------------

   @Override
   public void handle(ActionEvent t) {
      DirectoryChooser chooser = new DirectoryChooser();

      chooser.setTitle("LEGO Folder");

      File defaultDirectory = new File(System.getProperty("user.home"));

      chooser.setInitialDirectory(defaultDirectory);

      File selectedDirectory = chooser.showDialog(App.stage);

      System.out.println("Selected folder: " + selectedDirectory.getAbsolutePath());

      if (selectedDirectory != null) {
          LegoLoader loader = new LegoLoader(selectedDirectory);
          activityPanel.addTask(loader);
      } else {
         System.out.println("No selected file. ");
      }
   }

   //~--- inner classes -------------------------------------------------------

   private static class LegoLoader extends Task<Boolean> {
      private File[] files;

      //~--- constructors -----------------------------------------------------

      public LegoLoader(File selectedDirectory) {
         this.files = selectedDirectory.listFiles();
         updateProgress(0, files.length);
      }

      //~--- methods ----------------------------------------------------------

      @Override
      protected Boolean call() throws Exception {
         for (int i = 0; i < files.length; i++) {
            updateProgress(i + 1, files.length);
            updateMessage("Processing: " + files[i].getName());
            File f = files[i];
            if (f.isFile() && f.getName().toLowerCase().endsWith(".xml")) {
               FileReader fr = null;

               try {
                  System.out.println("Uploading LEGO: " + f.getName());
                  fr = new FileReader(f);
                  SimRestClient.get().putLego(FileIO.readerToString(fr), f.getName().replace(' ', '_'));
               } catch (UniformInterfaceException | IOException ex) {
                  Logger.getLogger(UploadSmartEncounterEventHandler.class.getName()).log(Level.SEVERE, null,
                                   ex);
               } finally {
                  try {
                     fr.close();
                  } catch (IOException ex) {
                     Logger.getLogger(UploadSmartEncounterEventHandler.class.getName()).log(Level.SEVERE,
                                      null, ex);
                  }
               }
            }
         }
         updateProgress(files.length, files.length);
         updateMessage("Processing LEGOs complete");
         return true;
      }
   }
}
