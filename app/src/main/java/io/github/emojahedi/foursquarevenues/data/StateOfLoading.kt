package io.github.emojahedi.foursquarevenues.data

enum class StateOfLoading(val stateCode: Int) {
    LOADING(0),
    DONE_NET(1),
    DONE_CACHE(2),
    FAILED(-1)
}