package gov.faa.nnew;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * Interface for the evaluation of XPath expressions on an XML source document.
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
 * Copyright (c) 2011-2024 BCI Incorporated.
 * </pre>
 * 
 * @version 2011.12.22
 * @version 2024.07.15 added NamespaceContextMap
 * @author Peter V Donato
 */
public interface XPathEvaluator {
	
	public void initialize(String[][] nameSpaceArr, String source) throws IllegalStateException, XPathExpressionException;
	
	/**
	 * Evaluate the the xpath expression against the XML source and return a list
	 * of string values. The returning list may be empty. 
	 * @param expression String xpath expression
	 * @return List&lt;String&gt; value list
	 */
	public List<String> evaluate(String expression);

	/**
	 * Evaluate the xpath expression against the XML source and return the first result 
	 * String value or the default String value on empty evaluation.
	 * @param expression String xpath expression
	 * @param defaultValue String default value
	 * @return String value
	 */
	public String evaluateFirstString(String expression, String defaultValue);
	
	/**
	 * Evaluate the the xpath expression against the XML source and return a list
	 * of string values. The returning list may be empty. 
	 * @param expression String xpath expression
	 * @return List&lt;String&gt; value list
	 */
	public List<String> evaluateStringValues(String expression);
	
	/**
	 * Evaluate the xpath expression against the XML source and return the first result
	 * Integer value or the default Integer value on empty evaluation.
	 * @param expression String xpath expression
	 * @param defaultValue Integer default value
	 * @return Integer value
	 */
	public Integer evaluateFirstInteger(String expression, Integer defaultValue);
	
	/**
	 * Evaluate the xpath expression against the XML source and return a list of
	 * Integer values.
	 * @param expression String xpath expression
	 * @return List&lt;Integer&gt; Double list
	 */
	public List<Integer> evaluateIntegerValues(String expression);
	
	/**
	 * Evaluate the xpath expression against the XML source and return the first result
	 * Double value or the default Double value on empty evaluation.
	 * @param expression String xpath expression
	 * @param defaultValue Double default value
	 * @return Double value
	 */
	public Double evaluateFirstDouble(String expression, Double defaultValue);
	
	/**
	 * Evaluate the xpath expression against the XML source and return a list of
	 * Double values.
	 * @param expression String xpath expression
	 * @return List&lt;Double&gt; Double list
	 */
	public List<Double> evaluateDoubleValues(String expression);
	
	/**
	 * Evaluate the xpath expression against the XML source and return a list of Double
	 * values evaluated from a single element. For example evaluating the element 
	 * &lt;gml:posList&gt;44.0 -12.0 44.0 -8.0 40.0 -8.0 40.0 -12.0 44.0 -12.0&lt;/gml:posList&gt;
	 * will return the Double list 44.0,-12.0,44.0, ...
	 * @param expression String xpath expression
	 * @return List&lt;Double&gt; Double list
	 */
	public List<Double> evaluateFirstDoubleArr(String expression);
	
	/**
	 * Evaluate the xpath expression against the XML source and return a list of double[]
	 * values corresponding to multiple elements of double list values. For example
	 * &lt;gml:posList&gt;44.0 -12.0 44.0 -8.0 40.0 -8.0 40.0 -12.0 44.0 -12.0&lt;/gml:posList&gt;
	 * &lt;gml:posList&gt;54.0 -12.0 54.0 -8.0 50.0 -8.0 50.0 -12.0 54.0 -12.0&lt;/gml:posList&gt;
	 * will return a list of two double arrays. 
	 * @param expression String xpath expression
	 * @return List&lt;double[]&gt; double array list
	 */
	public List<double[]> evaluateDoubleArrValues(String expression);
	
	/**
	 * Evaluate the xpath expression against the XML source and return the first result
	 * Date value that follows the String date pattern or the default Date value on empty
	 * evaluation.
	 * @param expression String xpath expression
	 * @param datePattern String date pattern
	 * @param defaultValue Date default value
	 * @return Date value
	 */
	public Date evaluateFirstDate(String expression, String datePattern, Date defaultValue);
	
