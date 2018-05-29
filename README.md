## Kkiapay

[![Build Status](https://travis-ci.org/kkiapay/android-sdk.svg?branch=master)](https://travis-ci.org/kkiapay/android-sdk) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/co.opensi.kkiapay/kkiapay/badge.svg)](https://maven-badges.herokuapp.com/maven-central/co.opensi.kkiapay/kkiapay)

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

 Get your API Key on [kkiapay Dashboard at Developer section](https://api.kkiapay.me/#/developers) and initialize the Sdk in Application Class or Activity

#### Kotlin
```kotlin
KkiaPay("<kkiapay-api-key>")
```

#### Java
```java
KKiaPay manager = new KkiaPay("<kkiapay-api-key>");
```

### Submit a debit request to a mobile money account

### Kotlin
```kotlin
"22967434270" debit 100
//retrieve 100F CFA to 67434270 mobile money account
```

## Testimony