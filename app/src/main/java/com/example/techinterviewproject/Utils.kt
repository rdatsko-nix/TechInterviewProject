package com.example.techinterviewproject

object FakeImageUrlsProvider {

    fun getUrls(): List<String> {
        Thread.sleep(2000)
        return listOf(
            "https://images.pexels.com/photos/270408/pexels-photo-270408.jpeg",
            "https://images.pexels.com/photos/13268478/pexels-photo-13268478.jpeg",
            "https://images.pexels.com/photos/1000498/pexels-photo-1000498.jpeg"
        )
    }
}