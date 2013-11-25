
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.sim.impl.expression;

//~--- non-JDK imports --------------------------------------------------------

import gov.va.sim.act.expression.ExpressionRelBI;
import gov.va.sim.act.expression.ExpressionRelGroupBI;
import gov.va.sim.act.expression.node.ExpressionNodeBI;
import java.io.IOException;

import org.ihtsdo.tk.api.concept.ConceptVersionBI;

/**
 *
 * @author kec
 */
public class ExpressionRel implements ExpressionRelBI {
   private ExpressionNodeBI     destination;
   private ExpressionNodeBI     origin;
   private ExpressionRelGroupBI relGroup;
   private ConceptVersionBI     type;

    @Override
    public String getFullySpecifiedText() throws IOException {
        return type.getFsnDescsActive().iterator().next().getText();
    }

    @Override
    public String getPreferredText() throws IOException {
        return type.getPrefDescsActive().iterator().next().getText();
    }

   //~--- get methods ---------------------------------------------------------

   @Override
   public ExpressionNodeBI getDestination() {
      return destination;
   }

   @Override
   public ExpressionNodeBI getOrigin() {
      return origin;
   }

   @Override
   public ExpressionRelGroupBI getRelGroup() {
      return relGroup;
   }

   @Override
   public ConceptVersionBI getType() {
      return type;
   }

   //~--- set methods ---------------------------------------------------------

   public void setDestination(ExpressionNodeBI destination) {
      this.destination = destination;
   }

   public void setOrigin(ExpressionNodeBI origin) {
      this.origin = origin;
   }

   public void setRelGroup(ExpressionRelGroupBI relGroup) {
      this.relGroup = relGroup;
   }

   public void setType(ConceptVersionBI type) {
      this.type = type;
   }
}
