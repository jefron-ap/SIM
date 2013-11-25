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
package gov.va.sim.act;

import java.util.UUID;
import org.ihtsdo.tk.api.concept.ConceptVersionBI;

public interface AssertionRefBI
{
	/**
	 * 
	 * @return The UUID of the assertion referenced by this AssertionRef
	 */
	UUID getAssertionInstanceUuid();

	/**
	 * 
	 * @return a concept representing the type of the relationship between the 
	 *         parent AssertionBI and the AssertionBI referenced by the AssertionInstanceUuid
	 */
	ConceptVersionBI getType();
}
