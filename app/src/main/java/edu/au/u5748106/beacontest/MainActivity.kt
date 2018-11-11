package edu.au.u5748106.beacontest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import kotlinx.android.synthetic.main.activity_main.*
import org.altbeacon.beacon.*

class MainActivity : AppCompatActivity(), BeaconConsumer {

    private lateinit var map: HashMap<Int, Place>

    lateinit var notificationManager : NotificationManager

    private lateinit var mBeaconManager: BeaconManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        map = hashMapOf(
            /*4332 to Place(4332, R.drawable.l1, "Entrance", "Welcome to the Assumption University Library. We hope your visit, whether personal or virtual, will be successful and productive. Our resources, services and facilities exist to support learning, teaching,research and other activities of the University. \n" +
                    "We are striving to make the Library a comfortable and welcoming place to study and to launch your life-long learning work habits and it is a place to learn on your own and in collaboration with your colleagues. We are also continually working to improve users' information literacy skills."),*/
            4341 to Place(4341, R.drawable.asean, "ASEAN Corner", "Establishment\n" +
                    "The Association of Southeast Asian Nations, or ASEAN, was established on 8 August 1967 in Bangkok, Thailand, with the signing of the ASEAN Declaration (Bangkok Declaration) by the Founding Fathers of ASEAN, namely Indonesia, Malaysia, Philippines, Singapore and Thailand.\n" +
                    "Brunei Darussalam then joined on 7 January 1984, Viet Nam on 28 July 1995, Lao PDR and Myanmar on 23 July 1997, and Cambodia on 30 April 1999, making up what is today the ten Member States of ASEAN.\n" +
                    " \n" +
                    "Fundamental Principles\n" +
                    "In their relations with one another, the ASEAN Member States have adopted the following fundamental principles, as contained in the Treaty of Amity and Cooperation in Southeast Asia (TAC) of 1976:\n" +
                    "Mutual respect for the independence, sovereignty, equality, territorial integrity, and national identity of all nations;\n" +
                    "The right of every State to lead its national existence free from external interference, subversion or coercion;\n" +
                    "Non-interference in the internal affairs of one another;\n" +
                    "Settlement of differences or disputes by peaceful manner;\n" +
                    "Renunciation of the threat or use of force; and\n" +
                    "Effective cooperation among themselves.")
        )

        mBeaconManager = BeaconManager.getInstanceForApplication(this)

        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        /*mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15"))
        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"))
        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"))*/

        setContentView(R.layout.activity_main)
        mBeaconManager.bind(this@MainActivity)
    }

    private fun sendNotification() {
        if (lastPlace != null) {
            val intent = Intent(this, PlaceInformation::class.java)
            intent.putExtra("img", lastPlace!!.placeImg)
            intent.putExtra("title", lastPlace!!.placeTitle)
            intent.putExtra("descr", lastPlace!!.placeDescription)

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT)
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(this)
                .setContentTitle("You are near the " + lastPlace!!.placeTitle)
                .setContentText("Tap here to learn more about this place.")
                .setAutoCancel(true)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.ic_launcher_background))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_MAX)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(lastPlace!!.placeID, notificationBuilder.build())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBeaconManager.unbind(this@MainActivity)
    }

    override fun onPause() {
        super.onPause()
        //mBeaconManager.unbind(this@MainActivity)
    }

    override fun onResume() {
        super.onResume()
        //mBeaconManager.bind(this@MainActivity)
    }

    fun showPlaceInfo(view: View) {
        if (lastPlace != null) {
            val intent = Intent(this, PlaceInformation::class.java)
            intent.putExtra("img", lastPlace!!.placeImg)
            intent.putExtra("title", lastPlace!!.placeTitle)
            intent.putExtra("descr", lastPlace!!.placeDescription)
            this.startActivity(intent)
        }
    }

    var lastPlace: Place? = null

    override fun onBeaconServiceConnect() {
        val mRegion = Region(packageName, null, null, null)

        mBeaconManager.addMonitorNotifier(object : MonitorNotifier {
            override fun didEnterRegion(region: Region) {
                mBeaconManager.startRangingBeaconsInRegion(mRegion);
                Log.i("BEACON", "I just saw an beacon for the first time!");
            }

            override fun didExitRegion(region: Region) {
                mBeaconManager.stopRangingBeaconsInRegion(mRegion);
                Log.d("BEACON", "I no longer see an beacon")
                imageView.setImageResource(R.drawable.library)
                lastPlace = null
                textBeaconId.text = "Looking for the beacons..."
                textBeaconDistance.text = ""
                button2.visibility = View.INVISIBLE
            }

            override fun didDetermineStateForRegion(i: Int, region: Region) {
            }
        })

        mBeaconManager.addRangeNotifier { beacons, region ->
            beacons.map { it.id3.toString() + ";" + "%.2f".format(it.distance) }
                .forEach {
                    val b = it.split(";")

                    var showNotification = false
                    if (lastPlace == null || lastPlace!!.placeID != b[0].toInt()) {
                        showNotification = true
                    }
                    if (map.containsKey(b[0].toInt())) {
                        lastPlace = map[b[0].toInt()]!!

                        textBeaconId.text = lastPlace!!.placeTitle
                        textBeaconDistance.text = "~" + b[1] + "m"
                        button2.visibility = View.VISIBLE

                        if (showNotification) {
                            sendNotification()
                            imageView.setImageResource(R.drawable.library_asean)
                        }
                    }

                    Log.d("BEACON", it)
                }
        }

        try {
            mBeaconManager.startMonitoringBeaconsInRegion(mRegion)
        } catch (e: RemoteException) {
            Log.e("BEACON-ERROR", "Exception", e)
        }
    }

}
