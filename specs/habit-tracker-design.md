# Habit Tracker App — Requirements

## Purpose

A native Android habit tracker app built as the project for a YouTube coding course demonstrating Claude Code-assisted development. The app is the vehicle — Claude Code is the star. Scope: 3 screens, dark theme, purely local data.

## What Is a Habit

A habit is a recurring activity the user wants to track daily. Each habit has:
- A **name** (max 50 characters, e.g., "Morning Run", "Read 30min")
- An **icon** chosen from a predefined set of 20 icons
- A **frequency** — which days of the week it should be performed (e.g., weekdays only, every day, specific days)
- A **creation date** — streaks only count from this date forward

A habit is either **done** or **not done** on any given day. It can only be completed once per day. Duplicate habit names are allowed.

### Streaks

- **Current streak**: the number of consecutive scheduled days the habit has been completed, counting backward from today (or yesterday if today isn't completed yet). Only days the habit is scheduled for count.
- **Best streak**: the longest consecutive completion streak ever recorded for a habit.

### Completion

Completing a habit for today creates a record. Uncompleting removes it. A habit can only have one completion per day.

## Screens

### 1. Today Screen (Main)

The app's home screen. Shows all habits scheduled for the current day.

**Layout:**
- Top bar: date (e.g., "Monday, April 6") and screen title "Today" left-aligned. Stats icon button (bar chart) in the top-right corner, navigates to Stats screen.
- Daily progress section: label "Daily progress" with a fraction (e.g., "5 / 7"), and a horizontal progress bar below.
- Scrollable habit list: vertical list of habit cards.
- FAB: bottom-right corner, solid `#6C63FF`, rounded-square (16dp radius), "+" icon. Navigates to Create screen.

**Habit card:**
- Left: 42x42dp icon container (rounded square, `#252540` background) with the habit's icon in `#A855F7`.
- Center (left-aligned text): habit name (Inter 600, 14sp, white). Below: streak info — star icon + "X day streak" in `#F59E0B`, or "No streak yet" in `#666680` if no streak. Text is left-aligned and consistent whether a streak exists or not.
- Right: circular checkbox (30dp). Unchecked = hollow circle with `#333350` border. Checked = filled `#34D399` with white checkmark.
- Card background: `#1A1A2E`, 14dp corner radius.

**Interactions:**
- Tap the checkbox circle: toggles habit completion for today.
- Tap anywhere else on the card: navigates to Edit screen for that habit.
- Tap FAB: navigates to Create screen.
- Tap stats icon: navigates to Stats screen.

**Empty state:**
- When no habits exist, show vertically centered text: "No habits yet" (Inter 600, 16sp, `#B0B0C0`) with "Tap + to add your first habit" below (Inter 400, 13sp, `#666680`). No icons or illustrations. FAB remains visible at bottom-right.

### 2. Stats Screen

Shows all-time statistics. No time filtering.

**Layout:**
- Top bar: back arrow button (navigates to Today) + title "Statistics", left-aligned.
- Three summary cards in a row, each left-aligned internally:
  - Label on top, large number below.
  - "This Week" completion percentage in `#A855F7`
  - "Best Streak" count in `#34D399`
  - "Active" habits count in `#F472B6`
  - Numbers use Manrope 800, 26sp. Labels use Inter 500, 10sp, `#666680`.
- Activity heatmap section:
  - Title: "Activity", left-aligned.
  - Container: `#1A1A2E` card, 14dp radius.
  - Grid: 7 columns (M-S) x 4 rows (W1-W4), showing the last 4 weeks.
  - Each cell is a fixed 32x32dp square with 4dp gap, 6dp corner radius.
  - Color intensity maps to completion ratio for that day (0% = `#252540`, 25% = `#6C63FF` at 0.3 opacity, 50% = 0.6, 75% = 0.85, 100% = 1.0).
  - Today's cell has a `#A855F7` border to indicate current day.
  - Future cells use dashed `#333350` border.
  - Legend row at bottom-right: "Less" [5 color squares] "More".
  - The grid has a max-width constraint so cells remain square on wide screens.
- Streaks list section:
  - Title: "Streaks", left-aligned.
  - List of all habits showing: icon (36dp), name (left-aligned), current streak number in `#34D399`, "Best: X" label below.

### 3. Create / Edit Habit Screen

Shared screen with two modes. All labels and text content are left-aligned.

**Layout:**
- Top bar: back arrow + title ("New Habit" for create, "Edit Habit" for edit), left-aligned.
- Icon display: 72x72dp rounded square with selected icon, left-aligned alongside "Tap to change icon" label. Dashed `#6C63FF` border in create mode, solid border in edit mode.
- Icon picker grid: 5-column grid showing all 20 available icons. Selected icon has `#6C63FF` background with white icon stroke. Unselected icons have `#252540` background with `#A855F7` stroke. Label "CHOOSE ICON" above, left-aligned.
- Name text field: label "NAME" (left-aligned, uppercase caption), `#1A1A2E` background, `#333350` border, 12dp radius. Text content left-aligned.
- Day-of-week picker: label "REPEAT ON" (left-aligned, uppercase caption). Seven day buttons (M, T, W, T, F, S, S).
  - Selected: `#2E2E4A` background + `#6C63FF` 1.5dp border, text in `#A855F7`.
  - Unselected: `#1A1A2E` background + `#333350` 1dp border, text in `#666680`.
  - Intentionally different color from the CTA to maintain visual hierarchy.
- Save button (CTA): full-width, `#6C63FF` solid background, white centered text, 14dp radius.
  - Create mode: "Save Habit"
  - Edit mode: "Save Changes"
- Secondary action (below CTA, centered):
  - Create mode: "Discard Habit" in `#EF4444` — navigates back without saving.
  - Edit mode: "Delete Habit" in `#EF4444` — shows confirmation dialog, then deletes the habit and all its completion records.

**Validation:**
- Name cannot be empty.
- At least one day of the week must be selected.

## Navigation

- **Today -> Stats**: tap stats icon in top-right of Today screen.
- **Stats -> Today**: tap back arrow.
- **Today -> Create**: tap FAB.
- **Today -> Edit**: tap a habit card (not the checkbox).
- **Create/Edit -> Today**: tap back arrow, save, discard, or delete.

No bottom navigation bar (only 2 main destinations — bottom nav requires 3+ on Android).

Standard Compose Navigation transitions. No custom animations.

## Design System

### Colors

**Backgrounds:**
| Token | Hex | Usage |
|-------|-----|-------|
| Background | #0F0F15 | Screen background |
| Surface | #1A1A2E | Cards, containers |
| Surface Elevated | #252540 | Icon containers, nested elements |
| Surface Bright | #2E2E4A | Selected day picker states |

**Brand:**
| Token | Hex | Usage |
|-------|-----|-------|
| Primary | #6C63FF | FAB, CTA buttons, selected states, heatmap |
| Secondary | #A855F7 | Icons, accent text, stat numbers |
| Primary Light | #8B7BFF | Selected icon border highlight |
| Primary Dark | #4A42CC | Pressed state for primary buttons |

**Semantic:**
| Token | Hex | Usage |
|-------|-----|-------|
| Success | #34D399 | Completed checkbox, streak numbers |
| Streak | #F59E0B | Streak text and star icon |
| Accent | #F472B6 | Active habits count stat |
| Destructive | #EF4444 | Delete/discard text |

**Text:**
| Token | Hex | Usage |
|-------|-----|-------|
| Text Primary | #FFFFFF | Headings, habit names |
| Text Secondary | #B0B0C0 | Body text, descriptions |
| Text Tertiary | #666680 | Labels, captions, disabled text |
| Border | #333350 | Card borders, dividers |

### Typography

| Style | Font | Weight | Size |
|-------|------|--------|------|
| Display | Manrope | 800 | 26sp |
| Heading | Manrope | 800 | 22sp |
| Title | Inter | 600 | 16sp |
| Body | Inter | 500 | 14sp |
| Caption | Inter | 500 | 11sp, uppercase, 1px letter-spacing |
| Stat Number | Manrope | 800 | 26sp |
| Button | Inter | 700 | 16sp |

Both Manrope and Inter are Google Fonts.

### Habit Icons (20 total)

Each icon is rendered at 22dp inside cards, 28dp inside the icon picker.

| ID | Name | Visual |
|----|------|--------|
| run | Run | Mountain/heartbeat line |
| read | Read | Open book with lines |
| water | Water | Water droplet |
| meditate | Meditate | Concentric circles |
| sleep | Sleep | Clock with bed line |
| code | Code | Laptop screen |
| music | Music | Musical notes |
| cook | Cook | House/kitchen |
| journal | Journal | Notebook with lines |
| gym | Gym | Dumbbell/weights |
| yoga | Yoga | Person silhouette |
| walk | Walk | Mountain/triangle |
| cycle | Cycle | Wheel with handlebars |
| study | Study | Calendar grid |
| no_phone | No Phone | Locked device |
| vitamins | Vitamins | Sun/pill |
| language | Language | Globe with meridians |
| gratitude | Gratitude | Star outline |
| health | Health | Shield |
| organize | Organize | Four-square grid |

## Edge Cases

- **Habit created mid-week**: streaks only count from creation date forward. Days before creation don't count as "missed".
- **All days deselected**: validation prevents saving. Show error on the day picker.
- **Empty name**: validation prevents saving. Show error on the text field.
- **Duplicate names**: allowed.
- **Delete confirmation**: show a simple dialog before deleting.
- **Timezone**: use device local date for all date logic.
- **Stats with no data**: show 0% / 0 / 0 in summary cards, empty heatmap.
