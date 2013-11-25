/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ohi.sim.fx.relationship.view.table;

import gov.va.ohi.sim.fx.component.view.table.AbstractComponentCellValueFactory;
import org.ihtsdo.fxmodel.concept.component.FxComponentChronicle;
import org.ihtsdo.fxmodel.concept.component.relationship.FxRelationshipVersion;

/**
 *
 * @author kec
 */
public class RelationshipDestinationCellValueFactory  
        extends AbstractComponentCellValueFactory<FxRelationshipVersion,
           FxComponentChronicle<FxRelationshipVersion, ?>> {
   @Override
   public String getStringValue(FxRelationshipVersion version) {
      return version.getDestinationReference().getText();
   }
}