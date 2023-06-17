package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.repository.HistoryRepository
import javax.inject.Inject

class ManageHistoryUseCaseImpl @Inject constructor(private val historyRepository: HistoryRepository) : ManageHistoryUseCase {

    override  fun saveLastCity(city: String?) {
        historyRepository.setLastCity(city)
    }

    override  fun getLastCity(): String? {
       return historyRepository.getLastCity()
    }
}