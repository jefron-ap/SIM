package gov.va.med.cds.rest.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;
import org.junit.Ignore;


public class SmartFormsRestClientTest {

	private static final String WS_ADDRESS = "http://localhost:8089/cds-wsclient/cds-service/";
	private static final String SMART_FORMS_CREATE_TEMPLATE_ID = "SmartFormCreate1";
	private static final String SMART_FORMS_READ_TEMPLATE_ID = "SmartFormRead1";
	private static final String SMART_FORMS_READ_FILTER_ID = "SMART_FORM_SINGLE_PATIENT_ALL_DATA_FILTER";
	private static final String SMART_FORMS_DOC_ID = UUID.randomUUID().toString();
	private static final String SMART_FORMS_PID_IDENTITY = UUID.randomUUID().toString();
	private static final String SMART_FORMS_PID_ASSIGNING_FACILITY = "555";
	
	private static final String SMART_FORMS_CREATE_REQUEST1 = "<?xml version=\"1.0\" "
                + "encoding=\"UTF-8\"?>\n<clinicaldata:ClinicalData "
                + "xsi:schemaLocation=\"Clinicaldata SmartFormCreate1.xsd\" "
                + "xmlns:clinicaldata=\"Clinicaldata\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "<templateId>%s</templateId>"
                + "<clientName>PNCS</clientName>"
                + "<clientRequestInitiationTime>2012-07-25T13:26:32-06:00</clientRequestInitiationTime>"
                + "<patient><smartFormDocuments><smartFormDocument>"
                + "<documentNativeId>1</documentNativeId>"
                + "<documentUUID>%s</documentUUID>"
                + "<documentStartDate><literal>20120704162517-0600</literal></documentStartDate>"
                + "<documentEndDate><literal>20120705162517-0600</literal></documentEndDate>"
                + "<patient><identifier>"
                +   "<identity>%s</identity>"
                + "<assigningFacility>%s</assigningFacility><assigningAuthority>USVHA</assigningAuthority></identifier><name><prefix>PATIENT_PREFIX</prefix><given>PATIENT_GIVEN</given><middle>PATIENT_MIDDLE</middle><family>PATIENT_FAMILY</family><suffix>PATIENT_SUFFIX</suffix><title>PATIENT_TITLE</title></name></patient><provider><identifier><identity>ed2e69bf-b74b-4044-bf05-4df7f3d9faa3</identity><assigningFacility>200M</assigningFacility><assigningAuthority>USVHA</assigningAuthority></identifier><name><prefix>PROVIDER_PREFIX</prefix><given>PROVIDER_GIVEN</given><middle>PROVIDER_MIDDLE</middle><family>PROVIDER_FAMILY</family><suffix>PROVIDER_SUFFIX</suffix><title>PROVIDER_TITLE</title></name></provider><facility><identity>2292e5a7-08bf-4ed1-8708-fe4fe3c3d97b</identity><name>DUMMY_FACILITY</name><assigningAuthority>USVHA</assigningAuthority></facility><assertions><assertion><assertionUUID>54c95f25-8c5d-496e-ac0c-a0553aaf4922</assertionUUID><seqInDoc>0</seqInDoc><assertionStartDate><literal>20120704162517-0600</literal></assertionStartDate><assertionEndDate><literal>20120706162517-0600</literal></assertionEndDate><sectionENID>2</sectionENID><discernibleENID>3</discernibleENID><qualifierENID>4</qualifierENID><valueENID>5</valueENID></assertion></assertions></smartFormDocument></smartFormDocuments></patient></clinicaldata:ClinicalData>";

        
        
