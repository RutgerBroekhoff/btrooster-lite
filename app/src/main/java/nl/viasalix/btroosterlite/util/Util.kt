package nl.viasalix.btroosterlite.util

import android.content.Context
import android.net.ConnectivityManager

class Util {
    companion object {
        fun online(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo

            return networkInfo != null && networkInfo.isConnected
        }

        fun <K, V>getIndexByKey(map: LinkedHashMap<K, V>, key: K) : Int? {
            var i = 0

            map.forEach {
                if (it.key == key)
                    return i
                i++
            }

            return null
        }

        fun <K, V>getKeyByIndex(map: LinkedHashMap<K, V>, index: Int) : K? {
            var i = 0

            map.forEach {
                if (i == index)
                    return it.key
                i++
            }

            return null
        }
    }
}