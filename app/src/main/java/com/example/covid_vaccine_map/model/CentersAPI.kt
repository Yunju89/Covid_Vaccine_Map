package com.example.covid_vaccine_map.model

data class CentersAPI(
    val currentCount: Int,
    val `data`: MutableList<CentersModel>,
    val matchCount: Int,
    var page: Int,
    val perPage: Int,
    val totalCount: Int?
) {

    fun getTotalCount(): Int {
        return totalCount ?: 0
    }

    fun getListSize(): Int {
        return data.size
    }
}