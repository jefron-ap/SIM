
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

//~--- non-JDK imports --------------------------------------------------------

import gov.va.sim.act.expression.ExpressionBI;
import gov.va.sim.composition.CompositionNodeBI;
import gov.va.sim.id.IdentifiableInstanceBI;


//~--- JDK imports ------------------------------------------------------------

import java.util.Collection;

/**
 * Act: A formal and solemn writing, expressing that something has been done.
 *
 * @author Keith E. Campbell
 */
public interface ActBI extends IdentifiableInstanceBI, CompositionNodeBI {

   /**
    * equivalent: another is equivalent to this
    * @param another
    * @return true or false
    */
   boolean equivalent(ActBI another);

   /**
    * subsumedBy: another contains or includes this
    * @param another
    * @return true or false
    */
   boolean subsumedBy(ActBI another);

   /**
    * subsumes: this contains or includes another
    * @param another
    * @return true or false
    */
   boolean subsumes(ActBI another);

   //~--- get methods ---------------------------------------------------------

   /**
    *
    * @return the instance identifier for the subject of this act
    */
   ExpressionBI getActSubject();

   /**
    *
    * @return a mutable ConceptVersionBI collection that specifies the sections of
    * the document this act is contained within.
    */
   Collection<ExpressionBI> getSections();

   //~--- set methods ---------------------------------------------------------

   /**
    *
    * @param subjectUuid the instance identifier for the subject of
    * this act
    */
   void setActSubject(ExpressionBI subject);
}
