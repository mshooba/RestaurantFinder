package com.sciubba.apihomework.data.api.model
import APIService
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LocationViewModel: ViewModel() {

    //private and public versions of the list
    private val _locationList = mutableStateListOf<Location>()
    var errorMessage: String by mutableStateOf("")

    // Correct the typo in the variable name
    val locationList: List<Location> get() = _locationList

    private val locationIds = listOf(
        //paris
        "187147",
        //rome
        "187791",
        //tokyo
        "298184",
        //ny
        "60763",
        //copenhagen
        "189541",
        //hong kong
        "294217"
    ) // Add your location IDs here

    fun getLocations() {
        viewModelScope.launch {
            val apiService = APIService.getInstance()
            _locationList.clear()  // Clear the list at the beginning of the operation
            locationIds.forEach { locationId ->
                try {
                    val locationDetail = apiService.getLocationDetails(locationId, "5C14D491649840D584D022810F2F1D4F")
                    Log.d("LocationViewModel", "Fetched location: $locationDetail")
                    _locationList.add(locationDetail)
                } catch (e: Exception) {
                    Log.e("LocationViewModel", "Failed to fetch details", e)
                    errorMessage = "Failed to fetch details for location ID $locationId: ${e.message}"
                }
            }
        }
    }



}//ViewModel



