package io.github.emojahedi.foursquarevenues.data.source.network

import androidx.annotation.VisibleForTesting
import io.github.emojahedi.foursquarevenues.data.source.database.VenueEntry
import io.github.emojahedi.foursquarevenues.util.logi
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

class NetworkDataSourceImpl(private val foursquareClient: FoursquareClient) : NetworkDataSource {

    companion object {
        private var INSTANCE: NetworkDataSourceImpl? = null

        @JvmStatic
        fun getInstance(foursquareClient: FoursquareClient): NetworkDataSourceImpl {
            if (INSTANCE == null) {
                synchronized(NetworkDataSourceImpl::javaClass) {
                    INSTANCE = NetworkDataSourceImpl(foursquareClient)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }

    override fun getVenuesNearBy(
        locationName: String,
        loadListOfVenueEntriesCallback: NetworkDataSource.LoadListOfVenueEntriesCallback
    ) {
        foursquareClient.getNearByVenues(locationName)
            .subscribeOn(Schedulers.io())
            .subscribe(object : DisposableObserver<FoursquareVenueListJSON>() {
                override fun onComplete() {
                }

                override fun onNext(t: FoursquareVenueListJSON) {
                    logi("onNext :: before parseVenueListJson / t = $t")
                    val netVenueEntries = parseVenueListJson(t)

                    logi("onNext :: venueList = $netVenueEntries")

                    loadListOfVenueEntriesCallback.onLoaded(netVenueEntries)
                }

                override fun onError(e: Throwable) {
                    logi("onError :: Fetch venue list from database", e)
                    loadListOfVenueEntriesCallback.onDataNotAvailable(e.message ?: "")
                }

            })
    }

    override fun getVenueDetail(
        id: String, loadVenueEntryCallback: NetworkDataSource.LoadVenueEntryCallback
    ) {
        foursquareClient.getVenueDetail(id)
            .subscribeOn(Schedulers.io())
            .subscribe(object : DisposableObserver<FoursquareVenueDetailJSON>() {
                override fun onNext(t: FoursquareVenueDetailJSON) {
                    logi("onNext :: before venueJsonToVenueEntry / t = $t")
                    val venueEntry = venueJsonToVenueEntry(t.response.venue)

                    loadVenueEntryCallback.onLoaded(venueEntry)
                }

                override fun onError(e: Throwable) {
                    logi("onError :: Fetch venue data from database", e)
                    loadVenueEntryCallback.onDataNotAvailable(e.message ?: "")
                }

                override fun onComplete() {

                }
            })

    }

    private fun parseVenueListJson(foursquareVenueListJSON: FoursquareVenueListJSON): List<VenueEntry> {
        var venueEntry: VenueEntry
        val venueJsonList = foursquareVenueListJSON.response.venues
        val venues = mutableListOf<VenueEntry>()

        for (foursquareVenueDataModel in venueJsonList) {
            venueEntry = venueJsonToVenueEntry(foursquareVenueDataModel)
            venues.add(venueEntry)
        }

        return venues
    }


    private fun venueJsonToVenueEntry(venueDataModel: FoursquareVenueDataModel): VenueEntry {
        var l0 = ""
        var l1 = ""
        var l2 = ""
        val location = venueDataModel.location
        if (location != null && location.formattedAddress.size > 0) {
            l0 = location.formattedAddress[0]
            if (location.formattedAddress.size > 1) {
                l1 = location.formattedAddress[1]
                if (location.formattedAddress.size > 2) {
                    l2 = location.formattedAddress[2]
                }
            }
        }

        val venuePhotos: FoursquareVenueDataModel.Companion.VenuePhotoList
        var photoWidth = 0
        var photoHeight = 0
        var photoPrefix = ""
        var photoSuffix = ""

        val photos = venueDataModel.photos

        if (photos != null && photos.groups.size > 1) {
            venuePhotos = photos.groups[1]
            photoWidth = venuePhotos.items[0].width
            photoHeight = venuePhotos.items[0].height
            photoPrefix = venuePhotos.items[0].prefix
            photoSuffix = venuePhotos.items[0].suffix
        }

        var contactPhone = ""
        var contactFacebookUsername = ""
        val contact = venueDataModel.contact
        if (contact != null) {
            contactPhone = contact.phone
            contactFacebookUsername = contact.facebookUsername
        }

        val venueEntry = VenueEntry(
            id = venueDataModel.id,
            cacheDate = Calendar.getInstance().time,
            name = venueDataModel.name,
            contactPhone = contactPhone,
            contactFacebookUsername = contactFacebookUsername,
            rating = venueDataModel.rating,
            formatted_address_line0 = l0,
            formatted_address_line1 = l1,
            formatted_address_line2 = l2,
            photo0_width = photoWidth,
            photo0_height = photoHeight,
            photo0_prefix = photoPrefix,
            photo0_suffix = photoSuffix
        )

        logi("venueJsonToVenueEntry :: before returning $venueEntry")

        return venueEntry
    }
}