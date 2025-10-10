package gov.faa.nnew.sa.mmixm;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import aero.mmixm.base.x4.Message;
import aero.mmixm.base.x4.MessageDocument;
import gov.faa.nnew.sa.XmlbeansUtil;

/**
 * Builder for creating MessageDocument document.
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
 * @version 2025.09.18
 * @author Peter V Donato
 */
public interface MessageDocBuilder {

	public MessageDocBuilder setMessageId(String messageId);
	public MessageDocBuilder setMessageType(String messageType);
	public MessageDocBuilder setMessageTimestamp(Date messageTimestamp);

	public MessageDocBuilder setAsset(XmlObject asset);

	public MessageDocument build();
	
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
		public static MessageDocBuilder newInstance(final XmlOptions xmlOpts) {
			return new MessageDocBuilder() {
				private XmlbeansUtil.XmlbeansOps XOPS = gov.faa.nnew.sa.XmlbeansUtil.XmlbeansOps.Factory.newInstance();

				private String messageId = "";
				private String messageType = "";
				private Date messageTimestamp = new Date(0);
				private XmlObject asset = null;
				
				@Override
				public MessageDocBuilder setMessageId(String messageId) {
					this.messageId = messageId;
					return this;
				}

				@Override
				public MessageDocBuilder setMessageType(String messageType) {
					this.messageType = messageType;
					return this;
				}

				@Override
				public MessageDocBuilder setMessageTimestamp(Date messageTimestamp) {
					this.messageTimestamp = messageTimestamp;
					return this;
				}

				@Override
				public MessageDocBuilder setAsset(XmlObject asset) {
					this.asset = asset;
					return this;
				}
				
				@Override
				public MessageDocument build() {
					MessageDocument messageDoc = MessageDocument.Factory.newInstance(xmlOpts);
					Message message = messageDoc.addNewMessage();

					if(!messageId.isBlank()) {
						message.setId(messageId);
					}

					if(!messageType.isBlank()) {
						message.setMessageType(messageType);
					}

					if(messageTimestamp.getTime() > 0) {
						Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("zulu"));
						cal.setTime(messageTimestamp);
						message.setTimestamp(cal);
					}

					if(asset != null) {
						XOPS.copyAndAppendNode(asset, message);
					}

					// - - - - - - - - - - - - - - - - - - - -

					return messageDoc;
				}

				@Override
				public void build(XmlObject appendTo) {
					XOPS.copyAndAppendNode(build(), appendTo);
				}
				
			};
		}
	}
}
