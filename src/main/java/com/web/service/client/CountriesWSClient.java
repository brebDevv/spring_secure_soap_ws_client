package com.web.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import countries.wsdl.GetCountryRequest;
import countries.wsdl.GetCountryResponse;
import countries.wsdl.ObjectFactory;

@Component
public class CountriesWSClient {

	@Autowired
	private WebServiceTemplate webServiceTemplate;

	public void callWS() {
		ObjectFactory objectFactory = new ObjectFactory();
		GetCountryRequest getCountryReq = objectFactory.createGetCountryRequest();
		getCountryReq.setName("Spain");

		GetCountryResponse getCountryResp = (GetCountryResponse) webServiceTemplate
				.marshalSendAndReceive(getCountryReq);
		System.out.println("***********************");
		System.out.println(getCountryResp.getCountry().getCapital());
		System.out.println(getCountryResp.getCountry().getName());
		System.out.println(getCountryResp.getCountry().getPopulation());
		System.out.println(getCountryResp.getCountry().getCurrency());
		System.out.println("***********************");
	}
}
