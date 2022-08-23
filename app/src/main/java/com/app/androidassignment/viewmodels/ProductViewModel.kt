package com.app.androidassignment.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.androidassignment.models.FavoriteModel
import com.app.androidassignment.models.Hits
import com.app.androidassignment.models.ProductModel
import com.app.androidassignment.repositories.ProductRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel : ViewModel() {

    private val TAG = "ProductViewModel"
    val productRepository: ProductRepository by lazy {
        ProductRepository()
    }
    var productList: ArrayList<Hits>? = null
    private val handler = CoroutineExceptionHandler { _, throwable ->
        println("Exception Caught $throwable")
    }

    //used to fetch product list data from product endpoint
    fun getProductData(): LiveData<ProductModel?> {
        val productListMutableLiveData = MutableLiveData<ProductModel?>()
        viewModelScope.launch(Dispatchers.IO.plus(handler)) {
            val response = productRepository.getProductData()
            withContext(Dispatchers.Main) {
                response?.let {
                    if (it.isSuccessful) {
                        productListMutableLiveData.value = it.body()
                    } else {
                        productListMutableLiveData.value = null
                        Log.e(TAG, "getProductData: " + it.errorBody().toString())
                    }
                }
            }

        }
        return productListMutableLiveData
    }


    fun favoriteThisItem(): LiveData<FavoriteModel> {
        val favoriteItem = MutableLiveData<FavoriteModel>()
        viewModelScope.launch(Dispatchers.IO.plus(handler)) {
            val response = productRepository.favoriteItem()
            withContext(Dispatchers.Main) {
                response?.let {
                    if (it.isSuccessful) {
                        favoriteItem.value = it.body()
                    } else {
                        favoriteItem.value = null
                        Log.e(TAG, "favoriteThisItem: " + it.errorBody().toString())
                    }
                }
            }
        }
        return favoriteItem
    }

    fun deleteThisItemFromFavorite(): LiveData<FavoriteModel> {
        val deletedItemFromFavorite = MutableLiveData<FavoriteModel>()
        viewModelScope.launch(Dispatchers.IO.plus(handler)) {
            val response = productRepository.deleteItemFromFavorite()
            withContext(Dispatchers.Main) {
                response?.let {
                    if (it.isSuccessful) {
                        deletedItemFromFavorite.value = it.body()
                    } else {
                        deletedItemFromFavorite.value = null
                        Log.e(TAG, "favoriteThisItem: " + it.errorBody().toString())
                    }
                }
            }
        }
        return deletedItemFromFavorite
    }

}