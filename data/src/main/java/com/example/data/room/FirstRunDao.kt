package com.example.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.device.FirstRunEntity


@Dao
interface FirstRunDao {

    @Query("SELECT * FROM first_run WHERE id = :id")
    suspend fun getFirstRun(id: Int = 1): FirstRunEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFirstRun(firstRun: FirstRunEntity)
}