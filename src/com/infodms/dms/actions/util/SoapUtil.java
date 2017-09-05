package com.infodms.dms.actions.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public class SoapUtil {


	private static InputStream getSoapResponseIs(String address, String soapBody)
			throws IOException {
		URL url = new URL(address);
		HttpURLConnection httpConnection = (HttpURLConnection) url
				.openConnection();
		httpConnection.setDoInput(true);
		httpConnection.setDoOutput(true);
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type", "text/xml");
		httpConnection.setConnectTimeout(20000);
		// httpConnection.setRequestProperty("SOAPAction",
		// "http://tempuri.org/");
		OutputStream os = httpConnection.getOutputStream();
		PrintWriter out = new PrintWriter(os);
		out.println(soapBody);
		out.flush();

		InputStream is = null;
		if (HttpURLConnection.HTTP_OK == httpConnection.getResponseCode()) {
			is = httpConnection.getInputStream();
		}
		return is;
	}

	public static String getReturnedXmlContent(String address, String soapBody) {
		InputStream is = null;
		String xml = null;
		String result = null;
		String packname = "return";
		try {
			is = getSoapResponseIs(address, soapBody);

			if (is != null) {
				SAXReader reader = new SAXReader();
				Document document;

				document = reader.read(is);
				//System.out.println(document.asXML().toString());
				xml = document.asXML().toString();
				if (xml != null && xml.length() > 0) {
					xml = xml.replaceAll("&lt;", "<");
					xml = xml.replaceAll("&gt;", ">");
					int beginIndex = xml.indexOf("<" + packname, 0);
					if (beginIndex > 0) {
						//System.out.println("---------------------");
						String tempXml = xml.substring(beginIndex);
						int tmpBeginIndex = tempXml.indexOf(">");
						tempXml = tempXml.substring(tmpBeginIndex + 1);
						String content = tempXml.substring(0,
								tempXml.indexOf("</" + packname + ">", 0));
						result = content;
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}
}
