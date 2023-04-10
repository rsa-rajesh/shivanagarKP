package com.heartsun.shivanagarkp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.heartsun.shivanagarkp.domain.dbmodel.TblMember
import kotlinx.coroutines.flow.Flow



@Dao
interface TblMemberDao {
    @Query("SELECT * FROM tblMember")
    fun getAllData(): Flow<List<TblMember>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: TblMember)

    @Query("DELETE FROM tblMember")
    suspend fun deleteAll()


    @Query("DELETE FROM tblMember WHERE MemberID IN (:membersId)")
    suspend fun delete(membersId: Int)

    @Query("UPDATE tblMember SET PinCode=:changedPin WHERE MemberID = :memberID")
    suspend fun updatePin(memberID: Int, changedPin: Int)
}