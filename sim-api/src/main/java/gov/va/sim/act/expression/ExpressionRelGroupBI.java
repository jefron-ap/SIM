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

/**
 * A collection of grouped relationships 
 * 
 * @author Keith E. Campbell
 */
public interface ExpressionRelGroupBI {
    
    /**
     * 
     * @return the relationships in this group. 
     */
    ExpressionRelBI[] getRelsInGroup();
    
    /**
     * 
     * @param relsInGroup set the relationships in this group
     */
    void setRelsInGroup(ExpressionRelBI[] relsInGroup);
}
