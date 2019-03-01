package co.opensi.kkiapay_sdk

import android.app.Application
import co.opensi.kkiapay.uikit.Kkiapay
import co.opensi.kkiapay.uikit.SdkConfig

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        //Initialisation de l'API
        Kkiapay.init(applicationContext,
                "LprYUAyMpfAjq4z2yTHPiY0b6XktIQ",
            SdkConfig(themeColor = R.color.colorPrimary,
                imageResource = R.raw.armoiries))
    }
}
