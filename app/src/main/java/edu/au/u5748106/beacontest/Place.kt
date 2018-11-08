package edu.au.u5748106.beacontest

class Place {
    var placeImg: Int = 0
    var placeTitle = ""
    var placeDescription = ""

    constructor(img: Int, title: String, descr: String) {
        this.placeImg = img
        this.placeTitle = title
        this.placeDescription = descr
    }
}