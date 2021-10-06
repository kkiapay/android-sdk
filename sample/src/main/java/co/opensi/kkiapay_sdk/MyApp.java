package co.opensi.kkiapay_sdk;

import android.app.Application;

import co.opensi.kkiapay.uikit.Kkiapay;
import co.opensi.kkiapay.uikit.SdkConfig;

public class MyApp extends Application {
    @Override
    public void onCreate () {
        super.onCreate ();
        //Initialisation de l'API
        Kkiapay.init(this,
                "<your-api-key>",
                new SdkConfig (R.raw.armoiries, R.color.colorPrimary, false));
    }
}
