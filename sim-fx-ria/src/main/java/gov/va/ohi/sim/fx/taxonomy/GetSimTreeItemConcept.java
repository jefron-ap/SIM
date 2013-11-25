
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.ohi.sim.fx.taxonomy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import org.ihtsdo.fxmodel.FxComponentReference;
import org.ihtsdo.fxmodel.FxTaxonomyReferenceWithConcept;
import org.ihtsdo.fxmodel.concept.FxConcept;
import org.ihtsdo.fxmodel.concept.component.relationship.FxRelationshipChronicle;
import org.ihtsdo.fxmodel.concept.component.relationship.FxRelationshipVersion;
import org.ihtsdo.fxmodel.fetchpolicy.RefexPolicy;
import org.ihtsdo.fxmodel.fetchpolicy.RelationshipPolicy;
import org.ihtsdo.fxmodel.fetchpolicy.VersionPolicy;
import org.ihtsdo.tk.api.coordinate.StandardViewCoordinates;
import org.ihtsdo.tk.rest.client.TtkRestClient;
 
/**
 *
 * @author kec
 */
class GetSimTreeItemConcept implements Callable<Boolean> {
   ArrayList<SimTreeItem> childrenToAdd      = new ArrayList<>();
   boolean                addChildren        = true;
   VersionPolicy          versionPolicy      = VersionPolicy.ACTIVE_VERSIONS;
   RelationshipPolicy     relationshipPolicy =
      RelationshipPolicy.ORIGINATING_AND_DESTINATION_TAXONOMY_RELATIONSHIPS;
   RefexPolicy refexPolicy = RefexPolicy.ANNOTATION_MEMBERS;
   FxConcept   concept;
   SimTreeItem treeItem;

   //~--- constructors --------------------------------------------------------

   public GetSimTreeItemConcept(SimTreeItem treeItem) {
      this.treeItem = treeItem;
   }

   public GetSimTreeItemConcept(SimTreeItem treeItem, boolean addChildren) {
      this.treeItem    = treeItem;
      this.addChildren = addChildren;
   }

   public GetSimTreeItemConcept(SimTreeItem treeItem, VersionPolicy versionPolicy, RefexPolicy refexPolicy,
                                RelationshipPolicy relationshipPolicy) {
      this.treeItem           = treeItem;
      this.versionPolicy      = versionPolicy;
      this.refexPolicy        = refexPolicy;
      this.relationshipPolicy = relationshipPolicy;
   }

   //~--- methods -------------------------------------------------------------

   @Override
   public Boolean call() throws Exception {
      FxComponentReference reference;

      if (addChildren) {
         reference = treeItem.getValue().getRelationshipVersion().getOriginReference();
      } else {
         reference = treeItem.getValue().getRelationshipVersion().getDestinationReference();
      }

      concept = TtkRestClient.getRestClient().getFxConcept(reference,
              StandardViewCoordinates.getSnomedLatest(), versionPolicy, refexPolicy,
              relationshipPolicy);

      if ((concept.getConceptAttributes() == null) || concept.getConceptAttributes().getVersions().isEmpty()
              || concept.getConceptAttributes().getVersions().get(0).isDefined()) {
         treeItem.setDefined(true);
      }

      if (concept.getOriginRelationships().size() > 1) {
         treeItem.setMultiParent(true);
      }

      if (addChildren) {
         for (FxRelationshipChronicle fxrc : concept.getDestinationRelationships()) {
            for (FxRelationshipVersion rv : fxrc.getVersions()) {
               FxTaxonomyReferenceWithConcept fxtrc     = new FxTaxonomyReferenceWithConcept(rv);
               SimTreeItem                    childItem = new SimTreeItem(fxtrc);

               childrenToAdd.add(childItem);
            }
         }
      }

      Collections.sort(childrenToAdd);
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            FxTaxonomyReferenceWithConcept itemValue = treeItem.getValue();

            treeItem.setValue(null);
            treeItem.getChildren().clear();
            treeItem.computeGraphic();
            treeItem.getChildren().addAll(childrenToAdd);
            treeItem.setValue(itemValue);
            treeItem.getValue().conceptProperty().set(concept);
         }
      });

      return true;
   }
}
