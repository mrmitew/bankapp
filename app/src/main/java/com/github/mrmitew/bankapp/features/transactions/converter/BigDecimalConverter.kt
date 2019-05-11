package com.github.mrmitew.bankapp.features.transactions.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverter {
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal): String = value.toPlainString()

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal = BigDecimal(value)
}