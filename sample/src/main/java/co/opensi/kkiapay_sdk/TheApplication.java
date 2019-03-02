package co.opensi.kkiapay_sdk;

import android.app.Application;

import co.opensi.kkiapay.uikit.Kkiapay;
import co.opensi.kkiapay.uikit.SdkConfig;

/**
 * @author Armel FAGBEDJI ( armel.fagbedji@opensi.co )
 * created  at 01/03/2019
 */

public class TheApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Kkiapay.INSTANCE.init(this,
                "<api-key>",
                new SdkConfig(R.raw.armoiries, R.color.colorPrimary));
    }
}
