package com.example.polyproject

import java.time.LocalDate
import kotlin.random.Random

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.polyproject.ui.theme.PolyProjectTheme
import com.google.firebase.database.DataSnapshot
import kotlin.random.nextULong
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

val NOT_REVIEWED = 0
val IN_REVIEW = 1
val REVIEWED = 2

private val COLORS = listOf(Color.Red, Color.Yellow, Color.Green).map { it.toArgb() }
private val STATUSES = listOf("Not Reviewed", "In Review", "Reviewed")

public fun getEmailID(email: String?): String? {
    if (email != null) {
        return email.map { if (it == '@') "_at_" else if (it == '.') "_dot_" else it }.joinToString(separator = "")
    }

    return null
}

class Ticket {

    private var mapForm: Map<String, Any>
    private val DATABASE = Firebase.database
    private val STORAGE = Firebase.storage
    private var image: Uri? = null
    private var emailID: String? = null
    private var id: String

    constructor(name: String, description: String, image: Uri, email: String?) {
        var date = LocalDate.now()

        mapForm = mapOf("name" to name,
                        "description" to description,
                        "date" to listOf(date.monthValue, date.dayOfMonth, date.year),
                        "status" to NOT_REVIEWED)

        this.image = image
        id = Random.nextULong().toString()

        emailID = getEmailID(email)

        val DBRef = DATABASE.getReference("${emailID}/${id}")

        DBRef.setValue(mapForm)

        val storageRef = STORAGE.reference.child("imgs/${emailID}/${id}")

        storageRef.putFile(this.image!!)
    }

    constructor(snapshot: DataSnapshot, email: String?) {
        mapForm = snapshot.value as Map<String, Any>
        emailID = getEmailID(email)
        id = snapshot.key.toString()

        val storageRef = STORAGE.getReference("imgs/${emailID}/${id}")

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            this.image = uri
            print(this.image)
        }
    }

    fun addTicketView(inflater: LayoutInflater, addLoc: LinearLayout) {
        var view = inflater.inflate(R.layout.single_ticket, addLoc) as LinearLayout

        var title = view.findViewById<TextView>(R.id.ticket_name)
        title.text = mapForm["name"] as String

        var date = view.findViewById<TextView>(R.id.date)
        date.text = arrayOf(mapForm["date"]).joinToString(separator = "-")

        var status = view.findViewById<TextView>(R.id.status)
        var statusCode = (mapForm["status"] as Long).toInt()
        status.text = STATUSES[statusCode]
        status.setBackgroundColor(COLORS[statusCode])

        var description = view.findViewById<TextView>(R.id.description)
        description.text = mapForm["description"] as String

        var img = view.findViewById<ImageView>(R.id.ticket_img)
        println(this.image)
        img.setImageURI(this.image)
    }
}