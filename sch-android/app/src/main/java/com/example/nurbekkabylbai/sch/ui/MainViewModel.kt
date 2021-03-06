package com.example.nurbekkabylbai.sch.ui

import android.arch.lifecycle.ViewModel
import com.example.nurbekkabylbai.sch.ResponseListener
import com.example.nurbekkabylbai.sch.repository.InfoRepository
import com.example.nurbekkabylbai.sch.repository.ScheduleRepository

/**
 * Created by Nurbek Kabylbay on 28.01.2018.
 */
class MainViewModel : ViewModel() {

    private val scheduleRepository = ScheduleRepository()
    private val infoRepository = InfoRepository()

    fun requestClasses(weekDay: Int) {
        scheduleRepository.requestSchedule(weekDay)
    }

    fun setListener(listener: ResponseListener) {
        scheduleRepository.setResponseListener(listener)
    }

    fun requestEntities() {
        infoRepository.requestCourses()
        infoRepository.requestRooms()
        infoRepository.requestTeachers()
        infoRepository.requestGroups()
    }
}