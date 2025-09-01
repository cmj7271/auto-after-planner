package formatter

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import data.InstantSerializer
import data.LocalDateSerializer
import data.TraceReport
import kotlinx.serialization.KSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

class GenericYamlFormatter<T>(
    private val serializer: KSerializer<T>
): Formatter<String, T> {
    override fun format(data: T): String {
        return yaml.encodeToString(
            serializer, data)
    }

    override fun parse(data: String): T {
        return yaml.decodeFromString(
            serializer, data)
    }

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
    }
}