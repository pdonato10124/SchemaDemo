package gov.faa.nnew.sa.mmixm;

import java.util.logging.Level;

import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aero.mmixm.base.x4.MessageDocument;
import gov.faa.nnew.UnitTestUtil;
import gov.faa.nnew.sa.XmlUtil;
import gov.faa.nnew.sa.XmlbeansUtil;
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
	public final Logger logger = LoggerFactory.getLogger(AssetDocBuilderTest.class.getName());
	public static final Level logLevelInfo = Level.INFO;
	
	public static final XmlOptions XML_OPTS = XmlUtil.XML_OPTS;
	public static final XmlbeansUtil.XmlbeansOps XOPS = XmlbeansUtil.XmlbeansOps.Factory.newInstance();
	
	@Test
	@DisplayName("Message Doc Builder")
	public void testMessageDocBuilder(TestInfo testInfo) {
		logger.info(String.format(String.format("%n+------+%n| TEST | %s%n+------+%n", testInfo.getDisplayName())));

		MessageDocBuilder docBuilder = MessageDocBuilder.Factory.newInstance(XML_OPTS);
		
		{
			AssetDocBuilder assetDocBuilder = AssetDocBuilder.Factory.newInstance(XML_OPTS);
			assetDocBuilder.setAssetName("FAA Asset");
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
	
	
}
