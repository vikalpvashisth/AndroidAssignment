package com.app.androidassignment.webservice

import com.app.androidassignment.models.FavoriteModel
import com.app.androidassignment.models.ProductModel
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("products")
    suspend fun getProductData(): Response<ProductModel?>?

    @POST("favorites")
    suspend fun favoriteItem(): Response<FavoriteModel?>?

    @DELETE("favorites")
    suspend fun deleteItemFromFavorite(): Response<FavoriteModel?>?

}