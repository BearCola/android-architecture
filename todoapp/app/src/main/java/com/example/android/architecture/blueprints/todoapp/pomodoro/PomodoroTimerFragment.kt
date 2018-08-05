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

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.databinding.StatisticsFragBinding

/**
 * Main UI for the statistics screen.
 */
class PomodoroTimerFragment : Fragment() {

    private lateinit var viewDataBinding: StatisticsFragBinding

    private lateinit var pomodoroViewModel: PomodoroViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.statistics_frag, container,
                false)
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pomodoroViewModel = (activity as PomodoroTimerActivity).obtainViewModel()
        viewDataBinding.stats = pomodoroViewModel
    }

    override fun onResume() {
        super.onResume()
        pomodoroViewModel.start()
    }

    companion object {
        fun newInstance() = PomodoroTimerFragment()
    }
}
