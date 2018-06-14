# mysocialapp-java-client [![Release](https://jitpack.io/v/MySocialApp/mysocialapp-java-client.svg)](https://jitpack.io/#MySocialApp/mysocialapp-java-client)

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
| Profile management | :heavy_check_mark: | :heavy_check_mark:
| Feed | :heavy_check_mark: | :heavy_check_mark:
| Comment | :heavy_check_mark: | :heavy_check_mark:
| Like | :heavy_check_mark: | :heavy_check_mark:
| Notification | :heavy_check_mark: | Partially
| Private messaging | :heavy_check_mark: | :heavy_check_mark:
| Photo | :heavy_check_mark: | :heavy_check_mark:
| User | :heavy_check_mark: | :heavy_check_mark:
| Friend | :heavy_check_mark: | :heavy_check_mark:
| URL rewrite | :heavy_check_mark: | :heavy_check_mark:
| URL preview | :heavy_check_mark: | :heavy_check_mark:
| User mention | :heavy_check_mark: | :heavy_check_mark:
| Hash tag| :heavy_check_mark: | :heavy_check_mark:
| Search users | :heavy_check_mark: | :heavy_check_mark:
| Search news feed | :heavy_check_mark: | :heavy_check_mark:
| Search groups | :heavy_check_mark: | :heavy_check_mark:
| Search events | :heavy_check_mark: | :heavy_check_mark:
| Group [optional module] | :heavy_check_mark: | :heavy_check_mark:
| Event [optional module] | :heavy_check_mark: | :heavy_check_mark:
| Roadbook [optional module] | :heavy_check_mark: | Partially
| Live tracking with `RideShare` ([exemple here](https://www.nousmotards.com/rideshare/follow/f6e0c27e01beb4f4-3856809369215939951-f10c31fd2dcc4576a1b488385aaa61c2)) [optional module] | :heavy_check_mark: | Soon
| Point of interest [optional module] | :heavy_check_mark: | Soon
| Admin operations | :heavy_check_mark: | Soon

Looking for official Swift/iOS client API ? [Click here](https://github.com/MySocialApp/mysocialapp-swift-client)

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

For android, exclude duplicate META-INF files if needed. `Build.gradle`
```
android {
    packagingOptions {
        exclude 'META-INF/DEPENDENCIES.txt'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/notice.txt'
        exclude 'META-INF/license.txt'
        exclude 'META-INF/dependencies.txt'
        exclude 'META-INF/LGPL2.1'
    }
}
```

# Prerequisites

You must have "APP ID" to target the right App. Want to [create your app](https://support.mysocialapp.io/hc/en-us/articles/115003936872-Create-my-first-app)?
#### App owner/administrator:
Sign in to [go.mysocialapp.io](https://go.mysocialapp.io) and go to API section. Your **APP ID** is part of the endpoint URL provided to your app. Which is something like `https://u123456789123a123456-api.mysocialapp.io`. Your **APP ID** is `u123456789123a123456`

#### App user:
Ask for an administrator to give you the **APP ID**.

# Usage

Most of the actions can be synchronous and asynchronous with RxJava. We are using [RxJava](https://github.com/ReactiveX/RxJava) to provide an elegant way to handle asynchronous results.

### Profile
#### Create an account
Java
```java
String appId = "u123456789123a123456";
MySocialApp msa = new MySocialApp.Builder().setAppId(appId).build();

// or
String endpointURL = "https://u123456789123a123456-api.mysocialapp.io";
MySocialApp msa = new MySocialApp.Builder().setAPIEndpointURL(endpointURL).build();

// create an account and return an active session to do fluent operations
Session johnSession = msa.blockingCreateAccount("john@myapp.com", "myverysecretpassw0rd")
```

Kotlin
```kotlin
val appId = "u123456789123a123456"
val msa = MySocialApp.Builder().setAppId(appId).build()

// or
val endpointURL = "https://u123456789123a123456-api.mysocialapp.io";
val msa = MySocialApp.Builder().setAPIEndpointURL(endpointURL).build()

// create an account and return an active session to do fluent operations
val johnSession = msa.blockingCreateAccount("john@myapp.com", "myverysecretpassw0rd")
```

#### Do login with an access token and get session
Java
```java
Session johnSession = msa.blockingConnect("my access token");
```

Kotlin
```kotlin
val johnSession = msa.blockingConnect("my access token")
```


#### Do login with your account and get session
Java
```java
Session johnSession = msa.blockingConnect("john@myapp.com", "myverysecretpassw0rd");
```

Kotlin
```kotlin
val johnSession = msa.blockingConnect("john@myapp.com", "myverysecretpassw0rd")
```

#### Get your account info
Java
```java
User account = johnSession.getAccount().blockingGet();
account.getFirstName();
account.getDateOfBirth();
account.getLivingLocation();
[..]
```

Kotlin
```kotlin
val account = jognSession.account.blockingGet()
account.firstName
account.dateOfBirth
account.livingLocation?.completeCityAddress
[..]
```

#### Update your account
Java
```java
User account = johnSession.getAccount().blockingGet();
account.setLastName("James");
account.blockingSave(); // or use save() to asynchronously save it with Rx
```

Kotlin
```kotlin
val account = johnSession.account
account.lastName = "James"
account.blockingSave() // or use save() to asynchronously save it with Rx
```

#### How to integrate a MySocialApp user with an existing user in my application? 
MySocialApp allows you to use your own user IDs to find a user using the "external_id" property. 

```kotlin
val yourAppUserId = "12348-abcdy-82739-qzdqdq"

val s = johnSession

// set app external user id
val account = s?.account?.blockingGet()
account?.externalId = yourAppUserId
account?.blockingSave()

// find user by external id
val user = s?.user?.blockingGetByExternalId(yourAppUserId)
```

#### Delete your account (not recoverable)
⚠ Caution: this operation is not recoverable
```kotlin
val s = johnSession
val password = "your account password to confirm the ownership"

s?.account?.blockingRequestForDeleteAccount(password)
// Your account has been deleted..
// You are no more able to perform operations
```

### News feed
#### List news feed from specific page and size
Java
```java
johnSession.getNewsFeed().blockingList(0, 10)
```

Kotlin
```kotlin
johnSession?.newsFeed?.blockingList(0, 10)
```

#### Stream all 100 first news feed message
Java
```java
johnSession.getNewsFeed().blockingStream(100)
```

Kotlin
```kotlin
johnSession?.newsFeed?.blockingStream(100)
```

#### Post public news with hashtag + url + user mention
```kotlin
val s = johnSession

val post = FeedPost.Builder()
        .setMessage("This is a post with #hashtag url https://mysocialapp.io and someone mentioned [[user:3856809369215939951]]")
        .setVisibility(AccessControl.PUBLIC)
        .build()

s?.newsFeed?.blockingSendWallPost(post)
``` 

#### Post public photo with a hashtag
```kotlin
val s = johnSession

val post = FeedPost.Builder()
        .setMessage("This is a post with an image and a #hashtag :)")
        .setImage(File("/tmp/myimage.jpg"))
        .setVisibility(AccessControl.PUBLIC)
        .build()

s?.newsFeed?.blockingSendWallPost(post)
```

#### Post on a friend wall and mention him
```kotlin
val s = johnSession

// take my first friend
val friend = s?.account?.blockingGet()?.blockingListFriends()?.firstOrNull() ?: return

val post = FeedPost.Builder()
        .setMessage("Hey [[user:${friend.id}]] what's up?")
        .setVisibility(AccessControl.FRIEND)
        .build()

friend.blockingSendWallPost(post)
```

#### Ignore a news feed post
```kotlin
val s = getSession()
val newsFeed = s?.newsFeed?.blockingStream(1)?.firstOrNull()
newsFeed?.blockingIgnore()
```

#### Report a news feed post
```kotlin
val s = getSession()
val newsFeed = s?.newsFeed?.blockingStream(1)?.firstOrNull()
newsFeed?.blockingReport()
```

#### Delete a news feed post
```kotlin
val s = getSession()
val newsFeed = s?.newsFeed?.blockingStream(1)?.firstOrNull()
newsFeed?.blockingDelete()
```

### Search
#### Search for users by first name and gender
```kotlin
val s = johnSession

val searchQuery = FluentUser.Search.Builder()
        .setFirstName("alice")
        .setGender(Gender.FEMALE)
        .build()

val users = s?.user?.blockingSearch(searchQuery)?.data
// return the 10 first results
```

#### Search for users by their living location
```kotlin
val s = johnSession

val parisLocation = SimpleLocation(48.85661400000001, 2.3522219000000177)

val searchQuery = FluentUser.Search.Builder()
        .setLivingLocation(parisLocation)
        .build()

val users = s?.user?.blockingSearch(searchQuery)?.data
// return the 10 first results
```

#### Search for news feeds that contains "hello world"
```kotlin
val s = johnSession

val searchQuery = FluentFeed.Search.Builder()
        .setTextToSearch("hello world")
        .build()

val feeds = s?.newsFeed?.blockingSearch(searchQuery)?.data
// return the 10 first results
```

### Private conversation
#### List private conversations
```kotlin
val s = johnSession
val conversations = s?.conversation?.blockingList()
```

#### Create conversation
```kotlin
val s = johnSession

// take 3 first users
val people = s?.user?.blockingStream(3)?.toList()?.get(0)?.users?.toSet() ?: emptySet()

val conversation = Conversation.Builder()
        .setName("let's talk about the next event in private")
        .addMembers(people)
        .build()

val createdConversation = s?.conversation?.blockingCreate(conversation)
```

#### Post new message into conversation
```kotlin
val s = johnSession
val lastConversation = s?.conversation?.blockingList()?.firstOrNull()

val message = ConversationMessagePost.Builder()
        .setMessage("Hello, this is a message from our SDK #MySocialApp with an amazing picture. Enjoy")
        .setImage(File("/tmp/myimage.jpg"))
        .build()

val messageSent = lastConversation?.blockingSendMessage(message)
```

#### Get messages from conversation
```kotlin
val s = johnSession
val conversation = s?.conversation?.blockingList()?.firstOrNull()

// get 35 last messages without consuming them
val conversationMessages = conversation?.messages?.blockingStream(35)?.toList()

// get 35 last messages and consume them
val conversationMessages = conversation?.messages?.blockingStreamAndConsume(35)?.toList()
```

#### Change conversation name
```kotlin
val s = johnSession
val conversion = s?.conversation?.blockingList()?.firstOrNull()

conversion?.name = "new conversation title :)"
conversion?.blockingSave()
```

#### Kick/invite member from conversation
```kotlin
val s = johnSession
val conversation = s?.conversation?.blockingList()?.firstOrNull()

// kick member
conversion.blockingKickMember(user)

// invite user
conversion.blockingAddMember(user)
```

#### Send quick private message to someone
```kotlin
val s = johnSession
val user = s?.user?.blockingList()?.firstOrNull()?.users?.firstOrNull()

val message = ConversationMessagePost.Builder()
        .setMessage("Hey [[user:${user?.id}]] ! This is a quick message from our SDK #MySocialApp with an amazing picture. Enjoy")
        .setImage(File("/tmp/myimage.jpg"))
        .build()

user?.blockingSendPrivateMessage(message)
```

#### Quit conversation
```kotlin
val s = johnSession
val conversation = s?.conversation?.blockingList()?.firstOrNull()

conversation?.blockingQuit()
```

### Event
This module is optional. Please contact [us](mailto:support@mysocialapp.io) to request it

#### List 50 next events
```kotlin
val s = johnSession
s?.event?.blockingStream(50)
```

#### Create an event
```kotlin
val s = johnSession

val newarkLocation = SimpleLocation(40.736504474883915, -74.18175405)

val tomorrow = Calendar.getInstance().apply {
    time = Date()
    add(Calendar.DATE, 1)
}

val afterTomorrow = Calendar.getInstance().apply {
    time = Date()
    add(Calendar.DATE, 2)
}

val event = Event.Builder()
        .setName("New test event")
        .setDescription("This is a new event create with our SDK")
        .setStartDate(tomorrow.time)
        .setEndDate(afterTomorrow.time)
        .setLocation(newarkLocation)
        .setMaxSeats(100)
        .setMemberAccessControl(EventMemberAccessControl.PUBLIC)
        .setCoverImage(File("/tmp/image.jpg"))
        .build()

s?.event?.blockingCreate(event)
```

#### Update an event
```kotlin
event.name = "New event name"
event.save()
```

#### Join / participate to an event
```kotlin
event.blockingParticipate()
```

#### List my 10 next events
```kotlin
val s = johnSession
s?.account?.blockingGet()?.blockingStreamEvent(10)
```

#### List events between two dates
```kotlin
val s = johnSession

val tomorrow = Calendar.getInstance().apply {
    time = Date()
    add(Calendar.DATE, 1)
}

val afterTomorrow = Calendar.getInstance().apply {
    time = Date()
    add(Calendar.DATE, 2)
}

val query = FluentEvent.Search.Builder()
        .setLocationMaximumDistanceInKilometers(100.0)
        .setFromDate(tomorrow)
        .setToDate(afterTomorrow)
        .build()

s?.event?.blockingSearch(query)
```

#### Search for events by name or description
```kotlin
val s = johnSession

val query = FluentEvent.Search.Builder()
        .setName("my event name")
        .setDescription("my event description")
        .build()

s?.event?.blockingSearch(query)
```

#### Search for events by owner
```kotlin
[..]
user.blockingStreamEvent(10)
```

#### Create post on event
```kotlin
[..]
val post = FeedPost.Builder()
        .setMessage("This is a post with #hashtag url https://mysocialapp.io and someone mentioned [[user:3856809369215939951]]")
        .setVisibility(AccessControl.PUBLIC)
        .build()

event.blockingSendWallPost(post)
```

### Group
This module is optional. Please contact [us](mailto:support@mysocialapp.io) to request it 

#### List groups
```kotlin
val s = johnSession
s?.group?.blockingStream(100)
```

#### Create a group
```kotlin
val s = johnSession

val newarkLocation = SimpleLocation(40.736504474883915, -74.18175405)

val group = Group.Builder()
        .setName("New group")
        .setDescription("This is a new group create with our SDK")
        .setLocation(newarkLocation)
        .setMemberAccessControl(EventMemberAccessControl.PUBLIC)
        .setImage(File("/tmp/image.jpg"))
        .build()

s?.group?.blockingCreate(group)
```

#### Update a group
```kotlin
group.name = "New group name"
group.save()
```

#### Join a group
```kotlin
group.blockingJoin()
```

#### List my groups
```kotlin
val s = johnSession
s?.account?.blockingGet()?.blockingStreamGroup(10)
```

#### Search for groups by name or description
```kotlin
val s = johnSession

val query = FluentGroup.Search.Builder()
        .setName("my group name")
        .setDescription("my group description")
        .build()

s?.group?.blockingSearch(query)
```

#### Search for groups by owner
```kotlin
[..]
user.blockingStreamGroup(10)
```

#### Create post on group
```kotlin
[..]
val post = FeedPost.Builder()
        .setMessage("This is a post with #hashtag url https://mysocialapp.io and someone mentioned [[user:3856809369215939951]]")
        .setVisibility(AccessControl.PUBLIC)
        .build()

group.blockingSendWallPost(post)
```

### Handle exceptions

MySocialAppException and children can be handle by using the non blocking way with `.doOnError(..)` or `.onErrorResumeNext(..)`. Please refer to [RxJava](https://github.com/ReactiveX/RxJava) to know the difference between them.

Note: Exceptions handling on blocking calls are coming soon.

### Real time downstream notifications

They are coming soon and will be part of this lib using WebSocket and `mysocialapp-android-client` additional lib for Android. Expected for June/July 2018

### More examples?

[Look at our test classes for Java and Kotlin](https://github.com/MySocialApp/mysocialapp-java-client/tree/master/src/test)

# Credits

* [Kotlin](https://kotlinlang.org/)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [Retrofit](http://square.github.io/retrofit/)
* [OkHTTP3](https://github.com/square/okhttp)

# Contributions

All contributions are welcomed. Thank you