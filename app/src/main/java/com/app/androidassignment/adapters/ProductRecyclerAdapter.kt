package com.app.androidassignment.adapters

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.app.androidassignment.R
import com.app.androidassignment.databinding.RecyclerRowBinding
import com.app.androidassignment.helpers.GlideApp
import com.app.androidassignment.interfaces.ProductRecyclerAdapterClick
import com.app.androidassignment.models.Hits

// recycler adapter for displaying product list on ui
class ProductRecyclerAdapter(private val productRecyclerAdapterClick: ProductRecyclerAdapterClick) :
    RecyclerView.Adapter<ProductRecyclerAdapter.ProductViewHolder>(), Filterable {

    private var productListData = ArrayList<Hits>() // original data received from server
    var filteredDataList = ArrayList<Hits>() // filtered data as per user search

    // used to get data from activity after being received from the products api
    @SuppressLint("NotifyDataSetChanged")
    fun passingDataToAdapter(productList: ArrayList<Hits>?) {
        productList?.let {
            this.productListData = it
            this.filteredDataList = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val productView =
            RecyclerRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(productView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productModel = this.filteredDataList[position]
        holder.bindData(productModel, position)
    }

    override fun getItemCount(): Int {
        return this.filteredDataList.size
    }

    // viewholder class used to initialize the views for recycler view
    inner class ProductViewHolder(private val rowBinding: RecyclerRowBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(productModel: Hits, position: Int) {

            setProductImage(productModel)
            setBestsellerBadge(productModel)
            setListPrice(productModel)
            setFavouriteProduct(productModel, position)

            rowBinding.yourPriceTv.text =
                StringBuilder("$").append(productModel.vendor_inventory.first().price.toString())
                    .toString()
            rowBinding.titleTv.text = productModel.name

        }

        // used to mark if the product is favourite or not and also set click listener on it
        private fun setFavouriteProduct(productModel: Hits, position: Int) {
            if (productModel.is_favourite_product) {
                GlideApp.with(rowBinding.favouriteIv.context).load(R.drawable.ic_icon_heart_filled)
                    .into(rowBinding.favouriteIv)
            } else {
                GlideApp.with(rowBinding.favouriteIv.context)
                    .load(R.drawable.ic_icon_heart_unfilled)
                    .into(rowBinding.favouriteIv)
            }

            rowBinding.favouriteIv.setOnClickListener {
                productRecyclerAdapterClick.onFavouriteClick(productModel, position)
            }
        }

        // used to set list price
        private fun setListPrice(productModel: Hits) {
            rowBinding.listPriceTv.paintFlags =
                rowBinding.listPriceTv.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            rowBinding.listPriceTv.text =
                StringBuilder("$").append(productModel.vendor_inventory.first().list_price.toString())
                    .toString()
        }

        // used to show the bestseller image if the product is bestseller
        private fun setBestsellerBadge(productModel: Hits) {
            if (productModel.advertising_badges.has_badge) {
                rowBinding.bestsellerItemIv.visibility = View.VISIBLE
                GlideApp.with(rowBinding.bestsellerItemIv.context)
                    .load(productModel.advertising_badges.badges.first().badge_image_url)
                    .into(rowBinding.bestsellerItemIv)
            } else {
                rowBinding.bestsellerItemIv.visibility = View.GONE
            }
        }

        // used to set product image if it is available otherwise display the error image
        private fun setProductImage(productModel: Hits) {
            rowBinding.noImageFoundIv.visibility = View.VISIBLE
            GlideApp.with(rowBinding.productIv.context).load(R.drawable.search_background)
                .into(rowBinding.productIv)
            if (!productModel.main_image.isNullOrEmpty()) {
                rowBinding.noImageFoundIv.visibility = View.GONE
                GlideApp.with(rowBinding.productIv.context).load(productModel.main_image)
                    .into(rowBinding.productIv)
            } else {
                rowBinding.noImageFoundIv.visibility = View.VISIBLE
                GlideApp.with(rowBinding.noImageFoundIv.context).load(R.drawable.ic_no_image_found)
                    .into(rowBinding.noImageFoundIv)
            }
        }

    }

    // used to filter the data as per the search text, add it in filtered list and notify the result to the adapter
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredDataList = if (charSearch.isEmpty()) {
                    productListData
                } else {
                    val filterPattern = constraint.toString().lowercase().trim { it <= ' ' }
                    val resultList = ArrayList<Hits>()
                    for (item in productListData) {
                        if (item.name.lowercase().contains(filterPattern)) {
                            resultList.add(item)
                        }
                    }
                    resultList
                }
                val results = FilterResults()
                results.values = filteredDataList
                return results
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredDataList = results?.values as ArrayList<Hits>
                notifyDataSetChanged()
            }
        }
    }
}