package com.app.androidassignment.interfaces

import com.app.androidassignment.models.Hits


interface ProductRecyclerAdapterClick {

    fun onFavouriteClick(productModel: Hits, position: Int)
}