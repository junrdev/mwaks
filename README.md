# mwaks
- Mwaks is an application set to automate the processe through which individuals to share documents
- The current approach student based 

## Application Flow
- Users access the application by using a verified email adress and a password.
- New users opt in by providing an email, password and a profile picture (optional)
- Successfully authenticated users can upload and download documents from the app.

## Architecture

- The main interface runs as a web and android application.

    ### Web
    - The web application is still in development phase

    ### Android
    - The android application is done in Kotlin, a fast language build on top of the jvm. <code><a href="kotlinlang.org">Read more about kotlin</a></code> 

    - Internally the app uses Room Database for caching and Kotlin coroutines

    - The application utilizes google firebase for :
        - <b>Authentication (custom authentication)</b>. Using email and password. Google provides an interface and methods that I utilise to create user accounts with email.
         This credentials are later used for authentication into the app

        - <b>Realtime Database</b>.Am utilising a rtdb to keep track of user metadata such as the
                ```
                    joinedAt
                    activeStatus
                    profilePictureLink
                ```
        Rtdb provides a way to access this data in realtime and alter it with minimal efforts

        - <b>Firebase Storeage</b>. To store the files, image and any <code>blob</code> associated with the application.

    - The UI is completely in XML. Utilizing libraries such as <code>androidx, viewPager2, RecyclerViews</code> to achieve a seamless User Interface.

    - Releases
        - Am using github releases to ditribute the application. A better alternative is <code>Google Playstore</code>.
        - For each release there are tags, the version 1 of the app is yet to be released.
        - The pre releases helps to get reviews from end users to better the performance and interface of the application.


# Open Source
- This repo is made public to all Kotlin lovers to interact with the android code base and optimize it.



