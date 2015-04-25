## Weather Forecast ##

**Weather Forecast** is an Android application  for displaying weather details.

### Features ###
The application shows the current weather details for the current location of the device. The details shown are: 

1. Current Temperature (Celsius or Fahrenheit)
2. Moisture in the Atmosphere
3. Speed of the wind
4. Pressure (Metric or Imperial unit)
5. Amount of rain
6. Wind direction

Apart from this the application also shows the weather forecast for next 5 days.
  

### Installation ###
Install the given **.apk** file on an android device to start using the application. 


### Usage ###
The application can be used on a daily basis to see the weather conditions and act accordingly for your plans.
 
### Building Project ###
1. Clone this repository
2. Run `gradlew assemble` command in console with this repository as current working directory.
3. The **.apk** file should be available in `/mobile/build/outputs/apk` directory.

**Note:** A "local.properties" file is also needed to set the location of the SDK in the same way that the existing SDK requires, using the "sdk.dir" property. Example of "local.properties" on Windows: sdk.dir=C:\\adt-bundle-windows\\sdk. Alternatively, an environment variable called `ANDROID_HOME` can be set.

**Tip:** Command `gradlew assemble` builds both - debug and release APK. Use `gradlew assembleDebug` to build debug APK. Use `gradlew assembleRelease` to build release APK. Debug APK is signed by debug keystore. Release APK is signed by own keystore, stored in `/extras/keystore` directory.

**Signing process:** Keystore passwords are automatically loaded from property file during building the release APK.

### Testing ###
The application has been tested on following devices:

- Nexus 4 (Phone)
- Nexus 5 (Phone)
- Nexus 10 (Tablet)
 


### Dependencies ###
- GPS / Network Provider who can give your current location
- Internet Connection

### Developed By ###
Prerak Sola