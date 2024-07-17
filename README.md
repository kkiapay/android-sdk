## Kkiapay Android Sdk

[![Build Status](https://app.travis-ci.com/kkiapay/android-sdk.svg?branch=master)](https://app.travis-ci.com/kkiapay/android-sdk)
[![](https://jitpack.io/v/kkiapay/android-sdk.svg)](https://jitpack.io/#kkiapay/android-sdk)

[Kkiapay](https://kkiapay.me) is a developer friendly solution that allows you to accept mobile money and credit card payments in your application or website.

Before using this SDK, make sure you have an active Merchant Account on [Kkiapay](https://kkiapay.me), otherwise [create your account here](https://kkiapay.me), it's free of charge. :sunglasses:

## Installation

### Android Studio ( or Gradle )

  To add kkiapay in your android app:
  
   1- add the following line in your root `settings.gradle` file inside `repositories` of `dependencyResolutionManagement` section
   
   ```groovy
       maven { url 'https://jitpack.io' }
   ```
   
   2- add the following line in your app's `build.gradle` file inside `dependencies` section
   
   ```groovy
        implementation 'com.github.kkiapay:android-sdk:<latestVersion>'
   ```

## Usage
 **Please refer to** [KkiaPay Official Docs](https://docs.kkiapay.me/v1/plugin-et-sdk/sdk-android) **for updated docs** 


Get your API Key on [kkiapay Dashboard at Developer section](https://kkiapay.me/#/developers) and initialize the Sdk in your Application Class


### Initiate the API
In the onCreate method of your Application class
```kotlin
import co.opensi.kkiapay.uikit.Kkiapay
import co.opensi.kkiapay.uikit.SdkConfig
```

```kotlin
Kkiapay.init(applicationContext,
    "<kkiapay-api-key>",
    SdkConfig(themeColor = R.color.colorPrimary,
        /* set enableSandbox = false in case you are using live API Keys */
        enableSandbox = true )
)
```


### Example
```kotlin
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import co.opensi.kkiapay.uikit.Kkiapay
```

```kotlin
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
```


### Reference

<table>
<tr><td>Argument</td><td>Type</td><td>Required</td><td>Details</td></tr>
<tr><td>phone</td><td>String</td><td>Yes</td><td>Valid mobile money number to debit. ex : 22967434270 </td></tr>
<tr><td>amount</td><td>Numeric</td><td>Yes</td><td>Amount to debit from user account (XOF) </td></tr>
<tr><td>name</td><td>String</td><td>No</td><td>Client firstname and lastname </td></tr>
<tr><td>partnerId</td><td>String</td><td>No</td><td>Your id to find transaction</td></tr>
<tr><td>paymentMethods</td><td>List of String</td><td>No</td><td>Set widget payment methods ex: ["momo","card"] </td></tr>
<tr><td>api_key</td><td>String</td><td>Yes</td><td>public api key</td></tr>
<tr><td>sandbox</td><td>Boolean</td><td>No</td><td>The true value of this attribute allows you to switch to test mode</td></tr>
<tr><td>email</td><td>String</td><td>No</td><td>Client email </td></tr>
<tr><td>reason</td><td>String</td><td>No</td><td>Reason of transaction</td></tr>
</table>


#### Testimony :heart:

[GoMedical](https://play.google.com/store/apps/details?id=co.opensi.medical)
[MTN MOMO SHOP](http://opensi.co)
