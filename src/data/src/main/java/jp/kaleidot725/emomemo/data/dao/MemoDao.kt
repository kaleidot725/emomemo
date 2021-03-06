package jp.kaleidot725.emomemo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import jp.kaleidot725.emomemo.data.entity.MemoEntity

@Dao
interface MemoDao {
    @Insert
    suspend fun insert(memoEntity: MemoEntity)

    @Update
    suspend fun update(memoEntity: MemoEntity)

    @Delete
    suspend fun delete(memoEntity: MemoEntity)

    @Query("delete from memo where id in (:memoEntityIds)")
    suspend fun delete(memoEntityIds: List<Int>)

    @Query("select * from memo where id = :id")
    suspend fun getMemo(id: Int): MemoEntity

    @Query("select * from memo where notebookId = :notebookId")
    suspend fun getMemos(notebookId: Int): List<MemoEntity>

    @Query("select COUNT(*) from memo where notebookId = :notebookId")
    fun getMemoCountLiveData(notebookId: Int): LiveData<Int>
}
