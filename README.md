## Kkiapay

[![Build Status](https://travis-ci.org/kkiapay/android-sdk.svg?branch=master)](https://travis-ci.org/kkiapay/android-sdk)   [ ![Download](https://api.bintray.com/packages/kkiapay/KKIAPAY-ANDROID-SDK/android-sdk/images/download.svg) ](https://bintray.com/kkiapay/KKIAPAY-ANDROID-SDK/android-sdk/_latestVersion)

[Kkiapay](https://kkiapay.me) is developer friendly solution that allows you to accept mobile money and credit card payments
in your application or website.

Before using this SDK, make sure you have a right Merchant Account on [Kkiapay](https://kkiapay.me), otherwise [go](https://kkiapay.me)
and create your account is free and without pain :sunglasses:.

## Installation

### Android Studio ( or Gradle )

  To add kkiapay in your android app, just add the following line in your app's `build.gradle`
file inside `dependencies` section

```groovy
implementation 'co.opensi.kkiapay:kkiapay:<latestVersion>'
 ```


## Usage

 Get your API Key on [kkiapay Dashboard at Developer section](https://kkiapay.me/#/developers) and initialize the Sdk in Application Class

#### Kotlin
You still use java ? :frowning:, jump to this [section](#java) :frowning:
##### Initiate the API
In the onCreate method of your Application class
```kotlin
Kkiapay.init(applicationContext,
            "<kkiapay-api-key>",
            SdkConfig(themeColor = R.color.colorPrimary, imageResource = R.raw.armoiries))
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
In the onCreate method of your Application class
```java
    Kkiapay.INSTANCE.init(this,
                "<your-api-key>",
                new SdkConfig(R.raw.armoiries, R.color.colorPrimary));
```
**Request a payment**
```java
 MomoPay manager = Kkiapay.INSTANCE.get().getMomoPay();
 manager.from("22967434270")
        .debit(1500, new KKiapayCallback() {
           @Override
           public void onResponse(@NotNull STATUS status,
            @NotNull String phone, @NotNull String transactionId ) {
            
               switch (status) {
                     
                  case FAILED :
                           // .....
                        break;
                  case SUCCESS :
                          //....
                        break;
                  case INSUFFICIENT_FUND :
                        //...
                      break;
                }           
               
       }
  });
```

**Add subscribers details to request**
```java
// subscrber details are usefull on dashboard.kkiapay.me
 Subscriber subscriber =  new Subscriber("22967434270","ALI","SHAD");
        
 manager.from(subscriber)
        .debit(1500, new KKiapayCallback() {
           @Override
           public void onResponse(@NotNull STATUS status,
            @NotNull String phone , @NotNull String transactionId ) {
               //handle response

     }
  });
```

**Request payment via UI-SDK Kit**
First, configure a listener to UI-KIT SDK: 
```java
    Kkiapay.INSTANCE.get().setListener(new Function2<STATUS, String, Unit>() {
            @Override
            public Unit invoke(STATUS status, String s) {
                Toast.makeText(MA.this, "Transaction: ${status.name} -> $transactionId", Toast.LENGTH_LONG).show();
                return null;
            }
        });
```

Second, configure event handling from the UI-KIT SDK in your activity onActivityResult methode:
```java
    Kkiapay.INSTANCE.get().handleActivityResult(requestCode, resultCode, data);
```

Finally, launch your payment request via UI-KIT SDK:
```java
    Kkiapay.INSTANCE.get().requestPayment(activity,
                            "1",
                            "Paiement de services",
                            "Nom Prenom", "");
```


#### COMPLETE STATUS LIST

| STATUS      | DESCRIPTION             |
| ----------- | ----------------------- |
|  SUCCESS    |                         |
| FAILED      |                         |
| INSUFFICIENT_FUND    |                |
| INVALID_PHONE_NUMBER |                |
| INVALID_API_KEY |                     |




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
