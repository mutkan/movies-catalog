package study.moviescatalog.di

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import study.moviescatalog.repository.remote.utils.CacheRevalidationInterceptor
import study.moviescatalog.repository.remote.utils.isNetworkAvailable
import study.moviescatalog.repository.remote.utils.ItemTypeAdapterFactory
import study.moviescatalog.repository.remote.MoviesAPI
import java.io.File
import java.util.concurrent.TimeUnit

@Module
class RemoteModule {

    @Provides
    @RemoteUrl
    fun provideBaseURl(): String = "https://api.themoviedb.org/"

    @Provides
    @ApiKey
    fun provideApiKey(): String = "6529fbcde4ba3050af3b976c183a6f84"

    @Provides
    fun provideMovieApi(retrofit: Retrofit): MoviesAPI {
        return retrofit
                .create(MoviesAPI::class.java)
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapterFactory(ItemTypeAdapterFactory())
        gsonBuilder.setDateFormat("yyyy-MM-dd")

        val gson = gsonBuilder.create()
        return GsonConverterFactory.create(gson)
    }

    @Provides
    fun provideRetrofit(@RemoteUrl remoteBaseUrl: String,
                        gsonConverterFactory: GsonConverterFactory,
                        okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(remoteBaseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .client(okHttpClient)
                .build()
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor,
                            context: Context): OkHttpClient {

        val httpCacheDirectory = File(context.cacheDir, "app_cache")
        val cacheSize = 128L * 1024 * 1024
        val cache = Cache(httpCacheDirectory, cacheSize)

        val builder = OkHttpClient().newBuilder()
                .addInterceptor { chain ->
                    val request = chain.request()
                            .newBuilder()
                            .cacheControl(CacheControl.Builder()
                                    .maxAge(10, TimeUnit.MINUTES)
                                    .maxStale(30, TimeUnit.MINUTES)
                                    .build())
                            .build()

                    val originalResponse = chain.proceed(request)
                    if (context.isNetworkAvailable()) {
                        val maxAge = 60 * 10 // 10 mins
                        return@addInterceptor originalResponse.newBuilder()
                                .header("Cache-Control", "public, max-age=$maxAge, max-stale=$maxAge")
                                .build()
                    } else {
                        val maxStale = 60 * 30 // 30 mins
                        return@addInterceptor originalResponse.newBuilder()
                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                .build()
                    }
                }
                .addInterceptor(CacheRevalidationInterceptor(context))
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(2, TimeUnit.SECONDS)

        return builder.build()
    }
}