package com.app.androidassignment.ui.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.androidassignment.R
import com.app.androidassignment.adapters.ProductRecyclerAdapter
import com.app.androidassignment.databinding.ActivityMainBinding
import com.app.androidassignment.helpers.NetworkHandler
import com.app.androidassignment.interfaces.ProductRecyclerAdapterClick
import com.app.androidassignment.models.Hits
import com.app.androidassignment.models.ProductModel
import com.app.androidassignment.viewmodels.ProductViewModel

/**
 * MainActivity is the launcher activity of this app
 * It displays the user a list of products fetched from the products endpoint
 */
class MainActivity : AppCompatActivity(), ProductRecyclerAdapterClick {


    // lazy loading activity binding
    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // lazy loading view model instance
    private val productViewModel: ProductViewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }

    // lazy loading recycler adapter instance
    private val productRecyclerAdapter: ProductRecyclerAdapter by lazy {
        ProductRecyclerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)
        init()
    }

    /**
     * init function is used to initialize the various views and hit product list api
     */
    private fun init() {
        setRecyclerView()
        getProductList()
        initializeSearchListener()
        initializeSwipeRefresh()
    }

    // initializes the recycler view adapter and adds layout manager and decoration to it
    private fun setRecyclerView() {
        mainBinding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mainBinding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        mainBinding.recyclerView.adapter = productRecyclerAdapter
    }

    // initializes swipe refresh view and hits product list api when refreshed
    private fun initializeSwipeRefresh() {
        mainBinding.swipeRefreshView.setOnRefreshListener {
            getProductList()
            mainBinding.swipeRefreshView.isRefreshing = false
        }
    }

    // initializes search listener and adds text changed listener to perform searching on list
    private fun initializeSearchListener() {
        mainBinding.productSearchView.addTextChangedListener {
            productRecyclerAdapter.filter.filter(it.toString())
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                if (productRecyclerAdapter.filteredDataList.isEmpty()) {
                    mainBinding.noConnectionView.visibility = View.VISIBLE
                    mainBinding.recyclerView.visibility = View.GONE
                    mainBinding.noConnectionView.text = getString(R.string.product_not_found)
                } else {
                    mainBinding.noConnectionView.visibility = View.GONE
                    mainBinding.recyclerView.visibility = View.VISIBLE
                    mainBinding.noConnectionView.text =
                        getString(R.string.oops_internet_error_please_connect_to_the_internet)
                }
            }, 500)
        }
    }

    // used to hit product list api and manage its data
    private fun getProductList() {
        if (NetworkHandler.isConnected(mainBinding.root.context)) {
            mainBinding.progressCircular.visibility = View.VISIBLE
            val productObserver = productViewModel.getProductData()
            if (!productObserver.hasActiveObservers()) {
                productObserver.observe(this) { product ->
                    product?.let {
                        handlingProductListResponse(it)
                    }
                }
            }
        } else {
            mainBinding.recyclerView.visibility = View.GONE
            mainBinding.noConnectionView.visibility = View.VISIBLE

        }
    }

    // handles the response received from product list api
    private fun handlingProductListResponse(it: ProductModel) {
        productViewModel.productList = it.hits
        productRecyclerAdapter.passingDataToAdapter(productViewModel.productList)
        mainBinding.progressCircular.visibility = View.GONE
        mainBinding.recyclerView.visibility = View.VISIBLE
        mainBinding.noConnectionView.visibility = View.GONE
    }

    override fun onFavouriteClick(productModel: Hits, position: Int) {
        if (productModel.is_favourite_product) {
            hitApiForDeleteFavoriteItem(productModel, position)
        } else {
            hitApiForFavoriteItem(productModel, position)
        }

    }

    private fun hitApiForDeleteFavoriteItem(productModel: Hits, position: Int) {
        if (NetworkHandler.isConnected(mainBinding.root.context)) {
            val favoriteItemObserver = productViewModel.deleteThisItemFromFavorite()
            if (!favoriteItemObserver.hasActiveObservers()) {
                favoriteItemObserver.observe(this) { favorite ->
                    favorite?.let {
                        productModel.is_favourite_product = it.favorite
                        productRecyclerAdapter.notifyItemChanged(position)

                    }
                }
            }
        } else {
            Toast.makeText(
                this,
                getString(R.string.oops_internet_error_please_connect_to_the_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun hitApiForFavoriteItem(productModel: Hits, position: Int) {
        if (NetworkHandler.isConnected(mainBinding.root.context)) {
            val deleteFavoriteItemObserver = productViewModel.favoriteThisItem()
            if (!deleteFavoriteItemObserver.hasActiveObservers()) {
                deleteFavoriteItemObserver.observe(this) { favorite ->
                    favorite?.let {
                        productModel.is_favourite_product = it.favorite
                        productRecyclerAdapter.notifyItemChanged(position)
                    }
                }
            }
        } else {
            Toast.makeText(
                this,
                getString(R.string.oops_internet_error_please_connect_to_the_internet),
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}