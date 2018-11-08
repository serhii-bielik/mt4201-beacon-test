package edu.au.u5748106.beacontest

class Place {
    var placeImg: Int = 0
    var placeID: Int = 0
    var placeTitle = ""
    var placeDescription = ""

    constructor(id: Int, img: Int, title: String, descr: String) {
        this.placeID = id
        this.placeImg = img
        this.placeTitle = title
        this.placeDescription = descr
    }
}