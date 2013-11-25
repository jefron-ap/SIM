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
package gov.va.sim.lego;

import gov.va.sim.act.AssertionBI;
import gov.va.sim.id.IdentifiableInstanceBI;
import java.util.Collection;

/**
 * LegoBI
 * @author Dan Armbrust 
 */
public interface LegoBI extends IdentifiableInstanceBI
{
	public LegoStampBI getStamp();
	
	public PncsBI getPncs();
	
	public Collection<AssertionBI> getAssertions();
	
	public String getComment();
}
