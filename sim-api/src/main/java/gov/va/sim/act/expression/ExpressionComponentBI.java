/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.sim.act.expression;

import java.io.IOException;

/**
 *
 * @author kec
 */
public interface ExpressionComponentBI {
    
    String getPreferredText() throws IOException;
    String getFullySpecifiedText() throws IOException;
}
