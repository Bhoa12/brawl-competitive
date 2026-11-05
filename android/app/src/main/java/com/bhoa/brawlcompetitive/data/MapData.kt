package com.bhoa.brawlcompetitive.data

data class BrawlMap(
    val id: Int,
    val name: String,
    val mode: String? = null,
    val imageUrl: String? = null,
    val environment: String? = null
)
