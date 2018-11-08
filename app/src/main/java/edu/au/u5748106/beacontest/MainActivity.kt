package edu.au.u5748106.beacontest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.altbeacon.beacon.*

class MainActivity : AppCompatActivity(), BeaconConsumer {

    private lateinit var map: HashMap<Int, Place>

    private lateinit var mBeaconManager: BeaconManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        map = hashMapOf(
            4341 to Place(R.drawable.l1, "Entrance", "Welcome to the Assumption University Library. We hope your visit, whether personal or virtual, will be successful and productive. Our resources, services and facilities exist to support learning, teaching,research and other activities of the University. \n" +
                    "We are striving to make the Library a comfortable and welcoming place to study and to launch your life-long learning work habits and it is a place to learn on your own and in collaboration with your colleagues. We are also continually working to improve users' information literacy skills."),
            4332 to Place(R.drawable.l2, "Newspapers Collection", "Gain insight into Chinese political and social life during the turbulent 120 year period from 1832 to 1953 with 12 English-language Chinese historical newspapers. Included are critical perspectives on the ending of more than 2,000 years of imperial rule in China, the Taiping Rebellion, the Opium Wars with Great Britain, the Boxer Rebellion and the events leading up to the1911 Xinhai Revolution, and the subsequent founding of the Republic of China. In addition to the article content, the full-image newspapers offer searchable access to advertisements, editorials, cartoons, and classified ads that illuminate history.")
        )

        mBeaconManager = BeaconManager.getInstanceForApplication(this)

        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))
        /*mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15"))
        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"))
        mBeaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"))*/

        setContentView(R.layout.activity_main)
        mBeaconManager.bind(this@MainActivity)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBeaconManager.unbind(this@MainActivity)
    }

    override fun onPause() {
        super.onPause()
        mBeaconManager.unbind(this@MainActivity)
    }

    override fun onResume() {
        super.onResume()
        mBeaconManager.bind(this@MainActivity)
    }

    var beaconIds = arrayListOf<String>()

    override fun onBeaconServiceConnect() {
        val mRegion = Region(packageName, null, null, null)

        mBeaconManager.addMonitorNotifier(object : MonitorNotifier {
            override fun didEnterRegion(region: Region) {
                mBeaconManager.startRangingBeaconsInRegion(mRegion);
            }

            override fun didExitRegion(region: Region) {
                mBeaconManager.stopRangingBeaconsInRegion(mRegion);
            }

            override fun didDetermineStateForRegion(i: Int, region: Region) {
            }
        })

        /*mBeaconManager.addRangeNotifier { beacons, region ->
            beacons.map { "UUID:" + it.id1 + " major:" + it.id2 + " minor:" + it.id3 + " RSSI:" + it.rssi + " Distance:" + it.distance + " txPower" + it.txPower }
                .forEach {
                    //textBeaconId.text = it.id2
                    Log.d("BEACON", it)
                }
        }*/

        mBeaconManager.addRangeNotifier { beacons, region ->
            beacons.map { it.id3.toString() + ";" + "%.2f".format(it.distance) }
                .forEach {
                    val b = it.split(";")
                    textBeaconId.text = b[0]
                    textBeaconDistance.text = b[1] + "m"
                    if (!beaconIds.contains(b[0])) {
                        beaconIds.add(b[0])
                        val place: Place = map[b[0].toInt()]!!
                        val intent = Intent(this, PlaceInformation::class.java)
                        intent.putExtra("img", place.placeImg)
                        intent.putExtra("title", place.placeTitle)
                        intent.putExtra("descr", place.placeDescription)
                        this.startActivity(intent)
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
