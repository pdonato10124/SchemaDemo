package gov.faa.nnew.sa.mmixm;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import aero.mmixm.base.x4.AdditionalInformation;
import aero.mmixm.base.x4.CodeDescriptionType;
import aero.mmixm.base.x4.CodeFaaLocationType;
import aero.mmixm.base.x4.CodeFaaLocationType.Enum;
import aero.mmixm.base.x4.CodeLogStatusDescriptionType;
import aero.mmixm.base.x4.FaaLocation;
import aero.mmixm.base.x4.FaaLocationTypeChoice;
import aero.mmixm.base.x4.Location;
import aero.mmixm.base.x4.NameValuePair;
import aero.mmixm.base.x4.PhysicalAddress;
import aero.mmixm.base.x4.PostalAddress;
import aero.mmixm.features.x4.Asset;
import aero.mmixm.features.x4.AssetDocument;
import aero.mmixm.features.x4.AssetIdentifier;
import aero.mmixm.features.x4.FaaIdentifier;
import aero.mmixm.features.x4.FsepIdentifier;
import aero.mmixm.features.x4.LoggingCode;
import aero.mmixm.features.x4.LoggingEvent;
import aero.mmixm.features.x4.MaintenanceActivity;
import gov.faa.nnew.sa.XmlbeansUtil;

/**
 * Builder for creating AssetDocument document.
 * 
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
 * @version 2025.07.28
 * @author Peter V Donato
 */
public interface AssetDocBuilder {

	public AssetDocBuilder setAssetName(String assetName);
	public AssetDocBuilder addAdditionalInformation(String name, String value);
	public AssetDocBuilder setFaaLocation(FaaLocationType faaLocationType, String faaLocationValue);
	
	/**
	 * The FA/CA number (Federal Aviation/Civil Aviation Number) is an alphanumeric reference designator that 
	 * individually differentiates each Equipment type from others with the same short name.   An example of 
	 * the correct FA/CA# format is FA-10065. The manufacturer's model number is used when an FA/CA is not available.
	 * @param facaNumber String
	 * @return AssetDocBuilder
	 */
	public AssetDocBuilder setFacaNumber(String facaNumber);
	
	/**
	 * The five- character alphanumeric field contains the type of entity in FSEP, including services.
	 * This is the field FAC_TYPE in the RMLS/FSEP database. Sample values include the following.
	 *   ASR: Airport Surveillance Radar
	 *   ATIS: Automatic Terminal Information System
	 *   ATCT: Airport Traffic Control Tower
	 *   BUECS: Backup Emergency Communications Service
	 *   CFAD: Composite Flight Data Processing Service
	 *   EADS: En Route Automation Display System
	 * @param fsepFac FsepFac
	 * @return AssetDocBuilder
	 */
	public AssetDocBuilder setFsepFac(FsepFac fsepFac);
	
	/**
	 * The Facility Identification Code (FIC) provides the make, model, and class for identifying equipment in FSEP.
	 * The make and model codes make up the four-character FIC Code. The FIC Code is mandatory in all non-service records.
	 * The FIC Codes established prior to the release of older versions of Order 6000.5 are still valid and remain in the FSEP
	 * Desk guides until the equipment is retired from the NAS. Not all legacy codes are changed to the new FIC Code.
	 * This is the field FAC_CODE_FACILITY in the RMLS/FSEP database. Sample values include the following.
	 *   ASR FICs: 53AC,  53AG, SC00
	 *   GBAS FICs: NWA0
	 *   TBFM FICs: 61UB, AE00
	 * @param fsepFic FsepFic
	 * @return AssetDocBuilder
	 */
	public AssetDocBuilder setFsepFic(FsepFic fsepFic);
	
	/**
	 * This three or four character field contains the city and state where the equipment is physically located.
	 * Location identifiers assignment process is detailed in Order 6000.5E, Chapter 3, Section 3.1 Location Identifier Process.
	 * This is the field FAC_IDENT in the RMLS/FSEP database but which is typically referred to as LOC_ID.
	 * There are three sources for a LOC_ID:
	 *     1. A value from the IDENT column in the 7350.9.
	 *     2. A value from the IDENT column in the 7350.9 to which a single character suffix is added.
	 *        The suffix is added when there is more than one instance of the same FSEP Facility at a specific location.
	 *        An example is the Precision Approach Path Indicators (PAPI) at Boston Logan Airport.
	 *        There are 4 of these, and each has a different LOC_ID derived from BOS, the identifier for Logan. These are BOS, BOSA, BOSB and BOSC.
	 *     3. A made up value starting with a Q.
	 *        These are known as Q Identifiers and are created when there is no established identifier within 20 miles of the location of the FSEP Facility.
	 * Sample values include I90, DAL, TPA, CXYA, DCAP, PUB, ZDCC, DFWA.
	 * @param fsepLocId String
	 * @return AssetDocBuilder
	 */
	public AssetDocBuilder setFsepLocId(String fsepLocId);
	
	public AssetDocBuilder setPostalAddress(String city, String state);
	
