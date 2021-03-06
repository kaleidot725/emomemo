package jp.kaleidot725.emomemo.domain.usecase.select

import jp.kaleidot725.emomemo.data.repository.MessageRepository
import jp.kaleidot725.emomemo.data.repository.StatusRepository

class DeleteSelectedMessageUseCase(
    private val statusRepository: StatusRepository,
    private val messageRepository: MessageRepository
) {
    suspend fun execute() {
        val status = statusRepository.get() ?: return
        messageRepository.delete(listOf(status.messageId))
    }
}
