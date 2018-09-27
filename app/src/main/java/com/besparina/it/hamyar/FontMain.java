package com.besparina.it.hamyar;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Rm on 03/04/2018.
 */

public class FontMain extends Application {

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/IRANSans.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
