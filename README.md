## Kkiapay

[![Build Status](https://travis-ci.org/kkiapay/android-sdk.svg?branch=master)](https://travis-ci.org/kkiapay/android-sdk)   [ ![Download](https://api.bintray.com/packages/kkiapay/KKIAPAY-ANDROID-SDK/android-sdk/images/download.svg) ](https://bintray.com/kkiapay/KKIAPAY-ANDROID-SDK/android-sdk/_latestVersion)

[Kkiapay](https://kkiapay.me) is developer friendly solution that allows you to accept mobile money and credit card payments
in your application or website.

Before using this SDK, make sure you have a right Merchant Account on [Kkiapay](https://kkiapay.me), otherwise [go](https://kkiapay.me)
and create your account is free and without pain.

## Installation

### Android Studio ( or Gradle )

  To add kkiapay in your android app, just add the following line in your app's `build.gradle`
file inside `dependencies` section

```groovy
implementation 'co.opensi.kkiapay:kkiapay:+'
 ```

Don't worry about changes, we will maintain backward compatibility of this sdk


## Usage

 Get your API Key on [kkiapay Dashboard at Developer section](https://kkiapay.me/#/developers) and initialize the Sdk in Application Class or Activity

#### Kotlin
##### Initiate the API
```kotlin
KkiaPay("<kkiapay-api-key>")
```
**Quick payment request**
```kotlin
"22967434270" debit 100
//To recover 100 XOF from account (674324270)
```

**Complete payment request**
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
-------

#### JAVA
##### Initiate the API
```java
KkiaPay manager = new KkiaPay("");
```
**Request a payment**
```java
 manager.to("22967434270")
        .take(1500, new KKiapayCallback() {
           @Override
           public void onResponse(@NotNull STATUS status,
            @NotNull String phone, @NotNull String transactionId ) {
               
                //handle response                 
       }
  });
```

**Add subscribers details to request**
```java
// subscrber details are usefull on dashboard.kkiapay.me
 Subscriber subscriber =  new Subscriber("22967434270","ALI","SHAD");
        
 manager.to(subscriber)
        .take(1500, new KKiapayCallback() {
           @Override
           public void onResponse(@NotNull STATUS status,
            @NotNull String phone , @NotNull String transactionId ) {
               //handle response

     }
  });
```

#### TODO

- [x] MTN Mobile Money

- [ ] Cash 

- [ ] Flooz money

- [ ] Visa & Master Card

- [ ] Ecobank Express Cash


#### Testimony

[GoMedical](https://play.google.com/store/apps/details?id=co.opensi.medical)


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