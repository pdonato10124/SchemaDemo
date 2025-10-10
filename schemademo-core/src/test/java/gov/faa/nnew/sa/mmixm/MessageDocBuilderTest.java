package gov.faa.nnew.sa.mmixm;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.logging.Level;

import javax.xml.xpath.XPathExpressionException;

import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aero.mmixm.base.x4.MessageDocument;
import gov.faa.nnew.UnitTestUtil;
import gov.faa.nnew.XPathEvaluator;
import gov.faa.nnew.XPathEvaluator.NodeType;
import gov.faa.nnew.sa.XmlUtil;
import gov.faa.nnew.sa.XmlbeansUtil;
import gov.faa.nnew.sa.mmixm.AssetDocBuilder.AssetDocType;
import gov.faa.nnew.sa.mmixm.AssetDocBuilder.FaaLocationType;
import gov.faa.nnew.sa.mmixm.AssetDocBuilder.FsepFac;
import gov.faa.nnew.sa.mmixm.AssetDocBuilder.FsepFic;

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
 * Copyright (c) 2025 BCI Incorporated.
 * </pre>
 * 
 * @version 2025.08.18
 * @author Peter V Donato
 */
public class MessageDocBuilderTest {
	public final Logger logger = LoggerFactory.getLogger(MessageDocBuilderTest.class.getName());
	public static final Level logLevelInfo = Level.INFO;
	
	public static final XmlOptions XML_OPTS = XmlUtil.XML_OPTS;
	public static final XmlbeansUtil.XmlbeansOps XOPS = XmlbeansUtil.XmlbeansOps.Factory.newInstance();
	
	public static final String PATTERN_GMT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final String PATTERN_LOCAL = "yyyy-MM-dd'T'HH:mm:ss";
	public static final String PATTERN_GMT_FRAC_SEC = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	@Test
	@DisplayName("Message Doc Builder")
	public void testMessageDocBuilder(TestInfo testInfo) {
		logger.info(String.format("%n+------+%n| TEST | %s%n+------+%n", testInfo.getDisplayName()));

		MessageDocBuilder docBuilder = MessageDocBuilder.Factory.newInstance(XML_OPTS);
		
		docBuilder.setMessageId(UUID.randomUUID().toString());
		docBuilder.setMessageType("FSEP Message");

		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("zulu"));
		cal.set(2025, 8, 29);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		docBuilder.setMessageTimestamp(cal.getTime());

		{
			AssetDocBuilder assetDocBuilder = AssetDocBuilder.Factory.newInstance(XML_OPTS);
			assetDocBuilder.setAssetName("FAA Asset");
			assetDocBuilder.setAssetDocType(AssetDocType.SERVICE);

			assetDocBuilder.addAdditionalInformation("A", "1");
			assetDocBuilder.addAdditionalInformation("B", "2");
			assetDocBuilder.addAdditionalInformation("C", "3");
			assetDocBuilder.setFaaLocation(FaaLocationType.TRACON, "ZNY");
			
			assetDocBuilder.setFacaNumber("FA-10065");
			assetDocBuilder.setFsepFac(FsepFac.EnRouteAutomationDisplaySystem);
			assetDocBuilder.setFsepFic(FsepFic.TBFM_61UB);
			assetDocBuilder.setFsepLocId("DFWA");

			docBuilder.setAsset(assetDocBuilder.build());
		}
		MessageDocument doc = docBuilder.build();
		
