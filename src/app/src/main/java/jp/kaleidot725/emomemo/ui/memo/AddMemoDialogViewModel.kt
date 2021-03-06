package jp.kaleidot725.emomemo.ui.memo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import jp.kaleidot725.emomemo.domain.usecase.create.CreateMemoUseCase
import jp.kaleidot725.emomemo.domain.usecase.select.ReselectMemoUseCase
import kotlinx.coroutines.launch

class AddMemoDialogViewModel(
    private val createMemoUseCase: CreateMemoUseCase,
    private val reselectMemoUseCase: ReselectMemoUseCase
) : ViewModel() {
    private val _isCompleted: LiveEvent<Boolean> = LiveEvent()
    val isCompleted: LiveData<Boolean> = _isCompleted

    val inputTitle: MutableLiveData<String> = MutableLiveData()
    private var inputtedTitle: String = ""
    private val inputTitleObserver: Observer<String> = Observer { inputtedTitle = it }

    init {
        inputTitle.observeForever(inputTitleObserver)
    }

    override fun onCleared() {
        inputTitle.removeObserver(inputTitleObserver)
    }

    fun success() {
        if (inputtedTitle.isEmpty()) {
            return
        }

        viewModelScope.launch {
            createMemoUseCase.execute(inputtedTitle)
            reselectMemoUseCase.execute()
            complete()
        }
    }

    fun cancel() {
        complete()
    }

    private fun complete() {
        _isCompleted.value = true
    }
}
