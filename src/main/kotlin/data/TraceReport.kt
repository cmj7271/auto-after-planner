package data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

// YAML 문서의 최상위 구조를 나타내는 클래스
@Serializable
data class TraceReport(
    val data: List<DailyTrace>,
    @Contextual
    val generatedAt: Instant = Instant.now()
) {
    val totalCount: Int = data.size
    val totalMinute: Int = data.sumOf { it.durationMinute }
}