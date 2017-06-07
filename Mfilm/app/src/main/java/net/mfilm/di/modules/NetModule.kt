package net.mfilm.di.module


import android.app.Application
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import net.mfilm.BuildConfig
import net.mfilm.data.network_retrofit.ApisService
import net.mfilm.data.network_retrofit.RetrofitService
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Dieu on 22/02/2017.
 */

@Module
class NetModule(private val param: String) {

//    @Provides
//    @Singleton
//    fun providesApiSetting(application: Application): ApiSetting {
//        Timber.d("xyz--providesApiSetting--")
//        return ApiSetting(param)
//    }


    @Provides
    @Singleton
    fun provideOkHttpCache(application: Application): Cache {
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(application.cacheDir, cacheSize.toLong())
        return cache
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(cache: Cache): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .build()
        return client
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .build()
        return retrofit
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ApisService {
        return retrofit.create(ApisService::class.java)
    }

    @Provides
    @Singleton
    fun providesRetrofitService(apiService: ApisService): RetrofitService {
        return RetrofitService(apiService)
    }

    companion object {

        private val TAG = NetModule::class.java.simpleName
    }

}
