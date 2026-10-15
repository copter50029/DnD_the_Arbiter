# 🔮 the Arbiter (ผู้ชี้นำ) — Deep-Dive Architectural & Design Documentation

## 1. Executive Summary & Core Philosophy

**the Arbiter** is a specialized, production-ready Android companion application engineered for Tabletop Role-Playing Game (TTRPG) Dungeon Masters (DMs), specifically for D&D 5e.

Unlike traditional generative AI tools that focus on writing plot dialogue or continuing storylines, **the Arbiter** operates as an **advisory sparring partner**. Its core mission is to critique, stress-test, and refine a Dungeon Master's pre-session ideas using two analytical frameworks:
1. **Game Theory:** Evaluating choice architecture, Nash equilibria, dominant strategy checks, zero-sum vs. non-zero-sum stakes, and asymmetric information.
2. **Player Psychology:** Evaluating dramatic pacing (tension vs. relief cycles), Bartle player archetypes (Achievers, Explorers, Socializers, Killers), and player agency protection against railroading.

The application features a custom **Dark Gothic Grimoire Design System**, dynamic **bilingual engine** (English and Thai), **Room SQLite persistence**, **D&D 5e SRD REST API integration**, and a resilient **Google Gemini AI Engine** equipped with automated multi-model fallback chaining.

---

## 2. High-Level System Architecture

The application is structured following modern Android best practices, using a unidirectional data flow (UDF) **MVVM (Model-View-ViewModel)** architecture:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                            PRESENTATION LAYER                               │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │                     Jetpack Compose UI Screens                        │  │
│  │   CampaignScreen | SessionArchitectScreen | DramatisPersonaeScreen   │  │
│  │                     CompendiumScreen                                  │  │
│  └───────────────────────────────────▲───────────────────────────────────┘  │
│                                      │ StateFlow <UI State>
│                                      │ User Interactions / Intent Events
│  ┌───────────────────────────────────┴───────────────────────────────────┐  │
│  │                           ViewModels                                  │  │
│  │   CampaignViewModel | SessionArchitectViewModel                       │  │
│  │   DramatisPersonaeViewModel | CompendiumViewModel                     │  │
│  └───────────────────────────────────▲───────────────────────────────────┘  │
└──────────────────────────────────────┼──────────────────────────────────────┘
                                       │ Coroutines / Suspend Functions
┌──────────────────────────────────────┴──────────────────────────────────────┐
│                                DATA LAYER                                   │
│  ┌───────────────────────────────────────────────────────────────────────┐  │
│  │                           Repositories                                │  │
│  │   CampaignRepository | SessionRepository | CharacterRepository        │  │
│  │   UserSettingsRepository | DndRepository | AiRepository               │  │
│  └──────────────────┬─────────────────────────────────┬──────────────────┘  │
│                     │                                 │                     │
│  ┌──────────────────▼──────────────────┐  ┌───────────▼──────────────────┐  │
│  │       Local Persistence Source      │  │      Remote Network Source     │  │
│  │         Room Database v2            │  │  Retrofit 2 (D&D 5e SRD REST)  │  │
│  │   (SQLite DAOs & Entities)          │  │  Google Generative AI SDK      │  │
│  └─────────────────────────────────────┘  └─────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Data Layer & Persistence Architecture

### 3.1 Room Database Schema (`AppDatabase`)

Database file: `arbiter_grimoire.db` (Room Database Version `2`)

#### Entities
1. **`CampaignEntity` (`campaigns` table):**
   - `id` (Long, Primary Key, Auto-generate)
   - `title` (String) — Realm title
   - `description` (String) — Lore and premise
   - `partyLevel` (Int) — Average party level (1 to 20)
   - `createdAt` (Long) — Epoch timestamp

2. **`SessionLogEntity` (`session_logs` table):**
   - `id` (Long, Primary Key, Auto-generate)
   - `campaignId` (Long, Foreign Key concept)
   - `sessionNumber` (Int) — Session order index
   - `pastEventsSummary` (String) — Historical context
   - `currentDraft` (String) — DM encounter prep outline
   - `aiPacingFeedback` (String) — Cached psychology critique
   - `aiDilemmaFeedback` (String) — Cached game theory critique
   - `createdAt` (Long) — Epoch timestamp

3. **`CharacterEntity` (`characters` table):**
   - `id` (Long, Primary Key, Auto-generate)
   - `campaignId` (Long)
   - `name` (String) — Entity name
   - `characterType` (Enum: `BOSS`, `NPC`)
   - `rawBackstoryDraft` (String) — Backstory and objectives
   - `attachedMonsterIndex` (String?, Nullable) — Linked SRD creature slug
   - `aiPsychologyFeedback` (String) — Shadow-self / leverage critique
   - `aiGameTheoryFeedback` (String) — Minimax / Tit-for-Tat critique
   - `createdAt` (Long)

