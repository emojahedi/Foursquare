package io.github.emojahedi.foursquarevenues.data.source.network

class FoursquareVenueDataModel(
    var id: String,
    var name: String,
    var contact: VenueContact?,
    var rating: Float = 0F,
    var photos: VenuePhotos?,
    var location: VenueLocation?
) {

//    // A location object within the venue.
//    var location: FoursquareLocation? = null

    companion object {
        class VenueContact(
            var phone: String = "",
            var formattedPhone: String = "",
            var facebook: String = "",
            var facebookUsername: String = "",
            var facebookName: String = ""
        )

        class VenueLocation(
            var formattedAddress: ArrayList<String> = ArrayList()
        )

        class VenuePhotos {
            var groups = ArrayList<VenuePhotoList>()
        }

        class VenuePhotoList {
            var items = ArrayList<VenuePhotoItems>()
        }

        class VenuePhotoItems {
            var prefix: String = ""
            var suffix: String = ""
            var width: Int = 0
            var height: Int = 0
        }
    }


}