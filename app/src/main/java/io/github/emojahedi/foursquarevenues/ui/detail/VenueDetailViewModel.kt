package io.github.emojahedi.foursquarevenues.ui.detail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.emojahedi.foursquarevenues.data.VenueEntryWithState
import io.github.emojahedi.foursquarevenues.data.source.VenuesRepository
import io.github.emojahedi.foursquarevenues.data.source.database.CacheDatabase
import io.github.emojahedi.foursquarevenues.data.source.database.CacheDataSourceImpl
import io.github.emojahedi.foursquarevenues.data.source.network.FoursquareClient
import io.github.emojahedi.foursquarevenues.data.source.network.NetworkDataSourceImpl

class VenueDetailViewModel(application: Application) : ViewModel() {

    private val repository: VenuesRepository

    init {
        val cacheDB = CacheDatabase.getDatabase(application)
        val databaseDataSource = CacheDataSourceImpl.getInstance(cacheDB)
        val foursquareClient = FoursquareClient.getInstance(application)
        val networkDataSource = NetworkDataSourceImpl.getInstance(foursquareClient)
        repository = VenuesRepository.getInstance(databaseDataSource, networkDataSource)
    }

    fun getVenueDetail(id: String): LiveData<VenueEntryWithState> {
        return repository.getVenueDetail(id)
    }

}