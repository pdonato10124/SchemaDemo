package gov.faa.nnew.sa.mmixm;

import java.util.logging.Level;

import org.apache.xmlbeans.XmlOptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import aero.mmixm.features.x4.AssetDocument;
import gov.faa.nnew.UnitTestUtil;
import gov.faa.nnew.sa.XmlUtil;
import gov.faa.nnew.sa.XmlbeansUtil;
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
 * @version 2025.07.28
 * @author Peter V Donato
 */
public class AssetDocBuilderTest {
	public final Logger logger = LoggerFactory.getLogger(AssetDocBuilderTest.class.getName());
	
	public static final Level logLevelInfo = Level.INFO;
	
	public static final XmlOptions XML_OPTS = XmlUtil.XML_OPTS;
	public static final XmlbeansUtil.XmlbeansOps XOPS = XmlbeansUtil.XmlbeansOps.Factory.newInstance();
	
	@Test
	@DisplayName("Asset Doc Builder")
	public void testAssetDocBuilder(TestInfo testInfo) {
		logger.info(String.format(String.format("%n+------+%n| TEST | %s%n+------+%n", testInfo.getDisplayName())));
		
		AssetDocBuilder docBuilder = AssetDocBuilder.Factory.newInstance(XML_OPTS);
		
		// Set specific document parameters here
		docBuilder.setFacaNumber("FA-10065");
		docBuilder.setFsepFac(FsepFac.EnRouteAutomationDisplaySystem);
		docBuilder.setFsepFic(FsepFic.TBFM_61UB);
		docBuilder.setFsepLocId("DFWA");
		
		AssetDocument doc = docBuilder.build();
		logger.info(String.format("%nassetDoc:%n%s%n", doc.xmlText(XML_OPTS)));
		UnitTestUtil.assertValidDocument("Document Failed Validation!", doc);
	}
	
	
}
