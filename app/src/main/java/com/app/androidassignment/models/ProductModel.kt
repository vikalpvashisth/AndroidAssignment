package com.app.androidassignment.models

data class ProductModel(var hits: ArrayList<Hits>)

data class Hits(
    var main_image: String?,
    var name: String,
    var vendor_inventory: List<VendorInventory>,
    var advertising_badges: AdvertisingBadges,
    var is_favourite_product: Boolean
)

data class VendorInventory(
    var list_price: Double,
    var price: Double
)

data class AdvertisingBadges(
    var badges: ArrayList<Badges>,
    var has_badge: Boolean
)

data class Badges(
    var badge_image_url: String
)


