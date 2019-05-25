package io.github.emojahedi.foursquarevenues.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.nhaarman.mockitokotlin2.any
import io.github.emojahedi.foursquarevenues.data.source.VenuesRepository
import io.github.emojahedi.foursquarevenues.data.source.database.FakeCacheDataSource
import io.github.emojahedi.foursquarevenues.data.source.database.VenueEntry
import io.github.emojahedi.foursquarevenues.data.source.network.FakeNetworkDataSource
import io.github.emojahedi.foursquarevenues.data.source.network.NetworkDataSource
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class VenueRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val venueEntry1 = VenueEntry(
        "id-1",
        null,
        "name-1",
        "phone-1",
        "facebook-1",
        1.0F,
        "photo0_prefix-1",
        "photo0_suffix-1",
        100,
        100,
        "formatted_address_line0-1",
        "formatted_address_line1-1",
        "formatted_address_line2-1"
    )


    private lateinit var venueRepository: VenuesRepository
    @Mock
    private lateinit var cacheDataSource: FakeCacheDataSource
    @Mock
    private lateinit var networkDataSource: FakeNetworkDataSource


    @Before
    fun setupRepository() {
        MockitoAnnotations.initMocks(this)
        venueRepository = VenuesRepository.getInstance(cacheDataSource, networkDataSource)
    }

    @After
    fun destroyRepositoryInstance() {
        VenuesRepository.destroyInstance()
    }

    @Test
    fun getVenueDetail_internetIsAvailable() {
        networkDataSource.VENUES_DATA = LinkedHashMap()
        networkDataSource.VENUES_DATA[venueEntry1.id] = venueEntry1
        cacheDataSource.VENUES_DATA = LinkedHashMap()

        val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))
        val response = venueRepository.getVenueDetail(venueEntry1.id)
        response.observe({ lifecycle }) { venueEntryWithState ->
            val stateCode = venueEntryWithState.stateCode
            val venueEntry = venueEntryWithState.venueEntry

            println("stateCode=$stateCode / venueEntry=$venueEntry")

            if (stateCode == StateOfLoading.LOADING) {
            } else {
                assertEquals(stateCode, StateOfLoading.DONE_NET)
                assertNotNull(venueEntry)

                verify<FakeNetworkDataSource>(networkDataSource)
                    .getVenueDetail(
                        ArgumentMatchers.anyString(), any<NetworkDataSource.LoadVenueEntryCallback>()
                    )
                verify<FakeCacheDataSource>(cacheDataSource).storeVenueEntry(any())
                assertNotNull(cacheDataSource.VENUES_DATA[venueEntry1.id])
            }
        }
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

//        assertNotNull(cacheDataSource.VENUES_DATA[venueEntry1.id])
        println("end of getVenueDetail_internetIsAvailable")
    }

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