	/**
	 * Evaluate the xpath expression against the XML source and return a list of date
	 * values that match the provided datePattern.
	 * @param expression String xpath expression
	 * @param datePattern String pattern example: yyyy-MM-dd'T'HH:mm:ss'Z'
	 * @return List&lt;Date&gt; date list
	 */
	public List<Date> evaluateDateValues(String expression, String datePattern);
	
	/**
	 * Evaluate the xpath expression against the XML source and replace all occurrences
	 * of the matching node value with the provided string replacement.
	 * @param expression String xpath expression
	 * @param replacement String for replacing all matching node values
	 */
	public void replaceValues(String expression, String replacement);
	/**
	 * Evaluate the xpath expression against the XML source and replace all occurrences
	 * of the matching node value with a string replacement value from the provided
	 * list of strings. 
	 * 
	 * If the replacement list of strings is shorter than the list of matching nodes the
	 * list is repeated for all remaining node value matches.
	 * 
	 * @param expression String xpath expression
	 * @param replacementList List&lt;String&gt; list of replacement string values
	 */
	public void replaceValues(String expression, List<String> replacementList);
	/**
	 * Evaluate the xpath expression against the XML source and replace all occurrences
	 * of the matching node value with a formatted date value from the provided list of
	 * dates.  
	 * 
	 * If the replacement list of dates is shorter than the list of matching nodes the
	 * list is repeated for all remaining node value matches.
	 * 
	 * The datePattern is used for both matching the node value and for formatting the 
	 * replacement date. 
	 * 
	 * @param expression String xpath expression
	 * @param replacementList List&lt;Date&gt; list of replacement date values
	 * @param datePattern String pattern example: yyyy-MM-dd'T'HH:mm:ss'Z'
	 */
	public void replaceDates(String expression, List<Date> replacementList, String datePattern);
	
	public void addDateSeconds(String expression, int seconds, String datePattern);
	
	public String transformDocument();
	/**
	 * Evaluate the xpath expression against the XML source and extract 
	 * each resulting node returning a list of strings representing each node.
	 * @param expression String xpath expression
	 * @return List&lt;String&gt; value list
	 */
	public List<String> transformDocument(String expression);

	public List<String> transformDocument(String expression, NodeType accept);

	public enum NodeType {
		ELEMENT(1),
		ATTRIBUTE(2),
		TEXT(3),
		CDATA_SECTION(4),
		ENTITY_REFERENCE(5),
		ENTITY(6),
		PROCESSING_INSTRUCTION(7),
		COMMENT(8),
		DOCUMENT(9),
		DOCUMENT_TYPE(10),
		DOCUMENT_FRAGMENT(11),
		NOTATION(12),
		;
		private int value;
		private NodeType(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		public boolean matches(Node node) {
			return value == (int)node.getNodeType();
		}
	}
	
	/**
	 * This Factory class provides static methods for producing instances of the surrounding interface. 
	 * A call to the appropriate factory method, depending on the intended function or dataset, will 
	 * produce the instance. Use of a factory in this way loosely couples the caller and the callee.
	 * <pre>
	 *  _ )  __|_ _|
	 *  _ \ (     | 
	 * ___/\___|___|
	 * Copyright (c) 2011 BCI Incorporated.
	 * </pre>
	 * @author Peter V Donato
	 */
	public static class Factory {
		private Factory() {}
		
		public static final Logger logger = LoggerFactory.getLogger(Factory.class.getName());
		
		public static final class NamespaceContextMap implements NamespaceContext {
			private final Map<String, String> prefixMap;    // prefix -> ns
			private final Map<String, Set<String>> nsMap;   // ns -> Set<prefix>

