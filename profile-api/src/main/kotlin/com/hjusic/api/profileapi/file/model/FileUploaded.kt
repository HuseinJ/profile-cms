package com.hjusic.api.profileapi.file.model

import org.springframework.web.multipart.MultipartFile

class FileUploaded(file: File, val content: MultipartFile): FileEvent(file) {
}