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
import java.util.Collection;

/**
 * LegoListBI
 * @author Dan Armbrust 
 */
public interface LegoListBI extends IdentifiableInstanceBI
{
	public String getGroupName();
	
	public String getGroupDescription();
	
	public Collection<LegoBI> getLego();
	
	public String getComment();
}
