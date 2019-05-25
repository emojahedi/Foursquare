package io.github.emojahedi.foursquarevenues.data.source.database

object FakeCacheDataSource : CacheDataSource {

    var VENUES_DATA: LinkedHashMap<String, VenueEntry> = LinkedHashMap()
    var SEARCHES_DATA: LinkedHashMap<String, List<VenueEntry>> = LinkedHashMap()

    override fun getVenuesNearBy(locationName: String): List<VenueEntry> {
        // TODO: remove
        println("FakeCacheDataSource.getVenuesNearBy")
        return SEARCHES_DATA[locationName] ?: emptyList()
    }

    override fun getVenueDetail(id: String): VenueEntry? {
        // TODO: remove
        println("FakeCacheDataSource.getVenueDetail")

        return VENUES_DATA[id]
    }

    override fun storeVenueEntry(venueEntry: VenueEntry) {
    }

    override fun storeSearchResult(locationName: String, venueEntries: List<VenueEntry>) {
    }
}