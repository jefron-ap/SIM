
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

//~--- non-JDK imports --------------------------------------------------------

import gov.va.sim.act.expression.ExpressionComponentBI;
import gov.va.sim.act.expression.ExpressionRelBI;

import org.ihtsdo.tk.api.concept.ConceptVersionBI;

//~--- JDK imports ------------------------------------------------------------

import java.beans.PropertyVetoException;

import java.io.IOException;
import java.util.UUID;

/**
 * The super interface for value expression nodes, that defines
 * how nodes can be connected to form an expression.
 *
 * @param <T> This type of value this node represents.
 * @author Keith E. Campbell
 */
public interface ExpressionNodeBI<T extends Object> extends ExpressionComponentBI {
    
    static final UUID NAMESPACE_UUID = UUID.fromString("acf58d00-56d2-11e1-b86c-0800200c9a66");

   /**
    * Add a relationship that directionally connects this node to another
    * @param relType A <code>ConceptVersionBI</code> defining the type of the relationship.
    * @param relDestination A <code>ExpressionNodeBI</code> that is the destination of this relationship.
    * @return The <code>ExpressionRelBI</code> created by this method.
    * @throws PropertyVetoException if the expression constraints are violated.
    */
   ExpressionRelBI addRel(ConceptVersionBI relType, ExpressionNodeBI<?> relDestination)
           throws PropertyVetoException;

   //~--- get methods ---------------------------------------------------------

   /**
    *
    * @return all the relationships between this expression node, and
    * other expression nodes.
    * @throws IOException
    */
   ExpressionRelBI[] getAllRels() throws IOException;

   /**
    *
    * @return the value represented by this node.
    */
   T getValue();
   
   /**
    * Add xml to the provided string builder. 
    * @param sb The string builder to add the xml to. 
    * @param verbose true if optional attributes are to be included. 
    * @throws IOException 
    */
   void generateXml(StringBuilder sb, boolean verbose) throws IOException;
   
   
   void generateHtml(StringBuilder sb, boolean verbose) throws IOException;
   void generateHtml(StringBuilder sb, boolean verbose, int depth) throws IOException;

   void appendStringForUuidHash(StringBuilder sb) throws IOException;
}
