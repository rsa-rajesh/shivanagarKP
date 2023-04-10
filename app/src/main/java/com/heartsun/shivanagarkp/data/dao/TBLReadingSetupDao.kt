package com.heartsun.shivanagarkp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.heartsun.shivanagarkp.domain.dbmodel.TBLReadingSetup
import kotlinx.coroutines.flow.Flow

@Dao
interface  TBLReadingSetupDao {
    @Query("SELECT * FROM tBLReadingSetup")
    fun getAllData(): Flow<List<TBLReadingSetup>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: TBLReadingSetup)

    @Query("DELETE FROM tBLReadingSetup")
    suspend fun deleteAll()
}