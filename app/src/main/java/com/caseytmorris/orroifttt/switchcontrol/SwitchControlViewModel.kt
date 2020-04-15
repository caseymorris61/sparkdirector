package com.caseytmorris.orroifttt.switchcontrol

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.caseytmorris.orroifttt.database.RoomControl
import com.caseytmorris.orroifttt.database.RoomDatabaseDao
import kotlinx.coroutines.*

class SwitchControlViewModel (
    val roomControlDatabase: RoomDatabaseDao,
    application: Application)  : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val rooms: LiveData<List<RoomControl>> = roomControlDatabase.getAllRooms()

    private val _navigateToEditRoom = MutableLiveData<Long>()
    val navigateToEditRoom : LiveData<Long>
        get() = _navigateToEditRoom

    fun onRoomControlClicked(id: Long) {
        Log.i("Casey","Clicked on room: $id")
        _navigateToEditRoom.value = id
    }
    fun doneNavigating() {
        _navigateToEditRoom.value = null
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            roomControlDatabase.clearRooms()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
