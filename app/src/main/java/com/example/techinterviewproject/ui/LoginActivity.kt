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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        loginButton = findViewById(R.id.buttonLogin)
        progressBar = findViewById(R.id.progressBar)
        imagesContainer = findViewById(R.id.imagesContainer)
        loginForm = findViewById(R.id.loginForm)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password must not be empty", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            Thread {
                Thread.sleep(2000) // simulate network delay

                runOnUiThread {
                    progressBar.visibility = View.GONE
                    loginForm.visibility = View.GONE
                    if (email == "a@a.com" && password == "a") {
                        Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()
                        // Simulate image download after login
                        downloadImages()
                    } else {
                        Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }


                }
            }.start()
        }
    }

    private fun downloadImages() {
        progressBar.visibility = View.VISIBLE
        imagesContainer.removeAllViews()

        Thread {
            Thread.sleep(2000) // simulate image loading

            val imageUrls = listOf(
                "https://images.pexels.com/photos/270408/pexels-photo-270408.jpeg",
                "https://images.pexels.com/photos/13268478/pexels-photo-13268478.jpeg",
                "https://images.pexels.com/photos/1000498/pexels-photo-1000498.jpeg"
            )

            runOnUiThread {
                progressBar.visibility = View.GONE

                for (url in imageUrls) {
                    ImageDownloadTask { bitmap ->
                        if (bitmap != null) {
                            val imageView = ImageView(this).apply {
                                setImageBitmap(bitmap)
                                val params = LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    600
                                )
                                params.setMargins(0, 16, 0, 16)
                                layoutParams = params
                                scaleType = ImageView.ScaleType.CENTER_CROP
                            }
                            imagesContainer.addView(imageView)
                        } else {
                            Log.e("LoginActivity", "Failed to download image from $url")
                        }
                    }.execute(url)
                }
                Toast.makeText(this, "Images downloaded", Toast.LENGTH_SHORT).show()
            }
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
            super.onPostExecute(result)
            callback(result)
        }
    }}