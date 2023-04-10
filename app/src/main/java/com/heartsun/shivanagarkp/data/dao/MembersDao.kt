package com.heartsun.shivanagarkp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.heartsun.shivanagarkp.domain.dbmodel.TblBoardMemberType
import com.heartsun.shivanagarkp.domain.dbmodel.TblContact
import com.heartsun.shivanagarkp.domain.dbmodel.TblCustomers
import kotlinx.coroutines.flow.Flow

@Dao
interface TblContactDao {
    @Query("SELECT * FROM tblContact")
    fun getAllData(): Flow<List<TblContact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: TblContact)

    @Query("DELETE FROM tblContact")
    suspend fun deleteAll()

    @Query("SELECT * FROM tblContact WHERE MemberType IN (:memberTypeId) AND IsActive IN ('1')" )
    fun getFilteredContacts(memberTypeId: Int): Flow<List<TblContact>>

    @Query("SELECT * FROM tblContact WHERE MemberType IN (:memberTypeId) AND IsActive IN ('0')")
    fun getFilteredOldContacts(memberTypeId: Int): Flow<List<TblContact>>
}

@Dao
interface TblBoardMemberTypeDao {
    @Query("SELECT * FROM tblBoardMemberType")
    fun getAllData(): Flow<List<TblBoardMemberType>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: TblBoardMemberType)

    @Query("DELETE FROM tblBoardMemberType")
    suspend fun deleteAll()
}

@Dao
interface TblCustomersDao {
    @Query("SELECT * FROM tblCustomers")
    fun getAllData(): Flow<List<TblCustomers>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: TblCustomers)

    @Query("DELETE FROM tblCustomers")
    suspend fun deleteAll()

    @Query("SELECT * FROM tblCustomers WHERE  MemberID = (:search_query) OR  MemName LIKE '%' ||:search_query|| '%'" )
    fun getFilteredData(search_query: String?): Flow<List<TblCustomers>>
}