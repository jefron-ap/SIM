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

import gov.va.sim.id.IdentifiableInstanceBI;

/**
 * PncsBI
 * @author Dan Armbrust 
 */
public interface LegoStampBI extends IdentifiableInstanceBI
{
	public String getStatus();
	
	public long getTime();
	
	public String getAuthor();
	
	public String getModule();
	
	public String getPath();
}
