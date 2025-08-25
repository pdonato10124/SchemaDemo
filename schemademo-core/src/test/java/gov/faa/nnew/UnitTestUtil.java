package gov.faa.nnew;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xmlbeans.XmlError;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

/**
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
 * Copyright (c) 2025 BCI Incorporated.
 * </pre>
 * 
 * @version 2010.09.16
 * @version 2025.08.18 
 * @author Peter V Donato
 */
public abstract class UnitTestUtil {
	private static final Logger LOGGER = Logger.getLogger(UnitTestUtil.class.getName());

	public static void assertSubstrInStr(String expectedSubstr, String actual) {
		String message = "Substring is not contained within the actual string.";
		LOGGER.log(Level.INFO, "\n{0}\nExpectedSubstr:\n{1}\nActual:\n{2}\n", new String[] {message, expectedSubstr, actual});
		assertNotNull(expectedSubstr);
		assertNotNull(actual);
		StringBuilder builder = new StringBuilder();
		builder.append("^.*").append(expectedSubstr).append(".*");
		if(!actual.matches(builder.toString())) {
			// This section tries to show the user where the difference lies by using 
			// the junit framework comparison throwable
			int displayDiffAfterChars = 10;
			int start = actual.indexOf(expectedSubstr.substring(0, displayDiffAfterChars));
			assertTrue(start > -1);
			int end = start + expectedSubstr.length();
			assertTrue(end > -1);
			//System.out.println("start:"+start+" end:"+end);
			throw new junit.framework.ComparisonFailure(message, expectedSubstr, actual.substring(start, end));
		}
	}
	
	public static void assertPattern(String regex, String actual) {
		String message = "Pattern does not match the actual string.";
		LOGGER.log(Level.INFO, "\n\nExpecting RegEx Pattern:\n{0}\n\nActual:\n{1}\n", new String[] {regex, actual});
		assertNotNull(regex);
		assertNotNull(actual);

		// Use DOTALL mode to allow the expression . to match any character
	   // including a line terminator
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(actual);
		boolean found = matcher.find();
		LOGGER.log(Level.INFO, "found:{0}", found);
		assertTrue(found, message);
	}
	
	
	public static void assertValidDocument(String message, XmlObject doc) {
		XmlOptions validateOptions = new XmlOptions();
		ArrayList<XmlError> errorList = new ArrayList<XmlError>();
		validateOptions.setErrorListener(errorList);			
		
		// Returns true if the contents of this object are valid according to schemaType(). 
		// Does a deep validation of the entire subtree under the object, but does not validate the parents or siblings 
		// of the object if the object is in the interior of an xml tree.
		if(!doc.validate(validateOptions)) {
			StringBuilder errors = new StringBuilder();
			errors.append("Errors\n");
			int errorCount = 1;
			for(XmlError err : errorList) {
				//System.err.println("  "+errorCount+": line: "+err.getLine()+" col: "+err.getColumn());
				//System.err.println("  "+errorCount+": "+err.getSeverity());
				errors.append("  ").append(errorCount).append(": ").append(err.getMessage()).append("\n");
				errorCount++;
			}
			errors.append(message);
			fail(errors.toString());
		}
	}

	

}
