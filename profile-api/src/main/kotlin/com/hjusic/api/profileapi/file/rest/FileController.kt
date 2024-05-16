package com.hjusic.api.profileapi.file.rest

import com.hjusic.api.profileapi.file.application.GetFile
import com.hjusic.api.profileapi.file.model.RegisteredFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class FileController @Autowired constructor(
    private val getFile: GetFile
) {

    @GetMapping(path = ["/fd"])
    fun downloadOrView(@RequestParam("fileId") fileId: String): ResponseEntity<Resource> {
        val result = getFile.getFile(fileId)

        if (result.wasFailure()) {
            return ResponseEntity.badRequest().build()
        }

        val file = result.success as RegisteredFile?
        file ?: return ResponseEntity.badRequest().build()

        val content = file.content
        val fileName = file.name

        val resource = ByteArrayResource(content)

        // Determine content type based on file extension
        val contentType = getContentType(fileName)

        val responseBuilder = ResponseEntity.ok()
            .contentType(contentType)
            .contentLength(content.size.toLong())

        // Set content disposition header only if not an image
        if (!isImage(contentType)) {
            responseBuilder.header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"$fileName\""
            )
        }

        return responseBuilder.body(resource)
    }

    private fun getContentType(fileName: String): MediaType {
        val lowerCaseFileName = fileName.lowercase()
        return when {
            lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg") -> MediaType.IMAGE_JPEG
            lowerCaseFileName.endsWith(".png") -> MediaType.IMAGE_PNG
            lowerCaseFileName.endsWith(".gif") -> MediaType.IMAGE_GIF
            else -> MediaType.APPLICATION_OCTET_STREAM
        }
    }

    private fun isImage(mediaType: MediaType): Boolean {
        return mediaType == MediaType.IMAGE_JPEG ||
                mediaType == MediaType.IMAGE_PNG ||
                mediaType == MediaType.IMAGE_GIF
    }
}