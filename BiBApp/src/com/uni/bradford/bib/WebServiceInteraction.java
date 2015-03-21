package com.uni.bradford.bib;

import java.io.IOException;
import java.util.ArrayList;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


public class WebServiceInteraction
{
	private DataModel dataModel;
	
	private static final String NAME_SPACE = "http://bib.service.code/";
	private static final String URL = "http://medicalxtra.cloudapp.net/BIBService/BIBWebService?wsdl";
	
	private static final String GET_MOTHER_BY_ID = "GetMotherByID";
	private static final String GET_MOTHER_BY_ID_RESPONSE = "GetMotherByIDResponse";
	private static final String GET_CHILD_ID_BY_MOTHER_ID = "getChildIDbyMotherID"; // TODO: Stella.. big "G" please
	private static final String GET_CHILD_ID_BY_MOTHER_ID_RESPONSE = "getChildIDbyMotherIDResponse"; // TODO: Stella.. big "G" please
	private static final String GET_CHILD_GROWTH_DATA_BY_ID = "childgrowthdata"; // TODO: Stella.. same var chemata please
	private static final String GET_CHILD_GROWTH_DATA_BY_ID_RESPONSE = "childgrowthdataResponse"; // TODO: Stella.. same var chemata please
	
	public WebServiceInteraction(DataModel dataModel)
	{
		this.dataModel = dataModel;
	}
	
	public boolean getMotherById(String loginId, String phoneId)
	{
		ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();		
		
		PropertyInfo tempPropertyInfo = new PropertyInfo();
		tempPropertyInfo.setName("arg0");
		tempPropertyInfo.setValue(loginId);
		propertyInfos.add(tempPropertyInfo);
		
		tempPropertyInfo = new PropertyInfo();
		tempPropertyInfo.setName("arg1");
		tempPropertyInfo.setValue(phoneId);
		propertyInfos.add(tempPropertyInfo);
		
		return sendReceiveSoapMessage(GET_MOTHER_BY_ID, propertyInfos);
	}
	
	public boolean getChildIdByMotherId(String loginId)
	{
		ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();		
		
		PropertyInfo tempPropertyInfo = new PropertyInfo();
		tempPropertyInfo.setName("arg0");
		tempPropertyInfo.setValue(loginId);
		propertyInfos.add(tempPropertyInfo);
		
		return sendReceiveSoapMessage(GET_CHILD_ID_BY_MOTHER_ID, propertyInfos);
	}
	
	public boolean getChildGrowthById(String childId)
	{
		ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();
		
		PropertyInfo tempPropertyInfo = new PropertyInfo();
		tempPropertyInfo.setName("arg0");
		tempPropertyInfo.setValue(childId);
		propertyInfos.add(tempPropertyInfo);
		
		return sendReceiveSoapMessage(GET_CHILD_GROWTH_DATA_BY_ID, propertyInfos);
	}
	
	private boolean sendReceiveSoapMessage(String requestType, ArrayList<PropertyInfo> propertyInfos)
	{
		// Construct soap message for request
		SoapObject request = new SoapObject(NAME_SPACE, requestType);

		// Pass arguments to request
		for (PropertyInfo propertyInfo : propertyInfos)
		{
			request.addProperty(propertyInfo);
		}

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); // TODO: Which version Stella?
		envelope.setOutputSoapObject(request);

		HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
		
		
		// Allow to see constructed soap messages (with httpTransportSE.requestDump)
		httpTransportSE.debug=true; 

		SoapObject response = null;
		try  
		{
			// Workaround to deal with the EOFException problem.. might disappear in new version of ksoap2-android lib
			ArrayList<HeaderProperty> headerPropertyArrayList = new ArrayList<HeaderProperty>();
			headerPropertyArrayList.add(new HeaderProperty("Connection", "close"));
			
			// This is will call the WebService
			httpTransportSE.call (request.getNamespace() + request.getName(), envelope, headerPropertyArrayList);

			// Debug output
//			System.out.println("Request " + request);
//			System.out.println("Envelope " + envelope);
//			System.out.println("httpTransportSE " + httpTransportSE);
//			System.out.println("Soap action " + request.getNamespace() + request.getName());
//			System.out.println("RequestDump is :"+httpTransportSE.requestDump);
//			System.out.println("ResponseDump is :"+httpTransportSE.responseDump);
			
			// Prepare soap message for response
			response = (SoapObject)envelope.bodyIn; 
		} 
		catch (XmlPullParserException | IOException e) 
		{
			System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"); // TODO: Debug
			e.printStackTrace();
		} 

		// Received valid response?
		if (response == null)
		{
			System.out.println("No valid response!");

			return false;
		}

