# Auto After Planner
사용자의 인터넷 및 LLM 기록 등을 기록하여 유의미한 통계기록을 보여줍니다.  
매일의 회고기록 작성 회초리는 덤입니다.

## 목적
지극히 개인적인 이유로 만들고자 합니다.  
1. 제 공부 습관을 보완합니다.
2. Kotlin 이라는 언어와 Amper 라는 빌드 툴에 적응합니다.
3. 최대한 단기완성을 목표로 합니다.

### 1. 공부습관 보완하기
평소의 제 공부방식은 무계획 그자체입니다.  
순간순간 하고 싶은, 생각나는 공부를 합니다.  
이는 다양하고 폭넓게 공부할 수 있다는 장점이있지만, 반대로 말하면 깊이가 부족합니다.  
또한 체계적이지 못하므로, 어느 공부가 부족한지 알 수 있는 방법이 없습니다.  

이에 따라, 데이터에 따른 통계를 바탕으로 보완하고자 합니다.  
단, 지금까지의 개인적인 경험에 따르면 스스로 작성해야하는 부분이 많다면, 금방 버려질 것임을 알고 있으므로,  
되도록 자동화하는 것이 목표입니다.  

이에 평소 공부 습관을 고려하여, 인터넷 및 LLM 기록을 데이터로 삼을 것이며,  
컴퓨터가 없는 오프라인 공부(책 읽기, 필기 등)을 고려하여 수동으로도 작성가능하게 만듭니다.  

### 2. Kotlin & ~~Amper~~
방학동안 공부한 Kotlin 을 바탕으로 프로젝트를 진행합니다.  
~~Amper 는 개인적인 흥미로 사용합니다.~~  
Kotlin 이 Multiplatform 을 지원한다는 점에서, 추후 앱으로 확장 가능성도 있습니다만, 현재는 컴퓨터에서 CLI 로 돌아갈 예정입니다.

#### 8/31 수정
`com.charleskorn.kaml` 라이브러리 사용 과정 중에서 내부 의존성 라이브러리를 자동으로 불러오지 못하는 에러가 발생하여,  
`build.gradle.kts` 로 다시 수정하게 되었습니다.

### 3. 단기 완성
부끄럽게 여태까지 제대로 된 프로젝트가 없다는 점과 스스로 늘어지면 끝까지 수행하지 못한다는 점을 반영하여,  
최대한 빠르게 완성하는 것이 목표입니다.  
작성일 기준(8/22)으로 개강 전(9/1) 까지 어느정도 마무리하는 것이 목적입니다.  

## 기능
### 데이터
제가 주로 공부에 사용하는 도구는 아래의 3가지가 있습니다.
* 인터넷
* LLM(ChatGPT, Gemini)
* 개인 필기 및 PDF

로컬에 저장되는 인터넷 방문 기록을 활용하여, 자동으로 주제-태그로 분류됩니다.  
LLM 은 별도의 방법을 찾기 전까지는 수동으로 json 으로 변환 요청, 붙여넣기 합니다.  
개인 필기 및 PDF 와 같이 수동으로 작성할 수 있는 방안도 구현해둡니다.  

### 데이터 가공
서로 다른 원본 데이터를 가공해서, 일관된 형태로 저장합니다.  
고유 id, 원본 데이터 주소(URL, LLM 대화 내용 요약본, PDF 위치), 주제, 태그 등으로 관리합니다.

### 데이터 관리
기본적으로 로컬에 하루 단위로 파일이 분리되어 저장됩니다.  
이를 외부에서 가공할 수 있도록, csv 로 내보내고, 읽는 기능을 추가합니다.  
단, csv 기능은 후순위로 추후에 구현될 수 있습니다.  

### 데이터 통계
간단하게 하루 분량의 데이터를 바탕으로 통계치를 하루 회고 템플릿에 추가하여 제시합니다.  
주로 어떤 주제와 태그를 공부했는지 나타내며, 통계치 시각화는 추후 구현될 수도 있습니다.

### 하루 회고
매일 특정 시간에 회고 작성 알람을 보냅니다.  
회고는 obsidian 을 활용하여 작성하도록 합니다.  
회고에는 하루 분량의 통계치와 기본적인 템플릿을 제공합니다.

### 의존성
데이터 수집은 크로미움 기반인 ARC 의 History 파일에 의존합니다.  
정해진 양식의 json 파일 혹은 (조금 더 유저친화적으로) 작성하는 방법도 구현할 예정입니다.  

정기적인 데이터 수집 및 알람은 macOS 의 일부 기능을 활용할 예정이기 때문에,  
다른 os 에서는 당장은 지원하지 않습니다.  

obsidian 은 마크다운을 읽기 위한 도구로서, 마크다운을 읽을 수 있다면 다른 도구를 활용해도 괜찮습니다.

아래는 전체적인 기능 플로우차트입니다.
```mermaid
sequenceDiagram
    autonumber
    participant Launchd as "스케줄러(launchd)"
    participant CLI as "Kotlin CLI"
    participant Obsidian as "Obsidian 노트"
    participant Notifier as "terminal-notifier"
    participant User as "사용자"

    Note over Launchd: 매 2시간
    Launchd->>CLI: 동기화 실행(증분 수집·분류)
    CLI-->>CLI: events.jsonl 업데이트

    Note over Launchd: 22:30 (회고 시각)
    Launchd->>CLI: 오늘 요약/상세 생성
    CLI->>Obsidian: daily 요약 작성
    CLI->>Obsidian: details 전체 목록 작성
    Launchd->>Obsidian: 오늘 일일 노트 열기
    User-->>Obsidian: 회고 작성·저장

    Note over Launchd: 23:30 & 23:50
    Launchd->>CLI: 회고 작성 여부 확인(nag)
    alt 미작성/불충분
        CLI-->>Notifier: 클릭 가능한 배너 전송(딥링크)
        Notifier-->>User: 알림 표시(클릭 시 오늘 노트)
    else 작성 완료
        CLI-->>Launchd: 정상 종료
    end

```