	public AssetDocument build();
	
	
	public enum FaaLocationType {
		NONE(Enum.forString("None")),
		AIRPORT(CodeFaaLocationType.AIRPORT),
		ARTCC(CodeFaaLocationType.ARTCC),
		ATCSCC(CodeFaaLocationType.ATCSCC),
		ATCT(CodeFaaLocationType.ATCT),
		CERAP(CodeFaaLocationType.CERAP),
		TRACON(CodeFaaLocationType.TRACON),
		;
		private CodeFaaLocationType.Enum type;
		private FaaLocationType(CodeFaaLocationType.Enum type) {
			this.type = type;
		}
		public CodeFaaLocationType.Enum getType() {
			return type;
		}
	}
	
	// + - - - - - - - - - - - - - - - - - - - - - - - - -
	// | Facility, Service, and Equipment Profile (FSEP) |
	// + - - - - - - - - - - - - - - - - - - - - - - - - -
	
	public enum FsepFac {
		None("None"),
		AirportSurveillanceRadar("ASR"),
		AutomaticTerminalInformationSystem("ATIS"),
		AirportTrafficControlTower("ATCT"),
		BackupEmergencyCommunicationsService("BUECS"),
		CompositeFlightDataProcessingService("CFAD"),
		EnRouteAutomationDisplaySystem("EADS"),
		CPDS("CPDS"),
		;
		private String code;
		private FsepFac(String code) {
			this.code = code;		
		}
		public String getCode() {
			return this.code;
		}
	}
	
	public enum FsepFic {
		None("None"),
		ASR_53AC("53AC"),
		ASR_53AG("53AG"),
		ASR_SC00("SC00"),
		GBAS_NWA0("NWA0"),
		TBFM_61UB("61UB"),
		TBFM_AE00("AE00"),
		;
		private String code;
		private FsepFic(String code) {
			this.code = code;		
		}
		public String getCode() {
			return this.code;
		}
	}

	/**
	 * Build the document and append it to the supplied xml object. 
	 * @param appendTo XmlObject
	 */
	public void build(XmlObject appendTo);

