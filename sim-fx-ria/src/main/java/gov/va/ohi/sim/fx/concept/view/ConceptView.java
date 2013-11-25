
package gov.va.ohi.sim.fx.concept.view;

import gov.va.ohi.sim.fx.concept.GetConceptService;

/**
 *
 * @author kec
 */
public class ConceptView {
        private GetConceptService getConceptService = new GetConceptService();

        /*
         * When concept changes, recompute description versions, relationship versions...
         */
        
        /*
         * Create a "chronicle" table, rather than a version table. 
         * 
         * Each row has the same columns as the version table. 
         * 
         * Each column uses some type of layout (vbox?) that gives cellrow within the cell. 
         * 
         * Each cell uses a custom renderer to layout the cellrows within the cell.
         * 
         * Some type of shared row within a row property to track heights, to support wrapping, etc. 
         *  
         * row, cell, cellrow
         */
}