			/**
			 * Constructor that takes a map of XML prefix-namespaceURI values. A defensive
			 * copy is made of the map. An IllegalArgumentException will be thrown if the
			 * map attempts to remap the standard prefixes defined in the NamespaceContext
			 * contract.
			 * 
			 * @param prefixMappings
			 *          a map of prefix:namespaceURI values
			 */
			public NamespaceContextMap(Map<String, String> prefixMappings) {
				prefixMap = createPrefixMap(prefixMappings);
				nsMap = createNamespaceMap(prefixMap);
			}

//			/**
//			 * Convenience constructor.
//			 * 
//			 * @param mappingPairs
//			 *          pairs of prefix-namespaceURI values
//			 */
//			public NamespaceContextMap(String... mappingPairs) {
//				this(toMap(mappingPairs));
//			}
//
//			private static Map<String, String> toMap(String... mappingPairs) {
//				Map<String, String> prefixMappings = new HashMap<String, String>(mappingPairs.length / 2);
//				for(int i = 0; i < mappingPairs.length; i++) {
//					prefixMappings.put(mappingPairs[i], mappingPairs[++i]);
//				}
//				return prefixMappings;
//			}

			private Map<String, String> createPrefixMap(Map<String, String> prefixMappings) {
				Map<String, String> prefixMap = new HashMap<String, String>(prefixMappings);
				addConstant(prefixMap, XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
				addConstant(prefixMap, XMLConstants.XMLNS_ATTRIBUTE, XMLConstants.XMLNS_ATTRIBUTE_NS_URI);
				return Collections.unmodifiableMap(prefixMap);
			}

			private void addConstant(Map<String, String> prefixMap, String prefix, String nsURI) {
				//System.err.printf("| NamespaceContextMap prefixMap put(%s)->%s%n", prefix, nsURI);
				String previous = prefixMap.put(prefix, nsURI);
				if(previous != null && !previous.equals(nsURI)) {
					throw new IllegalArgumentException(prefix + " -> " + previous + "; see NamespaceContext contract");
				}
			}

			private Map<String, Set<String>> createNamespaceMap(Map<String, String> prefixMap) {
				Map<String, Set<String>> nsMap = new HashMap<String, Set<String>>();
				for(Map.Entry<String, String> entry : prefixMap.entrySet()) {
					String key = entry.getKey();
					String nsURI = entry.getValue();
					//System.err.printf("| NamespaceContextMap put(%s)->%s%n", key, nsURI);
					Set<String> prefixes = nsMap.get(nsURI);
					if(prefixes == null) {
						prefixes = new HashSet<String>();
						nsMap.put(nsURI, prefixes);
					}
					prefixes.add(key);
				}
				for(Map.Entry<String, Set<String>> entry : nsMap.entrySet()) {
					Set<String> readOnly = Collections.unmodifiableSet(entry.getValue());
					entry.setValue(readOnly);
				}
				return nsMap;
			}

			@Override
			public String getNamespaceURI(String prefix) {
				//System.err.printf("| NamespaceContext getNamespaceURI(%s)%n", prefix);
				checkNotNull(prefix);
				String nsURI = prefixMap.get(prefix);
				return nsURI == null ? XMLConstants.NULL_NS_URI : nsURI;
			}

			@Override
			public String getPrefix(String namespaceURI) {
				//System.err.printf("| NamespaceContext getPrefix(%s)%n", namespaceURI);
				checkNotNull(namespaceURI);
				Set<String> set = nsMap.get(namespaceURI);
				return set == null ? null : set.iterator().next();
			}

			@Override
			public Iterator<String> getPrefixes(String namespaceURI) {
				//System.err.printf("NamespaceContext getPrefixes(%s)%n", namespaceURI);
				checkNotNull(namespaceURI);
				Set<String> set = nsMap.get(namespaceURI);
				return set.iterator();
			}

			private void checkNotNull(String value) {
				if(value == null) {
					throw new IllegalArgumentException("null");
				}
			}

//			/**
//			 * @return an unmodifiable map of the mappings in the form prefix-namespaceURI
//			 */
//			public Map<String, String> getMap() {
//				System.err.printf("NamespaceContext getMap(%s)%n");
//				return prefixMap;
//			}
		}
		
