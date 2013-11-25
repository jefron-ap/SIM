
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.ohi.sim.fx.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import org.ihtsdo.helper.thread.NamedThreadFactory;

/**
 *
 * @author kec
 */
public class ActivityPanel {
   ExecutorService executorService =
      Executors.newCachedThreadPool(new NamedThreadFactory(new ThreadGroup("ActivityPanel"),
         "ActivityPanel executor"));

   ProgressIndicator p;

    public ActivityPanel(ProgressBar pb) {
        p = pb;
    }
   
   //~--- methods -------------------------------------------------------------

   public void addTask(Task task) {
      executorService.submit(task);
      p.progressProperty().bind(task.progressProperty());
      p.visibleProperty().bind(task.runningProperty());
      

   }
}
