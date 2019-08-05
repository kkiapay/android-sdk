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

        // Setup listener for payment status
        Kkiapay.get().setListener { status, transactionId ->

            //The following code will be run when user will end the payment
            Toast.makeText(this@MainActivity, "Transaction: ${status.name} -> $transactionId", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()

        test_button.setOnClickListener {
            // start the payment process
            // This will display a kkiapay payment dialog to user
            Kkiapay.get().requestPayment(this, "1", "Payment of awesome service", "Johna DOE")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Kkiapay.get().handleActivityResult(requestCode, resultCode, data)
    }
}
