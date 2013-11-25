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

package gov.va.sim.measurement;

/**
 * Interface for an interval that has an upper and lower bound. 
 * @author kec
 */
public interface IntervalBI extends MeasurementBI<IntervalBI> {
    
    /**
     * 
     * @return the upper bound of the interval. 
     */
    BoundBI getUpperBound();
    
    /**
     * 
     * @return the lower bound of the interval.
     */
    BoundBI getLowerBound();
    
    /**
     * 
     * @param upperBound the upper bound of the interval
     */
    
    void setUpperBound(BoundBI upperBound);
    
    /**
     * 
     * @param lowerBound the lower bound of the interval
     */
    void setLowerBound(BoundBI lowerBound);

}
