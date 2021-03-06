/*  BTRooster Lite: Roosterapp voor Calvijn College
 *  Copyright (C) 2017 Rutger Broekhoff <rutger broekhoff three at gmail dot com>
 *                 and Jochem Broekhoff
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package nl.viasalix.btroosterlite.fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import nl.viasalix.btroosterlite.R
import nl.viasalix.btroosterlite.activities.MainActivity
import nl.viasalix.btroosterlite.activities.SettingsActivity
import nl.viasalix.btroosterlite.cup.cupconfig.CUPConfigActivity
import nl.viasalix.btroosterlite.singleton.Singleton
import org.jetbrains.anko.*

class CUPFragment : Fragment() {
    private var currentView: View? = null
    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        currentView = inflater.inflate(R.layout.fragment_cup, container, false)
        webView = currentView!!.findViewById<View>(R.id.web_view) as WebView

        webView!!.settings.javaScriptEnabled = true

        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                return false
            }
        }

        webView!!.webChromeClient = WebChromeClient()

        return currentView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_mainactivity_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_reload -> {
                loadCUP()
                return true
            }
            R.id.action_settings -> {
                val intent = Intent(activity, SettingsActivity::class.java)
                activity.startActivity(intent)
            }
            R.id.action_opensource -> {
                val ossIntent = Intent(activity, OssLicensesMenuActivity::class.java)
                ossIntent.putExtra("title", getString(R.string.opensource_licences))
                activity.startActivity(ossIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()

        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        if ((activity as AppCompatActivity).supportActionBar != null) {
            (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(true)
            (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.CUP)
        }

        loadCUP()
    }

    private fun loggedIn() {
        if (defaultSharedPreferences.getBoolean("cupConfigured", false)) {
            loadCUP(true)
        } else {
            alert(getString(R.string.alert_cupintegration_text),
                    getString(R.string.alert_cupintegration_title)) {
                yesButton {
                    startActivity<CUPConfigActivity>()
                    activity!!.finish()
                }
                noButton {
                    if (activity != null) {
                        (activity!! as MainActivity).launchTimetableFragment()
                    }
                }
            }.show()
        }
    }

    private fun loadCUP(loggedIn: Boolean = false) {
        if (loggedIn) Singleton.cupIntegration!!.getCUPUrl { url -> webView!!.loadUrl(url.split("\n")[0]) }
        else loggedIn()
    }
}
