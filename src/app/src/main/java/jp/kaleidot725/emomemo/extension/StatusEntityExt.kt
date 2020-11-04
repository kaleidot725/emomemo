package jp.kaleidot725.emomemo.extension

import androidx.annotation.StringRes
import jp.kaleidot725.emomemo.R
import jp.kaleidot725.emomemo.model.db.entity.StatusEntity
import jp.kaleidot725.emomemo.model.db.entity.StatusEntity.Companion.UNSELECTED_MEMO
import jp.kaleidot725.emomemo.model.db.entity.StatusEntity.Companion.UNSELECTED_NOTEBOOK
import jp.kaleidot725.emomemo.ui.EmptyStatus

@StringRes
fun StatusEntity.getMemoErrorMessage(): Int {
    return when (this.memoId) {
        UNSELECTED_MEMO -> R.string.memo_no_message_text
        else -> R.string.empty
    }
}

@StringRes
fun StatusEntity.getHomeErrorMessage(): Int {
    return when {
        this.notebookId == UNSELECTED_NOTEBOOK -> R.string.home_notebook_is_not_found
        this.memoId == UNSELECTED_MEMO -> R.string.home_memo_is_not_found
        else -> R.string.empty
    }
}
