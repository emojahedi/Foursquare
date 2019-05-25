package io.github.emojahedi.foursquarevenues.data.source.network

import io.github.emojahedi.foursquarevenues.data.source.database.VenueEntry

interface NetworkDataSource {

    interface LoadListOfVenueEntriesCallback {
        fun onLoaded(venueEntries: List<VenueEntry>)
        fun onDataNotAvailable(errorMessage: String)
    }

    interface LoadVenueEntryCallback {
        fun onLoaded(venueEntry: VenueEntry)
        fun onDataNotAvailable(errorMessage: String)
    }

    fun getVenuesNearBy(
        locationName: String,
        loadListOfVenueEntriesCallback: LoadListOfVenueEntriesCallback
    )

    fun getVenueDetail(
        id: String, loadVenueEntryCallback: LoadVenueEntryCallback
    )
}