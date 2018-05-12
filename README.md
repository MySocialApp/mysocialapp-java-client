# mysocialapp-java-client [![Build Status](https://travis-ci.org/MySocialApp/mysocialapp-java-client.svg?branch=master)](https://travis-ci.org/MySocialApp/docker-cassandra) [![Release](https://jitpack.io/v/MySocialApp/mysocialapp-java-client.svg)](https://jitpack.io/#MySocialApp/mysocialapp-java-client)

[![mysocialapp header](https://msa-resources.s3.amazonaws.com/build%20your%20own%20social%20networking%20app%202.jpg)](https://mysocialapp.io)

Official Kotlin and Java client to interact with apps made with [MySocialApp](https://mysocialapp.io) (turnkey iOS and Android social network app builder - SaaS).

Note: This lib was made in Kotlin and can be used inside any Java and Android apps.

# Why using it?

MySocialApp is a very innovative way to have a turnkey native social network app for iOS and Android. Our API are open and ready to use for all your need inside one of our generated app or any thirds app. 

### What can you do?

Add social features to your existing app, automate actions, scrape contents, analyze the users content, add bot to your app, almost anything that a modern social network can bring.. There is no limit! Any suggestion to add here? Do a PR. 

# What features are available?

| Feature | Server side API | Java/Kotlin client API
| ------- | ----------- | -------------------------- |
| Account management | :heavy_check_mark: | Partially
| Feed | :heavy_check_mark: | Partially
| Comment | :heavy_check_mark: | Partially
| Like | :heavy_check_mark: | Partially
| Notification | :heavy_check_mark: | Soon
| Private messaging | :heavy_check_mark: | Soon
| Photo | :heavy_check_mark: | Soon
| User | :heavy_check_mark: | :heavy_check_mark:
| Friend | :heavy_check_mark: | :heavy_check_mark:
| URL rewrite | :heavy_check_mark: | :heavy_check_mark:
| URL preview | :heavy_check_mark: | :heavy_check_mark:
| User mention | :heavy_check_mark: | :heavy_check_mark:
| Hash tag| :heavy_check_mark: | :heavy_check_mark:
| Search | :heavy_check_mark: | Soon
| Group [optional module] | :heavy_check_mark: | Partially
| Event [optional module] | :heavy_check_mark: | Partially
| Roadbook [optional module] | :heavy_check_mark: | Partially
| Live tracking with `RideShare` ([exemple here](https://www.nousmotards.com/rideshare/follow/f6e0c27e01beb4f4-3856809369215939951-f10c31fd2dcc4576a1b488385aaa61c2)) [optional module] | :heavy_check_mark: | Soon
| Point of interest [optional module] | :heavy_check_mark: | Soon

Looking for official Swift/iOS client API ? This is coming soon 

Coming soon:
* Real time downstream handlers with FCM (Android), APNS (iOS) and Web Socket.

# Dependencies

Step 1. Add it in your root build.gradle at the end of repositories:
```
repositories {
	...
  maven { url 'https://jitpack.io' }
}
```

Step 2. Add the dependency
```
dependencies {
  compile 'com.github.mysocialapp:mysocialapp-java-client:{version}'
}
```

# Prerequisites

You must have "APP ID" to target the right App. Want to [create your app](https://support.mysocialapp.io/hc/en-us/articles/115003936872-Create-my-first-app)?
##### App owner/administrator:
Sign in to [go.mysocialapp.io](https://go.mysocialapp.io) and go to API section. Your **APP ID** is part of the endpoint URL provided to your app. Which is something like `https://u123456789123a123456-api.mysocialapp.io`. Your **APP ID** is `u123456789123a123456`

##### App user:
Ask for an administrator to give you the **APP ID**.

# Usage

Most of the actions can be synchronous and asynchronous with RxJava. We are using [RxJava](https://github.com/ReactiveX/RxJava) to provide an elegant way to handle asynchronous results.

Create an account
```java
String appId = "u123456789123a123456";
MySocialApp msa = new MySocialApp.Builder().setAppId(appId).build();

// create an account and return an active session to do fluent operations
Session johnSession = msa.createAccount("John", "john@myapp.com", "myverysecretpassw0rd")
```

```kotlin
val appId = "u123456789123a123456"
val msa = MySocialApp.Builder().setAppId(appId).build()

// create an account and return an active session to do fluent operations
val johnSession = msa.createAccount("John", "john@myapp.com", "myverysecretpassw0rd")
```

Do login with your account
```java
Session johnSession = msa.connect("John", "myverysecretpassw0rd");
```

```kotlin
val johnSession = msa.connect("John", "myverysecretpassw0rd")
```

Get your account info
```java
User account = johnSession.getAccount().blockingGet();
account.getFirstName();
account.getDateOfBirth();
account.getLivingLocation();
[..]
```

```kotlin
val account = jognSession.account.blockingGet()
account.firstName
account.dateOfBirth
account.livingLocation?.completeCityAddress
[..]
```

Update your account
```java
User account = johnSession.getAccount().blockingGet();
account.setLastName("James");
account.blockingSave(); // or use save() to asynchronously save it with Rx
```

```kotlin
val account = johnSession.account
account.lastName = "James"
account.blockingSave() // or use save() to asynchronously save it with Rx
```

List news feed from specific page and size
```java
johnSession.getNewsFeed().blockingList(0, 10)
```

```kotlin
johnSession?.newsFeed?.blockingList(0, 10)
```

Stream all 100 first news feed message
```java
johnSession.getNewsFeed().blockingStream(100)
```

```kotlin
johnSession?.newsFeed?.blockingStream(100)
```

Post public news with hashtag + url + user mention
```kotlin
val s = johnSession

val post = TextWallMessage.Builder()
        .setMessage("This is a post with #hashtag url https://mysocialapp.io and someone mentioned [[user:3856809369215939951]]")
        .setVisibility(AccessControl.PUBLIC)
        .build()

s?.newsFeed?.blockingSendWallPost(post)
``` 

Post public photo with a hashtag
```kotlin
val s = johnSession

val post = Photo.Builder()
        .setMessage("This is a post with an image and a #hashtag :)")
        .setImage(File("/tmp/myimage.jpg"))
        .setVisibility(AccessControl.PUBLIC)
        .build()

s?.newsFeed?.blockingSendWallPost(post)
```

Post on a friend wall and mention him
````kotlin
 val s = johnSession

// take my first friend
val friend = s?.account?.blockingGet()?.blockingListFriends()?.firstOrNull() ?: return

val post = TextWallMessage.Builder()
        .setMessage("Hey [[user:${friend.id}]] what's up?")
        .setVisibility(AccessControl.FRIEND)
        .build()

friend.blockingSendWallPost(post)
````

#### Want to see more examples?

[Look at our test classes for Java and Kotlin](https://github.com/MySocialApp/mysocialapp-java-client/tree/master/src/test)

# Credits

* [Kotlin](https://kotlinlang.org/)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [Retrofit](http://square.github.io/retrofit/)
* [OkHTTP3](https://github.com/square/okhttp)

# Contributions

All contributions are welcomed. Thank you