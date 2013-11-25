
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.ohi.sim.fx.component.view.table;

//~--- non-JDK imports --------------------------------------------------------

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;

import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javafx.util.Callback;

import org.ihtsdo.fxmodel.concept.component.FxComponentChronicle;
import org.ihtsdo.fxmodel.concept.component.FxComponentVersion;

/**
 *
 * @author kec
 */
public abstract class AbstractComponentCellValueFactory<V extends FxComponentVersion,
        C extends FxComponentChronicle<V, ?>>
        implements Callback<TableColumn.CellDataFeatures<C, Node>, ObservableValue<Node>> {
   @Override
   public ObservableValue<Node> call(TableColumn.CellDataFeatures<C, Node> p) {
      GridPane gridpane = new GridPane();
      String lastStringValue = "";
      int row  = 1;
      for (V version : p.getValue().getVersions()) {
         String currentStringValue = getStringValue(version);
         Text  textNode          = new Text(currentStringValue);
         textNode.wrappingWidthProperty().bind(p.getTableColumn().widthProperty());
         
         
         if (currentStringValue.equals(lastStringValue)) {
             textNode.setStyle(".text { -fx-font-smoothing-type: lcd; -fx-fill: #C0C0C0 }");
         } else {
             textNode.setStyle(".text { -fx-font-smoothing-type: lcd; }");
         }

         lastStringValue = currentStringValue;
         gridpane.add(textNode, 1, row++); // column=1 row= row++;
      }

      return new SimpleObjectProperty<Node>(gridpane);
   }

   //~--- get methods ---------------------------------------------------------

   public abstract String getStringValue(V version);
}
