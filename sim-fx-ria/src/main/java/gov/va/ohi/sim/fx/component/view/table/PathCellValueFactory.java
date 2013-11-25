/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ohi.sim.fx.component.view.table;

import org.ihtsdo.fxmodel.concept.component.FxComponentChronicle;
import org.ihtsdo.fxmodel.concept.component.FxComponentVersion;

/**
 *
 * @author kec
 */
public class PathCellValueFactory 
        extends AbstractComponentCellValueFactory<FxComponentVersion,
           FxComponentChronicle<FxComponentVersion, ?>> {
   @Override
   public String getStringValue(FxComponentVersion version) {
      return version.getPathReference().getText();
   }
}
