package com.nasportfolio.eventify.images

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.firebase.cloud.StorageClient
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
class ImageService {
    fun uploadImage(file: MultipartFile): String {
        val bucket = StorageClient.getInstance().bucket()
        val extension = file.originalFilename?.takeLastWhile { it != '.' } ?: "png"
        val name = UUID.randomUUID()
            .toString()
            .replace("-", "") + "." + extension
        val blobId = BlobId.of(bucket.name, name)
        val blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType("media")
            .build()
        val blob = bucket.storage.create(blobInfo, file.inputStream.readBytes())
        return buildImageUrl(blob.name)
    }

    fun deleteImage(url: String): Boolean {
        val bucket = StorageClient.getInstance().bucket()
        return bucket[extractKeyFromUrl(url)].delete()
    }

    private fun extractKeyFromUrl(url: String): String {
        val startIndex = url.lastIndexOf("/") + 1
        val endIndex = url.indexOf("?alt=media")
        return url.substring(startIndex, endIndex)
    }

    private fun buildImageUrl(name: String): String {
        val bucket = StorageClient.getInstance().bucket()
        return "https://firebasestorage.googleapis.com/v0/b/${bucket.name}/o/${name}?alt=media"
    }
}