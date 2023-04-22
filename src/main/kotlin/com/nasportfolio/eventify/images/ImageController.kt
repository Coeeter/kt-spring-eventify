package com.nasportfolio.eventify.images

import com.nasportfolio.eventify.images.models.UrlResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/images")
class ImageController(
    private val imageService: ImageService,
) {
    @PostMapping
    fun uploadImage(@RequestPart("image") image: MultipartFile): UrlResponse {
        return UrlResponse(
            url = imageService.uploadImage(image)
        )
    }
}