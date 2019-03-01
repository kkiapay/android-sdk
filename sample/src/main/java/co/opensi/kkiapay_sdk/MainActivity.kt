package co.opensi.kkiapay_sdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import co.opensi.kkiapay.STATUS
import co.opensi.kkiapay.debit
import co.opensi.kkiapay.uikit.Kkiapay
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        Kkiapay.get()
                .setListener{ status, transactionId  ->
                    Toast.makeText(this@MainActivity, "Transaction: ${status.name} -> $transactionId", Toast.LENGTH_LONG).show()
                }
    }

    override fun onResume() {
        super.onResume()
        momo_test.setOnClickListener {
            //Débit de 1 a l'utilisateur dont le numero de téléphone est 67 43 42 70
            ("22997000000" debit  1) {
                status, _, _ -> when (status) {
                STATUS.SUCCESS -> displayToUser("Bravooo")
                STATUS.INSUFFICIENT_FUND -> displayToUser("tu n'as pas le djeh bro")
                else-> displayToUser("pardon faut try encore")
            }
            }
        }

        test_button.setOnClickListener {
            //Lancement de Kkiapay UI-kit paiement
            Kkiapay.get().requestPayment(this, "1","Paiement de services","Nom Prenom")
        }
    }

    private fun displayToUser( a: String){
        Toast.makeText(this,a,Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Kkiapay.get().handleActivityResult(requestCode, resultCode, data)
    }
}
