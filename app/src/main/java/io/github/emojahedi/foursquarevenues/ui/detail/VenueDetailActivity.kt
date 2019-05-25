package io.github.emojahedi.foursquarevenues.ui.detail

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.disposables.CompositeDisposable
import io.github.emojahedi.foursquarevenues.R
import io.github.emojahedi.foursquarevenues.data.StateOfLoading
import io.github.emojahedi.foursquarevenues.util.logi

class VenueDetailActivity : AppCompatActivity() {

    private lateinit var mVenueDetailViewModel: VenueDetailViewModel
    private val mCompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_detail)

        val venueId = intent.extras.get("venue_id") as String
        val venueNameTextView = findViewById<TextView>(R.id.venue_name)
        val venueImageView = findViewById<ImageView>(R.id.venue_photo)
        val venueAddress = findViewById<TextView>(R.id.venue_address)
        val venueRating = findViewById<RatingBar>(R.id.ratingBar)
        val fabButton = findViewById<FloatingActionButton>(R.id.fab)

        val defaultPhoto =
            ContextCompat.getDrawable(applicationContext, R.drawable.default_venue_photo)

        val factory = VenueDetailViewModelFactory(application)
        mVenueDetailViewModel = ViewModelProviders.of(this, factory).get(VenueDetailViewModel::class.java)

        mVenueDetailViewModel.getVenueDetail(venueId)
            .observe(this, Observer { venueEntryWithState ->
                logi("observe :: stateCode=${venueEntryWithState.stateCode}")

                val venueEntry = venueEntryWithState.venueEntry
                val stateCode = venueEntryWithState.stateCode

                if (stateCode == StateOfLoading.LOADING) {
                    // Nothing!
                } else if (
                    (stateCode == StateOfLoading.DONE_NET || stateCode == StateOfLoading.DONE_CACHE) &&
                    venueEntry != null
                ) {
                    logi("observe :: venueEntry=$venueEntry / ${venueEntry.name}")

                    venueNameTextView.text = venueEntry.name
                    venueAddress.text =
                        "${venueEntry.formatted_address_line0}\n${venueEntry.formatted_address_line1}\n${venueEntry.formatted_address_line2}"
                    venueRating.rating = venueEntry.rating / 2
                    if (venueEntry.photo0_height > 0) {

                        val photoSize: String = venueEntry.photo0_width.toString() + "x" +
                                venueEntry.photo0_height.toString()

                        val photoPath = venueEntry.photo0_prefix + photoSize +
                                venueEntry.photo0_suffix

                        Picasso.get().load(photoPath).fit().centerCrop()
                            .error(R.drawable.default_venue_photo)
                            .placeholder(R.drawable.progress_animation)
                            .into(venueImageView, object : Callback {
                                override fun onSuccess() {
                                    venueImageView.alpha = (0f)
                                    venueImageView.animate().setDuration(200).alpha(1f).start()
                                }

                                override fun onError(e: Exception?) {
                                }

                            })
                    } else {
                        venueImageView.setImageDrawable(defaultPhoto)
                    }

                    fabButton.setOnClickListener {
                        displayVenueContactInfo(venueEntry.contactPhone, venueEntry.contactFacebookUsername)
                    }

                } else {
                    // TODO: Show error page
                    Toast.makeText(applicationContext, "Error getting venue info\n(${venueEntryWithState.errorMsg})", Toast.LENGTH_LONG).show()
                }
            }
            )

    }

    private fun displayVenueContactInfo(contactPhone: String, contactFacebookUsername: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Contact Info")

        val phone = if (contactPhone.isNotEmpty()) contactPhone else "Not Available"
        val facebookUsername =
            if (contactFacebookUsername.isNotEmpty()) contactFacebookUsername else "Not Available"
        val contactInfo = "Phone: " + phone + "\n" +
                "Facebook: " + facebookUsername
        builder.setMessage(contactInfo)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
        }
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable.dispose()
    }

}
