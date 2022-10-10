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

#### Kotlin
Still using java ? :frowning: We've got you covered too, jump to this [section](#java) :sunglasses:
##### Initiate the API
In the onCreate method of your Application class
```kotlin
Kkiapay.init(applicationContext,
            "<kkiapay-api-key>",
            SdkConfig(themeColor = R.color.colorPrimary, imageResource = R.raw.armoiries, 
            /* set this to false in prod */ enableSandbox = true))
```
**Quick payment request**

```kotlin
"22967434270" debit 100
//To recover 100 XOF from account (674324270)
```

To manage request status you should process like this
```kotlin
 ("22967434270" debit  100) { status, phone, transactionId -> when (status) {

               STATUS.SUCCESS -> // payment is succed
               STATUS.INSUFFICIENT_FUND -> // user haven't enough money
               // .....
            }

        }
```

**Complete payment request with user's data**
```kotlin
// subscrber details are usefull on dashboard.kkiapay.me
  from {
          phoneNumber = "22967434270"
          firstName = "ALI"
          lastName = "SHAD"
          
  }.debit(100) { status, phone, transactionId ->
        //handle response
   }
```

**Request payment via UI-SDK Kit**
First, configure a listener to UI-KIT SDK: 
```kotlin
    Kkiapay.get()
           .setListener{ status, transactionId  ->
              Toast.makeText(activity, "Transaction: ${status.name} -> $transactionId", Toast.LENGTH_LONG).show()
            }
```

Second, configure event handling from the UI-KIT SDK in your activity onActivityResult methode:
```kotlin
    Kkiapay.get().handleActivityResult(requestCode, resultCode, data)
```

Finally, launch your payment request via UI-KIT SDK:
```kotlin
    Kkiapay.get().requestPayment(this, "1","Paiement de services","Nom Prenom")
```

-------

#### JAVA
##### Initiate the API
In the onCreate method of your Application class or at the first line of  your MainActivity class.
```java
    Kkiapay.init(this, "<kkiapay-api-key>", new SdkConfig(R.drawable.ic_app_logo, R.color.colorPrimary, true));
```

**Add a payment status listener**
```java
    // Add this to your activity onCreate method
    Kkiapay.get().setListener(new Function2<STATUS, String, Unit>() {
            @Override
            public Unit invoke(STATUS status, String s) {
                Toast.makeText(YourActivityName.this, "Transaction: " + status + " -> " + s, Toast.LENGTH_LONG).show();
                return null;
            }
        });
```

Second, configure event handling from the SDK in your activity onActivityResult method :
```java
    Kkiapay.get().handleActivityResult(requestCode, resultCode, data);
```

Finally, launch your payment request:
```java
    Kkiapay.get().requestPayment(
      YourActivityName.this, 
      "1000", 
      "Paiement de services", 
      "JOHN DOE", 
      "61XXXXXX"
    );
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
