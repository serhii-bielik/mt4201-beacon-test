package edu.au.u5748106.beacontest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import kotlinx.android.synthetic.main.activity_place_information.*

class PlaceInformation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_information)

        val extras = intent.extras
        imgPlace.setImageResource(extras.getInt("img"))
        txtPlace.text = extras.getString("title")
        txtDetatils.text = extras.getString("descr")
        txtDetatils.movementMethod = ScrollingMovementMethod()
    }

    fun doClose(view: View) {
        finish()
    }
}
