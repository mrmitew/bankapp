package com.github.mrmitew.bankapp.features.transactions.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

/**
 * Type Converter for Room that will allow us to use [BigDecimal] in our entities.
 * Internally, it will be stored as a String field into the actual database entity.
 */
class BigDecimalConverter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String = value.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal = BigDecimal(value)
}