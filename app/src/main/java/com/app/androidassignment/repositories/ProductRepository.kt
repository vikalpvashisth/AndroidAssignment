package com.app.androidassignment.repositories

import com.app.androidassignment.webservice.RetrofitClient

class ProductRepository {

    private var apiService = RetrofitClient.getRetrofitClient()

    suspend fun getProductData() = apiService.getProductData()

    suspend fun favoriteItem() = apiService.favoriteItem()

    suspend fun deleteItemFromFavorite() = apiService.deleteItemFromFavorite()
}