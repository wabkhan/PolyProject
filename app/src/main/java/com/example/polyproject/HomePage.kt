package com.example.polyproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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

class HomePage : ComponentActivity() {
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var bundle = intent.extras

        if (bundle != null) {
            email = bundle.getString("email")
        }

        setContent {
            PolyProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()
                        .then(Modifier.padding(80.dp)),
                        contentAlignment = Alignment.Center) {
                        FloatingActionButton(
                            onClick = { startNewActivity(CreateTicket::class.java) },
                            modifier = Modifier.size(100.dp)
                                .then(Modifier.align(Alignment.CenterStart)), // Adjust the size as needed
                            shape = MaterialTheme.shapes.extraLarge
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Add")
                        }

                        FloatingActionButton(
                            onClick = { startNewActivity(TicketPage::class.java) },
                            modifier = Modifier.size(100.dp)
                                .then(Modifier.align(Alignment.CenterEnd)), // Adjust the size as needed
                            shape = MaterialTheme.shapes.extraLarge
                        ) {
                            Text(text = "View Tickets")
                        }
                    }
                }
            }
        }
    }

    private fun <T> startNewActivity(cls: Class<T>) {
        val intent = Intent(this, cls)

        val bundle = Bundle()

        bundle.putString("email", email)

        intent.putExtras(bundle)
        startActivity(intent)
    }
}

