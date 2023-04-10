package com.heartsun.shivanagarkp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.heartsun.shivanagarkp.domain.dbmodel.TblTapTypeMaster
import kotlinx.coroutines.flow.Flow

@Dao
interface  TblTapTypeMasterDao {
    @Query("SELECT * FROM tblTapTypeMaster")
    fun getAllData(): Flow<List<TblTapTypeMaster>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: TblTapTypeMaster)

    @Query("DELETE FROM tblTapTypeMaster")
    suspend fun deleteAll()
}