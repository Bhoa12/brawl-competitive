package com.bhoa.brawlcompetitive.data

data class MetaEntry(val brawlerName: String, val tier: String, val metaScore: Float, val pickRate: Float?, val winRate: Float?, val trend: Float?, val sampleSize: Int, val patch: String)
data class MetaSnapshot(val updatedAt: String, val patch: String, val sampleSize: Int, val entries: List<MetaEntry>)
