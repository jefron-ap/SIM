
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.sim.impl.expression;

//~--- non-JDK imports --------------------------------------------------------

import gov.va.sim.act.expression.ExpressionRelBI;
import gov.va.sim.act.expression.ExpressionRelGroupBI;

/**
 *
 * @author kec
 */
public class ExpressionRelGroup implements ExpressionRelGroupBI {
   ExpressionRelBI[] relsInGroup;

   //~--- get methods ---------------------------------------------------------

   @Override
   public ExpressionRelBI[] getRelsInGroup() {
      return relsInGroup;
   }

   //~--- set methods ---------------------------------------------------------

   public void setRelsInGroup(ExpressionRelBI[] relsInGroup) {
      this.relsInGroup = relsInGroup;
   }
}
