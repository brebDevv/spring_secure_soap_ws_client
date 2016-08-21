package com;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

public class CallHTTPSSoapWsTest {
	public static final String PASSWORD = "password";

	public static void main(String[] args)
			throws UnrecoverableKeyException, KeyManagementException, ClientProtocolException, NoSuchAlgorithmException,
			CertificateException, FileNotFoundException, KeyStoreException, UnsupportedEncodingException, IOException {
		HttpResponse response = buildHttpClient().execute(buildHttpPost());
		System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		System.out.println(result);
	}

	public static HttpPost buildHttpPost() throws UnsupportedEncodingException {
		// Create a StringEntity for the SOAP XML.
		String body = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gs=\"http://spring.io/guides/gs-producing-web-service\"><soapenv:Header/><soapenv:Body><gs:getCountryRequest><gs:name>Spain</gs:name></gs:getCountryRequest></soapenv:Body></soapenv:Envelope>";

		String url = "https://localhost:8443/spring_soap_ws-0.0.1-SNAPSHOT/ws";
		HttpPost post = new HttpPost(url);
		// add header
		post.setHeader("Content-Type", "text/xml");
		// Compose XML
		post.setEntity(new StringEntity(body));

		return post;
	}

	public static HttpClient buildHttpClient() throws NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
		SSLContext sslContext = SSLContexts.createDefault();
		sslContext.init(buildKeyManager(), buildTrustManager(), new java.security.SecureRandom());

		HttpClient httpClient = HttpClients.custom().setSSLContext(sslContext).build();

		return httpClient;
	}

	public static KeyManager[] buildKeyManager() throws NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException, KeyStoreException, UnrecoverableKeyException {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream("src/main/resources/certificates/client/clientKeystore.jks"),
				PASSWORD.toCharArray());

		KeyManagerFactory kf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kf.init(keyStore, PASSWORD != null ? PASSWORD.toCharArray() : null);

		return kf.getKeyManagers();
	}

	public static TrustManager[] buildTrustManager() throws NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, IOException, KeyStoreException, UnrecoverableKeyException {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(new FileInputStream("src/main/resources/certificates/client/clientTruststore.ts"),
				PASSWORD.toCharArray());

		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(keyStore);

		return tmf.getTrustManagers();
	}
}
