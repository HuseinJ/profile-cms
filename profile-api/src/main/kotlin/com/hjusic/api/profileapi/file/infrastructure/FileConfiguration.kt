package com.hjusic.api.profileapi.file.infrastructure

import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.file.application.GetFile
import com.hjusic.api.profileapi.file.application.UploadFile
import com.hjusic.api.profileapi.file.model.Files
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FileConfiguration {

    @Bean
    fun files(eventPublisher: EventPublisher, fileDatabaseEntityRepository: FileDatabaseEntityRepository): Files {
        return FileDatabaseService(eventPublisher, fileDatabaseEntityRepository)
    }
    @Bean
    fun uploadFile(files: Files): UploadFile{
        return UploadFile(files)
    }

    @Bean
    fun getFile(files: Files): GetFile{
        return  GetFile(files)
    }
}