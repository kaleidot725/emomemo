package jp.kaleidot725.emomemo.model.dao

import androidx.room.*
import jp.kaleidot725.emomemo.model.entity.Memo

@Dao
interface MemoDao {
    @Insert
    fun insert(memo: Memo)

    @Update
    fun update(memo: Memo)

    @Delete
    fun delete(memo: Memo)

    @Query("delete from memo")
    fun deleteAll()

    @Query("select * from memo")
    fun getAll(): List<Memo>

    @Query("select * from memo where id = :id")
    fun getUser(id: Int): Memo
}
