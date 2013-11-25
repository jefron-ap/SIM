/*
 * United States Government Work
 *
 * Veterans Health Administration
 * US Department of Veterans Affairs
 *
 * US Federal Government Agencies are required
 * to release their works as Public Domain.
 *
 * http://www.copyright.gov/title17/92chap1.html#105
 */

package gov.va.sim.act.expression.node;

import java.io.IOException;
import java.util.UUID;
import org.ihtsdo.tk.api.concept.ConceptVersionBI;

/**
 * A concept value expression node. 
 * 
 * @author Keith E. Campbell
 */
public interface ConceptNodeBI extends ExpressionNodeBI<ConceptVersionBI> {
    UUID getConceptUuid();
    String getPreferredDesc() throws IOException;
    String getFullySpecifiedDesc() throws IOException;

}
