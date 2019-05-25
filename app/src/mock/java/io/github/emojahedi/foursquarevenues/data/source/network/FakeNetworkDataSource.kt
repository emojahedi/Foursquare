package io.github.emojahedi.foursquarevenues.data.source.network

import io.github.emojahedi.foursquarevenues.data.source.database.VenueEntry

object FakeNetworkDataSource : NetworkDataSource {

    var VENUES_DATA: LinkedHashMap<String, VenueEntry> = LinkedHashMap()
    var SEARCHES_DATA: LinkedHashMap<String, List<VenueEntry>> = LinkedHashMap()


    override fun getVenuesNearBy(
        locationName: String,
        loadListOfVenueEntriesCallback: NetworkDataSource.LoadListOfVenueEntriesCallback
    ) {
        // TODO: remove
        println("FakeNetworkDataSource.getVenuesNearBy")

        val venues = SEARCHES_DATA[locationName]
        if (venues != null) {
            loadListOfVenueEntriesCallback.onLoaded(venues)
        } else {
            loadListOfVenueEntriesCallback.onDataNotAvailable("Data Not Available")
        }
    }

    override fun getVenueDetail(id: String, loadVenueEntryCallback: NetworkDataSource.LoadVenueEntryCallback) {
        // TODO: remove
        println("FakeNetworkDataSource.getVenueDetail")

        val venueDetail = VENUES_DATA[id]
        if (venueDetail != null) {
            loadVenueEntryCallback.onLoaded(venueDetail)
        } else {
            loadVenueEntryCallback.onDataNotAvailable("Data Not Available")
        }
    }
}