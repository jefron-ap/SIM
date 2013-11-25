
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package gov.va.sim.rest.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import java.io.IOException;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author kec
 */
public class SimRestClient {
   public static final String   defaultLocalHostServer = "http://localhost:8080/sim/rest/";
   private static String        serverUrlStr           = defaultLocalHostServer;
   private static Client        restClient;
   private static SimRestClient restClientSingleton;

   //~--- methods -------------------------------------------------------------

   public void putSmartDocumentHdr(String str) throws IOException {
      WebResource r = restClient.resource(serverUrlStr + "smartform/hdr");

      r.type(MediaType.TEXT_PLAIN_TYPE).put(str);
   }

   public void putSmartDocumentSim(String str) throws IOException {
      WebResource r = restClient.resource(serverUrlStr + "smartform/sim");

      r.type(MediaType.TEXT_PLAIN_TYPE).put(str);
   }

   public void putSmartDocumentTest(String str) throws IOException {
      WebResource r = restClient.resource(serverUrlStr + "smartform/test");

      r.type(MediaType.TEXT_PLAIN_TYPE).put(str);
   }

   public void putLego(String str, String fileName) throws IOException {
      WebResource r = restClient.resource(serverUrlStr + "smartform/lego/" + fileName);
      

      r.type(MediaType.TEXT_PLAIN_TYPE).put(str);
   }

   public static void setup(String serverUrlStr) throws IOException {
      SimRestClient.serverUrlStr = serverUrlStr;

      ClientConfig cc = new DefaultClientConfig();

      restClient          = Client.create(cc);
      restClientSingleton = new SimRestClient();
   }

   //~--- get methods ---------------------------------------------------------

   public static SimRestClient get() {
      if (restClientSingleton == null) {
         throw new UnsupportedOperationException(
             "You must call SimRestClient.setup(String serverUrlStr) prior to get");
      }

      return restClientSingleton;
   }
}
