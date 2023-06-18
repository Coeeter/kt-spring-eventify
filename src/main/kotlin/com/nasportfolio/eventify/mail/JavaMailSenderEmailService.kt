package com.nasportfolio.eventify.mail

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


@Service
class JavaMailSenderEmailService(
    private val mailSender: JavaMailSender
) : EmailService {
    @Value("spring.mail.username")
    lateinit var email: String

    override fun sendPasswordResetEmail(
        to: String,
        subject: String,
        html: String,
        inlineImages: List<EmailService.InlineImage>
    ) {
        val message = mailSender.createMimeMessage()
        MimeMessageHelper(message, true).run {
            setFrom(email)
            setTo(to)
            setSubject(subject)
            setText(html, true)
            inlineImages.forEach {
                addInline(it.contentId, it.image)
            }
        }
        mailSender.send(message)
    }
}