		public static XPathEvaluator newInstance() {
			return new XPathEvaluator() {
				protected XPath xPath;
				protected Node documentNode;
				protected boolean initialized = false;
				protected final Charset utf8 = Charset.forName("UTF-8");

				@Override
				public void initialize(final String[][] nameSpaceArr, final String source) throws IllegalStateException, XPathExpressionException {
					if(initialized)
						throw new IllegalStateException("The XPathEvaluator has already been initialized.");
					
					initialized = true;
					XPathFactory xFactory = XPathFactory.newInstance();
					xPath = xFactory.newXPath();
					
//					NamespaceContext context = new NamespaceContext() {
//						private Map<String, String> nameSpaceMap = new HashMap<String, String>();
//						{
//							for(String[] oneSpace : nameSpaceArr) {
//								nameSpaceMap.put(oneSpace[0], oneSpace[1]);
//								//System.err.printf("| NamespaceContext adding ns: %s : %s%n", oneSpace[0], oneSpace[1]);
//							}
//						}
//
//						@Override
//						public String getNamespaceURI(String prefix) {
//							System.err.printf("NamespaceContext getNamespaceURI(%s)%n", prefix);
//							
//							return nameSpaceMap.get(prefix);
//						}
//						
//						@Override
//						public String getPrefix(String namespaceURI) {
//							System.err.printf("NamespaceContext getPrefix(%s)%n", namespaceURI);
//							
//							String key;
//							Iterator<String> it = getPrefixes(namespaceURI);
//							if(it.hasNext())
//								key = it.next();
//							else
//								key = null;
//							return key;
//						}
//						
//						@Override
//						public Iterator<String> getPrefixes(String namespaceURI) {
//							System.err.printf("NamespaceContext getPrefixes(%s)%n", namespaceURI);
//							
//							List<String> prefixes = new ArrayList<String>();
//							if(nameSpaceMap.containsValue(namespaceURI)) {
//								Set<Entry<String, String>> entrySet = nameSpaceMap.entrySet();
//								for(Entry<String, String> entry: entrySet) {
//									if(namespaceURI.equals(entry.getValue()))
//										prefixes.add(entry.getKey());
//								}
//							}
//							return prefixes.iterator();
//						}
//					};
					
					Map<String, String> mappings = new HashMap<String, String>();
					for(String[] oneSpace : nameSpaceArr) {
						mappings.put(oneSpace[0], oneSpace[1]);
						//System.err.printf("| NamespaceContext adding ns: %s : %s%n", oneSpace[0], oneSpace[1]);
					}
					mappings.put(XMLConstants.XML_NS_PREFIX, XMLConstants.XML_NS_URI);
					NamespaceContextMap contextMap = new NamespaceContextMap(mappings);
					
					xPath.setNamespaceContext(contextMap);
					
					InputStream stream = null;
					try {
						stream = new ByteArrayInputStream(source.getBytes(utf8));
						//NodeList nodeList = (NodeList)xPath.evaluate("/", new InputSource(stream), XPathConstants.NODESET);
						//documentNode = nodeList.item(0);
						
						//System.err.printf("%nevaluating...%n");
						//XPathExpression expression = xPath.compile("/");
						//documentNode = (Node)expression.evaluate(new InputSource(stream), XPathConstants.NODE);
						documentNode = (Node)xPath.evaluate("/", new InputSource(stream), XPathConstants.NODE);
					}
					catch (XPathExpressionException e) {
						logger.error(String.format("XPathExpressionException: %s", e.getMessage()));
						throw e;
					}
					finally {
						try {
							if(stream != null)
								stream.close();
						}
						catch (IOException e) {
							logger.error(String.format("IOException: %s", e.getMessage()));
						}
					}
				}
				
				protected NodeList evaluateToNodeSet(String expression) {
					NodeList nodeList;
					try {
						nodeList = (NodeList)xPath.evaluate(expression, documentNode, XPathConstants.NODESET);
					}
					catch (XPathExpressionException e) {
						logger.error(String.format("XPathExpressionException: %s", e.getMessage()));
						nodeList = null;
					}
					return nodeList;
				}
				
				@Override
				public List<String> evaluate(String expression) {
					return evaluateStringValues(expression);
				}

				@Override
				public String evaluateFirstString(String expression, String defaultValue) {
					List<String> list = evaluateStringValues(expression);
					return (list.size() > 0) ? list.get(0) : defaultValue;
				}
				
				@Override
				public List<String> evaluateStringValues(String expression) {
					if(!initialized)
						throw new IllegalStateException("The XPathEvaluator has not yet been initialized.");
					
					List<String> resultList = new ArrayList<String>();
					try {
						NodeList nodeList = (NodeList)xPath.evaluate(expression, documentNode, XPathConstants.NODESET);
						for(int i = 0; i < nodeList.getLength(); i++) {
							Node node = nodeList.item(i);
							logger.debug(String.format("| nodeName: %s%n| nodeValue: %s%n| node: %s%n", node.getNodeName(), node.getNodeValue(), node.toString()));
							resultList.add(node.getNodeValue());
						}
					}
					catch (XPathExpressionException e) {
						logger.error(String.format("XPathExpressionException: %s", e.getMessage()));
					}		
					return resultList;
				}
				
				@Override
				public Integer evaluateFirstInteger(String expression, Integer defaultValue) {
					List<Integer> list = evaluateIntegerValues(expression);
					return (list.size() > 0) ? list.get(0) : defaultValue;
				}
				
				@Override
				public List<Integer> evaluateIntegerValues(String expression) {
					List<Integer> list = new ArrayList<Integer>();
					List<String> nodeValues = evaluateStringValues(expression);
					for(String node : nodeValues) {
						try {
							int parseInteger = Integer.parseInt(node);
							list.add(parseInteger);
						}
						catch (NumberFormatException e) {
						}
					}
					return list;
				}
				
				@Override
				public Double evaluateFirstDouble(String expression, Double defaultValue) {
					List<Double> list = evaluateDoubleValues(expression);
					return (list.size() > 0) ? list.get(0) : defaultValue;
				}
				
				@Override
				public List<Double> evaluateDoubleValues(String expression) {
					List<Double> list = new ArrayList<Double>();
					List<String> nodeValues = evaluateStringValues(expression);
					for(String node : nodeValues) {
						try {
							double parseDouble = Double.parseDouble(node);
							list.add(parseDouble);
						}
						catch (NumberFormatException e) {
						}
					}
					return list;
				}
				
				@Override
				public List<Double> evaluateFirstDoubleArr(String expression) {
					List<Double> list = new ArrayList<Double>();
					List<String> nodeValues = evaluateStringValues(expression);
					String test = nodeValues.get(0);
					String[] strValueArr = test.split("[,;\\s]");
					for(String v : strValueArr) {
						if(!v.isBlank()) { //v.length() > 0) {
							double d = Double.parseDouble(v);
							list.add(d);
						}
					}
					return list;
				}
				
				@Override
				public List<double[]> evaluateDoubleArrValues(String expression) {
					List<double[]> list = new ArrayList<double[]>();
					List<String> nodeValues = evaluateStringValues(expression);
					for(String test : nodeValues) {
						String[] strValueArr = test.split("[,;\\s]");
						List<Double> valueList = new ArrayList<Double>();
						for(String v : strValueArr) {
							if(!v.isBlank()) { //v.length() > 0) {
								double d = Double.parseDouble(v);
								valueList.add(d);
							}
						}
						double[] doubleArr = new double[valueList.size()];
						for(int i=0; i<valueList.size(); i++) {
							doubleArr[i] = valueList.get(i);
						}
						list.add(doubleArr);
					}
					return list;
				}
				
				@Override
				public Date evaluateFirstDate(String expression, String datePattern, Date defaultValue) {
					List<Date> list = evaluateDateValues(expression, datePattern);
					return (list.size() > 0) ? list.get(0) : defaultValue;
				}
				
				@Override
				public List<Date> evaluateDateValues(String expression, String datePattern) {
					if(!initialized)
						throw new IllegalStateException("The XPathEvaluator has not yet been initialized.");
					
					List<Date> dateValues = new ArrayList<Date>();
					List<String> nodeValues = evaluateStringValues(expression);
					DateFormatter formatter = DateFormatter.Factory.newInstanceGMTMyPattern(datePattern);
					
					for(String node : nodeValues) {
						Date date = formatter.parse(node);
						if(date != null)
							dateValues.add(date);
					}
					return dateValues;
				}
				
				@Override
				public String transformDocument() {
					if(!initialized)
						throw new IllegalStateException("The XPathEvaluator has not yet been initialized.");
					return transformNode(documentNode);
				}
				
				@Override
				public List<String> transformDocument(String expression) {
					if(!initialized)
						throw new IllegalStateException("The XPathEvaluator has not yet been initialized.");
					List<String> resultList = new ArrayList<String>();
					
					NodeList nodeSet = evaluateToNodeSet(expression);
					if(nodeSet != null) {
						for(int i=0; i<nodeSet.getLength(); i++) {
							Node node = nodeSet.item(i);
							//System.err.printf("node: type: %d value: %s%n", node.getNodeType(), node.getNodeValue());
							resultList.add(transformNode(node));
						}
					}
					return resultList;
				}
				
				@Override
				public List<String> transformDocument(String expression, NodeType accept) {
					if(!initialized)
						throw new IllegalStateException("The XPathEvaluator has not yet been initialized.");
					List<String> resultList = new ArrayList<String>();
					
					NodeList nodeSet = evaluateToNodeSet(expression);
					if(nodeSet != null) {
						for(int i=0; i<nodeSet.getLength(); i++) {
							Node node = nodeSet.item(i);
							//System.err.printf("node: type: %d value: %s%n", node.getNodeType(), node.getNodeValue());
							// Only add nodes of NodeType accept type
							if(accept.matches(node))
								resultList.add(transformNode(node));
						}
					}
					return resultList;
				}
				
				
				private String transformNode(Node node) {
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();

					try {
						Transformer transformer = transformerFactory.newTransformer();
						transformer.transform(new DOMSource(node), new StreamResult(baos));
					}
					catch (TransformerConfigurationException e) {
						logger.error(String.format("TransformerConfigurationException: %s", e.getMessage()));
					}
					catch (TransformerException e) {
						logger.error(String.format("TransformerException: %s", e.getMessage()));
					}
					finally {
						try {
							baos.close();
						}
						catch (IOException e) {
							logger.error(String.format("IOException: %s", e.getMessage()));
						}
					}
					return baos.toString(utf8);
				}
				
				@Override
				public void replaceValues(String expression, String replacement) {
					if(!initialized)
						throw new IllegalStateException("The XPathEvaluator has not yet been initialized.");
					NodeList nodeSet = evaluateToNodeSet(expression);
					if(nodeSet != null) {
						for(int i=0; i<nodeSet.getLength(); i++) {
							Node node = nodeSet.item(i);
							//String oldValue = node.getNodeValue();
							node.setNodeValue(replacement);
							//System.err.println("  replacing: "+oldValue+" with: "+replacement);
						}
					}
				}
				
				@Override
				public void replaceValues(String expression, List<String> replacementList) {
					if(!initialized)
						throw new IllegalStateException("The XPathEvaluator has not yet been initialized.");
					NodeList nodeSet = evaluateToNodeSet(expression);
					if(nodeSet != null) {
						for(int i=0; i<nodeSet.getLength(); i++) {
							Node node = nodeSet.item(i);
							//String oldValue = node.getNodeValue();
							String newValue = replacementList.get(i%replacementList.size());
							node.setNodeValue(newValue);
							//System.err.println("  replacing: "+oldValue+" with: "+newValue);
						}
					}
				}
				
				@Override
				public void replaceDates(String expression, List<Date> replacementList, String datePattern) {
					if(!initialized)
						throw new IllegalStateException("The XPathEvaluator has not yet been initialized.");
					DateFormatter formatter = DateFormatter.Factory.newInstanceGMTMyPattern(datePattern);
					NodeList nodeSet = evaluateToNodeSet(expression);
					if(nodeSet != null) {
						for(int i=0; i<nodeSet.getLength(); i++) {
							Node node = nodeSet.item(i);
							//String oldValue = node.getNodeValue();
							Date newDate = replacementList.get(i%replacementList.size());
							String newValue = formatter.format(newDate);
							node.setNodeValue(newValue);
						}
					}
				}
			
				@Override
				public void addDateSeconds(String expression, int seconds, String datePattern) {
					if(!initialized)
						throw new IllegalStateException("The XPathEvaluator has not yet been initialized.");
					DateFormatter formatter = DateFormatter.Factory.newInstanceGMTMyPattern(datePattern);
					Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
					NodeList nodeSet = evaluateToNodeSet(expression);
					if(nodeSet != null) {
						for(int i=0; i<nodeSet.getLength(); i++) {
							Node node = nodeSet.item(i);
							String nodeValue = node.getNodeValue();
							Date date = formatter.parse(nodeValue);
							if(date != null) {
								cal.setTime(date);
								cal.add(Calendar.SECOND, seconds);
								node.setNodeValue(formatter.format(cal.getTime()));
							}
						}
					}
				}
			};
		}
	}

	
	public interface DateFormatter {
		static final String PATTERN_GMT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		static final String PATTERN_LOCAL = "yyyy-MM-dd'T'HH:mm:ss";
		static final String PATTERN_GMT_FRAC_SEC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

