package it.notenotify.app

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import org.json.JSONArray
import org.json.JSONObject

class MusicNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        updateNotification(sbn, false)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        updateNotification(sbn, true)
    }

    private fun updateNotification(sbn: StatusBarNotification, removed: Boolean) {
        val app = when (sbn.packageName) {
            "com.whatsapp", "com.whatsapp.w4b" -> "WhatsApp"
            "com.instagram.android" -> "Instagram"
            else -> return
        }

        val prefs = getSharedPreferences("notenotify", MODE_PRIVATE)
        val old = try {
            JSONArray(prefs.getString("active_notifications", "[]") ?: "[]")
        } catch (_: Exception) { JSONArray() }

        val items = LinkedHashMap<String, JSONObject>()
        for (i in 0 until old.length()) {
            val obj = old.optJSONObject(i) ?: continue
            items[obj.optString("key")] = obj
        }

        if (removed) {
            items.remove(sbn.key)
        } else {
            val extras = sbn.notification.extras
            val title = extras.getCharSequence(Notification.EXTRA_TITLE)?.toString().orEmpty()
            val text = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString().orEmpty()
            items[sbn.key] = JSONObject().apply {
                put("key", sbn.key)
                put("app", app)
                put("title", title)
                put("text", text)
                put("when", sbn.postTime)
            }
        }

        val result = JSONArray()
        items.values.takeLast(100).forEach { result.put(it) }
        prefs.edit().putString("active_notifications", result.toString()).apply()
    }
}
