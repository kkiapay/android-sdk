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
                "3425dc6035d711eca8f5b92f2997955b",
                new SdkConfig (R.raw.armoiries, R.color.colorPrimary, true));
    }
}
