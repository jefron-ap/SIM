/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.va.sim.act.expression;

import java.beans.PropertyVetoException;
import gov.va.sim.act.expression.node.ExpressionNodeBI;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
/**
 * Idea: store all unique value expressions independent 
 * from the event document, and give each expression a unique
 * identifier. Then when searching, first search the value expression store
 * for matching identifiers, then search the instance data...
 *
 * @author Keith E. Campbell
 */
public interface ExpressionBI {

    /**
     * @return the focus of this value expression.
     */
    ExpressionNodeBI<?> getFocus();

    /**
     * @param focus the focus of this value expression.
     * @throws PropertyVetoException 
     */
    void setFocus(ExpressionNodeBI<?> focus) throws PropertyVetoException;
    
    /**
     * Returns a UUID computed from the expression such that two 
     * equal expressions will return identical UUIDs. 
     * @return the computed UUID. 
     * @throws IOException
     * @throws UnsupportedEncodingException 
     * @throws NoSuchAlgorithmException  
     */
    UUID getUuid() throws IOException, UnsupportedEncodingException, NoSuchAlgorithmException;

   /**
     * equivalent: another is equivalent to this
     * @param another
     * @return true or false
     */
    boolean equivalent(ExpressionBI another);

    /**
     * subsumes: this contains or includes another
     * @param another
     * @return true or false
     */
    boolean subsumes(ExpressionBI another);

    /**
     * subsumedBy: another contains or includes this
     * @param another
     * @return true or false
     */
    boolean subsumedBy(ExpressionBI another);

}
