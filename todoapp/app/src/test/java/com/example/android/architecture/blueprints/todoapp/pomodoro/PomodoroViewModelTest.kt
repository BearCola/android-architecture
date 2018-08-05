/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.architecture.blueprints.todoapp.pomodoro


import android.app.Application
import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.util.capture
import com.google.common.collect.Lists
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Unit tests for the implementation of [PomodoroViewModel]
 */
class PomodoroViewModelTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()
    @Mock private lateinit var tasksRepository: TasksRepository
    @Captor private lateinit var loadTasksCallbackCaptor:
            ArgumentCaptor<TasksDataSource.LoadTasksCallback>
    private lateinit var pomodoroViewModel: PomodoroViewModel
    private lateinit var tasks: MutableList<Task>

    @Before fun setupStatisticsViewModel() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this)

        // Get a reference to the class under test
        pomodoroViewModel = PomodoroViewModel(mock(Application::class.java), tasksRepository)

        // We initialise the tasks to 3, with one active and two completed
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2").apply {
            isCompleted = true
        }
        val task3 = Task("Title3", "Description3").apply {
            isCompleted = true
        }
        tasks = Lists.newArrayList(task1, task2, task3)
    }

    @Test fun loadEmptyTasksFromRepository_EmptyResults() {
        // Given an initialized PomodoroViewModel with no tasks
        tasks.clear()

        // When loading of Tasks is requested
        pomodoroViewModel.loadStatistics()

        // Callback is captured and invoked with stubbed tasks
        verify<TasksRepository>(tasksRepository).getTasks(capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onTasksLoaded(tasks)

        // Then the results are empty
        assertThat(pomodoroViewModel.empty.get(), `is`(true))
    }

    @Test fun loadNonEmptyTasksFromRepository_NonEmptyResults() {
        // When loading of Tasks is requested
        pomodoroViewModel.loadStatistics()

        // Callback is captured and invoked with stubbed tasks
        verify<TasksRepository>(tasksRepository).getTasks(capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onTasksLoaded(tasks)

        // Then the results are empty
        assertThat(pomodoroViewModel.empty.get(), `is`(false))
    }


    @Test fun loadStatisticsWhenTasksAreUnavailable_CallErrorToDisplay() {
        // When statistics are loaded
        pomodoroViewModel.loadStatistics()

        // And tasks data isn't available
        verify<TasksRepository>(tasksRepository).getTasks(capture(loadTasksCallbackCaptor))
        loadTasksCallbackCaptor.value.onDataNotAvailable()

        // Then an error message is shown
        assertEquals(pomodoroViewModel.empty.get(), true)
        assertEquals(pomodoroViewModel.error.get(), true)
    }
}
