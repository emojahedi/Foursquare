package io.github.emojahedi.foursquarevenues.data.source.database

import androidx.annotation.VisibleForTesting
import io.github.emojahedi.foursquarevenues.util.logi

class CacheDataSourceImpl private constructor(
    private val cacheDatabase: CacheDatabase
) : CacheDataSource {
    companion object {
        private var INSTANCE: CacheDataSourceImpl? = null

        @JvmStatic
        fun getInstance(cacheDatabase: CacheDatabase): CacheDataSourceImpl {
            if (INSTANCE == null) {
                synchronized(CacheDataSourceImpl::javaClass) {
                    INSTANCE = CacheDataSourceImpl(cacheDatabase)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }

    override fun getVenuesNearBy(locationName: String): List<VenueEntry> {
        val venueDao = cacheDatabase.venueDao()
        return venueDao.getVenuesForSearchTerm(locationName)
    }

    override fun getVenueDetail(id: String): VenueEntry? {
        val venueDao = cacheDatabase.venueDao()
        return venueDao.getVenueById(id)
    }

    override fun storeVenueEntry(venueEntry: VenueEntry) {
        cacheDatabase.venueDao().insert(venueEntry)
    }

    override fun storeSearchResult(locationName: String, venueEntries: List<VenueEntry>) {
        val venueDao = cacheDatabase.venueDao()
        val searchVenueDao = cacheDatabase.searchVenueDao()

        logi("storeSearchResult :: before venueDao.bulkInsert")
        venueDao.bulkInsert(venueEntries)

        val searchVenues = ArrayList<SearchVenue>()
        for ((index, venue) in venueEntries.withIndex()) {
            searchVenues.add(SearchVenue(locationName, venue.id, index))
        }
        logi("storeSearchResult :: before searchVenueJoinDao.bulkInsert")
        searchVenueDao.bulkInsert(searchVenues)
    }
}