		/** 
		 * Format the argument date object to a string. 
		 * @param date Date to format
		 * @return String formatted date
		 */
		public String format(Date date);
		/** 
		 * Parse the argument string formatted date to a date object. 
		 * @param date String date to parse
		 * @return Date parsed date 
		 */
		public Date parse(String date);
		
		/**
		 * Abstract class implementing the Formmtter interface and generalizing the common methods.
		 * Implementers need only modify the protected member SimpleDateFormat by setting the 
		 * timezone and pattern.
		 * <pre>
		 *  _ )  __|_ _|
		 *  _ \ (     | 
		 * ___/\___|___|
		 * Copyright (c) 2010 BCI Incorporated.
		 * </pre> 
		 * @author Peter V Donato
		 */
		abstract class AbstractFormatter implements DateFormatter {
			protected SimpleDateFormat dateFormat = (SimpleDateFormat)DateFormat.getDateTimeInstance();
			public final Logger logger = LoggerFactory.getLogger(AbstractFormatter.class.getName());
			
			public String format(Date date) {
				return dateFormat.format(date);
			}
			
			public Date parse(String date) {
				Date d;
				if(date != null) {
					try {
						d = dateFormat.parse(date);
					}
					catch (ParseException e) {
						logger.error(String.format("ParseException: %s", e.getMessage()));
						d = null;
					}
				}
				else
					d = null;
				return d;
			}
		}

