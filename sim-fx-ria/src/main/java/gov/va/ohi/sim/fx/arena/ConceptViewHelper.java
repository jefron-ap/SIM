
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.ohi.sim.fx.arena;

import com.sun.javafx.scene.control.skin.TreeCellSkin;
import javafx.scene.control.TreeView;
import javafx.util.Callback;
import com.sun.javafx.scene.control.skin.TreeViewSkin;

/**
 *
 * @author kec
 */
public class ConceptViewHelper {
   TreeView conceptView;

   //~--- constructors --------------------------------------------------------

   public ConceptViewHelper(TreeView conceptView) {
      this.conceptView = conceptView;

      ConceptViewItem root =
         new ConceptViewItem(new ConceptViewHeaderComponent(ViewComponentType.ROOT));

      root.setExpanded(true);
      root.getChildren().addAll(
      //J-
              new ConceptViewItem(new ConceptViewHeaderComponent(ViewComponentType.CONCEPT_HEADER)), 
              new ConceptViewItem(new ConceptViewHeaderComponent(ViewComponentType.DESCRIPTION_HEADER)),
              new ConceptViewItem(new ConceptViewHeaderComponent(ViewComponentType.RELATIONSHIP_ORIGIN_HEADER)),
              new ConceptViewItem(new ConceptViewHeaderComponent(ViewComponentType.RELATIONSHIP_DESTINATION_HEADER)),
              new ConceptViewItem(new ConceptViewHeaderComponent(ViewComponentType.MEDIA_HEADER)),
              new ConceptViewItem(new ConceptViewHeaderComponent(ViewComponentType.REFSET_MEMBERS_HEADER))
              //J+
      );
      this.conceptView.setCellFactory(new Callback<TreeView<ConceptViewComponentBI>, ConceptViewCell>() {
         @Override
         public ConceptViewCell call(TreeView<ConceptViewComponentBI> p) {
            return new ConceptViewCell();
         }
      });
      this.conceptView.setRoot(root);
      this.conceptView.setShowRoot(false);
      this.conceptView.setSkin(new TreeViewSkin(this.conceptView));
      TreeViewSkin skin = (TreeViewSkin) this.conceptView.getSkin();
//      skin.setIndent(0);
      
   }
}
