package co.opensi.kkiapay_sdk

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.opensi.kkiapay.uikit.Kkiapay


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
            Toast.makeText(
                this@MainActivity,
                "Transaction: ${status.name} -> $transactionId",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        val testButtonWithKkiapay = findViewById<Button>(R.id.test_button_with_kkiapay)
        testButtonWithKkiapay.setOnClickListener {
            // start the payment process
            // This will display a kkiapay payment dialog to user
            Kkiapay.get().requestPayment(
                this,
                1,
                reason = "Payment of awesome service",
                api_key = "LprYUAyMpfAjq4z2yTHPiY0b6XktIQ",
                sandbox = false,
                name = "Johna DOE",
                partnerId = "AxXxxXXid",
                phone = "22961877882",
                email = "email@mail.com",
                paymentMethods = listOf("momo", "card", "direct_debit"),
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Kkiapay.get().handleActivityResult(requestCode, resultCode, data)
    }

}
