package com.laurinka.skga.app.rest;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.laurinka.skga.app.DomXmlParser;
/**
 * Generic handler who parses list of name-number xmls.
 * @author radimpavlicek
 *
 */
public final class OnSearchResponse implements OnRestResponse {
	private final OnSKGASearchResponse onRestResponseponse;

	public OnSearchResponse(OnSKGASearchResponse callBack) {
		this.onRestResponseponse = callBack;
	}

	@Override
	public void onResponse(String response) {
		Document document = DomXmlParser.XMLfromString(response);
		if (null == document)
			return;
		
		List<NameNumber> l = new LinkedList<NameNumber>();
		NodeList handicap = document.getElementsByTagName("nameNumberXml");
		for (int i = 0; i < handicap.getLength(); i++) {
			try {
			Node item = handicap.item(i);
			String name = item.getChildNodes().item(0).getFirstChild().getNodeValue();
			String nr = item.getChildNodes().item(1).getFirstChild().getNodeValue();
			l.add(new NameNumber(name, nr));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		onRestResponseponse.onResponse(l);
	}

	@Override
	public void onError(Integer errorCode, String errorMessage) {
		onRestResponseponse.onError(errorCode, errorMessage);
	}
}