4. **`UserSettingsEntity` (`user_settings` table):**
   - `id` (Long, Primary Key = 1L — Singleton Row)
   - `preferredLanguage` (Enum: `EN`, `TH`)
   - `apiKey` (String) — User's Google Gemini API Key
   - `selectedModel` (String) — Active Gemini model (e.g. `gemini-3.8-flash`)

#### Converters (`Converters.kt`)
Converts complex types (`AppLanguage`, `CharacterType`) into Strings for SQLite storage using `@TypeConverter`.

---

## 4. Remote API & Network Architecture

### 4.1 D&D 5e System Reference Document (SRD) API
- **Endpoint:** `https://www.dnd5eapi.co/api/`
- **Network Engine:** Retrofit 2 + OkHttp 3 + Gson Converter
- **Service (`Dnd5eApiService`):**
  - `getMonsters()` $\rightarrow$ Queries entire bestiary index (`MonsterListDto`).
  - `getMonsterDetail(index)` $\rightarrow$ Fetches stats, CR, AC, HP, actions, and legendary traits (`MonsterDetailDto`).

### 4.2 Google Generative AI (Gemini) Engine & Fallback Architecture
The `AiRepository` wraps `com.google.ai.client.generativeai.GenerativeModel` with an automated **Resilience & Fallback Engine**:

```
                  ┌───────────────────────────────┐
                  │   User Triggers AI Analysis   │
                  └───────────────┬───────────────┘
                                  │
                  ┌───────────────▼───────────────┐
                  │ Try Selected Model (e.g. 3.8) │
                  └───────────────┬───────────────┘
                                  │
                        ┌─────────┴─────────┐
                        │ Success / Failure?│
                        └────┬─────────┬────┘
                Success      │         │ Error (503/429/High Demand)
       ┌─────────────────────┘         └─────────────────────┐
       │                                                     │
┌──────▼────────────┐                              ┌─────────▼─────────┐
│ Return AI Result  │                              │ Pause 1s & Retry  │
└───────────────────┘                              └─────────┬─────────┘
                                                             │
                                                   ┌─────────┴─────────┐
                                                   │ Success / Failure?│
                                                   └────┬─────────┬────┘
                                           Success      │         │ Still Failing
                                  ┌─────────────────────┘         │
                                  │                       ┌───────▼───────────────────────────┐
                          ┌───────▼───────────┐           │ Iterate Fallback Candidates Chain: │
                          │ Return AI Result  │           │  1. gemini-3.8-flash              │
                          └───────────────────┘           │  2. gemini-3.5-flash-lite         │
                                                          │  4. gemini-1.5-flash              │
                                                          │  5. gemini-1.5-flash-8b           │
                                                          └───────────────┬───────────────────┘
                                                                          │
                                                                ┌─────────┴─────────┐
                                                                │ Any Candidate OK? │
                                                                └────┬─────────┬────┘
                                                      Success        │         │ All Exhausted
                                             ┌───────────────────────┘         │
                                             │                       ┌─────────▼─────────┐
                                   ┌─────────▼─────────┐             │ Return Clean User │
                                   │ Return AI Result  │             │ High-Demand Error │
                                   └───────────────────┘             └───────────────────┘
```

#### Why This Is Critical:
On free-tier Gemini API keys, high-traffic models (or 2 RPM limit models like `gemini-1.5-pro`) trigger HTTP 503 ("High Demand") or 429 ("Resource Exhausted") errors.
The fallback engine automatically recovers by switching through low-latency Flash models without requiring manual user intervention.

---

## 5. Delimiter-Based Output Parsing

To guarantee structured UI separation without forcing JSON schemas that LLMs often misformat, prompts enforce exact delimiter tags:

```
<<<PSYCHOLOGY_START>>>
[Analysis content for Player Psychology, Pacing, Bartle Archetypes, and Agency]
<<<PSYCHOLOGY_END>>>

<<<GAME_THEORY_START>>>
[Analysis content for Game Theory, Minimax, Nash Equilibrium, and Choice Architecture]
<<<GAME_THEORY_END>>>
```

`AiRepository.extractSection()` uses substring indexing to cleanly parse out `pacingFeedback` and `dilemmaFeedback` for rendering in dedicated UI cards.

---

## 6. Dynamic Bilingual Engine (Locale Switching)

The application supports real-time switching between **English (EN)** and **Thai (TH)**:
1. **System Helper (`LocaleHelper.kt`):** Constructs a `Configuration` with the chosen `Locale("th")` or `Locale("en")`.
2. **Context Injection (`MainActivity.kt`):**
   ```kotlin
   val updatedConfig = Configuration(currentConfig).apply { setLocale(currentLocale) }
   val localizedContext = LocalContext.current.createConfigurationContext(updatedConfig)

   CompositionLocalProvider(
       LocalConfiguration provides updatedConfig,
       LocalContext provides localizedContext
   ) {
       ArbiterTheme { ArbiterMainScreen() }
   }
   ```
