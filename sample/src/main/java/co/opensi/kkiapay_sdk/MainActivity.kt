package co.opensi.kkiapay_sdk

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import co.opensi.kkiapay.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


       KkiaPay("<kkiapay-api-key>")
        //should be replaced with the correct api-key



        //inline sample
        ("22967434270" debit 10) { status, _ -> Log.e("end",status.toString()) }



        // send more information about user
        //usefull on dashboard.kkiapay.me
        from {
            phoneNumber = "222"
            firstName = "ALI"
            lastName = "ARC"
        }.debit(10) { status: STATUS, s: String -> }

    }

}
