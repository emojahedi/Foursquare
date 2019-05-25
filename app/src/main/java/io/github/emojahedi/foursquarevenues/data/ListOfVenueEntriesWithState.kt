package io.github.emojahedi.foursquarevenues.data

import io.github.emojahedi.foursquarevenues.data.source.database.VenueEntry

class ListOfVenueEntriesWithState(
    var venueEntries: List<VenueEntry>?,
    var stateCode: StateOfLoading,
    var errorMsg: String = ""
) {

}

