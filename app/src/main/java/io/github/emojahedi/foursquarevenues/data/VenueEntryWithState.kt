package io.github.emojahedi.foursquarevenues.data

import io.github.emojahedi.foursquarevenues.data.source.database.VenueEntry

class VenueEntryWithState(
    var venueEntry: VenueEntry?,
    var stateCode: StateOfLoading,
    var errorMsg: String = ""
) {

}

