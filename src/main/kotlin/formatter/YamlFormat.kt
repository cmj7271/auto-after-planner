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

class YamlFormater: Formatter<String, TraceReport> {
    override fun format(data: TraceReport): String = Companion.format(data)
    override fun parse(data: String): TraceReport = Companion.parse(data)

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
        fun format(data: TraceReport): String {
            return yaml.encodeToString(
                TraceReport.serializer(), data)
        }

        fun parse(data: String): TraceReport {
            return yaml.decodeFromString(
                TraceReport.serializer(), data)
        }
    }
}