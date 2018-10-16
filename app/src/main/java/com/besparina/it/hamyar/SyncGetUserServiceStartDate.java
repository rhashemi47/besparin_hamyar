package com.besparina.it.hamyar;

import android.app.ProgressDialog;
import android.content.Context;
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

public class SyncGetUserServiceStartDate {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
	InternetConnection IC;
	private Context activity;
	private String WsResponse;
	private String pUserServiceCode;
	private boolean CuShowDialog = false;

	//Contractor
	public SyncGetUserServiceStartDate(Context activity, String pUserServiceCode) {
		this.activity = activity;
		this.pUserServiceCode = pUserServiceCode;

		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		PublicVariable.theard_GetUserServiceStartDate=false;
		dbh = new DatabaseHelper(this.activity.getApplicationContext());
		try {

			dbh.createDataBase();

		} catch (IOException ioe) {
			PublicVariable.theard_GetUserServiceStartDate=true;
			throw new Error("Unable to create database");

		}

		try {

			dbh.openDataBase();

		} catch (SQLException sqle) {
			PublicVariable.theard_GetUserServiceStartDate=true;
			throw sqle;
		}
	}

	public void AsyncExecute() {
		if (IC.isConnectingToInternet() == true) {
			try {
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			} catch (Exception e) {
				PublicVariable.theard_GetUserServiceStartDate=true;
				//akeText(this.activity.getApplicationContext(), PersianReshape.reshape("ط¹ط¯ظ… ط¯ط³طھط±ط³غŒ ط¨ظ‡ ط³ط±ظˆط±"), Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		} else {
			PublicVariable.theard_GetUserServiceStartDate=true;
		}
	}

	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context activity;

		public AsyncCallWS(Context activity) {
			this.activity = activity;
			this.dialog = new ProgressDialog(activity);		    this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				CallWsMethod("GetUserServiceStartDate");
			} catch (Exception e) {
				PublicVariable.theard_GetUserServiceStartDate=true;
				result = e.getMessage().toString();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {
				PublicVariable.theard_GetUserServiceStartDate=true;
				if (WsResponse.toString().compareTo("ER") == 0) {
					//akeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
				}
				else if (WsResponse.toString().compareTo("0") == 0) {
					//akeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
				}
				else if (WsResponse.toString().compareTo("2") == 0) {
					//akeText(this.activity.getApplicationContext(), "کاربر شناسایی نشد", Toast.LENGTH_LONG).show();
				}
				else
				{
					InsertDataFromWsToDb(WsResponse);
				}
			} else {
				//akeText(this.activity, "ط®ط·ط§ ط¯ط± ط§طھطµط§ظ„ ط¨ظ‡ ط³ط±ظˆط±", Toast.LENGTH_SHORT).show();
			}
			try {
				if (this.dialog.isShowing()) {
					this.dialog.dismiss();
				}
			} catch (Exception e) {
				PublicVariable.theard_GetUserServiceStartDate=true;
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

	public void CallWsMethod(String METHOD_NAME) {
		//Create request
		SoapObject request = new SoapObject(PV.NAMESPACE, METHOD_NAME);
		PropertyInfo pUserServiceCodePI = new PropertyInfo();
		//Set Name
		pUserServiceCodePI.setName("pUserServiceCode");
		//Set Value
		pUserServiceCodePI.setValue(pUserServiceCode);
		//Set dataType
		pUserServiceCodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(pUserServiceCodePI);
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
		res = WsResponse.split("@@");
		db = dbh.getWritableDatabase();
		for (int i = 0; i < res.length; i++) {
			value = res[i].split("##");
			try {
				String query = "INSERT INTO StartDateService (" +
						"Code," +
						"BsUserServiceCode," +
						"HamyarCode," +
						"StartDate," +
						"UserCode," +
						"UserConfirmDate) VALUES('" +
						value[0] + "','" +
						value[1] + "','" +
						value[2] + "','" +
						value[3] + "','" +
						value[4] + "','" +
						value[5] + "')";
				db.execSQL(query);
				String message = "برای سرویس به شماره: " + value[1] + "اعلام شروع به کار تایید شده است";
				runNotification("بسپارینا", message, i, value[1], ViewJob.class);
			}
			catch (Exception ex)
			{
				PublicVariable.theard_GetUserServiceStartDate=true;
			}
		}
		db.close();
	}
	public void runNotification(String title,String detail,int id,String OrderCode,Class<?> Cls)
	{
		NotificationClass notifi=new NotificationClass(this.activity);
		notifi.Notificationm(this.activity,title,detail,OrderCode,"0",id,Cls);
	}

}

