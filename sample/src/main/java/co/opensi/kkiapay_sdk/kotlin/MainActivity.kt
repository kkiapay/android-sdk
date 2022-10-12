package co.opensi.kkiapay_sdk.kotlin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.opensi.kkiapay.KKiapayCallback
import co.opensi.kkiapay.STATUS
import co.opensi.kkiapay.Subscriber
import co.opensi.kkiapay.uikit.Kkiapay
import co.opensi.kkiapay_sdk.R
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
        test_button_with_kkiapay.setOnClickListener {
            // start the payment process
            // This will display a kkiapay payment dialog to user
            Kkiapay.get().requestPayment(this, 1, "Payment of awesome service", name = "Johna DOE",
                sandbox = true,
                phone = "22961000000", email = "email@mail.com")
        }

        test_button_without_kkiapay.setOnClickListener {
            val subscriber = Subscriber("22961877882", "DOE", "Johna","")
            val manager = Kkiapay.get().momoPay
            manager.from(subscriber).debit(1, object : KKiapayCallback {
                override fun onResponse(
                    status: STATUS,
                    phone: String, transactionId: String
                ) {
                    when (status) {
                        STATUS.FAILED -> {
                            Toast.makeText(this@MainActivity, "Transaction: FAILED -> $transactionId", Toast.LENGTH_LONG).show()
                        }
                        STATUS.SUCCESS -> {
                            Toast.makeText(this@MainActivity, "Transaction: SUCCESS -> $transactionId", Toast.LENGTH_LONG).show()
                        }
                        STATUS.INSUFFICIENT_FUND -> {
                        }
                        else -> { }
                    }
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Kkiapay.get().handleActivityResult(requestCode, resultCode, data)
    }

}
