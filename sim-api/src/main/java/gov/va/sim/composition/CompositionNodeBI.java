
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
*
 */
package gov.va.sim.composition;

//~--- JDK imports ------------------------------------------------------------

import java.util.Collection;
import java.util.UUID;

/**
 * An interface to represent compositionality of components that are identified by UUIDs.
 * In mathematics, semantics, and philosophy of language, the Principle of Compositionality
 * is the principle that the meaning of a complex expression is determined by the meanings
 * of its constituent expressions and the rules used to combine them.
 *
 * http://en.wikipedia.org/wiki/Principle_of_compositionality
 *
 * @author kec
 */
public interface CompositionNodeBI {

   /**
    *
    * @return the UUIDs of components that are compositional parts of this component
    */
   Collection<UUID> getChildrenUuids();

   /**
    *
    * @return the UUIDs of components that are composition of which this component is part of
    */
   Collection<UUID> getParentUuids();
}
