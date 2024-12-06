package com.example.waypointjournalosm.utils

import kotlin.math.max
import android.util.Log

fun stringSimilarity(str1: String, str2: String): Double {
    val levenshteinDistance = calculateLevenshteinDistance(str1, str2)
    val maxLength = max(str1.length, str2.length)
    val similarity = if (maxLength > 0) {
        (1 - levenshteinDistance.toDouble() / maxLength) * 100
    } else {
        100.0 // Both strings are empty, consider 100% similar
    }

    // Log the result
    Log.d("StringSimilarity", "Comparing '$str1' and '$str2': Levenshtein Distance = $levenshteinDistance, Similarity = $similarity%")

    return similarity
}

fun calculateLevenshteinDistance(str1: String, str2: String): Int {
    val dp = Array(str1.length + 1) { IntArray(str2.length + 1) }
    for (i in 0..str1.length) dp[i][0] = i
    for (j in 0..str2.length) dp[0][j] = j

    for (i in 1..str1.length) {
        for (j in 1..str2.length) {
            val cost = if (str1[i - 1] == str2[j - 1]) 0 else 1
            dp[i][j] = minOf(
                dp[i - 1][j] + 1,
                dp[i][j - 1] + 1,
                dp[i - 1][j - 1] + cost
            )
        }
    }
    return dp[str1.length][str2.length]
}