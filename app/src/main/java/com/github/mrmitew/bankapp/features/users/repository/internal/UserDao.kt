package com.github.mrmitew.bankapp.features.users.repository.internal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mrmitew.bankapp.features.users.entity.UserEntity

/**
 * Created by Stefan Mitev on 4-5-19.
 */

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createUser(user: UserEntity)

    @Query("DELETE FROM ${UserEntity.TABLE_NAME} WHERE id=:id")
    suspend fun deleteUser(id: Int)
}
