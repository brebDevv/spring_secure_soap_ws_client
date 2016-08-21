package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

public class CallHTTPSoapWsTest {
	
	@Test
	public void test(){
		try {
			callWebService("<gs:getCountryRequest><gs:name>Spain</gs:name></gs:getCountryRequest>");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void callWebService(String soapEnvBody) throws ClientProtocolException, IOException {
		// Create a StringEntity for the SOAP XML.
		String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gs=\"http://spring.io/guides/gs-producing-web-service\"><soapenv:Header/><soapenv:Body>"
				+ soapEnvBody + "</soapenv:Body></soapenv:Envelope>";
		
        String url = "http://localhost:8080/spring_soap_ws-0.0.1-SNAPSHOT/ws";
        
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);   
        // add header
        post.setHeader("Content-Type", "text/xml");
        //Compose XML
        post.setEntity(new StringEntity(body));
        
        HttpResponse response = client.execute(post);
        System.out.println("Response Code : "
                    + response.getStatusLine().getStatusCode());     
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));   
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        System.out.println(result);
	}

}
