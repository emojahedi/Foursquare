package io.github.emojahedi.foursquarevenues.data.source.database

interface CacheDataSource {
    fun getVenuesNearBy(locationName: String): List<VenueEntry>
    fun getVenueDetail(id: String): VenueEntry?
    fun storeVenueEntry(venueEntry: VenueEntry)
    fun storeSearchResult(locationName: String, venueEntries: List<VenueEntry>)
}