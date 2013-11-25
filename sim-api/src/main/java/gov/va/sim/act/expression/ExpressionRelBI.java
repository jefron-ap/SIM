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


package gov.va.sim.act.expression;

import gov.va.sim.act.expression.node.ExpressionNodeBI;
import org.ihtsdo.tk.api.concept.ConceptVersionBI;

/**
 * A directed relationship between two value expression nodes
 * 
 * @author Keith E. Campbell
 */
public interface ExpressionRelBI extends ExpressionComponentBI {
    /**
     * 
     * @return the origin of the relationship
     */
   ExpressionNodeBI<?> getOrigin();
   
   /**
    * 
    * @return a concept representing the type of the relationship
    */
   ConceptVersionBI getType();
   
   /**
    * 
    * @return the destination of the relationship
    */
   ExpressionNodeBI<?> getDestination();
   
   /**
    * 
    * @return the relationship group that this relationship participates in.
    */
   ExpressionRelGroupBI getRelGroup();
   
   /**
    * 
    * @param relGroup set the relationship group that this relationship participates in
    */
   void setRelGroup(ExpressionRelGroupBI relGroup);
}
