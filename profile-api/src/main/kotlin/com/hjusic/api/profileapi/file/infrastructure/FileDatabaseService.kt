package com.hjusic.api.profileapi.file.infrastructure

import com.hjusic.api.profileapi.common.event.EventPublisher
import com.hjusic.api.profileapi.file.model.*
import java.util.*

class FileDatabaseService(
    private val eventPublisher: EventPublisher, private val fileDatabaseEntityRepository: FileDatabaseEntityRepository
) : Files {
    override fun getFile(fileUUID: UUID): Optional<File> {
        var potentialFile = fileDatabaseEntityRepository.findById(fileUUID)

        if (potentialFile.isEmpty) {
            return Optional.empty()
        }

        return Optional.of(
            map(
                fileDatabaseEntityRepository.findById(fileUUID).get()
            )
        )
    }

    override fun trigger(fileEvent: FileEvent): File {
        val file = when (fileEvent) {
            is FileUploaded -> handle(fileEvent)
            else -> {
                throw java.lang.IllegalArgumentException("Unsupported argument")
            }
        }

        eventPublisher.publish(fileEvent)

        return file
    }

    private fun handle(fileUploaded: FileUploaded): File {
        return map(
            fileDatabaseEntityRepository.save(
                FileDatabaseEntity(
                    fileUploaded.file.id, fileUploaded.file.name, fileUploaded.content.bytes, FileState.REGISTERED
                )
            )
        )
    }

    fun map(fileDatabaseEntity: FileDatabaseEntity): File {
        return when (fileDatabaseEntity.state) {
            FileState.REGISTERED -> RegisteredFile.from(fileDatabaseEntity.id, fileDatabaseEntity.name, fileDatabaseEntity.fileContent)
            else -> throw java.lang.IllegalArgumentException("Unsupported argument")
        }
    }
}