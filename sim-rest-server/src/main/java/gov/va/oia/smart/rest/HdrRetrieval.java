
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.oia.smart.rest;

import gov.va.demo.nb.sim.jpa.Documents;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author kec
 */
public class HdrRetrieval {
   private static final String SMART_FORMS_READ_REQUEST1 =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<filter:filter vhimVersion=\"Vhim_4_00\" xsi:schemaLocation=\"Filter Smart_Form_Single_Patient_All_Data_Filter.xsd\" xmlns:filter=\"Filter\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><filterId>SMART_FORM_SINGLE_PATIENT_ALL_DATA_FILTER</filterId><clientName>PNCS</clientName><clientRequestInitiationTime>2001-12-17T09:30:47Z</clientRequestInitiationTime><patients><resolvedIdentifiers><assigningAuthority>USVHA</assigningAuthority><assigningFacility>PATIENT_ASSIGNING_FACILITY</assigningFacility><identity>PATIENT_IDENTITY</identity></resolvedIdentifiers></patients><entryPointFilter queryName=\"ID_1\"><domainEntryPoint>SmartFormDocument</domainEntryPoint><startDate>2010-07-01</startDate><endDate>2013-08-31</endDate><queryTimeoutSeconds>60</queryTimeoutSeconds></entryPointFilter></filter:filter>";

   //~--- fields --------------------------------------------------------------

   String hdrRetrievalString;

   //~--- constructors --------------------------------------------------------

   public HdrRetrieval(Documents doc) {
      StringBuilder     sb  = new StringBuilder();
      DateTimeFormatter dtf = DateTimeFormat.forPattern(HdrDocument.DATE_FORMAT_PATTERN);

      hdrRetrievalString = SMART_FORMS_READ_REQUEST1.
              replaceFirst("PATIENT_IDENTITY",doc.getPatientnid().getPInternalEntryNumber()).
              replaceFirst("PATIENT_ASSIGNING_FACILITY", "552");
      
      
   }

   //~--- methods -------------------------------------------------------------

   private static void parse(InputStream stream)
           throws SAXException, ParserConfigurationException, IOException {
      SAXParserFactory.newInstance().newSAXParser().parse(stream, new DefaultHandler());
   }

   @Override
   public String toString() {
      return hdrRetrievalString;
   }

   public boolean validate() {
      boolean valid = true;

      try {
         parse(new ByteArrayInputStream(hdrRetrievalString.getBytes()));
      } catch (SAXException | ParserConfigurationException | IOException ex) {
         Logger.getLogger(HdrRetrieval.class.getName()).log(Level.SEVERE, null, ex);
      }

      String[] encodings = { "UTF-8", "UTF-16", "ISO-8859-1" };

      for (String actual : encodings) {
         for (String declared : encodings) {
            if (!actual.equals(declared)) {
               valid = false;

               String xml     = "<?xml version='1.0' encoding='" + declared + "'?><x/>";
               byte[] encoded = xml.getBytes(Charset.forName(actual));

               try {
                  parse(new ByteArrayInputStream(encoded));
                  System.out.println("HIDDEN ERROR! actual:" + actual + " " + xml);
               } catch (SAXException | ParserConfigurationException | IOException e) {
                  System.out.println(e.getMessage() + " actual:" + actual + " xml:" + xml);
               }
            }
         }
      }

      return valid;
   }
}
