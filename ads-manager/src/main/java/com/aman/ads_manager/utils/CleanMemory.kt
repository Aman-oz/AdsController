package com.aman.ads_manager.utils

import com.aman.ads_manager.constants.AdsConstants

/**
 * @Author: Aman Ullah
 * @Date: 08,May,2024.
 * Software Engineer Android
 */
object CleanMemory {
    fun clean() {
        AdsConstants.reset()
    }
}