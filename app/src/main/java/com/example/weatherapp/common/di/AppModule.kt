package com.example.weatherapp.common.di

import android.app.Application
import android.util.Log
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.common.constants.Constants
import com.example.weatherapp.common.utils.UtilityHelper
import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.data.repository.GetWeatherRepositoryImpl
import com.example.weatherapp.data.usecase.ManageWeatherPreviousImpl
import com.example.weatherapp.data.usecase.ManageWeatherUseCaseImpl
import com.example.weatherapp.domain.repository.GetWeatherRepository
import com.example.weatherapp.domain.usecase.ManageWeatherPrevious
import com.example.weatherapp.domain.usecase.ManageWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("okHttp:", message)
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideOkhttp(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient.Builder {
        val okHttpBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(httpLoggingInterceptor)
        }
        return okHttpBuilder
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient.build())
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    fun provideWeatherInfo(weatherApi: WeatherApi): GetWeatherRepository {
        return GetWeatherRepositoryImpl(
            weatherApi = weatherApi
        )
    }

    @Provides
    fun provideManageWeatherHistory( context: Application): ManageWeatherPrevious {
        return ManageWeatherPreviousImpl(context)
    }

    @Provides
    fun provideUtilityHelper(): UtilityHelper {
        return UtilityHelper()
    }

    @Provides
    fun provideManageWeatherUseCase(getWeatherRepository: GetWeatherRepository, utilityHelper: UtilityHelper): ManageWeatherUseCase {
        return ManageWeatherUseCaseImpl(getWeatherRepository = getWeatherRepository, utilityHelper = utilityHelper)
    }

}