package io.github.emojahedi.foursquarevenues.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.disposables.CompositeDisposable
import io.github.emojahedi.foursquarevenues.R
import io.github.emojahedi.foursquarevenues.data.StateOfLoading
import io.github.emojahedi.foursquarevenues.ui.detail.VenueDetailActivity
import io.github.emojahedi.foursquarevenues.util.logd
import io.github.emojahedi.foursquarevenues.util.logi


class MainActivity : AppCompatActivity() {

    private lateinit var mVenueListViewModel: VenueListViewModel
    private val mCompositeDisposable = CompositeDisposable()
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRecyclerView = findViewById(R.id.recyclerview)
        val venueNameEditText = findViewById<EditText>(R.id.venue_name_edit_text)
        mProgressBar = findViewById(R.id.list_progress)

        val adapter = VenueListAdapter(this)
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.addOnItemTouchListener(
            RecyclerTouchListener(this,
                mRecyclerView,
                object : RecyclerTouchListener.Companion.CustomClickListener {
                    override fun onClick(view: View, position: Int) {
                        var venueId = adapter.getItemIdAt(position)
                        val intent = Intent(this@MainActivity, VenueDetailActivity::class.java)
                        intent.putExtra("venue_id", venueId)
                        startActivity(intent)
                    }
                })
        )

        venueNameEditText.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                if (event?.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (venueNameEditText.right - venueNameEditText.compoundDrawables[2].bounds.width())) {
                        search(venueNameEditText.text.toString(), adapter)
                        return true
                    }
                }
                return false
            }

        })


        val factory = VenueListViewModelFactory(application)
        mVenueListViewModel = ViewModelProviders.of(this, factory).get(VenueListViewModel::class.java)

        logd("onCreate / done!")
    }


    fun search(searchTerm: String, adapter: VenueListAdapter) {

        mVenueListViewModel.getVenuesNearBy(searchTerm)
            .observe(this, Observer { listOfVenueEntriesWithState ->

                logi("observe :: stateCode=${listOfVenueEntriesWithState.stateCode}")

                val listOfVenueEntries = listOfVenueEntriesWithState.venueEntries
                val stateCode = listOfVenueEntriesWithState.stateCode

                if (stateCode == StateOfLoading.LOADING) {
                    mProgressBar.visibility = View.VISIBLE
                    mRecyclerView.visibility = View.GONE
                } else if (
                    (stateCode == StateOfLoading.DONE_NET || stateCode == StateOfLoading.DONE_CACHE) &&
                    listOfVenueEntries != null
                ) {
                    logi("observe :: listOfVenueEntry=${listOfVenueEntriesWithState.venueEntries}")
                    mProgressBar.visibility = View.GONE
                    mRecyclerView.visibility = View.VISIBLE
                    adapter.setVenues(listOfVenueEntries)
                } else {
                    // TODO: Show error page
                    Toast.makeText(
                        applicationContext,
                        "Error getting venue list\n(${listOfVenueEntriesWithState.errorMsg})",
                        Toast.LENGTH_LONG
                    ).show()
                }

            })
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.dispose()
    }

}
