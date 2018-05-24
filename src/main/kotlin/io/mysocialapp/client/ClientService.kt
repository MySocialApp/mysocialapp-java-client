package io.mysocialapp.client

import io.mysocialapp.client.repositories.*
import io.mysocialapp.client.utils.MyObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by evoxmusic on 27/04/2018.
 */
class ClientService(private val configuration: Configuration,
                    private val clientConfiguration: ClientConfiguration,
                    private val session: Session? = null) {

    val login: RestLogin by lazy { retrofit.create(RestLogin::class.java) }
    val logout: RestLogout by lazy { retrofit.create(RestLogout::class.java) }
    val reset: RestReset by lazy { retrofit.create(RestReset::class.java) }
    val register: RestRegister by lazy { retrofit.create(RestRegister::class.java) }
    val account: RestAccount by lazy { retrofit.create(RestAccount::class.java) }
    val feed: RestFeed by lazy { retrofit.create(RestFeed::class.java) }
    val feedComment: RestFeedComment by lazy { retrofit.create(RestFeedComment::class.java) }
    val feedLike: RestFeedLike by lazy { retrofit.create(RestFeedLike::class.java) }
    val photo: RestPhoto by lazy { retrofit.create(RestPhoto::class.java) }
    val photoComment: RestPhotoComment by lazy { retrofit.create(RestPhotoComment::class.java) }
    val photoLike: RestPhotoLike by lazy { retrofit.create(RestPhotoLike::class.java) }
    val status: RestStatus by lazy { retrofit.create(RestStatus::class.java) }
    val statusLike: RestStatusLike by lazy { retrofit.create(RestStatusLike::class.java) }
    val statusComment: RestStatusComment by lazy { retrofit.create(RestStatusComment::class.java) }
    val user: RestUser by lazy { retrofit.create(RestUser::class.java) }
    val userExternal: RestUserExternal by lazy { retrofit.create(RestUserExternal::class.java) }
    val userActive: RestUserActive by lazy { retrofit.create(RestUserActive::class.java) }
    val userFriend: RestUserFriend by lazy { retrofit.create(RestUserFriend::class.java) }
    val userWall: RestUserWall by lazy { retrofit.create(RestUserWall::class.java) }
    val userWallMessage: RestUserWallMessage by lazy { retrofit.create(RestUserWallMessage::class.java) }
    val friendRequest: RestFriendRequest by lazy { retrofit.create(RestFriendRequest::class.java) }
    val notificationRead: RestNotificationRead by lazy { retrofit.create(RestNotificationRead::class.java) }
    val notificationUnread: RestNotificationUnread by lazy { retrofit.create(RestNotificationUnread::class.java) }
    val notificationUnreadConsume: RestNotificationUnreadConsume by lazy { retrofit.create(RestNotificationUnreadConsume::class.java) }
    val notificationAck: RestNotificationAck by lazy { retrofit.create(RestNotificationAck::class.java) }
    val search: RestSearch by lazy { retrofit.create(RestSearch::class.java) }
    val conversation: RestConversation by lazy { retrofit.create(RestConversation::class.java) }
    val conversationMessage: RestConversationMessage by lazy { retrofit.create(RestConversationMessage::class.java) }
    val conversationMessageConsume: RestConversationMessageConsume by lazy { retrofit.create(RestConversationMessageConsume::class.java) }
    val conversationMessagePhoto: RestConversationMessagePhoto by lazy { retrofit.create(RestConversationMessagePhoto::class.java) }

    private val retrofit by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        httpLoggingInterceptor.level = if (clientConfiguration.debug) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        val coreOkHttpClient = OkHttpClient.Builder()
                .connectionPool(clientConfiguration.connectionPool)
                .readTimeout(clientConfiguration.readTimeoutInMilliseconds, TimeUnit.MILLISECONDS)
                .writeTimeout(clientConfiguration.writeTimeoutInMilliseconds, TimeUnit.MILLISECONDS)
                .addInterceptor { chain ->

                    val builder = chain.request().newBuilder()

                    // add request headers
                    clientConfiguration.headersToInclude?.forEach { k, v -> builder.addHeader(k, v) }

                    session?.authenticationToken?.accessToken?.let { builder.addHeader("Authorization", it) }

                    chain.proceed(builder.build())
                }
                .addInterceptor { chain ->

                    val res = chain.proceed(chain.request().newBuilder().build())

                    if (res.code() == 401) {
                        // TODO add handler
                    } else if (res?.code() == 400 || res?.code() == 403) {
                        // TODO add handler
                    }

                    res
                }
                .addInterceptor(httpLoggingInterceptor)
                .build()

        Retrofit.Builder()
                .baseUrl(configuration.completeAPIEndpointURL)
                .client(coreOkHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(MyObjectMapper.objectMapper))
                .build()
    }

}