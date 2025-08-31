package data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDate

enum class Source { WEB, AI, MANUAL }
enum class Status { PENDING, CONFIRMED, IGNORED }

@Serializable
sealed interface Origin {
    @SerialName("Web") @Serializable
    data class Web(val url: String, val domain: String /* URL 축소버전 hidden */,
                   val visitId: Long? /* 브라우저 히스토리 id */,
                   val profile: String? = "Default" /* 브라우저 프로필 */) : Origin

    @SerialName("Ai") @Serializable
    data class Ai(val conversationId: String, val messageId: String?, val notes: String?) : Origin

    @SerialName("Manual") @Serializable
    data class Manual(val note: String?) : Origin
}

@Serializable
data class DailyTrace(
    val id: String,                 // 안정키 hidden for the user
    val source: Source,             // WEB/AI/MANUAL
    val title: String?,             // 페이지 제목
    val topic: String,              // 확정 주제 (없으면 "unassigned")
    val tags: Set<String> = emptySet(),
    val topicConfidence: Double? = null,

    @Contextual
    val timestamp: Instant,         // UTC
    @Contextual
    val dayBucket: LocalDate,       // 하루 경계 반영된 '소속 날짜'
    val durationMinute: Int? = null,   // 체류/학습 시간(분)

    val origin: Origin,             // 원천 상세
    val status: Status = Status.CONFIRMED // hidden for the user
)
