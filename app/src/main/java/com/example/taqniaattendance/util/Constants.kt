package com.example.taqniaattendance.util

object Constants {

    object ViewType {
        const val SNACKBAR = "SNACKBAR"
        const val TOAST = "TOAST"
    }

    object Environment {
        const val KEY_ENVIRONMENT = "KEY_ENVIRONMENT"

        // Keys.
        const val PRODUCTION = "production"
        const val SANDBOX = "sandbox"
        const val DEVELOPMENT = "development"

        // Server Urls.
        const val PRODUCTION_SERVER_URL = "http://att.ncrst.edu.sa/api/"
        const val SANDBOX_SERVER_URL = "http://att.ncrst.edu.sa/api/api/"
        const val DEVELOPMENT_SERVER_URL = "http://att.ncrst.edu.sa/api/"
    }

    object PreferenceFile {
        const val DEFAULT_PREF = "default_pref"
        const val SETTINGS_PREF = "settings_pref"
    }

    object User {
        const val USER = "user"
        const val TOKEN = "token"
    }

    object GeneralKeys {
        const val DEFAULT_COUNTRY_CODE = "966"
        const val PHONE_NUMBER_FIELD = "phone_number"
        const val KEY_HINT_POSITION = "0"
        const val BUGSNAG_ENVIRONMENT_TAB = "Environment"
        const val KEY_FORCE_UPDATE = "KEY_FORCE_UPDATE"
        const val KEY_FORCE_LOG_OUT = "KEY_FORCE_LOG_OUT"
        const val KEY_BUSINESS_ERROR_MSG = "KEY_BUSINESS_ERROR_MSG"
        const val LANGUAGE_AR = "ar"
        const val LANGUAGE_EN = "en"
        const val COUNTRY_US = "US"
        const val COUNTRY_SA = "SA"
        const val KEY_OBJECT = "KEY_OBJECT"
        const val KEY_OBJECT_ID = "KEY_OBJECT_ID"
        const val KEY_MAP = "KEY_MAP"
        const val DATABASE_NAME = "Hsr.db"
        const val ONLINE = "online"
        const val OFFLINE = "offline"
        const val ANDROID = "android"
        const val PROVIDER = "Provider" // this is case sensitive... Must be Provider..
        const val API_KEY = "AIzaSyD8nSFHi3zbyyo_YjV-Xp54O7WFelR8tfs"
        const val ACCEPT_REQUEST = "accept"
        const val REJECT_REQUEST = "reject"
        const val FROM_REQUEST_LIST_SCREEN = "FROM_REQUEST_LIST_SCREEN"

        const val KEY_GENERAL_NOTIFICATION_CHANNEL= "key_general_notification_channel"

    }

    object ErrorConstants {
        const val ERROR = "error"
        const val ERROR_CODE_NETWORK = 102
        const val ERROR_CODE_GENERAL = 103
        const val ERROR_CODE_TIME_OUT = 104
        const val ERROR_CODE_UNAUTHORIZED = 401
        const val ERROR_CODE_FORCE_UPDATE = 499
        const val ERROR_CODE_INTERNAL_SERVER_ERROR = 500
        const val ERROR_CODE_SERVICE_UNAVAILABLE = 503
        const val ERROR_CODE_RESOURCE_NOT_FOUND = 404
        const val ERROR_CODE_NOT_REGISTERED = 403 // Custom error with backend to indicated that user does not have provider account.
        const val ERROR_CODE_GPS_DISABLED = 1105
        const val ERROR_CODE_NOTIFICATION_DISABLED = 999 //Notification is disabled
    }

    object RemoteMetaData {
        // Keys.
        const val AUTHORIZATION = "Authorization"
        const val CONTENT_TYPE = "Content-Type"
        const val ACCEPT_LANGUAGE = "Accept-Language"
        const val PLATFORM = "Platform"
        const val APP_VERSION = "App-Version"
        const val VERSION = "Version"
        const val MODEL = "Model"
        const val ACCEPT = "Accept"
        const val APP_SOURCE = "App-Source"
        const val APP_TYPE = "App-Type"

        //Static Values.
        const val TOKEN = "token"
        const val APPLICATION_JSON = "Application/json"
        const val MULTIPART = "multipart/form-data"
        const val ANDROID = "android"
        const val GOOGLE_STORE = "google_store"
        const val HSR = "hsr"

    }

    object AppPermissionKeys {
        const val CAMERA_REQUEST_PERMISSION_CODE = 3355
        const val GALLERY_REQUEST_PERMISSION_CODE = 4477
        const val REQUEST_CODE_CAMERA_CAPTURE = 6699
        const val REQUEST_CODE_SELECT_FROM_GALLERY = 75
        const val REQUEST_CODE_CALL_PHONE = 7704
    }

    object LoginConstants {
        const val FIELD_USER_NAME = "field_user_name"
        const val FIELD_PASSWORD = "field_password"
    }

    object UserLocation {
        const val KEY_LATITUDE = "Latitude"
        const val KEY_LONGITUDE = "Longitude"

        const val REQUEST_CODE_LOCATION = 1111
    }

    object ApiConstants {
        const val AuthUrl = "https://gis.ncrst.edu.sa/arcgis/tokens/?request=getToken&f=json"
    }

}