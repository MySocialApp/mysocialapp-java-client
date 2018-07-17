# MySocialApp Java Library [![Release](https://jitpack.io/v/MySocialApp/mysocialapp-java-client.svg)](https://jitpack.io/#MySocialApp/mysocialapp-java-client)

The Java library for interacting with the [MySocialApp](https://mysocialapp.io?ref=github) API.

In order to use this library, you need to have a free account on [https://go.mysocialapp.io](https://go.mysocialapp.io?ref=github). After registering, you will need the application credentials for your app.

# What is MySocialApp?
#### MySocialApp - Seamless Social Networking features for your app

MySocialApp’s powerful API lets you quickly and seamlessly implement social networking features within your websites, mobile and back-end applications. Save months of development headache and focus on what makes your app unique.

# Table of contents

- [Dependencies](#dependencies)
- [Getting Started](#getting-started)
- [Account](#account)
- [User](#user)
- [News Feed / Activity Stream](#news-feed-/-activity-stream)
- [Event](#event)
- [Group](#group)
- [Private Messaging](#private-messaging)
- [Search](#search)
    - [News Feed](#search-news-feed)
    - [User](#search-user)
    - [Event](#search-event)
    - [Group](#search-group)
- [Notification](#notification)
- [Receiving Notification Event](#receiving-notification-event)
- [Handle Exception](#handle-exception)
- [Documentation](https://docs.mysocialapp.io?ref=github)
- [Credits](#credits)
- [Demo app](#demo-app)
- [Contributions](#contributions)

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

# Usage

Actions are synchronous and asynchronous. We are using [RxJava](https://github.com/ReactiveX/RxJava) to provide an elegant way to handle asynchronous results.

## Getting Started

[Read Java getting started](https://docs.mysocialapp.io/v1.0/docs/quick-start-java?ref=github)

## Account

## User

## News Feed / Activity Stream

## Event

## Group

## Private Messaging

## Search

#### Search News Feed

#### Search User

#### Search Event

#### Search Group

## Notification

## Receiving Notification Event

## Handle Exception

MySocialAppException and children can be handle by using the non blocking way with `.doOnError(..)` or `.onErrorResumeNext(..)`. Please refer to [RxJava](https://github.com/ReactiveX/RxJava) to know the difference between them.

# Demo app

Here are demo apps that use the 100% MySocialApp API

* [MySocialApp Android](https://play.google.com/store/apps/details?id=io.mysocialapp.android)
* [MySocialApp iOS](https://itunes.apple.com/fr/app/mysocialapp-your-social-app/id1351250650)

# Credits

* [Kotlin](https://kotlinlang.org/)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [Retrofit](http://square.github.io/retrofit/)
* [OkHTTP3](https://github.com/square/okhttp)

# Contributions

All contributions are welcomed
