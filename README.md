# the Arbiter (ผู้ชี้นำ) 🗡️🔮

**the Arbiter** is an AI-assisted Dungeon Master (DM) companion application designed for D&D 5e TTRPGs. Acting as an advisory sparring partner, it uses **Game Theory** and **Player Psychology** to critique and stress-test session drafts, boss encounters, and NPC social dynamics.

---

## 🌟 Key Features

### 1. 📜 Campaign Chronicle (บันทึกแคมเปญแห่งโชคชะตา)
- **Realm & Sanctuary Overview:** Manage multiple TTRPG campaign realms, track average party levels (1–20), total sessions, dread bosses, and active NPCs.
- **Oracle Key & Model Configuration:** Securely configure your Google Gemini API Key and select your preferred AI model (`gemini-3.8-flash` or `gemini-3.5-flash-lite`, or custom model names).
- **Grimoire Tongue (Bilingual Support):** Real-time dynamic locale switching between **English** and **Thai (ภาษาไทย)** with localized UI and evocative prompt framing.

### 2. 🏛️ Session Architect (ผู้ออกแบบเซสชัน)
- **Contextual Recaps (Echoes of the Past):** Automatically loads previous session logs to maintain campaign continuity.
- **AI Oracle Consultation:** Evaluates scenario drafts against two strategic lenses:
  - **Player Psychology & Dramatic Pacing:** Analyzes tension vs. relief curves, Bartle player archetypes (Achievers, Explorers, Socializers, Killers), and agency stress-testing (railroading prevention).
  - **Game Theory & Choice Architecture:** Checks for dominant strategy trivialization, zero-sum vs. non-zero-sum dilemmas, and Nash equilibrium/information asymmetry.

### 3. 👺 Dramatis Personae (บุคคลในเรื่องราว)
- **Dread Bosses & Notable NPCs:** Deconstruct key campaign figures beyond simple lore.
- **SRD Statblock Linking:** Directly attach creature statblocks from the D&D 5e SRD Bestiary.
- **Deep AI Analysis:**
  - *Bosses:* Minimax tactics, non-TPK win conditions, action economy, and Shadow-Self psychological flaws.
  - *NPCs:* Tit-for-Tat alignment, asymmetric hidden information, emotional leverage, and quid-pro-quo boundaries.

### 4. 📖 Grand Compendium (คลังข้อมูล SRD สากล)
- **D&D 5e Bestiary Search:** Live search and filter monsters, beasts, and monstrosities from the D&D 5e System Reference Document (SRD).
- **Statblock Inspector:** View complete Challenge Rating (CR), Armor Class (AC), Hit Points (HP), special traits, actions, and legendary abilities.
- **One-Tap Export:** Export any SRD monster directly into Dramatis Personae.

---

## 🏗️ Architecture & Technical Design

The project strictly follows **Modern Android App Architecture** guidelines, leveraging **MVVM (Model-View-ViewModel)** with clean layer separation:

```
┌────────────────────────────────────────────────────────┐
│                      UI LAYER                          │
│   Jetpack Compose | Material 3 Theme | NavHost         │
└───────────────────────────┬────────────────────────────┘
                            │ StateFlow / Events
┌───────────────────────────▼────────────────────────────┐
│                  VIEWMODEL LAYER                       │
│   CampaignViewModel | SessionArchitectViewModel       │
│   DramatisPersonaeViewModel | CompendiumViewModel      │
└───────────────────────────┬────────────────────────────┘
                            │ Coroutines / Repository Calls
┌───────────────────────────▼────────────────────────────┐
│                  DATA LAYER                            │
│  ┌─────────────────────────┬────────────────────────┐  │
│  │ Local: Room Database    │ Remote: Retrofit & AI  │  │
│  │ (SQLite DAOs & Entities)│ (D&D SRD API & Gemini) │  │
│  └─────────────────────────┴────────────────────────┘  │
└────────────────────────────────────────────────────────┘
```

### 🛠️ Architecture Highlights

- **UI & Presentation Layer:**
  - **Jetpack Compose & Material 3:** Declarative UI styled in a dark gothic grimoire aesthetic (Arcane Gold, Deep Crimson, and Dark Leather cards).
  - **State Hoisting:** Reactive UI driven by Kotlin `StateFlow` and `collectAsState()`.
  - **Localization:** Runtime dynamic locale updates (`LocaleHelper`) enabling smooth English/Thai toggling without requiring app restarts.

- **Data & Repository Layer:**
  - **Room Database (`AppDatabase`):** Local SQLite persistence for campaigns, session logs, character personages, and user settings with destructive migration handling.
  - **Retrofit & OkHttp:** REST client fetching D&D 5e SRD bestiary data from `https://www.dnd5eapi.co/api/`.
  - **Google Generative AI SDK (Gemini):**
    - **Multi-Model Fallback Chain:** Automatically retries and cascades through fallback models (`gemini-3.8-flash` → `gemini-2.0-flash-lite` → `gemini-2.0-flash` → `gemini-1.5-flash` → `gemini-1.5-flash-8b`) when server load spikes or 503 errors occur.
    - **Graceful Error Recovery:** Custom exception handling intercepting rate limits, 503 high-demand responses, and SDK serialization errors to inform the user cleanly.

- **Dependency Management:**
  - Singleton Service Locator pattern encapsulated inside `ArbiterApplication`.


## 📦 Tech Stack & Libraries

| Category | Technology / Library |
| :--- | :--- |
| **Language** | Kotlin 2.2.10 |
| **UI Framework** | Jetpack Compose (Compose BOM 2026.02.01) |
| **Design System** | Material 3 with Custom Grimoire Dark Theme |
| **Navigation** | Navigation Compose |
| **Local Database** | Room 2.7.2 (KSP) |
| **Networking** | Retrofit 2.11.0, OkHttp 4.12.0, Gson 2.11.0 |
| **AI Integration** | Google Generative AI SDK (`com.google.ai.client.generativeai:0.9.0`) |
| **Asynchronous** | Kotlin Coroutines 1.9.0 & StateFlow |

---

## 📜 License
Developed for Dungeon Masters seeking strategic depth, game theory balance, and player agency protection in their tabletop sagas.
