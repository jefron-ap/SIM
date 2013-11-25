
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.sim.impl.expression;

//~--- non-JDK imports --------------------------------------------------------

import gov.va.sim.act.expression.ExpressionBI;
import gov.va.sim.act.expression.node.ExpressionNodeBI;

//~--- JDK imports ------------------------------------------------------------

import java.beans.PropertyVetoException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import org.ihtsdo.tk.uuid.UuidT5Generator;


/**
 *
 * @author kec
 */
public class Expression implements ExpressionBI {
   private ExpressionNodeBI<?> focus;

   //~--- methods -------------------------------------------------------------

   @Override
   public boolean equivalent(ExpressionBI another) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public boolean subsumedBy(ExpressionBI another) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public boolean subsumes(ExpressionBI another) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public String toString() {
        try {
            return getVerboseXml();
        } catch (IOException ex) {
           throw new RuntimeException(ex);
        }
   }

   //~--- get methods ---------------------------------------------------------

   @Override
   public ExpressionNodeBI<?> getFocus() {
      return focus;
   }

    @Override
    public UUID getUuid() throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException {
      StringBuilder sb = new StringBuilder();
        
      if (focus != null) {
         focus.appendStringForUuidHash(sb);
      } else {
         return null;
      }

      return UuidT5Generator.get(ExpressionNodeBI.NAMESPACE_UUID, sb.toString());
    }

   public String getVerboseXml() throws IOException {
      StringBuilder sb = new StringBuilder();

      if (focus != null) {
         focus.generateXml(sb, true);
      } else {
         sb.append("null focus");
      }

      return sb.toString();
   }

   public String getXml() throws IOException {
      StringBuilder sb = new StringBuilder();

      if (focus != null) {
         focus.generateXml(sb, false);
      } else {
         sb.append("null focus");
      }

      return sb.toString();
   }

   public String getHtmlFragment(boolean verbose) throws IOException {
      StringBuilder sb = new StringBuilder();

      if (focus != null) {
         focus.generateHtml(sb, false);
      } else {
         sb.append("null focus");
      }

      return sb.toString();
   }

   //~--- set methods ---------------------------------------------------------

   @Override
   public void setFocus(ExpressionNodeBI<?> focus) throws PropertyVetoException {
      this.focus = focus;
   }
}
