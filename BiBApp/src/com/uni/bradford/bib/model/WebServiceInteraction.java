package com.uni.bradford.bib.model;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Class to deal with Web service interaction
 * 
 * @author Martin
 */
public class WebServiceInteraction
{
	private DataModel dataModel;
	
	private static final String NAME_SPACE = "http://bib.service.code/";
	private static final String URL = "http://vmjsp1.inf.brad.ac.uk:8080/BIBService/BIBWebService?wsdl";
//	private static final String URL = "http://medicalxtra.cloudapp.net/BIBService/BIBWebService?wsdl";
//	private static final String URL = "http://192.168.137.1:8084/BIBService/BIBWebService?wsdl"; // TODO: Switch and adjustfor live demo
	 
	private static final String GET_NEW_VERSION_DATE = "GetNewVersionDate";
	private static final String GET_NEW_VERSION_DATE_RESPONSE = "GetNewVersionDateResponse";
	private static final String GET_RECIPIENT_MAIL = "GetRecipientMail";
	private static final String GET_RECIPIENT_MAIL_RESPONSE = "GetRecipientMailResponse";
	private static final String GET_SURVEY_URL = "GetSurveyUrl";
	private static final String GET_SURVEY_URL_RESPONSE = "GetSurveyUrlResponse";
	private static final String GET_MOTHER_BY_ID = "GetMotherByID";
	private static final String GET_MOTHER_BY_ID_RESPONSE = "GetMotherByIDResponse";
	private static final String GET_CHILD_ID_BY_MOTHER_ID = "GetChildIDbyMotherID"; 
	private static final String GET_CHILD_ID_BY_MOTHER_ID_RESPONSE = "GetChildIDbyMotherIDResponse";
	private static final String GET_CHILD_GROWTH_DATA_BY_ID = "ChildGrowthData"; // TODO: S.. same var chemata please
	private static final String GET_CHILD_GROWTH_DATA_BY_ID_RESPONSE = "ChildGrowthDataResponse"; // TODO: S.. same var chemata please
	private static final String GET_ALL_INFO_BY_MOTHER_ID = "GetAllInfoByMotherID";
	private static final String GET_ALL_INFO_BY_MOTHER_ID_RESPONSE = "GetAllInfoByMotherIDResponse";
	
	/**
	 * Init Web service
	 * 
	 * @param dataModel Data model reference
	 */
	public WebServiceInteraction(DataModel dataModel)
	{
		this.dataModel = dataModel;
	}
	
	/**
	 * Build request to ask for information about recipient for forgotten login id requests
	 */
	public boolean getRecipientMail()
	{
		return sendReceiveSoapMessage(GET_RECIPIENT_MAIL, null);
	}
	
	/**
	 * Build request to ask for information about the current survey link
	 */
	public boolean getSurveyUrl()
	{
		return sendReceiveSoapMessage(GET_SURVEY_URL, null);
	}
	
	/**
	 * Build request to ask for information about time of last update
	 */
	public boolean getLastUpdate()
	{
		return sendReceiveSoapMessage(GET_NEW_VERSION_DATE, null);
	}
	
	/**
	 * Build request to ask for information about mother
	 * 
	 * @param loginId Login ID to identify mother
	 * @param phoneId Phone ID to identify phone
	 */
	public boolean getMotherById(String loginId, String phoneId)
	{
		// Construct request to ask for information about a particular mother
		ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();		
		
		PropertyInfo tempPropertyInfo = new PropertyInfo();
		tempPropertyInfo.setName("Token");
		tempPropertyInfo.setValue(loginId);
		propertyInfos.add(tempPropertyInfo);
		
		tempPropertyInfo = new PropertyInfo();
		tempPropertyInfo.setName("PhoneID");
		tempPropertyInfo.setValue(phoneId);
		propertyInfos.add(tempPropertyInfo);
		
		return sendReceiveSoapMessage(GET_MOTHER_BY_ID, propertyInfos);
	}
	
	/**
	 * Build request to ask for information about children
	 * 
	 * @param loginId Login ID to identify mother
	 */
	public boolean getChildIdByMotherId(String loginId)
	{
		// Construct request to ask for information about a mothers children
		ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();		
		
		PropertyInfo tempPropertyInfo = new PropertyInfo();
		tempPropertyInfo.setName("MotherID");
		tempPropertyInfo.setValue(loginId);
		propertyInfos.add(tempPropertyInfo);
		
		return sendReceiveSoapMessage(GET_CHILD_ID_BY_MOTHER_ID, propertyInfos);
	}
	
	/**
	 * Build request to ask for information about a particular child
	 * 
	 * @param childId Child ID to identify a particular child
	 */
	public boolean getChildGrowthById(String childId)
	{
		// Construct request to ask for information about a particular child
		ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();
		
		PropertyInfo tempPropertyInfo = new PropertyInfo();
		tempPropertyInfo.setName("ChildID");
		tempPropertyInfo.setValue(childId);
		propertyInfos.add(tempPropertyInfo);
		
		return sendReceiveSoapMessage(GET_CHILD_GROWTH_DATA_BY_ID, propertyInfos);
	}
	
	/**
	 * Build request to ask for information about average child of particular sex
	 * 
	 * @param sex Specifies male or female
	 */
	public boolean getAverageChildBySex(String sex)
	{
		// Construct request to ask for information average child with particular sex
		ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();		
		
		PropertyInfo tempPropertyInfo = new PropertyInfo();
		tempPropertyInfo.setName("sex");
		tempPropertyInfo.setValue(sex);
		propertyInfos.add(tempPropertyInfo);
		
		return sendReceiveSoapMessage("GetAverageChildBySex", propertyInfos);
	}
	
