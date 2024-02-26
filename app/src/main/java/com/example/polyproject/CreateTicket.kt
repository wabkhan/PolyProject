package com.example.polyproject

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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

class CreateTicket : ComponentActivity() {

        private val PICK_IMG = 1
        private lateinit var ticketImg: android.net.Uri
        private var email: String? = null
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)

                var bundle = intent.extras

                if (bundle != null) {
                        email = bundle.getString("email")
                }

                setContentView(R.layout.ticket_creation)

                val doneButton = findViewById<Button>(R.id.done)
                val imgButton = findViewById<Button>(R.id.add_img)

                imgButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(intent, PICK_IMG)
                }

                doneButton.setOnClickListener(::submit)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)

                if (requestCode == PICK_IMG && resultCode == Activity.RESULT_OK && data != null) {
                        ticketImg = data.data!!

                        val imgElement = ImageView(this)

                        val params = LinearLayout.LayoutParams(
                                300,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                        imgElement.setImageURI(ticketImg)
                        imgElement.adjustViewBounds = true
                        imgElement.layoutParams = params

                        var parent = findViewById<LinearLayout>(R.id.img_holder)

                        parent.removeAllViews()

                        parent.addView(imgElement)
                }
        }

        private fun submit(view: android.view.View) {
                val issueName = findViewById<TextView>(R.id.issue).text.toString()
                val description = findViewById<TextView>(R.id.description).text.toString()

                Ticket(issueName, description, ticketImg, email)

                startNewActivity()
        }

        private fun startNewActivity() {
                val intent = Intent(this, HomePage::class.java)

                val bundle = Bundle()

                bundle.putString("email", email)

                intent.putExtras(bundle)
                startActivity(intent)
        }
}

