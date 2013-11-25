/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ohi.sim.fx.component.view.table;

import org.ihtsdo.fxmodel.concept.component.FxComponentChronicle;
import org.ihtsdo.fxmodel.concept.component.FxTypedComponentVersion;

/**
 *
 * @author kec
 */
public class TypeCellValueFactory  
        extends AbstractComponentCellValueFactory<FxTypedComponentVersion,
           FxComponentChronicle<FxTypedComponentVersion, ?>> {
   @Override
   public String getStringValue(FxTypedComponentVersion version) {
      return version.getTypeReference().getText();
   }
}