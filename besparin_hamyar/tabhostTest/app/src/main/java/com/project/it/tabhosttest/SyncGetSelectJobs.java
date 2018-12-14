package com.besparina.it.hamyar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
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

public class SyncGetSelectJobs {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
	InternetConnection IC;
	private Activity activity;
	private String guid;
	private String hamyarcode;
	private String WsResponse;
	private String LastHamyarSelectUserServiceCode;
	//private String acceptcode;
	private boolean CuShowDialog = false;

	//Contractor
	public SyncGetSelectJobs(Activity activity, String guid, String hamyarcode, String LastHamyarSelectUserServiceCode) {
		this.activity = activity;
		this.guid = guid;
		this.LastHamyarSelectUserServiceCode = LastHamyarSelectUserServiceCode;
		this.hamyarcode = hamyarcode;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();

		dbh = new DatabaseHelper(this.activity.getApplicationContext());
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

	public void AsyncExecute() {
		if (IC.isConnectingToInternet() == true) {
			try {
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			} catch (Exception e) {

				e.printStackTrace();
			}
		} else {
			Toast.makeText(this.activity.getApplicationContext(), "لطفا ارتباط شبکه خود را چک کنید", Toast.LENGTH_SHORT).show();
		}
	}

	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Activity activity;

		public AsyncCallWS(Activity activity) {
			this.activity = activity;
			this.dialog = new ProgressDialog(activity);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				CallWsMethod("GetHamyarUserServiceSelect");
			} catch (Exception e) {
				result = e.getMessage().toString();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				if (WsResponse.toString().compareTo("ER") == 0) {
					Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
				} else if (WsResponse.toString().compareTo("0") == 0) {
					//Toast.makeText(this.activity.getApplicationContext(), "سرویسی اعلام نشده", Toast.LENGTH_LONG).show();
					//LoadActivity(MainActivity.class, "guid", guid,"hamyarcode",hamyarcode,"updateflag","1");
				} else if (WsResponse.toString().compareTo("2") == 0) {
					Toast.makeText(this.activity.getApplicationContext(), "همیار شناسایی نشد!", Toast.LENGTH_LONG).show();
				} else {
					InsertDataFromWsToDb(WsResponse);
				}
			} else {
				//Toast.makeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
			}
			try {
				if (this.dialog.isShowing()) {
					this.dialog.dismiss();
				}
			} catch (Exception e) {
			}
		}

		@Override
		protected void onPreExecute() {
			if (CuShowDialog) {
				this.dialog.setMessage("در حال پردازش");
				this.dialog.show();
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}

	}

	String LastNewsId;

	public void LoadMaxNewId() {
		db = dbh.getReadableDatabase();
		Cursor cursors = db.rawQuery("select IFNULL(max(id),0)MID from news", null);
		if (cursors.getCount() > 0) {
			cursors.moveToNext();
			LastNewsId = cursors.getString(cursors.getColumnIndex("MID"));
		}
	}

	public void CallWsMethod(String METHOD_NAME) {
		//Create request
		SoapObject request = new SoapObject(PV.NAMESPACE, METHOD_NAME);
		PropertyInfo GuidPI = new PropertyInfo();
		//Set Name
		GuidPI.setName("GUID");
		//Set Value
		GuidPI.setValue(this.guid);
		//Set dataType
		GuidPI.setType(String.class);
		//Add the property to request object
		request.addProperty(GuidPI);
		//*****************************************************
		PropertyInfo HamyarCodePI = new PropertyInfo();
		//Set Name
		HamyarCodePI.setName("HamyarCode");
		//Set Value
		HamyarCodePI.setValue(this.hamyarcode);
		//Set dataType
		HamyarCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(HamyarCodePI);
		//*****************************************************
		PropertyInfo LastUserServiceCodePI = new PropertyInfo();
		//Set Name
		LastUserServiceCodePI.setName("LastHamyarUserServiceCode");
		//Set Value
		LastUserServiceCodePI.setValue(this.LastHamyarSelectUserServiceCode);
		//Set dataType
		LastUserServiceCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(LastUserServiceCodePI);
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
			androidHttpTransport.call("http://tempuri.org/" + METHOD_NAME, envelope);
			//Get the response
			SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
			//Assign it to FinalResultForCheck static variable
			WsResponse = response.toString();
			if (WsResponse == null) WsResponse = "ER";
		} catch (Exception e) {
			WsResponse = "ER";
			e.printStackTrace();
		}
	}


	public void InsertDataFromWsToDb(String AllRecord) {
		String[] res;
		String[] value;
		String query = null;
		String LastHamyarUserServiceCode = null;
		res = WsResponse.split("@@");
		try {	if (!db.isOpen()) {	db = dbh.getWritableDatabase();	}}	catch (Exception ex){	db = dbh.getWritableDatabase();	}
		for (int i = 0; i < res.length; i++) {
			value = res[i].split("##");
			query = "INSERT INTO BsHamyarSelectServices (Code," +
					"UserCode" +
					",UserName" +
					",UserFamily" +
					",ServiceDetaileCode" +
					",MaleCount" +
					",FemaleCount" +
					",StartDate" +
					",EndDate" +
					",AddressCode" +
					",AddressText" +
					",Lat" +
					",Lng" +
					",City" +
					",State" +
					",Description" +
					",IsEmergency," +
					"InsertUser" +
					",InsertDate,StartTime,EndTime,IsDelete,Status) VALUES('" + value[0] +
					"','" + value[1] +
					"','" + value[2] +
					"','" + value[3] +
					"','" + value[4] +
					"','" + value[5] +
					"','" + value[6] +
					"','" + value[7] +
					"','" + value[8] +
					"','" + value[9] +
					"','" + value[10] +
					"','" + value[11] +
					"','" + value[12] +
					"','" + value[13] +
					"','" + value[14] +
					"','" + value[15] +
					"','" + value[16] +
					"','" + value[17] +
					"','" + value[18] +
					"','" + value[19] +
					"','" + value[20] +
					"','0','1')";
			db.execSQL(query);
		}
		Cursor cursors = db.rawQuery("SELECT ifnull(MAX(code),0)as code FROM BsHamyarSelectServices", null);
		if(cursors.getCount()>0)
		{
			cursors.moveToNext();
			LastHamyarUserServiceCode=cursors.getString(cursors.getColumnIndex("code"));
		}
		SyncJobs jobs=new SyncJobs(this.activity, guid,hamyarcode,LastHamyarUserServiceCode);
		jobs.AsyncExecute();
	}
}