	/**
	 * Build request to ask for all information for a mother
	 * 
	 * @param loginId Login ID to identify mother
	 * @param phoneId Phone ID to identify phone
	 */
	public boolean getAllInfoByMotherID(String loginId, String phoneId)
	{
		// Construct request to ask for all information for a particular mother
		ArrayList<PropertyInfo> propertyInfos = new ArrayList<PropertyInfo>();		
		
		PropertyInfo tempPropertyInfo = new PropertyInfo();
		tempPropertyInfo.setName("Token");
		tempPropertyInfo.setValue(loginId);
		propertyInfos.add(tempPropertyInfo);
		
		tempPropertyInfo = new PropertyInfo();
		tempPropertyInfo.setName("PhoneID");
		tempPropertyInfo.setValue(phoneId);
		propertyInfos.add(tempPropertyInfo);
		
		return sendReceiveSoapMessage(GET_ALL_INFO_BY_MOTHER_ID, propertyInfos);
	}
	
	/**
	 * Use pre-build request with parameters to send soap request 
	 * 
	 * @param requestType Typ of the request
	 * @param propertyInfos Parameters for the request
	 */
	private boolean sendReceiveSoapMessage(String requestType, ArrayList<PropertyInfo> propertyInfos)
	{
		// Construct soap message for request
		SoapObject request = new SoapObject(NAME_SPACE, requestType);

		// Pass arguments to request
		if (propertyInfos != null)
		{
			for (PropertyInfo propertyInfo : propertyInfos)
			{
				request.addProperty(propertyInfo);
			}
		}

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
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
			System.out.println("Request " + request);
			System.out.println("Envelope " + envelope);
			System.out.println("httpTransportSE " + httpTransportSE);
			System.out.println("Soap action " + request.getNamespace() + request.getName());
			System.out.println("RequestDump is :"+httpTransportSE.requestDump);
			System.out.println("ResponseDump is :"+httpTransportSE.responseDump);
			
			// Prepare soap message for response
			response = (SoapObject)envelope.bodyIn; 
		} 
		catch (XmlPullParserException | IOException e) 
		{
			System.out.println("XmlPullParserException or IOException");
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
			case GET_NEW_VERSION_DATE_RESPONSE:
			{
				if (response.getPropertyCount() == 0)
				{
					// Something went wrong
					return false;
				}
				
				String updateDate = response.getProperty("return").toString();
				
				// Convert date string to time in milliseconds
				try
				{
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date date = format.parse(updateDate);
					long milliseconds = date.getTime();
					dataModel.setLastUpdate(milliseconds);
				} 
				catch (ParseException e)
				{
					e.printStackTrace();
				}
				
				
				break;
			}
			case GET_RECIPIENT_MAIL_RESPONSE:
			{
				if (response.getPropertyCount() == 0)
				{
					// Something went wrong
					return false;
				}
				
				dataModel.setRecipientMail(response.getProperty("return").toString());
				
				break;
			}
			case GET_SURVEY_URL_RESPONSE:
			{
				if (response.getPropertyCount() == 0)
				{
					// Something went wrong
					return false;
				}
				
				dataModel.setSurveyUrl(response.getProperty("return").toString());
				
				break;
			}
			case GET_MOTHER_BY_ID_RESPONSE:
			{
				if (response.getPropertyCount() == 0)
				{
					// Wrong loginId phoneId constellation
					return false;
				}
				
				SoapObject soapObject = (SoapObject)response.getProperty(0);
				
				// Just for debug.. 
				System.out.println("id: " + soapObject.getProperty("id").toString());
				System.out.println("phoneID: " + soapObject.getProperty("phoneID").toString());
				System.out.println("PrimaryCare: " + soapObject.getProperty("primaryCare").toString());

				// Add result to data model
				dataModel.setMother(new Mother(soapObject.getProperty("id").toString(), 
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
					
					// Just for debug.. 
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
					System.out.println("-------------------");
					
					// Add result to data model
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
				
					// Just for debug.. 
					System.out.println("childID: " + soapObject.getProperty("childID").toString());
					System.out.println("source: " + soapObject.getProperty("source").toString());
					System.out.println("AgeDays: " + soapObject.getProperty("ageDays").toString());
					System.out.println("Weight: " + soapObject.getProperty("weight").toString());
					System.out.println("Height: " + soapObject.getProperty("height").toString());
//					System.out.println("BMI: " + soapObject.getProperty("BMI").toString());
					System.out.println("-------------------");
					
					if (!soapObject.getProperty("height").toString().equals("0.0"))
					{
						// We only want entries with height value					
						int pos = dataModel.getMother().getChildPos(soapObject.getProperty("childID").toString());
					
						// Add result to data model
						dataModel.getMother().getChild(pos).addChildData(new ChildData(soapObject.getProperty("source").toString(), 
																				   Integer.parseInt(soapObject.getProperty("ageDays").toString()), 
																				   Double.parseDouble(soapObject.getProperty("weight").toString()),
																				   Double.parseDouble(soapObject.getProperty("height").toString()),
																				   /*Double.parseDouble(soapObject.getProperty("BMI").toString())*/ 0.0 ));
					}
				}	
				
				break;
			}
			case GET_ALL_INFO_BY_MOTHER_ID_RESPONSE:
			{
				System.out.println("Do nothing.. not part of the prototype");
				
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
