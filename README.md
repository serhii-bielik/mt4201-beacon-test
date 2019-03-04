# Captcha Recognition
This is an example for basic usage [Beacons](https://kontakt.io/beacon-basics/what-is-a-beacon/) in Android Application using Kotlin. [Project for Social Interests, Government Policies And Technology]

The idea is to create an interactive guide for library to practically demonstrate beacon technology.

## How it works?
1.	Several beacons located in various places in the library. They constantly transmitting their IDs via Bluetooth.
2.	User installs application to the smartphone.
3.	The application contains list of information bounded with beacon IDs.
4.	Once application catches a beacon signal it receives beacon ID and then fetches information bounded with this id and sends push notification to the user.
5.	If user clicks to the notification the information about the place will be shown.  

The beacon range is about 10-20 meters. Each of them has battery. The battery enough to work for about a year (depends on the beacon model).

### Screenshots (this is just an ugly concept :)

![Push Notification](Screenshot_20181111-224924.png?raw=true "Push Notification")
![Map](Screenshot_20181111-224940.png?raw=true "Map")
![Place Information](Screenshot_20181111-224949.png?raw=true "Place Information")
