package jp.kaleidot725.emomemo.domain.usecase.delete

import jp.kaleidot725.emomemo.data.entity.NotebookEntity
import jp.kaleidot725.emomemo.data.entity.StatusEntity.Companion.UNSELECTED_MEMO
import jp.kaleidot725.emomemo.data.entity.StatusEntity.Companion.UNSELECTED_MESSAGE
import jp.kaleidot725.emomemo.data.entity.StatusEntity.Companion.UNSELECTED_NOTEBOOK
import jp.kaleidot725.emomemo.data.repository.NotebookRepository
import jp.kaleidot725.emomemo.data.repository.StatusRepository

class DeleteNotebookUseCase(
    private val statusRepository: StatusRepository,
    private val notebookRepository: NotebookRepository
) {
    suspend fun execute(notebook: NotebookEntity) {
        notebookRepository.delete(notebook)
        reselectNotebook(notebook)
    }

    private suspend fun reselectNotebook(deleteItem: NotebookEntity) {
        val oldStatus = statusRepository.get()
        if (deleteItem.id == oldStatus?.notebookId) {
            val notebooks = notebookRepository.getAll()
            if (notebooks.isNotEmpty()) {
                statusRepository.update(notebooks.first().id, UNSELECTED_MEMO, UNSELECTED_MESSAGE)
            } else {
                statusRepository.update(UNSELECTED_NOTEBOOK, UNSELECTED_MEMO, UNSELECTED_MESSAGE)
            }
        }
    }
}
