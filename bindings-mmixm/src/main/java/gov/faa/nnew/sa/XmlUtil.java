package gov.faa.nnew.sa;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * <pre>
 *  ____     ____    ______     
 * /\  _`\  /\  _`\ /\__  _\    
 * \ \ \L\ \\ \ \/\_\/_/\ \/    
 *  \ \  _ /_\ \ \/_/_ \ \ \    
 *   \ \ \L\ \\ \ \L\ \ \_\ \__ 
 *    \ \____/ \ \____/ /\_____\
 *     \/___/   \/___/  \/_____/
 * 
 * Prepared for the Federal Aviation Administration.
 * Copyright (c) 2009-2020 BCI Incorporated.
 * </pre>
 * 
 * @version 2009.09.30 
 * @author Peter V Donato
 */
public abstract class XmlUtil {
	private static final Logger LOGGER = Logger.getLogger(XmlUtil.class.getName());
	
	// Attempt to define all used namespaces in one place
	public static final Map<String, String> NAMESPACE_MAP = new HashMap<String, String>();
	static {
		NAMESPACE_MAP.put("xlink","http://www.w3.org/1999/xlink");
		NAMESPACE_MAP.put("xsi",  "http://www.w3.org/2001/XMLSchema-instance");

//		NAMESPACE_MAP.put("wfs",  "http://www.opengis.net/wfs/2.0");
//		NAMESPACE_MAP.put("gml",  "http://www.opengis.net/gml/3.2");
//		NAMESPACE_MAP.put("om",   "http://www.opengis.net/om/2.0");
//		NAMESPACE_MAP.put("oom",  "http://www.opengis.net/om/1.0/gml32"); // Put the old om tag back in, but call it oom (Old-OM) 12/09/2015 pvd
//		NAMESPACE_MAP.put("swe",  "http://www.opengis.net/swe/1.0/gml32");
//		NAMESPACE_MAP.put("fes",  "http://www.opengis.net/fes/2.0");
//		NAMESPACE_MAP.put("avwx", "http://www.eurocontrol.int/avwx/1.1");
//		NAMESPACE_MAP.put("wx",   "http://www.eurocontrol.int/wx/1.1");
//		NAMESPACE_MAP.put("an3",  "http://www.icao.int/annex3/1.0");
//		NAMESPACE_MAP.put("nawx", "http://www.faa.gov/nawx/1.4");   // Changed from 1.2 to 1.4 11/05/2015 pvd
//		NAMESPACE_MAP.put("nawx3", "http://www.faa.gov/nawx/1.3");
		
//		NAMESPACE_MAP.put("wsnt", "http://docs.oasis-open.org/wsn/b-2");
//		NAMESPACE_MAP.put("owsnt", "http://www.opengis.net/owsnt/1.1");
//		NAMESPACE_MAP.put("kml", "http://www.opengis.net/kml/2.2");
		
//		NAMESPACE_MAP.put("wcs", "http://www.opengis.net/wcs/2.0"); // Updated 1.1 -> 2.0 09/15/2023 pvd
//		NAMESPACE_MAP.put("ows", "http://www.opengis.net/ows/2.0"); // Updated 1.1 -> 2.0 09/15/2023 pvd
		
//		NAMESPACE_MAP.put("wxxm", "http://www.wxxm.aero/wxxm/2.0");
//		NAMESPACE_MAP.put("iwxxm", "http://icao.int/iwxxm/1.1");
//		NAMESPACE_MAP.put("iwxxm", "http://icao.int/iwxxm/2.1");                               // Updated 04/11/2019 pvd
//		NAMESPACE_MAP.put("iwxxm", "http://icao.int/iwxxm/3.0");                                 // Updated 03/10/2020 pvd
//		NAMESPACE_MAP.put("iwxxm-us", "http://nws.weather.gov/schemas/IWXXM-US/1.0/Release");
//		NAMESPACE_MAP.put("iwxxm-us", "http://nws.weather.gov/iwxxm-us/2.0");                  // Updated 04/11/2019 pvd
//		NAMESPACE_MAP.put("iwxxm-us", "http://www.weather.gov/iwxxm-us/3.0");                    // Updated 03/10/2020 pvd
//		NAMESPACE_MAP.put("faawx", "http://www.faa.gov/wx/2.1-prerelease"); // Updated 12/04/2015 pvd
//		NAMESPACE_MAP.put("faawx", "http://www.faa.gov/wx/2.2");                                 // Updated 04/11/2019 pvd
//		NAMESPACE_MAP.put("uswx", "http://nws.weather.gov/uswx/1.0");
//		NAMESPACE_MAP.put("uswx", "http://nws.weather.gov/schemas/USWX/1.0"); // Updated 06/07/2017
//		NAMESPACE_MAP.put("sams", "http://www.opengis.net/samplingSpatial/2.0");
//		NAMESPACE_MAP.put("sam", "http://www.opengis.net/sampling/2.0");
//		NAMESPACE_MAP.put("sf", "http://www.opengis.net/sampling/2.0");  // Replaced the sam namespace tag with sf 11/18/2015 pvd
//		NAMESPACE_MAP.put("saf", "http://icao.int/saf/1.1");
//		NAMESPACE_MAP.put("metce", "http://def.wmo.int/metce/2013");
//		NAMESPACE_MAP.put("aixm", "http://www.aixm.aero/schema/5.1.1"); // Added 06/20/2018 pvd - changed from 5.1 to 5.1.1 04/11/2019
//		NAMESPACE_MAP.put("gmlcov", "http://www.opengis.net/gmlcov/1.0"); // Added 09/18/2023 pvd
		
		
		NAMESPACE_MAP.put("xs", "http://www.w3.org/2001/XMLSchema");
		NAMESPACE_MAP.put("mxm", "https://mmixm.aero/4");
		NAMESPACE_MAP.put("mb", "https://mmixm.aero/base/4");
		NAMESPACE_MAP.put("mx", "https://mmixm.aero/features/4");
	}
	