		logger.info(String.format("%nmessageDoc:%n%s%n", doc.xmlText(XML_OPTS)));
		UnitTestUtil.assertValidDocument("Document Failed Validation!", doc);
	}

	@Test
	@DisplayName("Message XPath")
	public void testMessageXpath(TestInfo testInfo) {
		logger.info(String.format("%n+------+%n| TEST | %s%n+------+%n", testInfo.getDisplayName()));

		MessageDocBuilder docBuilder = MessageDocBuilder.Factory.newInstance(XML_OPTS);
		{
			AssetDocBuilder assetDocBuilder = AssetDocBuilder.Factory.newInstance(XML_OPTS);
			assetDocBuilder.setAssetName("FAA Asset");
			assetDocBuilder.addAdditionalInformation("A", "1.1");
			assetDocBuilder.addAdditionalInformation("B", "2.2");
			assetDocBuilder.addAdditionalInformation("C", "3.3");
			assetDocBuilder.setFaaLocation(FaaLocationType.TRACON, "ZNY");

			assetDocBuilder.setFacaNumber("FA-10065");
			assetDocBuilder.setFsepFac(FsepFac.EnRouteAutomationDisplaySystem);
			assetDocBuilder.setFsepFic(FsepFic.TBFM_61UB);
			assetDocBuilder.setFsepLocId("DFWA");

			docBuilder.setAsset(assetDocBuilder.build());
		}
		MessageDocument doc = docBuilder.build();

		// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

		XPathEvaluator xPathEvaluator = XPathEvaluator.Factory.newInstance();

		String[][] NAME_SPACES = new String[][] {
			{"mb", "https://mmixm.aero/base/4"},
			{"mx", "https://mmixm.aero/features/4"},
		};

		try {
			xPathEvaluator.initialize(NAME_SPACES, doc.xmlText(XML_OPTS));

			/*
			<mb:Message xmlns:mb="https://mmixm.aero/base/4" xmlns:mx="https://mmixm.aero/features/4" messageType="message_type" timestamp="2025-10-07T15:03:21.174Z">
			  <mx:Asset>
			    <mx:assetIdentifier>
			      <mx:faaIdentifier>
			        <mx:facaNumber>FA-10065</mx:facaNumber>
			*/
			String facaNumber = xPathEvaluator.evaluateFirstString("/mb:Message/mx:Asset/mx:assetIdentifier/mx:faaIdentifier/mx:facaNumber/node()", "");
			logger.info(String.format("facaNumber: %s", facaNumber));

			/*
		    <mx:additionalInformation>
		      <mb:nameValue name="A" value="1.1"/>
		      <mb:nameValue name="B" value="2.2"/>
		      <mb:nameValue name="C" value="3.3"/>
			 */
			List<String> nodeList = xPathEvaluator.transformDocument("/mb:Message/mx:Asset/mx:additionalInformation/node()", NodeType.ELEMENT);
			logger.info(String.format("| additionalInformation:"));
			for(String node : nodeList) {
				XPathEvaluator xPathElementEvaluator = XPathEvaluator.Factory.newInstance();
				xPathElementEvaluator.initialize(NAME_SPACES, node);

				String name = xPathElementEvaluator.evaluateFirstString("/mb:nameValue/@name","");
				double value = xPathElementEvaluator.evaluateFirstDouble("/mb:nameValue/@value",Double.NaN);
				logger.info(String.format("| | name: %s  value: %f", name, value));
			}

			/*
			<mb:Message >
			  <mx:Asset>
			    <mx:loggingEvent>
			      <mx:createdDateTime>2025-10-07T15:41:11.677Z</mx:createdDateTime>
			      <mx:logId>885903724</mx:logId>
			*/
			Date createdDateTime = xPathEvaluator.evaluateFirstDate("//mx:loggingEvent/mx:createdDateTime/node()", PATTERN_GMT_FRAC_SEC, new Date(0));
			logger.info(String.format("createdDateTime: %s", createdDateTime.toString()));

			int logId = xPathEvaluator.evaluateFirstInteger("//mx:loggingEvent/mx:logId/node()", -1);
			logger.info(String.format("logId: %s%n%n", logId));
		}
		catch (IllegalStateException e) {
			logger.error(String.format("IllegalStateException: %s", e.getMessage()));
		}
		catch (XPathExpressionException e) {
			logger.error(String.format("XPathExpressionException: %s", e.getMessage()));
		}

	}
}