		/**
		 * Static factory for the Formatter interface.
		 */
		public static class Factory {
			/**
			 * Create a new instance of the Formatter interface using the GMT timezone pattern.
			 * @return Formatter object
			 */
			public static DateFormatter newInstanceGMT() {
				return new AbstractFormatter() {
					{
						dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
						dateFormat.applyPattern(PATTERN_GMT);
					}
				};
			}

			/**
			 * Create a new instance of the Formatter interface using the GMT timezone pattern
			 * with fractional seconds.
			 * @return Formatter object
			 */
			public static DateFormatter newInstanceGMTFractionalSec() {
				return new AbstractFormatter() {
					{
						dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
						dateFormat.applyPattern(PATTERN_GMT_FRAC_SEC);
					}
				};
			}

			/**
			 * Create a new instance of the Formatter interface using the local timezone pattern.
			 * @return Formatter object
			 */
			public static DateFormatter newLocalInstance() {
				return new AbstractFormatter() {
					{
						dateFormat.setTimeZone(TimeZone.getDefault());
						dateFormat.applyPattern(PATTERN_LOCAL);
					}
				};
			}
			
			public static DateFormatter newInstanceGMTMyPattern(final String pattern) {
				return new AbstractFormatter() {
					{
						dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
						dateFormat.applyPattern(pattern);
					}
				};
			}


		}
	}

	
}