		// Check for particular response type
		switch (response.getName())
		{
			case GET_MOTHER_BY_ID_RESPONSE:
			{
				if (response.getPropertyCount() == 0)
				{
					// Wrong loginId phoneId constellation
					return false;
				}
				
				SoapObject soapObject =  (SoapObject)response.getProperty(0);
				
				// TODO: Just for debug.. 
				System.out.println("ID: " + soapObject.getProperty("ID").toString());
				System.out.println("PhoneID: " + soapObject.getProperty("phoneID").toString()); // TODO: Stella.. why emty?
				System.out.println("PrimaryCare: " + soapObject.getProperty("primaryCare").toString());

				dataModel.setMother(new Mother(soapObject.getProperty("ID").toString(), 
											   "?empty?", 
											   soapObject.getProperty("phoneID").toString(),
											   Boolean.parseBoolean(soapObject.getProperty("primaryCare").toString())));
				
				break;
			}
			case GET_CHILD_ID_BY_MOTHER_ID_RESPONSE:
			{
				if (response.getPropertyCount() == 0)
				{
					// Something went wrong
					return false;
				}
				
				for (int index = 0; index < response.getPropertyCount(); index++)
				{
					SoapObject soapObject =  (SoapObject)response.getProperty(index);
					
					// TODO: Just for debug.. 
					System.out.println("Child ID: " + soapObject.getProperty("childID").toString());
					System.out.println("MotherID: " + soapObject.getProperty("motherID").toString());
					System.out.println("Pregnancy: " + soapObject.getProperty("pregnancy").toString());
					System.out.println("BirthOrder: " + soapObject.getProperty("birthOrder").toString());
					System.out.println("GenderID: " + soapObject.getProperty("genderID").toString());
					System.out.println("MonthOfBirth: " + soapObject.getProperty("monthOfBirth").toString());
					System.out.println("YearOfBirth: " + soapObject.getProperty("yearOfBirth").toString());
					System.out.println("MaternalBaseQ: " + soapObject.getProperty("maternalBaseQ").toString());
					System.out.println("Eclipse: " + soapObject.getProperty("eclipse").toString());
					System.out.println("PrimaryCare: " + soapObject.getProperty("primaryCare").toString());
					System.out.println("education: " + soapObject.getProperty("education").toString());
					System.out.println("BIB1000: " + soapObject.getProperty("BIB1000").toString());
					System.out.println("MeDALL: " + soapObject.getProperty("meDALL").toString());
					System.out.println("ALL_IN: " + soapObject.getProperty("ALL_IN").toString());
					System.out.println("Dateofbirth: " + soapObject.getProperty("dateofbirth").toString()); // TODO: Stella: Why twice?
					System.out.println("-------------------");
					
					dataModel.getMother().addChild(new Child(soapObject.getProperty("childID").toString(), 
															 soapObject.getProperty("pregnancy").toString(), 
															 Short.parseShort(soapObject.getProperty("birthOrder").toString()), 
															 Short.parseShort(soapObject.getProperty("genderID").toString()), 
															 Integer.parseInt(soapObject.getProperty("monthOfBirth").toString()), 
															 Integer.parseInt(soapObject.getProperty("yearOfBirth").toString()), 
															 Integer.parseInt(soapObject.getProperty("maternalBaseQ").toString()), 
															 Integer.parseInt(soapObject.getProperty("eclipse").toString()), 
															 Integer.parseInt(soapObject.getProperty("primaryCare").toString()), 
															 Integer.parseInt(soapObject.getProperty("education").toString()),
															 Integer.parseInt(soapObject.getProperty("BIB1000").toString()), 
															 Integer.parseInt(soapObject.getProperty("meDALL").toString()), 
															 Integer.parseInt(soapObject.getProperty("ALL_IN").toString())));
				}
				break;
			}
			case GET_CHILD_GROWTH_DATA_BY_ID_RESPONSE:
			{
				if (response.getPropertyCount() == 0)
				{
					// Something went wrong
					return false;
				}
				
				for (int index = 0; index < response.getPropertyCount(); index++)
				{
					SoapObject soapObject =  (SoapObject)response.getProperty(index);
				
					// TODO: Just for debug.. 
					System.out.println("childID: " + soapObject.getProperty("childID").toString());
					System.out.println("source: " + soapObject.getProperty("source").toString());
					System.out.println("AgeDays: " + soapObject.getProperty("ageDays").toString());
					System.out.println("Weight: " + soapObject.getProperty("weight").toString());
					System.out.println("Height: " + soapObject.getProperty("height").toString());
					System.out.println("BMI: " + soapObject.getProperty("BMI").toString());
					System.out.println("-------------------");
					
					int pos = dataModel.getMother().getChildPos(soapObject.getProperty("childID").toString());
					
					dataModel.getMother().getChild(pos).addChildData(new ChildData(soapObject.getProperty("source").toString(), 
																				   Integer.parseInt(soapObject.getProperty("ageDays").toString()), 
																				   Double.parseDouble(soapObject.getProperty("weight").toString()),
																				   Double.parseDouble(soapObject.getProperty("height").toString()),
																				   Double.parseDouble(soapObject.getProperty("BMI").toString())));
				}
				
				break;
			}
			default:
			{
				System.out.println("Unknown SOAP response");
				
				return false;
			}
		}  
		
		return true;
	}
}
