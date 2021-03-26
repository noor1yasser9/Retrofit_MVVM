# Architectural Pattern(MVVM) Example
The objective of this example is to explain architecture pattern that are widely used in Android apps (MVVM) the purpose of this example is just to explain the concept of these architecture pattern


# Project description
This project is just a simple two-screen app. The first screen is LoginActivity which allows the user to login into the app using a username and password. And the second screen is a ListActivity that shows a list of photos along with the title and date of each photo.

# Implementation
This app uses Retrofit networking library to pull the data from the Web APIs and Gson converter to convert the Web APIs responses to Java objects, it's also implementing pagination so that the data will be pulled incrementally page by page. Furthermore, the app handles network and HTTP errors and show the appropriate message to the user as well as updating the loading status and allow the user to retry failed requests if the error is recoverable.
