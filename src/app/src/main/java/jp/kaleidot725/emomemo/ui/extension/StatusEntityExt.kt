package jp.kaleidot725.emomemo.ui.extension

import androidx.annotation.StringRes
import jp.kaleidot725.emomemo.R
import jp.kaleidot725.emomemo.data.entity.StatusEntity
import jp.kaleidot725.emomemo.data.entity.StatusEntity.Companion.UNSELECTED_MEMO
import jp.kaleidot725.emomemo.data.entity.StatusEntity.Companion.UNSELECTED_NOTEBOOK

@StringRes
fun StatusEntity.getHomeErrorMessage(): Int {
    return when {
        this.notebookId == UNSELECTED_NOTEBOOK -> R.string.home_notebook_is_not_found
        this.memoId == UNSELECTED_MEMO -> R.string.home_memo_is_not_found
        else -> R.string.empty
    }
}
