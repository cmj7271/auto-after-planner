package formatter

import data.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.LocalDate

class YamlFormatTest : StringSpec({

    // --- 테스트 설정 ---
    // 여러 테스트 케이스에서 공통으로 사용할 입력 데이터입니다.
    val inputTraceList: List<DailyTrace> = listOf(
        DailyTrace(
            id = "trace-web-001",
            source = Source.WEB,
            title = "Kotlin Serialization Guide",
            topic = "Kotlin",
            tags = setOf("kotlin", "serialization", "kaml"),
            topicConfidence = 0.95,
            timestamp = Instant.parse("2025-08-31T10:15:30.00Z"),
            dayBucket = LocalDate.of(2025, 8, 31),
            durationMinute = 25,
            origin = Origin.Web(
                url = "https://kotlinlang.org/docs/serialization.html",
                domain = "kotlinlang.org",
                visitId = 98765L,
                profile = "Work"
            ),
            status = Status.CONFIRMED
        ),
        DailyTrace(
            id = "trace-ai-002",
            source = Source.AI,
            title = "Amper 설정에 대한 질문",
            topic = "Amper",
            tags = setOf("amper", "build-tool"),
            topicConfidence = null, // nullable 필드 테스트
            timestamp = Instant.parse("2025-08-31T11:20:00.00Z"),
            dayBucket = LocalDate.of(2025, 8, 31),
            durationMinute = 5,
            origin = Origin.Ai(
                conversationId = "conv-abc-123",
                messageId = "msg-xyz-456",
                notes = "Amper YAML configuration test"
            ),
            status = Status.PENDING
        )
    )

    // 테스트 1: `format` 메소드가 TraceReport 객체를 올바르게 생성하고 직렬화하는지 검증합니다.
    "format should serialize a list of traces into a TraceReport YAML" {
        // --- 실행 ---
        val actualYaml = YamlFormat.format(inputTraceList)
        println("--- Generated YAML for format test ---\n$actualYaml")

        // --- 검증 ---
        // `generatedAt` 타임스탬프가 동적이므로, 고정된 문자열과 비교할 수 없습니다.
        // 대신, 생성된 YAML을 다시 객체로 파싱하여 그 구조와 내용이 올바른지 확인합니다.
        val parsedReport = YamlFormat.parse(actualYaml)

        // 메타데이터가 올바른지 확인합니다.
        parsedReport.totalCount shouldBe inputTraceList.size

        // 내부에 포함된 데이터가 원본과 동일한지 확인합니다.
        parsedReport.data shouldBe inputTraceList
    }

    // 테스트 2: `parse` 메소드가 YAML 문자열을 TraceReport 객체로 올바르게 역직렬화하는지 검증합니다.
    "parse should deserialize a TraceReport YAML into an object" {
        // --- 테스트용 정적 데이터 ---
        // 역직렬화 기능을 테스트하기 위한, 타임스탬프가 고정된 YAML 문자열입니다.
        val yamlToParse = """
            totalCount: 2
            totalMinute: 30
            generatedAt: "2025-08-31T12:00:00Z"
            data:
              - id: "trace-web-001"
                source: "WEB"
                title: "Kotlin Serialization Guide"
                topic: "Kotlin"
                tags:
                  - "kotlin"
                  - "serialization"
                  - "kaml"
                topicConfidence: 0.95
                timestamp: "2025-08-31T10:15:30Z"
                dayBucket: "2025-08-31"
                durationMinute: 25
                origin:
                  type: "Web"
                  url: "https://kotlinlang.org/docs/serialization.html"
                  domain: "kotlinlang.org"
                  visitId: 98765
                  profile: "Work"
                status: "CONFIRMED"
              - id: "trace-ai-002"
                source: "AI"
                title: "Amper 설정에 대한 질문"
                topic: "Amper"
                tags:
                  - "amper"
                  - "build-tool"
                topicConfidence: null
                timestamp: "2025-08-31T11:20:00Z"
                dayBucket: "2025-08-31"
                durationMinute: 5
                origin:
                  type: "Ai"
                  conversationId: "conv-abc-123"
                  messageId: "msg-xyz-456"
                  notes: "Amper YAML configuration test"
                status: "PENDING"
        """.trimIndent()

        // 위 YAML 문자열이 파싱되었을 때 기대되는 객체입니다.
        val expectedReportObject = TraceReport(
            totalCount = 2,
            totalMinute = 30,
            generatedAt = Instant.parse("2025-08-31T12:00:00Z"),
            data = inputTraceList // `data` 부분은 위에서 정의한 inputTraceList와 동일합니다.
        )

        // --- 실행 ---
        val parsedReport = YamlFormat.parse(yamlToParse)

        // --- 검증 ---
        // 파싱된 객체가 기대하는 객체와 완전히 일치하는지 확인합니다.
        parsedReport shouldBe expectedReportObject
    }
})
