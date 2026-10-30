package it.notenotify.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : Activity() {
    private lateinit var root: LinearLayout
    private lateinit var content: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildScreen()
        renderNotifications()
    }

    override fun onResume() {
        super.onResume()
        if (::content.isInitialized) renderNotifications()
    }

    private fun buildScreen() {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(24), dp(28), dp(24), dp(18))
            setBackgroundColor(0xFFFFFFFF.toInt())
        }

        val title = TextView(this).apply {
            text = "NOTE NOTIFY"
            textSize = 12f
            letterSpacing = 0.22f
            setTextColor(0xFF555555.toInt())
        }
        root.addView(title)

        val heading = TextView(this).apply {
            text = "Le tue notifiche,\ncome musica."
            textSize = 32f
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            setTextColor(0xFF111111.toInt())
            setPadding(0, dp(14), 0, dp(8))
        }
        root.addView(heading)

        val subtitle = TextView(this).apply {
            text = "Una notazione ritmica per le app che usi ogni giorno."
            textSize = 15f
            setTextColor(0xFF666666.toInt())
        }
        root.addView(subtitle)

        val permissionButton = Button(this).apply {
            text = "Attiva accesso alle notifiche"
            isAllCaps = false
            setTextColor(0xFFFFFFFF.toInt())
            setBackgroundColor(0xFF111111.toInt())
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            }
        }
        val btnParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, dp(52)
        ).apply { topMargin = dp(22); bottomMargin = dp(18) }
        root.addView(permissionButton, btnParams)

        val section = TextView(this).apply {
            text = "SPARTITO DELLE NOTIFICHE"
            textSize = 11f
            letterSpacing = 0.12f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(0xFF777777.toInt())
            setPadding(0, dp(8), 0, dp(10))
        }
        root.addView(section)

        val scroll = ScrollView(this)
        content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        val footer = TextView(this).apply {
            text = "WHATSAPP  ♩     INSTAGRAM  ♪"
            textSize = 12f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(0xFF333333.toInt())
            gravity = Gravity.CENTER
            setPadding(0, dp(16), 0, dp(6))
        }
        root.addView(footer)
        setContentView(root)
    }

    private fun renderNotifications() {
        if (!::content.isInitialized) return
        content.removeAllViews()

        val prefs = getSharedPreferences("notenotify", MODE_PRIVATE)
        val json = prefs.getString("active_notifications", "[]") ?: "[]"
        val array = try { JSONArray(json) } catch (_: Exception) { JSONArray() }

        var whatsappCount = 0
        var instagramCount = 0
        val rows = mutableListOf<JSONObject>()

        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            when (item.optString("app")) {
                "WhatsApp" -> whatsappCount++
                "Instagram" -> instagramCount++
            }
            rows.add(item)
        }

        if (whatsappCount > 0 && instagramCount > 0) {
            addNoteCard("♩.", "WhatsApp + Instagram", "Entrambe le app hanno notifiche attive · 1,5 battiti")
        } else if (whatsappCount > 0) {
            addNoteCard("♩", "WhatsApp", "$whatsappCount notifica/e attiva/e · 1 battito")
        } else if (instagramCount > 0) {
            addNoteCard("♪", "Instagram", "$instagramCount notifica/e attiva/e · mezzo battito")
        } else {
            addNoteCard("𝄽", "Nessuna notifica in attesa", "Quando arrivano notifiche da WhatsApp o Instagram, le vedrai qui.")
        }

        if (rows.isNotEmpty()) {
            val divider = View(this).apply { setBackgroundColor(0xFFE5E5E5.toInt()) }
            content.addView(divider, LinearLayout.LayoutParams(-1, dp(1)).apply {
                topMargin = dp(14); bottomMargin = dp(8)
            })
            val label = TextView(this).apply {
                text = "NOTIFICHE RILEVATE"
                textSize = 11f
                letterSpacing = 0.1f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(0xFF777777.toInt())
                setPadding(0, dp(6), 0, dp(8))
            }
            content.addView(label)
            rows.takeLast(30).asReversed().forEach { item ->
                val app = item.optString("app")
                val symbol = if (app == "WhatsApp") "♩" else "♪"
                val title = item.optString("title").ifBlank { app }
                val text = item.optString("text")
                addNoteCard(symbol, title, if (text.isBlank()) app else "$app  ·  $text")
            }
        }
    }

    private fun addNoteCard(symbol: String, title: String, description: String) {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(14), dp(14), dp(14), dp(14))
            setBackgroundColor(0xFFF7F7F7.toInt())
        }
        val note = TextView(this).apply {
            text = symbol
            textSize = 36f
            typeface = Typeface.create("serif", Typeface.NORMAL)
            setTextColor(0xFF111111.toInt())
            gravity = Gravity.CENTER
            minWidth = dp(58)
        }
        card.addView(note)
        val textBox = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(12), 0, 0, 0)
        }
        val titleView = TextView(this).apply {
            text = title
            textSize = 16f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(0xFF111111.toInt())
        }
        val descView = TextView(this).apply {
            text = description
            textSize = 12f
            setTextColor(0xFF666666.toInt())
            setPadding(0, dp(4), 0, 0)
        }
        textBox.addView(titleView)
        textBox.addView(descView)
        card.addView(textBox, LinearLayout.LayoutParams(0, -2, 1f))
        content.addView(card, LinearLayout.LayoutParams(-1, -2).apply {
            bottomMargin = dp(8)
        })
    }

    private fun dp(value: Int): Int =
        (value * resources.displayMetrics.density).toInt()
}