	public static Map<String, String> PREFIX_MAP = new HashMap<String, String>();
	static {
		for(Map.Entry<String, String> entry : NAMESPACE_MAP.entrySet()) {
			PREFIX_MAP.put(entry.getValue(), entry.getKey());
		}
	}
	
	public static final XmlOptions XML_OPTS = buildGmlXmlOptions(true);
	public static final XmlOptions XML_OPTS_UG = buildGmlXmlOptions(false);
	
	public static XmlOptions buildGmlXmlOptions(boolean pretty) {

		// Set up an XmlOptions object to allow XMLBeans debugging output to
		// be nicely formatted. This includes controlling the namespace
		// prefixes (which otherwise appear as 'ns1:', 'ns2:', etc...)
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setCharacterEncoding("UTF-8");
		if(pretty) {
			xmlOptions.setSavePrettyPrint();
			xmlOptions.setSavePrettyPrintIndent(2);
			xmlOptions.setSavePrettyPrintOffset(0);
		}

		/**
		 * How do I add the following schema location for xsi ?
		 * 
		 * xsi:schemaLocation="http://www.opengis.net/wfs 
		 * 	http://schemas.opengis.net/wfs/1.1.0/WFS-transaction.xsd 
		 * 	http://www.openplans.org/myP 
		 * 	http://localhost:8081/geoserver/wfs/DescribeFeatureType?typename=myP:lightning"
		 */

		xmlOptions.setSaveNamespacesFirst();      // This option will cause the saver to save namespace attributes first.
		xmlOptions.setSaveAggressiveNamespaces(); // Causes the saver to reduce the number of namespace prefix declarations.
		xmlOptions.setUseDefaultNamespace();      // If this option is set, the saver will try to use the default namespace for the most commonly used URI.
		xmlOptions.setSaveInner();

		/*
		 * If namespaces have already been declared outside the scope of the
		 * fragment being saved, this allows those mappings to be passed
		 * down to the saver, so the prefixes are not re-declared.
		 */
		xmlOptions.setSaveImplicitNamespaces(PREFIX_MAP);

		/*
		 * A map of hints to pass to the saver for which prefixes to use for which 
		 * namespace URI.
		 */
		xmlOptions.setSaveSuggestedPrefixes(PREFIX_MAP); 
		
		//xmlOptions.setLoadAdditionalNamespaces(suggestedPrefixMap);
		xmlOptions.setSaveOuter(); // This option controls whether saving begins on the element or its contents

		return xmlOptions;
	}
	
	
	/**
	 * This is a convenience method for parsing a string representation of an XML document into
	 * a DOM Document object.
	 *
	 * @param in_is - String representation of the XML file
	 * @return the parsed Document object or null if it was unable to be parsed
	 */
	public static Document parseDocument(InputStream in_is) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			DocumentBuilder builder = dbf.newDocumentBuilder();
			doc = builder.parse(in_is);
		}
		catch(IOException e) {
			LOGGER.log(Level.WARNING, "Unable to parse the xml from the provided input stream into a document.", e);
		}
		catch(ParserConfigurationException e) {
			LOGGER.log(Level.WARNING, "Unable to parse the xml from the provided input stream into a document.", e);
		}
		catch(SAXException e) {
			LOGGER.log(Level.WARNING, "Unable to parse the xml from the provided input stream into a document.", e);
		}
		return doc;
	}


	/**
	 * This is a convenience method used to write the provided Document to standard out.
	 *
	 * @param in_doc the Document to be written to stdout
	 */
	public static void printDocumentToStdOut(Document in_doc) {
		Source source = new DOMSource(in_doc);
		Result result = new StreamResult(System.out);
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
		}
		catch(TransformerException e) {
			LOGGER.log(Level.WARNING, "Unable to transform the provided document.", e);
		}
	}

	/**
	 * This is a convenience method used to write the provided Document to the provided output stream.
	 *
	 * @param in_doc the Document to be written to the output stream
	 * @param in_os the output stream to which the Document is to be written
	 */
	public static void printDocumentToOutputStream(Document in_doc, OutputStream in_os) {
		printNodeToOutputStream(in_doc, in_os);
	}


	/**
	 * This is a convenience method used to write the provided Node to the provided output stream.
	 *
	 * @param in_node the Node to be written to the output stream
	 * @param in_os the output stream to which the Document is to be written
	 */
	public static void printNodeToOutputStream(Node in_node, OutputStream in_os) {
		Source source = new DOMSource(in_node);
		Result result = new StreamResult(in_os);
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
		}
		catch (TransformerException e) {
			LOGGER.log(Level.WARNING, "Unable to transform the provided document.", e);
		}
	}

	
}
