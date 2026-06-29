# Habit Tracker — Claude Guide

## Specs & Design

Read requirements and designs lazily — only when the task needs them.

- **Requirements**: `specs/habit-tracker-design.md`
- **UI mockups**: `specs/Habit Tracker — Design System & Screens.html` ← open in a browser for visual reference; use this whenever generating or reviewing UI

## Fonts

Variable fonts live in `specs/Fonts/`. Copy to `app/src/main/res/font/` when wiring up the type system.

- **Inter** (body text): `specs/Fonts/Inter/Inter-VariableFont_opsz,wght.ttf`
- **Manrope** (headings): `specs/Fonts/Manrope/Manrope-VariableFont_wght.ttf`

## Tech Stack

| Layer | Choice |
|---|---|
| UI | Jetpack Compose |
| Architecture | MVI + single `:app` module, feature/core package split |
| Persistence | Room |
| DI | Koin |

## Skills — When to Use

Invoke the matching skill **before** writing code in that area.

| Concern | Skill |
|---|---|
| Package & module layout | `/android-module-structure` |
| Compose UI & composables | `/android-compose-ui` |
| MVI, ViewModel, presentation layer | `/android-presentation-mvi` |
| Compose Navigation | `/android-navigation` |
| Data layer (Room, repos, mappers) | `/android-data-layer` |
| Dependency injection (Koin) | `/android-di-koin` |
| Error handling & Result types | `/android-error-handling` |
| Testing | `/android-testing` |
