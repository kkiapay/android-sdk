package co.opensi.kkiapay_sdk

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import co.opensi.kkiapay.KkiaPay
import co.opensi.kkiapay.STATUS
import co.opensi.kkiapay.debit


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)







        //Initialisation de l'API
            KkiaPay("DSKGFF45VSVQFBVF2FSRVSFDX7DZ6VQ4SSV5")

        //Débit de 1500 a l'utilisateur dont le numero de téléphone est 67 43 42 70
        ("22967434270" debit  1500) {

            status, _ -> when (status) {


               STATUS.SUCCESS -> displayToUser("Bravooo")
            }

        }


    }


    fun displayToUser( a: String){

    }

}