        private static final String SMART_FORMS_READ_REQUEST1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<filter:filter vhimVersion=\"Vhim_4_00\" xsi:schemaLocation=\"Filter Smart_Form_Single_Patient_All_Data_Filter.xsd\" xmlns:filter=\"Filter\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><filterId>SMART_FORM_SINGLE_PATIENT_ALL_DATA_FILTER</filterId><clientName>PNCS</clientName><clientRequestInitiationTime>2001-12-17T09:30:47Z</clientRequestInitiationTime><patients><resolvedIdentifiers><assigningAuthority>USVHA</assigningAuthority><assigningFacility>%s</assigningFacility><identity>%s</identity></resolvedIdentifiers><resolvedIdentifiers><assigningAuthority>USVHA</assigningAuthority><assigningFacility>777</assigningFacility><identity>7676</identity></resolvedIdentifiers></patients><entryPointFilter queryName=\"ID_1\"><domainEntryPoint>SmartFormDocument</domainEntryPoint><startDate>2012-07-01</startDate><endDate>2012-08-31</endDate><queryTimeoutSeconds>60</queryTimeoutSeconds><otherQueryParameters><documentNativeId>1</documentNativeId></otherQueryParameters></entryPointFilter></filter:filter>";
	
	
	//@Test
        @Ignore
	public void testSmartFormsCdsSoapClientCreateClinicalData() throws Exception
	{	
		try {
			/* Populate the template XML string with the create template identifier, 
                         * the smart forms document id, a unique patient identifier (for testing), 
                         * and a valid assigning facility. */
			String createRequest = String.format(SMART_FORMS_CREATE_REQUEST1, 
                                SMART_FORMS_CREATE_TEMPLATE_ID, 
                                SMART_FORMS_DOC_ID, 
                                SMART_FORMS_PID_IDENTITY, 
                                SMART_FORMS_PID_ASSIGNING_FACILITY);
			
			// Create the CDS service proxy object that abstracts the remoting mechanism. 
			ClinicalDataServiceRestfulInterfacePortType proxy = 
                                new ClinicalDataServiceRestfulInterfacePortTypeProxy(WS_ADDRESS);
			
			// This makes the remote call to CDS. The parameters are as follows.
			// 1) The create template XML document.
			// 2) The craete template identifier. For CDS 3.3 the only supported Smart Forms create template is
			//    'SmartFormCreate1'.
			// 3) The request identifier. A unique identifier that can be used to track the request.
			String response = proxy.createClinicalData(createRequest, 
                                SMART_FORMS_CREATE_TEMPLATE_ID, UUID.randomUUID().toString());
			
			// Assert that the response data includes no errors.
			assertFalse(response.indexOf("<fatalError>") != -1);
			System.out.println("CDS Create Response: " + response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	//@Test
        @Ignore
	public void testSmartFormsCdsSoapClientReadClinicalData1() {
		try {
			String readRequest = String.format(SMART_FORMS_READ_REQUEST1, SMART_FORMS_PID_ASSIGNING_FACILITY, SMART_FORMS_PID_IDENTITY);
			
			// Create the CDS service proxy object that abstracts the remoting mechanism. 
			ClinicalDataServiceRestfulInterfacePortType proxy = new ClinicalDataServiceRestfulInterfacePortTypeProxy(WS_ADDRESS);
			
			// This makes the remote call to CDS. The parameters are as follows.
			//   1) The Smart Forms Read Template Identifier. The only read template supported for CDS 3.3 is 
			//      'SmartFormRead1'.
			//   2) The Smart Forms Filter Request. This is an XML Document that defines how you  want the data 
			//      filtered. (See filter schema for details).
			//   3) The Smart Forms Filter Identifier. The only filter supported for the CDS 3.3 release is 
			//      'SMART_FORM_SINGLE_PATIENT_ALL_DATA_FILTER'.
			//   4) The Request Identifier. This is a unique identifier that the request can be tracked with.
			String response = proxy.readClinicalData1(SMART_FORMS_READ_TEMPLATE_ID, readRequest, SMART_FORMS_READ_FILTER_ID, UUID.randomUUID().toString());
			
			// Some basic assertions that the test succeeded.
			assertTrue(response.indexOf("<smartFormDocument>") > -1);
			System.out.println("CDS Read Response: " + response);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		} 
	}

}
