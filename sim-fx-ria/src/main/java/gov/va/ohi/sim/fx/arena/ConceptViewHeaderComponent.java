
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.ohi.sim.fx.arena;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 *
 * @author kec
 */
public class ConceptViewHeaderComponent implements ConceptViewComponentBI {
   private ViewComponentType viewComponentType;
   private Node node;

   //~--- constructors --------------------------------------------------------

   public ConceptViewHeaderComponent(ViewComponentType viewComponentType) {
      this.viewComponentType = viewComponentType;
   }

    //~--- methods -------------------------------------------------------------

   @Override
   public int compareTo(ConceptViewComponentBI o) {
      if (viewComponentType != o.getViewComponentType()) {
         return viewComponentType.ordinal() - o.getViewComponentType().ordinal();
      }

      throw new UnsupportedOperationException();
   }

   @Override
   public String toString() {
      return viewComponentType.toString();
   }

   //~--- get methods ---------------------------------------------------------

    @Override
   public ViewComponentType getViewComponentType() {
      return viewComponentType;
   }

    @Override
    public Node getNode() throws IOException {
        // TODO convert this to use fxml, and a controller class. 
        if (node == null) {
            node = FXMLLoader.load(getClass().getResource("ConceptHeader.fxml"));
        }
        return node;
    }

    @Override
    public String getText() {
        return "";
    }
}
