package com.nasportfolio.eventify.mail

import java.io.File

interface EmailService {
    fun sendPasswordResetEmail(
        to: String,
        subject: String,
        html: String,
        inlineImages: List<InlineImage> = emptyList()
    )

    data class InlineImage(
        val image: File,
        val contentId: String,
    )
}
