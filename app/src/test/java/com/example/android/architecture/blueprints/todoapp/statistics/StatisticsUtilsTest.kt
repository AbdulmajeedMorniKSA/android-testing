package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by Abdulmajeed Alyafey on 4/28/20.
 */
class StatisticsUtilsTest {

    /**
     * Name of the test case should follow this pattern:
     * subjectUnderTest_actionOrInput_resultState
     *
     * Subject under test is the method or class that is being tested (getActiveAndCompletedStats).
     * Next is the action or input (noCompleted).
     * Finally you have the expected result (returnsFiftyFifty).
     */
    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsFiftyFifty() {
        /**** Given ****/
        val tasks = listOf(
             Task("title1", "decs1", isCompleted = false),
             Task("title2", "decs2", isCompleted = true),
             Task("title3", "decs3", isCompleted = true),
             Task("title4", "decs4", isCompleted = false)
        )
        /**** When ****/
        val result = getActiveAndCompletedStats(tasks)

        /**** Then ****/
        assertThat(result.completedTasksPercent, `is`(50f))
        assertThat(result.activeTasksPercent, `is`(50f))
    }
}