package io.github.emojahedi.foursquarevenues.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.emojahedi.foursquarevenues.data.ListOfVenueEntriesWithState
import io.github.emojahedi.foursquarevenues.data.StateOfLoading
import io.github.emojahedi.foursquarevenues.data.VenueEntryWithState
import io.github.emojahedi.foursquarevenues.data.source.database.CacheDataSource
import io.github.emojahedi.foursquarevenues.data.source.database.VenueEntry
import io.github.emojahedi.foursquarevenues.data.source.network.NetworkDataSource


class VenuesRepository(
    private val cacheDataSource: CacheDataSource,
    private val networkDataSource: NetworkDataSource
) {

    companion object {
        @Volatile
        private var INSTANCE: VenuesRepository? = null

        @JvmStatic
        fun getInstance(
            cacheDataSource: CacheDataSource,
            networkDataSource: NetworkDataSource
        ): VenuesRepository {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    VenuesRepository(cacheDataSource, networkDataSource)
                INSTANCE = instance
                return instance
            }
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }


    fun getVenuesNearBy(locationName: String): LiveData<ListOfVenueEntriesWithState> {
        val response = MutableLiveData<ListOfVenueEntriesWithState>()

        val venueEntriesWithState = ListOfVenueEntriesWithState(
            null,
            StateOfLoading.LOADING
        )
        response.postValue(venueEntriesWithState)

        networkDataSource.getVenuesNearBy(locationName, object : NetworkDataSource.LoadListOfVenueEntriesCallback {
            override fun onLoaded(venueEntries: List<VenueEntry>) {
                venueEntriesWithState.venueEntries = venueEntries
                venueEntriesWithState.stateCode = StateOfLoading.DONE_NET
                response.postValue(venueEntriesWithState)

                // TODO: are we in background?
                cacheDataSource.storeSearchResult(locationName, venueEntries)
            }

            override fun onDataNotAvailable(errorMessage: String) {
                // TODO: are we in background?

                val listOfVenueEntries = cacheDataSource.getVenuesNearBy(locationName)
                if (listOfVenueEntries.isNotEmpty()) {
                    venueEntriesWithState.venueEntries = listOfVenueEntries
                    venueEntriesWithState.stateCode = StateOfLoading.DONE_CACHE
                    response.postValue(venueEntriesWithState)
                } else {
                    venueEntriesWithState.stateCode = StateOfLoading.FAILED
                    venueEntriesWithState.errorMsg = errorMessage
                    response.postValue(venueEntriesWithState)
                }
            }
        })

        return response
    }

    fun getVenueDetail(id: String): LiveData<VenueEntryWithState> {
        val response = MutableLiveData<VenueEntryWithState>()

        val venueEntryWithState = VenueEntryWithState(
            null,
            StateOfLoading.LOADING
        )
        response.postValue(venueEntryWithState)

        networkDataSource.getVenueDetail(id, object : NetworkDataSource.LoadVenueEntryCallback {
            override fun onLoaded(venueEntry: VenueEntry) {
                venueEntryWithState.venueEntry = venueEntry
                venueEntryWithState.stateCode = StateOfLoading.DONE_NET
                response.postValue(venueEntryWithState)

                // TODO: are we in background?
                cacheDataSource.storeVenueEntry(venueEntry)
            }

            override fun onDataNotAvailable(errorMessage: String) {
                // TODO: are we in background?

                val venueEntry = cacheDataSource.getVenueDetail(id)
                if (venueEntry != null) {
                    venueEntryWithState.venueEntry = venueEntry
                    venueEntryWithState.stateCode = StateOfLoading.DONE_CACHE
                    response.postValue(venueEntryWithState)
                } else {
                    venueEntryWithState.stateCode = StateOfLoading.FAILED
                    venueEntryWithState.errorMsg = errorMessage
                    response.postValue(venueEntryWithState)
                }
            }
        })

        return response
    }

}