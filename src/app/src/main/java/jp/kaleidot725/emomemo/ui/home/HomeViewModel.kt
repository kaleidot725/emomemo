package jp.kaleidot725.emomemo.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.hadilq.liveevent.LiveEvent
import jp.kaleidot725.emomemo.R
import jp.kaleidot725.emomemo.data.entity.StatusEntity
import jp.kaleidot725.emomemo.data.entity.StatusEntity.Companion.UNSELECTED_NOTEBOOK
import jp.kaleidot725.emomemo.data.view.MemoStatusView
import jp.kaleidot725.emomemo.domain.usecase.delete.DeleteMemoUseCase
import jp.kaleidot725.emomemo.domain.usecase.get.GetMemosUseCase
import jp.kaleidot725.emomemo.domain.usecase.get.GetStatusUseCase
import jp.kaleidot725.emomemo.domain.usecase.observe.ObserveMemoCountUseCase
import jp.kaleidot725.emomemo.domain.usecase.observe.ObserveNotebookCountUseCase
import jp.kaleidot725.emomemo.domain.usecase.select.SelectMemoUseCase
import jp.kaleidot725.emomemo.ui.common.SingleSelectList
import kotlinx.coroutines.launch

data class MemosWithSelectedSet(
    val memos: PagedList<MemoStatusView>,
    val selectedMemos: List<MemoStatusView>
)

class HomeViewModel(
    private val getStatusUseCase: GetStatusUseCase,
    private val getMemosUseCase: GetMemosUseCase,
    private val selectMemoUseCase: SelectMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
    private val observeNotebookCountUseCase: ObserveNotebookCountUseCase,
    private val observeMemoCountUseCase: ObserveMemoCountUseCase
) : ViewModel() {
    private val selectedMemos: SingleSelectList<MemoStatusView> = SingleSelectList()

    private val _navEvent: LiveEvent<NavEvent> = LiveEvent()
    val navEvent: LiveData<NavEvent> = _navEvent

    private val status: MutableLiveData<StatusEntity> = MutableLiveData()
    val canAddNotebook: LiveData<Boolean> = status.map { it.notebookId != UNSELECTED_NOTEBOOK }

    private val memos: LiveData<PagedList<MemoStatusView>> =
        status.switchMap { getMemosUseCase.executeLiveData(it.notebookId) }
    val memosWithSelectedSet: LiveData<MemosWithSelectedSet> = memos.map { MemosWithSelectedSet(it, selectedMemos.getList()) }

    private var notebookCount: Int = 0
    private var memoCount: Int = 0
    private val _error: MutableLiveData<Int> = MutableLiveData()
    val error: LiveData<Int> = _error

    private val _hasError: MutableLiveData<Boolean> = MutableLiveData(false)
    val hasError: LiveData<Boolean> = _hasError

    init {
        observeNotebookCountUseCase.dispose()
        observeNotebookCountUseCase.execute { count ->
            notebookCount = count
            updateHomeErrorMessage()
        }

        status.observeForever { status ->
            observeMemoCountUseCase.dispose()
            observeMemoCountUseCase.execute(status.notebookId) { count ->
                memoCount = count
                updateHomeErrorMessage()
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            status.value = getStatusUseCase.execute()
        }
    }

    fun tap(memo: MemoStatusView) {
        viewModelScope.launch {
            selectMemoUseCase.execute(memo.id)
            _navEvent.value = NavEvent.NavigateMemo
        }
    }

    fun longTap(memo: MemoStatusView) {
        viewModelScope.launch {
            selectMemoUseCase.execute(memo.id)
            _navEvent.value = NavEvent.NavigateMemoOption
        }
    }

    private fun updateHomeErrorMessage() {
        _hasError.value = (notebookCount == 0) || (memoCount == 0)
        _error.value = when {
            (notebookCount == 0) -> R.string.home_notebook_is_not_found
            (memoCount == 0) -> R.string.home_memo_is_not_found
            else -> R.string.empty
        }
    }

    sealed class NavEvent {
        object NavigateMemo : NavEvent()
        object NavigateMemoOption : NavEvent()
    }
}
