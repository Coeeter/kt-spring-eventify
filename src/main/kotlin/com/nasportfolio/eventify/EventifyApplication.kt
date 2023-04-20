package com.nasportfolio.eventify

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.io.FileInputStream

@SpringBootApplication
class EventifyApplication: CommandLineRunner {
    override fun run(vararg args: String?) {
        val inputStream = FileInputStream("eventify-secret-key.json")
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(inputStream))
            .setStorageBucket(System.getenv("FIREBASE_STORAGE_LOCATION"))
            .build()
        FirebaseApp.initializeApp(options)
    }
}

fun main(args: Array<String>) {
    runApplication<EventifyApplication>(*args)
}
