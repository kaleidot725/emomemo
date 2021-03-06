package jp.kaleidot725.emomemo.domain.usecase.get

import androidx.lifecycle.LiveData
import jp.kaleidot725.emomemo.data.repository.NotebookRepository

class GetNotebookCountUseCase(private val notebookRepository: NotebookRepository) {
    fun execute(): LiveData<Int> {
        return notebookRepository.getNotebookCountLiveData()
    }
}
