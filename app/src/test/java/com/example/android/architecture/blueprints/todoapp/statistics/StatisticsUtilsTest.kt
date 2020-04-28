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
     * Naming convention of the test case should follow be like:
     * (subjectUnderTest_actionOrInput_resultState)
     *
     * Subject under test is the method or class that is being tested, here is (getActiveAndCompletedStats).
     * Next is the action or input, here is (noCompleted).
     * Finally you have the expected result, here is (returnsHundredZero).
     */
    @Test
    fun getActiveAndCompletedStats_noCompleted_returnsHundredZero() {
        /**** Given ****/
        val tasks = listOf(Task("title1", "decs1", isCompleted = false))

        /**** When ****/
        val result = getActiveAndCompletedStats(tasks)

        /**** Then ****/
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompletedStats_noActive_returnsHundredZero() {
        val tasks = listOf(
                Task("title1", "decs1", isCompleted = true),
                Task("title2", "decs2", isCompleted = true)
        )
        val result = getActiveAndCompletedStats(tasks)
        assertThat(result.completedTasksPercent, `is`(100f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_both_returnsFortySixty() {
        val tasks = listOf(
                Task("title1", "decs1", isCompleted = false),
                Task("title2", "decs2", isCompleted = true),
                Task("title3", "decs3", isCompleted = false),
                Task("title4", "decs4", isCompleted = false),
                Task("title5", "decs5", isCompleted = true)
        )
        val result = getActiveAndCompletedStats(tasks)
        assertThat(result.completedTasksPercent, `is`(40f))
        assertThat(result.activeTasksPercent, `is`(60f))
    }

    @Test
    fun getActiveAndCompletedStats_empty_returns_Zero() {
        val tasks = emptyList<Task>()
        val result = getActiveAndCompletedStats(tasks)
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_error_returns_Zero() {
        val result = getActiveAndCompletedStats(null)
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }
}