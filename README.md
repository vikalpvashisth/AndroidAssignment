# AndroidAssignment

**Sowingo Assignment**

This project is based on MVVM architecture and provides the user with a single screen that displays
a list of products that is fetched from a remote source. The user can mark the products as favorite
or not favorite by tapping on the favorite image on list items.

**Main Activity**

This screen is the launching screen of the application and as soon as it is launched we initialize
the UI and its various components like swipeRefresh, recyclerView and also hits the Products api to
fetch product list from the server.

As soon as data is received from the server, it is rendered on the screen in the form of a list and
is available for the user to interact with.

The User can perform two operations on this screen namely:- 1.Search for a product by its name with
the help of the search box available on the top of the screen. 2.Can mark the product as favorite or
not favorite. The state for keeping the product as favorite or not favorite is kept locally and not
managed on the server.

**Technical Details**

1. Minimum android version support:
   API 24(Android 7.0 Nougat)

2. Language:
   Kotlin

3. Architecture:
   MVVM architecture

4. Medium of third party library a. Gradle Libraries

5. Custom fonts:
   a. SfProBold b. SfProMedium c. SfProRegular

6. Third party library:
   b. Retrofit d. Glide

7. Build environments:
   a. Development

8. Network layer:
   a. RetrofitClient.kt

9. Product Listing Module:
   a. MainActivity.kt b. ProductViewModel.kt c. ProductRecyclerAdapter.kt

Steps to run the project:-

1. Open the project in android studio.
2. Sync Gradle
3. When gradle sync is successful, go to Build -> Build Bundle(s)/APKs -> Build APK(s)
4. After apk build is successful navigate to locate the apk and transfer the apk to your android
   phone and install it.
