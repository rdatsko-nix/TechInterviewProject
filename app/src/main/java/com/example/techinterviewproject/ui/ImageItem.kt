package com.example.techinterviewproject.ui

import org.json.JSONArray
import org.json.JSONObject

data class ImageItem(val name: String, val url: String)

object FakeImageApi {

    fun fakeJsonFile1(): String {
        return """
            {
              "images": [
                {
                  "name": "Mountain Lake",
                  "url": "https://images.pexels.com/photos/270408/pexels-photo-270408.jpeg"
                },
                {
                  "name": "Street Cat",
                  "url": "https://images.pexels.com/photos/13268478/pexels-photo-13268478.jpeg"
                },
                {
                  "name": "City Night",
                  "url": "https://images.pexels.com/photos/1000498/pexels-photo-1000498.jpeg"
                }
              ]
            }
        """.trimIndent()
    }

    fun fakeJsonFile2(): String {
        return """
            {
              "images": [
                {
                  "name": "Japan",
                  "url": "https://images.pexels.com/photos/33116177/pexels-photo-33116177.jpeg"
                },
                {
                  "name": "Phone",
                  "url": "https://images.pexels.com/photos/1024864/pexels-photo-1024864.jpeg"
                },
                {
                  "name": "Camera",
                  "url": "https://images.pexels.com/photos/15752372/pexels-photo-15752372.jpeg"
                }
              ]
            }
        """.trimIndent()
    }

    fun fetchImages(fakeJson: String): List<ImageItem> {
        val result = mutableListOf<ImageItem>()
        val root = JSONObject(fakeJson)
        val arr: JSONArray = root.getJSONArray("images")
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            val name = obj.optString("name")
            val url = obj.optString("url")
            if (name.isNotBlank() && url.isNotBlank()) {
                result.add(ImageItem(name, url))
            }
        }
        return result
    }
}
