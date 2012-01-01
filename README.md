### Android Scala Installer
This is an Android application that lets you install the Scala standard library onto your rooted Android device as a set of shared libraries.

This is *ONLY* useful for development purposes as it lets you build Android applications without including the Scala library
in your APK.  When packaging your application for general consumption (e.g. the Android Market) you should not rely on Scala being pre-installed
or require users to use this application.

Your device must be rooted to do use this application.

The majority of the work done for this was completed by [Macarse](https://github.com/Macarse)
This application is essentially a line-for-line conversion of his work with a few minor additions.

Here is Macarse's original [blog post](http://android-argentina.blogspot.com/2011/11/roboinstaller-install-roboguice.html)
and the [GitHub repo](https://github.com/Macarse/Roboinstaller)
