## Kkiapay Android Sdk

[![Build Status](https://app.travis-ci.com/kkiapay/android-sdk.svg?branch=master)](https://app.travis-ci.com/kkiapay/android-sdk)
[![](https://jitpack.io/v/kkiapay/android-sdk.svg)](https://jitpack.io/#kkiapay/android-sdk)

[Kkiapay](https://kkiapay.me) is a developer friendly solution that allows you to accept mobile money and credit card payments in your application or website.

Before using this SDK, make sure you have an active Merchant Account on [Kkiapay](https://kkiapay.me), otherwise [create your account here](https://kkiapay.me), it's free of charge. :sunglasses:

## Installation

### Android Studio ( or Gradle )

  To add kkiapay in your android app:
  
   1- add the following line in your root `build.gradle` file inside `repositories` sections
   
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
Kkiapay.init(applicationContext,
    "<kkiapay-api-key>",
    SdkConfig(themeColor = R.color.colorPrimary,
        /* set enableSandbox = false in case you are using live API Keys */
        enableSandbox = true )
)
```


### Example
```kotlin
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
                api_key = "<kkiapay-api-key>",
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
```


#### COMPLETE STATUS LIST

| STATUS      | DESCRIPTION             |
| ----------- | ----------------------- |
|  SUCCESS    |                         |
| FAILED      |                         |
| INSUFFICIENT_FUND    |                |
| PENDING     |                |
| INVALID_PHONE_NUMBER |                |
| INVALID_API_KEY |                     |
| TRANSACTION_NOT_FOUND |                     |
| INVALID_TRANSACTION |                     |




#### Testimony :heart:

[GoMedical](https://play.google.com/store/apps/details?id=co.opensi.medical)
[MTN MOMO SHOP](http://opensi.co)



LICENSE
```
    The MIT License
    
    Copyright (c) 2018 Open SI, . http://opensi.co
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in
    all copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
    THE SOFTWARE.
```
