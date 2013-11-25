
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.ohi.sim.fx.arena;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;

/**
 *
 * @author kec
 */
public class ConceptViewItem extends TreeItem<ConceptViewHeaderComponent>
        implements Comparable<ConceptViewHeaderComponent> {
   public ConceptViewItem() {}

   public ConceptViewItem(ConceptViewHeaderComponent t) {
      super(t);
   }

   public ConceptViewItem(ConceptViewHeaderComponent t, Node node) {
      super(t, node);
   }

   //~--- methods -------------------------------------------------------------

   @Override
   public int compareTo(ConceptViewHeaderComponent o) {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
