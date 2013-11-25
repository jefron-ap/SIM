
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

/**
 * Request: the act of requesting something
 *
 * @author Keith E. Campbell
 */
public interface RequestBI extends ActBI {

   /**
    * For a medication, the encoded request includes the form,
    * dosage, & route of administration
    * @return
    */
   ExpressionBI getEncodedRequest();

   /**
    * Interval in which the Request is to be performed
    * @return
    */
   long[] getInterval();

   /**
    * Possibly null if no recurrence.
    *
    * Recurrence consists of:
    *  TIMING e.g. q AM
    *  REPETITION e.g. MWF
    *  DURATION e.g. 2 months
    * @return
    */
   public Object getRecurrance();
}
