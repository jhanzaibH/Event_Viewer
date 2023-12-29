package com.example.event_viewer.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.event_viewer.database.Event
import com.example.event_viewer.database.EventDao
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private lateinit var eventDao: EventDao
    val events = MutableLiveData<List<Event>>()

    fun init(eventDao: EventDao) {
        this.eventDao = eventDao
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            events.value = eventDao.getAllEvents()
        }
    }
}
