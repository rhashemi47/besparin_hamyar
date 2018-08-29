package com.besparina.it.hamyar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

/**
 * Created by hashemi on 08/04/2018.
 */

public class Alert_Clock extends Dialog implements View.OnClickListener {



    public Activity activity;
    public Button yes, no;
    public NumberPicker pHoure, pMinute;
    private final Alert_Clock.OnTimeSetListener mCallback;
    public int intHour=0,intMinute=0;
    public int value_Hour=0,value_Minute=0;
    public Alert_Clock(Activity activity, OnTimeSetListener mCallback, int hourOfDay, int minute) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.mCallback = mCallback;
        this.value_Hour=hourOfDay;
        this.value_Minute=minute;
    }
    public interface OnTimeSetListener {

        /**
         * @param hourOfDay The hour that was set.
         * @param minute The minute that was set.
         */
        void onTimeSet(String hourOfDay, String minute);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_custome);
        yes = (Button) findViewById(R.id.btnAccept);
        no = (Button) findViewById(R.id.btnCansel);
        pHoure = (NumberPicker) findViewById(R.id.pHoure);
        pMinute = (NumberPicker) findViewById(R.id.pMinute);
        pHoure.setMinValue(0);
        pHoure.setMaxValue(23);
        pHoure.setWrapSelectorWheel(true);
        pMinute.setMinValue(0);
        pMinute.setMaxValue(55);
        pMinute.setWrapSelectorWheel(true);
        pHoure.setValue(this.value_Hour);
        pMinute.setValue(this.value_Minute);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnAccept:
                String  hour,min;
                if(pHoure.getValue()<10)
                {
                    hour="0"+String.valueOf(pHoure.getValue());
                }
                else
                {
                    hour=String.valueOf(pHoure.getValue());
                }
                if(pMinute.getValue()<10)
                {
                    min="0"+String.valueOf(pMinute.getValue());
                }
                else
                {
                    min=String.valueOf(pMinute.getValue());
                }
                mCallback.onTimeSet(PersianDigitConverter.PerisanNumber(hour),
                        PersianDigitConverter.PerisanNumber(min));
                break;
            case R.id.btnCansel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}