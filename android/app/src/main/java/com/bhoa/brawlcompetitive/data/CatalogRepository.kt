package com.bhoa.brawlcompetitive.data

import com.bhoa.brawlcompetitive.BuildConfig
import com.bhoa.brawlcompetitive.network.CatalogApi
import com.bhoa.brawlcompetitive.network.RemoteCatalog

class CatalogRepository(
    private val api: CatalogApi = CatalogApi(BuildConfig.COMPETITIVE_API_BASE_URL)
) {
    @Volatile private var cache: RemoteCatalog? = null

    fun current(): RemoteCatalog? = cache

    fun refresh(): RemoteCatalog {
        val fresh = api.fetch()
        cache = fresh
        return fresh
    }
}
