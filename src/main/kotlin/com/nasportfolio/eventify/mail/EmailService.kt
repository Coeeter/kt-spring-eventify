package com.nasportfolio.eventify.mail

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.io.File

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    @Value("spring.mail.username")
    lateinit var email: String
    private val pathToTemplate = "/static/email-template.html"
    private val pathToImage = "/static/images/lock.png"
    fun sendPasswordResetEmail(to: String, url: String) {
        val message = mailSender.createMimeMessage()
        MimeMessageHelper(message, true).run {
            setFrom(email)
            setTo(to)
            setSubject("Eventify: Reset your password")
            val html = this::class.java
                .getResourceAsStream(pathToTemplate)!!
                .bufferedReader()
                .readText()
                .replace("{PASSWORD_RESET_LINK}", url)
            setText(html, true)
            val image = File(
                this::class.java
                    .getResource(pathToImage)!!
                    .file
            )
            addInline("lock", image)
        }
        mailSender.send(message)
    }
}