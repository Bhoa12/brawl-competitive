package com.bhoa.brawlcompetitive.data

data class CompetitiveBuild(
    val gadget: VisualAsset? = null,
    val starPower: VisualAsset? = null,
    val gear1: VisualAsset? = null,
    val gear2: VisualAsset? = null,
    val hypercharge: VisualAsset? = null,
    val patch: String? = null
)

data class CounterRecommendation(
    val targetName: String,
    val counterName: String,
    val confidence: Float,
    val reason: String,
    val build: CompetitiveBuild
)
