package com.heartsun.shivanagarkp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.heartsun.shivanagarkp.domain.dbmodel.TBLReadingSetupDtl
import kotlinx.coroutines.flow.Flow

@Dao
interface  TBLReadingSetupDtlDao {
    @Query("SELECT * FROM tBLReadingSetupDtl")
    fun getAllData(): Flow<List<TBLReadingSetupDtl>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: TBLReadingSetupDtl)

    @Query("DELETE FROM tBLReadingSetupDtl")
    suspend fun deleteAll()
}