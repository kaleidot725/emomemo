package jp.kaleidot725.emomemo.data.repository

import androidx.lifecycle.LiveData
import jp.kaleidot725.emomemo.data.dao.MemoDao
import jp.kaleidot725.emomemo.data.entity.MemoEntity

class MemoRepository(private val dao: MemoDao) {
    suspend fun insert(memo: MemoEntity) {
        dao.insert(memo)
    }

    suspend fun update(memoEntity: MemoEntity) {
        dao.update(memoEntity)
    }

    suspend fun delete(memoEntity: MemoEntity) {
        dao.delete(memoEntity)
    }

    suspend fun delete(memoIds: List<Int>) {
        dao.delete(memoIds)
    }

    suspend fun getMemo(id: Int): MemoEntity {
        return dao.getMemo(id)
    }

    suspend fun getMemos(notebookId: Int): List<MemoEntity> {
        return dao.getMemos(notebookId)
    }

    fun getMemoCountLiveData(notebookId: Int): LiveData<Int> {
        return dao.getMemoCountLiveData(notebookId)
    }
}
