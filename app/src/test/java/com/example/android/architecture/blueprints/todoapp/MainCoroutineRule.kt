package com.example.android.architecture.blueprints.todoapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * If you're using coroutines in your app, any local test that involves calling code in a view model
 * is highly likely to call code which uses viewModelScope.
 *
 * Instead of copying and pasting the code to set up and tear down the TestCoroutineDispatcher into each test class,
 * you can make a custom JUnit rule to avoid this boilerplate code.
 *
 * For local tests (tests that run on your local machine in the test source set),
 * the usage of Dispatcher.Main causes an issue: Dispatchers.Main uses Android's Looper.getMainLooper().
 *
 * The main looper is the execution loop for a real application. The main looper is not available (by default) in local tests,
 * because you're not running the full application.
 * To address this, use the method setMain() (from kotlinx.coroutines.test) to modify Dispatchers.Main to use TestCoroutineDispatcher.
 *
 * TestCoroutineDispatcher is a dispatcher specifically meant for testing.
 */
@ExperimentalCoroutinesApi
class MainCoroutineRule(val dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()):
   TestWatcher(),
   TestCoroutineScope by TestCoroutineScope(dispatcher) {
   override fun starting(description: Description?) {
       super.starting(description)
       Dispatchers.setMain(dispatcher)
   }

   override fun finished(description: Description?) {
       super.finished(description)
       cleanupTestCoroutines()
       Dispatchers.resetMain()
   }
}