3. **Prompt Localizer:** `AiRepository` adjusts prompt framing instructions based on `AppLanguage`:
   - `AppLanguage.TH` $\rightarrow$ Instructs Gemini to respond in evocative Thai (ภาษาไทย) preserving TTRPG nomenclature.
   - `AppLanguage.EN` $\rightarrow$ Instructs Gemini to respond in articulate English with fantasy vocabulary.

---

## 7. UI & Design System (`ui/theme` & `ui/components`)

Designed around a **Dark Gothic Fantasy Grimoire** aesthetic:

- **Color Palette (`Color.kt`):**
  - `DarkLeatherBg` (`#120C0B`): Deep background
  - `DarkLeatherCard` (`#1A1210`): Card container surface
  - `ArcaneGold` (`#D4AF37`): Primary brand color
  - `ArcaneGoldBright` (`#F3E5AB`): Highlight text
  - `ArcaneGoldDark` (`#8C6D23`): Subtle borders
  - `DeepCrimson` (`#8B0000`): Error/warning accent
  - `ParchmentText` (`#E8DCC4`): Primary typography color

- **Custom Reusable Components (`GrimoireComponents.kt`):**
  - `GrimoireCard`: Elevated surface with golden borders and rounded corners.
  - `GrimoireTextField`: Outlined text field styled with serif labels.
  - `GrimoireButton`: Primary gold-accented call-to-action button.
  - `GrimoireSectionHeader`: Section divider with antique iconography.

---

## 8. Screen & Navigation Flow

Managed by `ArbiterNavigation.kt` with 4 primary destinations in the bottom navigation bar:

1. **Chronicle (`CampaignScreen`):**
   - Displays all registered campaign realms.
   - Summarizes statistics (total sessions, dread bosses, notable NPCs).
   - Contains top bar controls for **Language Toggle** and **Settings Dialog** (`ApiKeyDialog`).

2. **Session Architect (`SessionArchitectScreen`):**
   - Campaign realm selector.
   - Collapsible past session echoes ("Echoes of the Past").
   - Encounter draft text editor.
   - "Consult the Oracle" button triggering AI critique.
   - Side-by-side or stacked Psychology & Game Theory critique cards.

3. **Dramatis Personae (`DramatisPersonaeScreen`):**
   - Tab switcher: **Dread Boss** vs. **Notable NPC**.
   - Backstory & strategic motivation editor.
   - SRD Monster Statblock Attachment dialog.
   - AI Analysis triggers (Boss: Minimax & Win Conditions | NPC: Tit-for-Tat & Quid-Pro-Quo).

4. **Grand Compendium (`CompendiumScreen`):**
   - Live search bar querying D&D 5e SRD bestiary.
   - Monster detail view modal displaying CR, AC, HP, actions, and legendary traits.
   - "Export to Dramatis Personae" one-tap action.

---

## 9. Key Bug Fixes & Architectural Hardening Implemented

1. **Room Database Schema Sync:**
   - Incremented database version from `1` to `2` in `AppDatabase.kt` after adding `selectedModel` to `UserSettingsEntity`, preventing startup crashes.

2. **Race-Condition-Free Atomic Settings Persistence:**
   - Fixed a parallel coroutine issue where saving API key and model concurrently caused one task to overwrite the other with an old empty key.
   - Combined operations into an atomic method `userSettingsRepository.saveSettings(apiKey, model)`.

3. **503 High-Demand Mitigation:**
   - Created multi-stage fallback chain across high-throughput models (`gemini-3.8-flash`, `gemini-2.0-flash-lite`, `gemini-2.0-flash`, `gemini-1.5-flash`, `gemini-1.5-flash-8b`).
   - Added automated 1-second retry pause before fallback escalation.

---

## 10. Summary Table of Key Classes

| Package | Class / Component | Responsibilities |
| :--- | :--- | :--- |
| `data.local.entity` | `UserSettingsEntity` | Stores single-row user config (Language, API Key, AI Model) |
| `data.repository` | `AiRepository` | Prepares prompts, communicates with Gemini SDK, handles retries/fallbacks, and parses delimiters |
| `data.repository` | `UserSettingsRepository` | Manages atomic read/write operations for application settings |
| `ui.screens` | `CampaignScreen` | Displays campaign realms and hosts the Oracle Settings Dialog |
| `ui.screens` | `SessionArchitectScreen` | Encounter prep workspace and AI sparring partner view |
| `ui.screens` | `DramatisPersonaeScreen` | Boss/NPC management with attached SRD statblocks and strategic critique |
| `ui.screens` | `CompendiumScreen` | Searchable D&D 5e SRD bestiary index and statblock viewer |
| `ui.util` | `LocaleHelper` | Dynamically updates runtime Locale for English/Thai support |
