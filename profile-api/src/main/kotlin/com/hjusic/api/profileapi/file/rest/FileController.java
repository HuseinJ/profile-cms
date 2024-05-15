package com.hjusic.api.profileapi.file.rest;

import com.hjusic.api.profileapi.file.application.GetFile;
import com.hjusic.api.profileapi.file.model.RegisteredFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FileController {

    @Autowired
    private GetFile getFile;

    @GetMapping(path = "/fd")
    public ResponseEntity<Resource> downloadOrView(@RequestParam("fileId") String fileId) {
        var result = getFile.getFile(fileId);

        if (result.wasFailure()) {
            return ResponseEntity.badRequest().build();
        }

        var file = result.getSuccess();
        byte[] content = ((RegisteredFile) file).getContent();
        String fileName = file.getName();

        ByteArrayResource resource = new ByteArrayResource(content);

        // Determine content type based on file extension
        MediaType contentType = getContentType(fileName);

        var responseBuilder = ResponseEntity.ok()
                .contentType(contentType)
                .contentLength(content.length);

        // Set content disposition header only if not an image
        if (!isImage(contentType)) {
            responseBuilder.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        }

        return responseBuilder.body(resource);
    }

    private MediaType getContentType(String fileName) {
        MediaType contentType = MediaType.APPLICATION_OCTET_STREAM;
        String lowerCaseFileName = fileName.toLowerCase();
        if (lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg")) {
            contentType = MediaType.IMAGE_JPEG;
        } else if (lowerCaseFileName.endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG;
        } else if (lowerCaseFileName.endsWith(".gif")) {
            contentType = MediaType.IMAGE_GIF;
        }
        return contentType;
    }

    private boolean isImage(MediaType mediaType) {
        return mediaType.equals(MediaType.IMAGE_JPEG) ||
                mediaType.equals(MediaType.IMAGE_PNG) ||
                mediaType.equals(MediaType.IMAGE_GIF);
    }
}