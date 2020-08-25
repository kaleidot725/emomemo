package jp.kaleidot725.emomemo.model.db.repository

import android.util.Log
import androidx.lifecycle.LiveData
import jp.kaleidot725.emomemo.model.db.dao.StatusDao
import jp.kaleidot725.emomemo.model.db.entity.StatusEntity

class StatusRepository(private val dao: StatusDao) {
    suspend fun update(notebookId: Int, memoID: Int) {
        Log.v("TAG", "update note $notebookId memo $memoID")
        dao.insert(StatusEntity(DEFAULT_ID, notebookId, memoID))
    }

    fun get(): LiveData<StatusEntity> {
        return dao.get(DEFAULT_ID)
    }

    companion object {
        private const val DEFAULT_ID = 0
    }
}