package com.besparina.it.hamyar;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;

public class SyncGetUserServiceForHamyarUpdated {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
    InternetConnection IC;
	private Context activity;
	private String GUID;
	private String HamyarCode;
	private String UserServiceCode;
	private String WsResponse;
	private boolean CuShowDialog=false;
	//Contractor
	public SyncGetUserServiceForHamyarUpdated(Context activity, String GUID, String HamyarCode, String UserServiceCode,
											  DatabaseHelper dbh,
											  SQLiteDatabase db) {
		this.activity = activity;
		this.GUID = GUID;
		this.HamyarCode = HamyarCode;
		this.UserServiceCode = UserServiceCode;
		this.dbh = dbh;
		this.db = db;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		PublicVariable.theard_GetJobUpdate=false;
//		dbh=new DatabaseHelper(this.activity.getApplicationContext());
//		try {
//
//			dbh.createDataBase();
//
//   		} catch (IOException ioe) {
//			PublicVariable.theard_GetJobUpdate=true;
//   			throw new Error("Unable to create database");
//
//   		}
//
//   		try {
//
//   			dbh.openDataBase();
//
//   		} catch (SQLException sqle) {
//			PublicVariable.theard_GetJobUpdate=true;
//   			throw sqle;
//   		}
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
				 PublicVariable.theard_GetJobUpdate=true;
	            e.printStackTrace();
			 }
		}
		else
		{
			PublicVariable.theard_GetJobUpdate=true;
		}
	}

	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context activity;

		public AsyncCallWS(Context activity) {
		    this.activity = activity;
		    this.dialog = new ProgressDialog(activity);
			this.dialog.setCanceledOnTouchOutside(false);
		}

        @Override
        protected String doInBackground(String... params) {
        	String result = null;
        	try
        	{
        		CallWsMethod("GetUserServiceForHamyarUpdated");
        	}
	    	catch (Exception e) {
				PublicVariable.theard_GetJobUpdate=true;
	    		result = e.getMessage().toString();
			}
	        return result;
        }

        @Override
        protected void onPostExecute(String result) {
        	if(result == null)
        	{
        		PublicVariable.theard_GetJobUpdate=true;
	            if(WsResponse.toString().compareTo("ER") == 0)
	            {
	            	//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else if(WsResponse.toString().compareTo("0") == 0)
	            {
	            	//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
	            }
	            else
	            {
	            	InsertDataFromWsToDb(WsResponse);
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
            catch (Exception e) {
				PublicVariable.theard_GetJobUpdate=true;
			}
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
	    PropertyInfo GUIDPI = new PropertyInfo();
	    //Set Name
		GUIDPI.setName("GUID");
	    //Set Value
		GUIDPI.setValue(GUID);
	    //Set dataType
		GUIDPI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(GUIDPI);
	    //Create envelope
	    PropertyInfo HamyarCodePI = new PropertyInfo();
	    //Set Name
		HamyarCodePI.setName("HamyarCode");
	    //Set Value
		HamyarCodePI.setValue(HamyarCode);
	    //Set dataType
		HamyarCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(HamyarCodePI);
	    PropertyInfo UserServiceCodePI = new PropertyInfo();
	    //Set Name
		UserServiceCodePI.setName("UserServiceCode");
	    //Set Value
		UserServiceCodePI.setValue(UserServiceCode);
	    //Set dataType
		UserServiceCodePI.setType(String.class);
	    //Add the property to request object
	    request.addProperty(UserServiceCodePI);
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


	public void InsertDataFromWsToDb(String AllRecord)
    {
		String[] res;
		String[] value;
		String GenderStr="";
		try {	if (!db.isOpen()) {	db = dbh.getReadableDatabase();	}}	catch (Exception ex){	db = dbh.getReadableDatabase();	}
		Cursor cursor=db.rawQuery("SELECT * FROM Profile",null);
		if(cursor.getCount()>0)
		{
			cursor.moveToNext();
			GenderStr=cursor.getString(cursor.getColumnIndex("Gender"));
		}
		db.close();
		res=WsResponse.split("@@");
		db=dbh.getWritableDatabase();
		for(int i=0;i<res.length;i++) {
			value = res[i].split("##");
			if (value[34].compareTo("0") != 0 ) {
				if(!db.isOpen())
				{
					db=dbh.getWritableDatabase();
				}
				db.execSQL("DELETE FROM BsUserServices WHERE Code=" + value[0]);
				db.close();
			} else {
				if (GenderStr.compareTo("1") == 0) {
					if (GenderStr.compareTo(value[5]) != 0 && (value[21].compareTo("0") == 0 || value[21].compareTo("") == 0)) {
						if(!db.isOpen())
						{
							db=dbh.getWritableDatabase();
						}
						db.execSQL("DELETE FROM BsUserServices WHERE Code=" + value[0]);
						db.close();
					} else {
						Update(value);
					}
				} else {
					if (GenderStr.compareTo(value[6]) != 0 && (value[21].compareTo("0") == 0 || value[21].compareTo("") == 0)) {
						if(!db.isOpen())
						{
							db=dbh.getWritableDatabase();
						}
						db.execSQL("DELETE FROM BsUserServices WHERE Code=" + value[0]);
						db.close();
					} else {
						Update(value);
					}
				}

			}
		}
		if(db.isOpen()) {
			db.close();
		}
    }
	private void Update(String[] value)
	{
		db=dbh.getWritableDatabase();
		String query = "UPDATE  BsUserServices SET " +
				"UserCode='" + value[1] +
				"' , UserName='" + value[2] +
				"' , UserFamily='" + value[3] +
				"' , ServiceDetaileCode='" + value[4] +
				"' , MaleCount='" + value[5] +
				"' , FemaleCount='" + value[6] +
				"' , StartDate='" + value[7] +
				"' , EndDate='" + value[8] +
				"' , AddressCode='" + value[9] +
				"' , AddressText='" + value[10] +
				"' , Lat='" + value[11] +
				"' , Lng='" + value[12] +
				"' , City='" + value[13] +
				"' , State='" + value[14] +
				"' , Description='" + value[15] +
				"' , IsEmergency='" + value[16] +
				"' , InsertUser='" + value[17] +
				"' , InsertDate='" + value[18] +
				"' , StartTime='" + value[19] +
				"' , EndTime='" + value[20] +
				"' , HamyarCount='" + value[21] +
				"' , PeriodicServices='" + value[22] +
				"' , EducationGrade='" + value[23] +
				"' , FieldOfStudy='" + value[24] +
				"' , StudentGender='" + value[25] +
				"' , TeacherGender='" + value[26] +
				"' , EducationTitle='" + value[27] +
				"' , ArtField='" + value[28] +
				"' , CarWashType='" + value[29] +
				"' , CarType='" + value[30] +
				"' , Language='" + value[31] + "' WHERE Code='" + value[0] + "'";
		db.execSQL(query);
		if(db.isOpen()) {
			db.close();
		}
	}
}
