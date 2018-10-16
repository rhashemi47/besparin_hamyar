package com.besparina.it.hamyar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

public class SyncGetInvoiceAccept {

	//Primary Variable
	DatabaseHelper dbh;
	SQLiteDatabase db;
	PublicVariable PV;
	InternetConnection IC;
	private Context activity;
	private String guid;
	private String hamyarcode;
	private String WsResponse;
	private String invoicecode;
	private String ServiceCode;
	//private String acceptcode;
	private boolean CuShowDialog = false;

	//Contractor
	public SyncGetInvoiceAccept(Context activity, String guid, String hamyarcode, String invoicecode, String ServiceCode) {
		this.activity = activity;
		this.guid = guid;
		this.invoicecode = invoicecode;
		this.ServiceCode = ServiceCode;
		this.hamyarcode = hamyarcode;
		IC = new InternetConnection(this.activity.getApplicationContext());
		PV = new PublicVariable();
		PublicVariable.theard_GetFactorAccept=false;
		dbh = new DatabaseHelper(this.activity.getApplicationContext());
		try {

			dbh.createDataBase();

		} catch (IOException ioe) {
			PublicVariable.theard_GetFactorAccept=true;
			throw new Error("Unable to create database");

		}

		try {

			dbh.openDataBase();

		} catch (SQLException sqle) {
			PublicVariable.theard_GetFactorAccept=true;
			throw sqle;
		}
	}

	public void AsyncExecute() {
		if (IC.isConnectingToInternet() == true) {
			try {
				AsyncCallWS task = new AsyncCallWS(this.activity);
				task.execute();
			} catch (Exception e) {
				PublicVariable.theard_GetFactorAccept=true;
				e.printStackTrace();
			}
		} else {
			PublicVariable.theard_GetFactorAccept=true;
		}
	}

	//Async Method
	private class AsyncCallWS extends AsyncTask<String, Void, String> {
		private ProgressDialog dialog;
		private Context activity;

		public AsyncCallWS(Context activity) {
			this.activity = activity;
//			this.dialog = new ProgressDialog(this.activity);
//			this.dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected String doInBackground(String... params) {
			String result = null;
			try {
				CallWsMethod("GetInvoiceAccept");
			} catch (Exception e) {
				PublicVariable.theard_GetFactorAccept=true;
				result = e.getMessage().toString();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			PublicVariable.theard_GetFactorAccept=true;
			if (result == null) {
				String res[] = WsResponse.split("##");
				if (res[1].compareTo("ER") == 0) {
					//Toast.makeText(this.activity.getApplicationContext(), "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
				}
				else if (res[1].compareTo("-1") == 0) {
				}
				else {
					InsertDataFromWsToDb(WsResponse);
				}
			}
			else
				{
			}
			try {
				if (this.dialog.isShowing()) {
					this.dialog.dismiss();
				}
			} catch (Exception e) {
				PublicVariable.theard_GetFactorAccept=true;
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
		PropertyInfo invoicecodePI = new PropertyInfo();
		//Set Name
		invoicecodePI.setName("InvoiceCode");
		//Set Value
		invoicecodePI.setValue(this.invoicecode);
		//Set dataType
		invoicecodePI.setType(String.class);
		//Add the property to request object
		request.addProperty(invoicecodePI);
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
		String query;
		String[] res;
		String[] value;
		res = WsResponse.split("@@");
		for (int i = 0; i < res.length; i++) {
			value = res[i].split("##");
			String Title;
			db = dbh.getReadableDatabase();
			Cursor c = db.rawQuery("SELECT * FROM HeadFactor WHERE Code='" + invoicecode + "'", null);
			if (c.getCount() > 0) {
				c.moveToNext();
				if (c.getString(c.getColumnIndex("InvocAccept")).compareTo(value[1]) != 0) {
					if (value[1].compareTo("0") == 0) {
						Title = "فاکتور نهایی شماره  " + invoicecode + "تایید نشد";
						db = dbh.getWritableDatabase();
						query = "UPDATE HeadFactor SET Type='2',InvocAccept='" + value[1] + "' WHERE Code='" + invoicecode + "'";
						db.execSQL(query);
					} else {
						Title = "فاکتور نهایی شماره  " + invoicecode + "تایید شد";
						db = dbh.getWritableDatabase();
						query = "UPDATE HeadFactor SET Type='2',InvocAccept='" + value[1] + "',AcceptDateInvoc='" + value[2] + "' WHERE Code='" + invoicecode + "'";
						db.execSQL(query);
					}
					runNotification("بسپارینا", Title, i, ServiceCode, ViewJob.class);
				}
			}
		}
		if(db.isOpen())
		{
			db.close();
		}
	}
	public void runNotification(String title,String detail,int id,String BsUserServicesID,Class<?> Cls)
	{
		NotificationClass notifi=new NotificationClass(this.activity);
		notifi.Notificationm(this.activity,title,detail,BsUserServicesID,"0",id,Cls);
	}
}
