package com.project.it.hamyar;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Rm on 03/04/2018.
 */

public class FontMain extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/BMitra.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
