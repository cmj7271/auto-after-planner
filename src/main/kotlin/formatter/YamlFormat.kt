package formatter

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.SequenceStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import data.DailyTrace
import data.InstantSerializer
import data.LocalDateSerializer
import data.TraceReport
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

class YamlFormat {
    companion object {
        private val yaml: Yaml = Yaml(
            serializersModule = SerializersModule {
                contextual(LocalDateSerializer)
                contextual(InstantSerializer)
            },
            configuration = YamlConfiguration(
                polymorphismStyle = PolymorphismStyle.Property,
                sequenceBlockIndent = 2

            )
        )

        fun format(data: List<DailyTrace>): String {
            val report = TraceReport(
                totalCount = data.size,
                totalMinute = data.sumOf { it.durationMinute },
                generatedAt = data.first().timestamp,
                data = data)
            return yaml.encodeToString(
                TraceReport.serializer(), report)
        }

        fun parse(data: String): TraceReport {
            return yaml.decodeFromString(
                TraceReport.serializer(), data)
        }
    }
}