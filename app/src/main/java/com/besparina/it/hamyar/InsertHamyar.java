package com.besparina.it.hamyar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class InsertHamyar {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Activity activity;
	private String hamyarcode;
	private String phonenumber;
	private String Name;
	private String Family;
	private String EducationCode;
	private String Expertise;
	private String acceptcode;
	private String guid;
	private String WsResponse;
	private String BthYear;
	private String BthMonth;
	private String BthDay;
	private String ReagentCode;
	//private String gender;
	private boolean CuShowDialog=true;
	private String[] res;
	//Contractor
	public InsertHamyar(Activity activity, String phonenumber, String acceptcode, String Name, String Family, String EducationCode, String Expertise, String BthYear, String BthMonth, String BthDay,String ReagentCode/*,String gender*/) {
		this.activity = activity;
		this.phonenumber = phonenumber;		
		this.acceptcode=acceptcode;
		this.Name=Name;
		this.Family=Family;
		this.EducationCode=EducationCode;
		this.Expertise=Expertise;
		this.BthYear=BthYear;
		this.BthMonth=BthMonth;
		this.BthDay=BthDay;
		this.ReagentCode=ReagentCode;
		//this.gender=gender;

		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		
		dbh=new DatabaseHelper(this.activity.getApplicationContext());
		try {

			dbh.createDataBase();

   		} catch (IOException ioe) {

   			throw new Error("Unable to create database");

   		}

   		try {

   			dbh.openDataBase();

   		} catch (SQLException sqle) {

   			throw sqle;
   		}
   		
	}
	
	public void AsyncExecute()
	{
		if(IC.isConnectingToInternet()==true)
		{
			try
			{
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			}	
			 catch (Exception e) {
				//Toast.makeText(this.activity.getApplicationContext(), PersianReshape.reshape("ط¹ط¯ظ… ط¯ط³طھط±ط³غŒ ط¨ظ‡ ط³ط±ظˆط±"), Toast.LENGTH_SHORT).show();
	            e.printStackTrace();
			 }
		}
		else
		{
			Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}
	
	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Activity activity;
		
		public AsyncCallWS(Activity activity) {
		    this.activity = activity;
		    this.dialog = new ProgressDialog(activity);		    		    this.dialog.setCanceledOnTouchOutside(false);
		}
		
        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("InsertHamyar");
        	}
	    	catch (Exception e) {
	    		result = e.getMessage().toString();
			}
	        return result;
        }
 
        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
        		res=WsResponse.split("##");
	            if(res[0].toString().compareTo("ER") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(res[0].toString().compareTo("0") == 0)
	            {
	            	Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	         
	            else
	            {
	            	hamyarcode=res[0];
	            	guid=res[1];
	            	InsertDataFromWsToDb(res);
	            }
        	}
        	else
        	{
        		//Toast.makeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
        	}
            try
            {
            	if (this.dialog.isShowing()) {
            		this.dialog.dismiss();
            	}
            }
            catch (Exception e) {}
        }
 
        @Override
        protected void onPreExecute() {
        	if(CuShowDialog)
        	{
        		this.dialog.setMessage("در حال پردازش");
        		this.dialog.show();
        	}
        }
 
        @Override
        protected void onProgressUpdate(Void... values) {
        }
        
    }

	
	public void CallWsMethod(String METHOD_NAME) {
	    //Create request
	    SoapObject request = new SoapObject(PV.NAMESPACE, METHOD_NAME);
	    PropertyInfo phoneNuberPI = new PropertyInfo();
	    //Set Name
	    phoneNuberPI.setName("PhoneNumber");
	    //Set Value
	    phoneNuberPI.setValue(this.phonenumber);
	    //Set dataType
	    phoneNuberPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(phoneNuberPI);
	    PropertyInfo acceptcodePI = new PropertyInfo();
	    //Set Name
	    acceptcodePI.setName("AcceptCode");
	    //Set Value
	    acceptcodePI.setValue(this.acceptcode);
	    //Set dataType
	    acceptcodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(acceptcodePI);	
	    PropertyInfo NamePI = new PropertyInfo();
	    //Set Name
	    NamePI.setName("Name");
	    //Set Value
	    NamePI.setValue(this.Name);
	    //Set dataType
	    NamePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(NamePI);
	    //
	    PropertyInfo FamilyPI = new PropertyInfo();
	    //Set Name
	    FamilyPI.setName("Family");
	    //Set Value
	    FamilyPI.setValue(this.Family);
	    //Set dataType
	    FamilyPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(FamilyPI);	
	    PropertyInfo EducationCodePI = new PropertyInfo();
	    //Set Name
	    EducationCodePI.setName("EducationCode");
	    //Set Value
	    EducationCodePI.setValue(this.EducationCode);
	    //Set dataType
	    EducationCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(EducationCodePI);
	    //
	    PropertyInfo ExpertisePI = new PropertyInfo();
	    //Set Name
	    ExpertisePI.setName("Expertise");
	    //Set Value
	    ExpertisePI.setValue(this.Expertise);
	    //Set dataType
	    ExpertisePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(ExpertisePI);
	    PropertyInfo BthYearPI = new PropertyInfo();
	    //Set Name
	    BthYearPI.setName("BthYear");
	    //Set Value
	    BthYearPI.setValue(this.BthYear);
	    //Set dataType
	    BthYearPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(BthYearPI);	
	    PropertyInfo BthMonthPI = new PropertyInfo();
	    //Set Name
	    BthMonthPI.setName("BthMonth");
	    //Set Value
	    BthMonthPI.setValue(this.BthMonth);
	    //Set dataType
	    BthMonthPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(BthMonthPI);	
	    //
	    PropertyInfo BthDayPI = new PropertyInfo();
	    //Set Name
	    BthDayPI.setName("BthDay");
	    //Set Value
	    BthDayPI.setValue(this.BthDay);
	    //Set dataType
	    BthDayPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(BthDayPI);
	    //
	    PropertyInfo ReagentCodePI = new PropertyInfo();
	    //Set Name
		ReagentCodePI.setName("ReagentCode");
	    //Set Value
		ReagentCodePI.setValue(this.ReagentCode);
	    //Set dataType
		ReagentCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(ReagentCodePI);
//	    //
//	    PropertyInfo genderPI = new PropertyInfo();
//	    //Set Name
//		genderPI.setName("Gender");
//	    //Set Value
//		genderPI.setValue(this.gender);
//	    //Set dataType
//		genderPI.setType(String.class);
//	    //Add the property to request object
//	    request.addProperty(genderPI);
	    
	    //Create envelope
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
	            SoapEnvelope.VER11);
	    envelope.dotNet = true;
	    //Set output SOAP object
	    envelope.setOutputSoapObject(request);
	    //Create HTTP call object
	    HttpTransportSE androidHttpTransport = new HttpTransportSE(PV.URL);
	    try {
	        //Invoke web service
	        androidHttpTransport.call("http://tempuri.org/"+METHOD_NAME, envelope);
	        //Get the response
	        SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
	        //Assign it to FinalResultForCheck static variable
	        WsResponse = response.toString();	
	        if(WsResponse == null) WsResponse="ER";
	    } catch (Exception e) {
	    	WsResponse = "ER";
	    	e.printStackTrace();
	    }
	}
	
	
	public void InsertDataFromWsToDb(String[] AllRecord)
    {
    	String notext="ثبت نشده";
    	String brith=BthYear+"/"+BthMonth+"/"+BthDay;
		db=dbh.getWritableDatabase();	
		db.execSQL("DELETE FROM login");
		db.execSQL("DELETE FROM Profile");
		db.execSQL("INSERT INTO login (hamyarcode,guid,islogin) VALUES('"+hamyarcode+"','"+guid+"','1')");
		db.execSQL("INSERT INTO Profile " +
				"(" +
				"Code" +
				",Name" +
				",Fam" +
				",BthDate" +
				",ShSh" +
				",BirthplaceCode" +
				",Sader" +
				",StartDate" +
				",Address" +
				",Tel" +
				",Mobile" +
				",ReagentName" +
				",AccountNumber" +
				",HamyarNumber" +
				",IsEmrgency" +
				",Status" +
				") " +
				"VALUES" +
				"('"+notext
				+"','"+Name
				+"','"+Family
				+"','"+brith
				+"','"+notext
				+"','"+notext
				+"','"+notext
				+"','"+notext
				+"','"+notext
				+"','"+notext
				+"','"+phonenumber
				+"','"+notext
				+"','"+notext
				+"','"+notext
				+"','"+notext
				+"','0')");
		try {	if (db.isOpen()) {	db.close();	}}	catch (Exception ex){	}
		LoadActivity(MainMenu.class, "guid", guid,"hamyarcode",hamyarcode);
    }
	public void LoadActivity(Class<?> Cls, String VariableName, String VariableValue, String VariableName2, String VariableValue2)
	{
		Intent intent = new Intent(activity,Cls);
		intent.putExtra(VariableName, VariableValue);
		intent.putExtra(VariableName, VariableValue);
		activity.startActivity(intent);
	}	
}
