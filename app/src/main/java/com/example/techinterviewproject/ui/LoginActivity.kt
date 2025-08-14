package com.example.techinterviewproject.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.techinterviewproject.R
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var imagesContainer: LinearLayout
    private lateinit var loginForm: LinearLayout

    private val prefs by lazy { getSharedPreferences("auth_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        progressBar = findViewById(R.id.progressBar)
        imagesContainer = findViewById(R.id.imagesContainer)
        loginForm = findViewById(R.id.loginForm)

        val savedEmail = prefs.getString("email", null)
        val savedPass = prefs.getString("password", null)

        if (!savedEmail.isNullOrEmpty() && !savedPass.isNullOrEmpty()) {
            emailEditText.setText(savedEmail)
            passwordEditText.setText(savedPass)

            loginForm.visibility = View.GONE
            downloadImages()
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            progressBar.visibility = View.VISIBLE
            prefs.edit()
                .putString("email", email)
                .putString("password", password)
                .apply()

            Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()
            loginForm.visibility = View.GONE
            downloadImages()
        }
    }

    private fun downloadImages() {
        progressBar.visibility = View.VISIBLE
        imagesContainer.removeAllViews()

        Thread {
            val items1: List<ImageItem> = FakeImageApi.fetchImages(FakeImageApi.fakeJsonFile1())
            val items2: List<ImageItem> = FakeImageApi.fetchImages(FakeImageApi.fakeJsonFile2())

            val items = items1 + items2
                for (item in items) {
                    ImageDownloadTask { bitmap ->
                        if (bitmap != null) {
                            val itemContainer = LinearLayout(this).apply {
                                orientation = LinearLayout.VERTICAL
                                val params = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                params.setMargins(0, 16, 0, 16)
                                layoutParams = params
                            }

                            val imageView = ImageView(this).apply {
                                setImageBitmap(bitmap)
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    600
                                )
                                scaleType = ImageView.ScaleType.CENTER_CROP
                            }

                            val titleView = TextView(this).apply {
                                text = item.name
                                layoutParams = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                )
                                textSize = 16f
                            }

                            itemContainer.addView(imageView)
                            itemContainer.addView(titleView)
                            imagesContainer.addView(itemContainer)
                        } else {
                            Log.e("LoginActivity", "Failed to download image from ${item.url}")
                        }
                    }.execute(item.url)
                }
                progressBar.visibility = View.GONE

        }.start()
    }


    private class ImageDownloadTask(val callback: (Bitmap?) -> Unit) :
        AsyncTask<String, Void, Bitmap?>() {
        override fun doInBackground(vararg params: String): Bitmap? {
            val urlStr = params[0]
            return try {
                val url = URL(urlStr)
                val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input: InputStream = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
        override fun onPostExecute(result: Bitmap?) {
            callback(result)
        }
    }
}