## 기능 세부 사항
아래는 기능을 구현하기 위한 기술적인 구조도입니다.  
실제 구현 전까지는 변동가능성이 있습니다.

```mermaid
flowchart TD
%% ===== 원천 =====
    subgraph S[기록 원천]
        A1["브라우저 기록 (Arc/Chrome)"]:::src
        A2["AI 대화 기록 (JSON)"]:::src
        A3["직접 기록 (수동 입력)"]:::src
    end

%% ===== 모으기(Kotlin CLI) =====
    I1["기록 모으기(증분)\n- 프로필 자동 탐색\n- 새 방문만 수집\n- 체류시간 = 다음 방문까지(최대 10분)"]:::proc
    S --> I1

%% ===== 표준화/저장 (.study-insights) =====
    subgraph N[표준화 & 저장]
        N1["events.jsonl\n(시간/주소/제목/주제/체류)"]:::store
        N2["checkpoints.json\n(프로필별 마지막 시각)"]:::store
        N3["overrides.yml\n(주제·태그·시간·제목 덮어쓰기)"]:::store
    end
    I1 --> N1
    I1 --> N2

%% ===== 설정 =====
    subgraph C[설정]
        C1["topics.yml\n(키워드/사이트/제외어)"]:::conf
        C2["settings.yml\n(회고 시각·하루 경계·최대 체류)"]:::conf
        C3["ignore.yml\n(검색·광고·리다이렉트 등 무시)"]:::conf
    end

%% ===== 자동 분류 파이프라인 =====
    subgraph T[자동 분류]
        T1["규칙 매칭\n(키워드·도메인 점수 ≥ θ₁)"]:::proc
        T2["최근 기록 기반 분류\n(TF-IDF + NaiveBayes/LogReg\n확률 ≥ θ₂ & 격차 Δ₂)"]:::proc
        T3["문장 임베딩 유사도\n(유사도 ≥ θ₃ & 격차 Δ₃)"]:::proc
        T4["보류 → 주제: unassigned\n(선택) AI-제안 태그"]:::proc
    end
    N1 --> T1 --> T2 --> T3 --> T4
    C1 --> T1
    C1 --> T2
    C1 --> T3
    C3 --> T1

%% ===== 수동 수정 반영 =====
    N3 -->|덮어쓰기| T1
    N3 -->|덮어쓰기| T2
    N3 -->|덮어쓰기| T3
    N3 -->|덮어쓰기| T4

%% ===== 요약/리포트 생성(Kotlin CLI) =====
    subgraph R[요약/리포트]
        R1["오늘 요약 만들기\n(Top 과목·출처 비율·미분류·히트맵)"]:::out
        R2["전체 소스 테이블 만들기"]:::out
        R3["CSV 내보내기/적용\n(대량 편집)"]:::tool
        R4["오래된 기록 묶기(compact)"]:::tool
    end
    T1 --> R1
    T2 --> R1
    T3 --> R1
    T4 --> R1
    N1 --> R2
    N1 --> R3
    N1 --> R4
    N3 --> R1

%% ===== Obsidian 연동(A안: 요약만 본문) =====
    subgraph O[Obsidian Vault]
        O1["Journal/daily/YYYY-MM-DD.md\n→ 회고 본문(요약만)"]:::vault
        O2["Journal/details/YYYY-MM-DD-details.md\n→ 전체 소스 목록"]:::vault
        O3["Reports/weekly-rolling.md\n→ 주간 요약(옵션)"]:::vault
    end
    R1 -->|쓰기| O1
    R2 -->|쓰기| O2

%% ===== 스케줄 & 알림 =====
    subgraph L[launchd & 알림]
        L1["launchd 에이전트\n- 2시간마다 동기화"]:::sched
        L2["launchd 에이전트\n- 22:30 요약 생성·노트 열기"]:::sched
        L3["launchd 에이전트\n- 23:30/23:50 회고 확인"]:::sched
        TN["terminal-notifier\n(클릭 시 Obsidian 열기)"]:::notif
    end
    L1 --> I1
    L2 --> R1
    L2 --> R2
    L2 --> O1
    L3 --> TN
    C2 --> L2
    C2 --> L3

%% ===== 사용자 액션 =====
    U["사용자(회고 작성)"]:::user
    O1 --> U
    TN --> U

%% ===== 다크 모드 친화 스타일 =====
    classDef src   fill:#1e40af,stroke:#60a5fa,color:#ffffff,stroke-width:1.5;
    classDef proc  fill:#065f46,stroke:#34d399,color:#ffffff,stroke-width:1.5;
    classDef store fill:#7c2d12,stroke:#fb923c,color:#ffffff,stroke-width:1.5;
    classDef conf  fill:#4c1d95,stroke:#a78bfa,color:#ffffff,stroke-width:1.5;
    classDef out   fill:#164e63,stroke:#22d3ee,color:#ffffff,stroke-width:1.5;
    classDef tool  fill:#7f1d1d,stroke:#fda4af,color:#ffffff,stroke-width:1.5;
    classDef vault fill:#0f172a,stroke:#38bdf8,color:#ffffff,stroke-width:1.5;
    classDef sched fill:#1f2937,stroke:#9ca3af,color:#ffffff,stroke-width:1.5;
    classDef notif fill:#111827,stroke:#f59e0b,color:#ffffff,stroke-width:1.5;
    classDef user  fill:#0b1020,stroke:#93c5fd,color:#ffffff,stroke-width:1.5;
```