package gov.faa.nnew.sa;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XmlBeans node level methods.
 * 
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
 * Copyright (c) 2011-2020 BCI Incorporated.
 * </pre>
 * 
 * @version 2011.04.07 
 * @version 2014.11.12 
 * @author Peter V Donato
 */
public abstract class XmlbeansUtil {
	private XmlbeansUtil(){}
	
	
	public interface XmlbeansOps {
		/**
		 * <p>This method adds an additional element to the target object when it's schema allows for it
		 * with the &lt;xsd:any/&gt; tag.
		 * Since xmlbeans does not provide an accessor for the "any" element, you must go down to the
		 * Node level and import the desired node from one document to another.	What we do here is
		 * similar to the DOM Level 3 adoptNode() method of Document except that a new, deep copy 
		 * of the source node is created and then appended as a child to the target document.
		 * </p>
		 * 
		 * &lt;xsd:any namespace="##other" processContents="lax" minOccurs="0" maxOccurs="unbounded"/&gt;
		 * 
		 * <p>The namespace designation, ##other, of the any tag (which has the default ##any, allowing 
		 * elements in any namespace), here, allows elements in any namespace *except* that of the 
		 * schema's target namespace.
		 * </p>
		 * 
		 * @param source XmlObject node source to copy 
		 * @param target XmlObject node target where copied node is adopted as child
		 */
		public void copyAndAppendNode(XmlObject source, XmlObject target);
		/**
		 * Similar to method copyAndAppendNode() above, but will import all the children
		 * of the source node to the target document.
		 * @param source XmlObject node source from which to copy children
		 * @param target XmlObject node target where copied nodes are adopted as children
		 */
		public void copyAndAppendChildrenNodes(XmlObject source, XmlObject target);
		/**
		 * Inserts a namespace declaration immediately before each child of the source node, 
		 * giving it the specified prefix and URI.
		 * @param source XmlObject node source
		 * @param namespaceMap Map namespaces to add
		 */
		public void addNamespaceMap(XmlObject source, Map<String, String> namespaceMap);
		
		
		
		public static class Factory {
			private Factory(){}
			private static Logger LOGGER = Logger.getLogger(Factory.class.getName());
			
			public static XmlbeansOps newInstance() {
				return new XmlbeansOps() {

					@Override
					public void copyAndAppendNode(XmlObject source, XmlObject target) {
						Node targetNode = target.getDomNode();
						Document targetOwnerDocument = targetNode.getOwnerDocument();

						Node sourceNode = source.getDomNode();
						short nodeType = sourceNode.getNodeType();

						LOGGER.log(Level.FINEST, "importing from source node:{0} of type:{1}",
						      new String[] { source.toString(), String.valueOf(nodeType) });
						// Since Document nodes may not be imported, try using it's first child
						if(Node.DOCUMENT_NODE == nodeType) {
							LOGGER.log(Level.FINEST, "Cannot import document node; instead try to import first child node.");
							sourceNode = sourceNode.getFirstChild();
						}

						if(sourceNode == null) {
							LOGGER.log(Level.WARNING, "Failed to import node.");
						}
						else {
							try {
								Node importedNode = targetOwnerDocument.importNode(sourceNode, true);
								targetNode.appendChild(importedNode);
							}
							catch (DOMException e) {
								LOGGER.log(Level.WARNING, "DOMException on import/append node: {0}", e.getMessage());
							}
						}
					}

					@Override
					public void copyAndAppendChildrenNodes(XmlObject source, XmlObject target) {
						Node targetNode = target.getDomNode();
						Document targetOwnerDocument = targetNode.getOwnerDocument();

						Node sourceNode = source.getDomNode();
						LOGGER.log(Level.FINEST, "importing children of source node:{0} of type:{1}",
						      new String[] { source.toString(), String.valueOf(sourceNode.getNodeType())});

						int appendCount = 0;
						if(sourceNode.hasChildNodes()) {
							NodeList childNodes = sourceNode.getChildNodes();
							for(int i=0; i<childNodes.getLength(); i++) {
								Node child = childNodes.item(i);
								if(child == null || Node.DOCUMENT_NODE == child.getNodeType())
									break;
								
								try {
									Node importNode = targetOwnerDocument.importNode(child, true);
									targetNode.appendChild(importNode);
									appendCount++;
								}
								catch (DOMException e) {
									LOGGER.log(Level.WARNING, "DOMException on import/append node: {0}", e.getMessage());
								}
							}
							LOGGER.log(Level.FINEST, "{0} nodes appended to document.", appendCount);
						}
						else
							LOGGER.log(Level.WARNING, "Failed to import; node has no children.");
					}

					@Override
					public void addNamespaceMap(XmlObject source, Map<String, String> namespaceMap) {
						Node sourceNode = source.getDomNode();
						short nodeType = sourceNode.getNodeType();
						LOGGER.log(Level.FINEST, "Adding namespace map, nodeType:{0}", new String[]{String.valueOf(nodeType)});
						
						XmlCursor newCursor = source.newCursor();
						//System.err.println("  cursor name:"+newCursor.getName());
						
						newCursor.toStartDoc(); // Moves the cursor to the STARTDOC token, which is the root of the document.
						//System.err.println("  cursor name:"+newCursor.getName());

						newCursor.toFirstContentToken(); // Moves the cursor to the first token in the content of the current START or STARTDOC. 
						//System.err.println("  cursor name:"+newCursor.getName());
						
						boolean moved = newCursor.toFirstChild(); // Attempts to move the cursor to the first child element
						//System.err.println("  cursor name:"+newCursor.getName());
						LOGGER.log(Level.FINEST, "Moved to first child:{0}", new String[]{String.valueOf(moved)});
						
						Set<Entry<String,String>> entrySet = namespaceMap.entrySet();
						for(Entry<String, String> entry : entrySet) {
							// Inserts a namespace declaration immediately before the cursor's location
							newCursor.insertNamespace(entry.getValue(), entry.getKey());
						}
						newCursor.close();
					}
				};
			}
		}
	}
	
	
}