	/**
	 * <pre>
	 *  _ )  __|_ _|
	 *  _ \ (     | 
	 * ___/\___|___|
	 * Copyright (c) 2025 BCI Incorporated.
	 * </pre>
	 * @author Peter V Donato
	 */
	public static final class Factory {
		public static AssetDocBuilder newInstance(final XmlOptions xmlOpts) {
			return new AssetDocBuilder() {
				private XmlbeansUtil.XmlbeansOps XOPS = gov.faa.nnew.sa.XmlbeansUtil.XmlbeansOps.Factory.newInstance();

				private String assetName = "";
				private String facaNumber = "";
				private FsepFac fsepFac = FsepFac.None;
				private FsepFic fsepFic = FsepFic.None;
				private String fsepLocId = "";

				private FaaLocationType faaLocationType = FaaLocationType.NONE;
				private String faaLocationValue = "";
				
				private Map<String,String> addInfoMap = new HashMap<>();
				
				private String city = "";
				private String state = "";
				
				
				@Override
				public AssetDocBuilder setAssetName(String assetName) {
					this.assetName = assetName;
					return this;
				}
				
				@Override
				public AssetDocBuilder addAdditionalInformation(String name, String value) {
					addInfoMap.put(name, value);
					return this;
				}
				
				@Override
				public AssetDocBuilder setFaaLocation(FaaLocationType faaLocationType, String faaLocationValue) {
					this.faaLocationType = faaLocationType;
					this.faaLocationValue = faaLocationValue;
					return this;
				}
				
				@Override
				public AssetDocBuilder setFacaNumber(String facaNumber) {
					this.facaNumber = facaNumber;
					return this;
				}
				
				@Override
				public AssetDocBuilder setFsepFac(FsepFac fsepFac) {
					this.fsepFac = fsepFac;
					return this;
				}
				
				@Override
				public AssetDocBuilder setFsepFic(FsepFic fsepFic) {
					this.fsepFic = fsepFic;
					return this;
				}
				
				@Override
				public AssetDocBuilder setFsepLocId(String fsepLocId) {
					this.fsepLocId = fsepLocId;
					return this;
				}

				@Override
				public AssetDocBuilder setPostalAddress(String city, String state) {
					this.city = city;
					this.state = state;
					return this;
				}

				
				
				@Override
				public AssetDocument build() {
					AssetDocument assetDoc = AssetDocument.Factory.newInstance(xmlOpts);
					Asset asset = assetDoc.addNewAsset();

					if(!assetName.isBlank()) {
						asset.setAssetName(assetName);
					}
					
					AssetIdentifier assetIdentifier = asset.addNewAssetIdentifier();
					if(!facaNumber.isEmpty()) {
						FaaIdentifier faaIdentifier = assetIdentifier.addNewFaaIdentifier();
						faaIdentifier.setFacaNumber(facaNumber);
					}
					
					FsepIdentifier fsepIdentifier = assetIdentifier.addNewFsepIdentifier();
					if(fsepFac != FsepFac.None) {
						fsepIdentifier.setFsepFac(fsepFac.getCode());
					}
					if(fsepFic != FsepFic.None) {
						fsepIdentifier.setFsepFic(fsepFic.getCode());
					}
					if(!fsepLocId.isEmpty()) {
						fsepIdentifier.setFsepLocId(fsepLocId);
					}
					
					if(!addInfoMap.isEmpty()) {
						AdditionalInformation additionalInformation = asset.addNewAdditionalInformation();
						for(Map.Entry<String,String> entry : addInfoMap.entrySet()) {
							NameValuePair nameValue = additionalInformation.addNewNameValue();
							nameValue.setName(entry.getKey());
							nameValue.setValue(entry.getValue());
						}
					}
					
					if(faaLocationType != FaaLocationType.NONE) {
						Location location = asset.addNewLocation();
						FaaLocation faaLocation = location.addNewFaaLocation();
						FaaLocationTypeChoice type = faaLocation.addNewType();
						type.setLocationType(faaLocationType.getType());
						faaLocation.setIdentifier(faaLocationValue);
					}
					else if(!state.isBlank() && !city.isBlank()){
						Location location = asset.addNewLocation();
						PhysicalAddress physicalAddress = location.addNewPhysicalAddress();
						PostalAddress postalAddress = physicalAddress.addNewAddress();
						postalAddress.setCity(city);
						postalAddress.setAdministrativeArea(state);
					}
					
					
					
					// - - - - - - - - - - - - - - - - - - - -
					// TODO 
					
//					CageCode cageCode = asset.addNewCageCode();
//					cageCode.setCageCode("c0123");
//					
//					CostCenter costCenterCode = asset.addNewCostCenterCode();
//					costCenterCode.setCostCenterCode("ABCDE");
//					
//					asset.setDescription("Now is the time for all good men");
//					
//					FsepOperationalInformation fsepOperationalInformation = asset.addNewFsepOperationalInformation();
//					FsepCode fsepCode = fsepOperationalInformation.addNewFsepCode();
//					fsepCode.setFsepSystemCode("0");
//					fsepCode.setCapabilityCode("capcode");
//					
//					InventoryInformation inventoryInformation = asset.addNewInventoryInformation();
//					inventoryInformation.setAssetInventoryStatus(CodeManufacturedAssetStatusType.ACTIVE);
//					inventoryInformation.setCondition(CodeConditionType.SURVEY);
//					
//					
					
					Calendar now = Calendar.getInstance(TimeZone.getTimeZone("zulu"));
					
					LoggingEvent loggingEvent = asset.addNewLoggingEvent();
					loggingEvent.setStatus(CodeLogStatusDescriptionType.CLOSED);
					loggingEvent.setLogId(BigInteger.valueOf(885903724));
					loggingEvent.setCreatedDateTime(now);
					loggingEvent.setModifiedDateTime(now);
					loggingEvent.setComment("Return to Service .Battery System 4DCA Quarterly PM completed. Contacted SOC (CMO) .Ready to RTS.");
					
					MaintenanceActivity maintenanceActivity = loggingEvent.addNewMaintenanceActivity();
					maintenanceActivity.setActivityStartDateTime(now);
					maintenanceActivity.setActivityEndDateTime(now);
					
					LoggingCode loggingCode = loggingEvent.addNewLoggingCode();
					CodeDescriptionType maintenanceAction = loggingCode.addNewMaintenanceAction();
					maintenanceAction.setCode("null");
					//CodeDescriptionType category = loggingCode.addNewCategory();
					//category.setCode("LC0");

					
					
					
					
//					MonitoringEvent monitoringEvent = asset.addNewMonitoringEvent();
//					Configuration configuration = monitoringEvent.addNewConfiguration();
//					configuration.setMonitored(MonitoringType.MONITORED);
//					
//					Parameter parameter = monitoringEvent.addNewParameter();
//					ParameterState parameterState = parameter.addNewParameterState();
//					
//					Frequency f = Frequency.Factory.newInstance(xmlOpts);
//					f.setValue(600.0);
//					f.setUom(UomFrequency.GHZ);
//					parameterState.setCurrentValue(f);
					
//					Organization responsibleOrganization = asset.addNewResponsibleOrganization();
//					Person person = responsibleOrganization.addNewOrganizationContact();
//					PersonName personName = person.addNewPersonName();
//					personName.setFirstName("Bob");
//					personName.setLastName("Roberts");
//					
//					AssetRelationship composedOf = asset.addNewComposedOf();
//					
//					AssetRelationship memberOf = asset.addNewMemberOf();
//					
//					AssetRelationship assetRelationship = asset.addNewInterfacesWith();
//					
//					AssetRelationship rmlsAdjacentTo = asset.addNewRmlsAdjacentTo();
//					
//					AssetRelationship rmlsAssociatedWith = asset.addNewRmlsAssociatedWith();
//					
//					AssetRelationship rmlsColocated = asset.addNewRmlsColocated();
//					
//					AssetRelationship rmlsRelatedTo = asset.addNewRmlsRelatedTo();
					
					return assetDoc;
				}

				@Override
				public void build(XmlObject appendTo) {
					XOPS.copyAndAppendNode(build(), appendTo);
				}
				
			};
		}
	}
}
