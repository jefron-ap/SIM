/**
 * 
 */
package gov.va.med.cds.rest.client;

import java.rmi.RemoteException;

import javax.ws.rs.core.MultivaluedMap;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * @author vhaislegberb
 *
 */
public class ClinicalDataServiceRestfulInterfacePortTypeProxy implements ClinicalDataServiceRestfulInterfacePortType {
	
	private String resourceAddress = null;
	
	
	public ClinicalDataServiceRestfulInterfacePortTypeProxy(String resourceAddress) {
		
		this.resourceAddress = resourceAddress;
		
	}

	public String createClinicalData(String createTemplateXML, String templateId, String requestId)
			throws RemoteException {
		
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("createRequest", createTemplateXML);
		queryParams.add("requestId", requestId);
		queryParams.add("templateId", templateId);
		
		Client createClient = new Client();
		WebResource resource = createClient.resource(resourceAddress + "createClinicalData");
		return resource.queryParams(queryParams).post(String.class);
	}

	public String readClinicalData1(String templateId, String filterRequest, String filterId, String requestId ) throws RemoteException {
		
		MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
		queryParams.add("filterId", filterId);
		queryParams.add("templateId", templateId);
		queryParams.add("requestId", requestId);
		queryParams.add("filterRequest", filterRequest);
		
		Client readClient = new Client();
		WebResource resource = readClient.resource(resourceAddress + "readClinicalData1");
		return resource.queryParams(queryParams).get(String.class);
	}

}
