package com.example.weatherapp.common.di

import android.content.Context
import android.util.Log
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.common.constants.Constants
import com.example.weatherapp.common.utils.UtilityHelper
import com.example.weatherapp.data.local.DataStoreProvider
import com.example.weatherapp.data.remote.WeatherApi
import com.example.weatherapp.data.repository.GetWeatherRepositoryImpl
import com.example.weatherapp.data.repository.HistoryRepositoryImpl
import com.example.weatherapp.domain.usecase.ManageHistoryUseCaseImpl
import com.example.weatherapp.domain.usecase.ManageWeatherUseCaseImpl
import com.example.weatherapp.domain.repository.GetWeatherRepository
import com.example.weatherapp.domain.repository.HistoryRepository
import com.example.weatherapp.domain.usecase.ManageHistoryUseCase
import com.example.weatherapp.domain.usecase.ManageWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideManageWeatherHistory( historyRepository: HistoryRepository): ManageHistoryUseCase {
        return ManageHistoryUseCaseImpl(historyRepository)
    }

    @Provides
    fun provideHistoryRepository( dataStore: DataStoreProvider): HistoryRepository {
        return HistoryRepositoryImpl(dataStore)
    }

    @Provides
    fun provideDataStore(@ApplicationContext applicationContext: Context): DataStoreProvider {
        return DataStoreProvider(applicationContext)
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