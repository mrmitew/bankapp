package com.github.mrmitew.bankapp.features.common.converter

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.math.BigDecimal

/**
 * Created by Stefan Mitev on 10-5-19.
 */

/**
 * Used by Kotlin's [Serializable] to serialize [BigDecimal]
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = BigDecimal::class)
class BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor = PrimitiveSerialDescriptor("java.math.BigDecimal", PrimitiveKind.DOUBLE)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toPlainString())
    }

    override fun deserialize(decoder: Decoder): BigDecimal {
        return BigDecimal(decoder.decodeString())
    }
}
