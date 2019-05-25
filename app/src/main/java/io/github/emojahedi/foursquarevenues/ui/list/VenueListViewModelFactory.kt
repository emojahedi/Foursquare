package io.github.emojahedi.foursquarevenues.ui.list

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VenueListViewModelFactory(private val application : Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VenueListViewModel(application) as T
    }
}