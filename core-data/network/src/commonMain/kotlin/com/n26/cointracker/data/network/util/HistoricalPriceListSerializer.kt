package com.n26.cointracker.data.network.util

import com.n26.cointracker.core.domain.model.HistoricalPriceDto
import com.n26.core.common.ktx.toLocalDate
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

// Custom serializer for a List<HistoricalPriceDto>
// It expects a JSON array where each element is an array of two numbers: [timestamp, value]
object HistoricalPriceListSerializer : KSerializer<List<HistoricalPriceDto>> {
	@OptIn(InternalSerializationApi::class)
	override val descriptor: SerialDescriptor =
		buildSerialDescriptor(
			serialName = "HistoricalPriceDtoList",
			kind = StructureKind.LIST,
		)

	override fun serialize(
		encoder: Encoder,
		value: List<HistoricalPriceDto>,
	) {
		val jsonEncoder = encoder as? JsonEncoder ?: error("This serializer only works with JSON")
		// Convert each HistoricalPriceDto into a JSON array [timestamp, value]
		val jsonArray =
			JsonArray(
				value.map { dataPoint ->
					JsonArray(listOf(JsonPrimitive(dataPoint.utcTimestamp), JsonPrimitive(dataPoint.price)))
				},
			)
		jsonEncoder.encodeJsonElement(jsonArray)
	}

	override fun deserialize(
		decoder: Decoder,
	): List<HistoricalPriceDto> {
		val jsonDecoder =
			decoder as? JsonDecoder
				?: error("This serializer only works with JSON")
		val jsonElement = jsonDecoder.decodeJsonElement()
		val jsonArray = jsonElement as? JsonArray ?: error("Expected JsonArray")

		return jsonArray.map { element ->
			val innerArray = element as? JsonArray ?: error("Expected inner array")
			if (innerArray.size != 2) error("Expected inner array of size 2")
			val timestamp = innerArray[0].jsonPrimitive.long
			val value = innerArray[1].jsonPrimitive.double

			HistoricalPriceDto(
				utcTimestamp = timestamp,
				localDate = timestamp.toLocalDate(),
				price = value,
			)
		}
	}
}
