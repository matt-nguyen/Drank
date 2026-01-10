package com.nghianguyen.drinks.usecase.request

data class AddBrandRequest(val brandName: String) {
    init {
        require(brandName.isNotBlank() && brandName.isNotEmpty()) {
            "brandName should not be empty nor blank: $brandName"
        }
    }
}
