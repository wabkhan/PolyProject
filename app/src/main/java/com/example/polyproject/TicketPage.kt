package com.example.polyproject

import android.app.Activity
import android.content.Intent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.polyproject.ui.theme.PolyProjectTheme
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class TicketPage : ComponentActivity() {
    private var email: String? = null
    private lateinit var inflater: LayoutInflater
    private lateinit var user_dir: com.google.firebase.database.DatabaseReference
    private lateinit var ticket_add_loc: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var bundle = intent.extras

        if (bundle != null) {
            email = bundle.getString("email")
        }

        val database = Firebase.database

        if (email != null) {
            user_dir = database.getReference(getEmailID(email)!!)
        }

        inflater =  LayoutInflater.from(this)

        setContentView(R.layout.ticket_frame)

        queryTickets("")

        ticket_add_loc = findViewById<LinearLayout>(R.id.add_loc)
    }

    private fun queryTickets(query: String) {
        user_dir.get().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                val tickets = task.result

                for (ticket in tickets.children) {
                    var ticketObj = Ticket(ticket, email)

                    ticketObj.addTicketView(inflater, ticket_add_loc)
                }
            }
        }
    }
}

