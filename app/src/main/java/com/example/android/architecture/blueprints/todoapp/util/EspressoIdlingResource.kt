package com.example.android.architecture.blueprints.todoapp.util

import androidx.test.espresso.idling.CountingIdlingResource

/**
 * Deals with the long running operation in your repository.
 */
object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    /**
     * Allows you to increment and decrement a counter such that:
     * - When the counter is greater than zero, the app is considered working.
     * - When the counter is zero, the app is considered idle.
     *
     * Basically, whenever the app starts doing some work, increment the counter. When that work finishes, decrement the counter.
     *
     * Therefore, CountingIdlingResource will only have a "count" of zero if there is no work being done.
     *
     * This is a singleton so that you can access this idling resource anywhere in the app where long-running work might be done.
     *
     * Allows Espresso to waits for data to be loaded.
     */
    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}

inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
    // Espresso does not work well with coroutines yet. See
    // https://github.com/Kotlin/kotlinx.coroutines/issues/982
    EspressoIdlingResource.increment() // Set app as busy.
    return try {
        function()
    } finally {
        EspressoIdlingResource.decrement() // Set app as idle.
    }
}