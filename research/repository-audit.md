# Repository Audit

Searched GitHub (api, 2026-10-10) for reusable licence-clean source:

- `imvvk11/Basic-Electrical-Calculator-using-python3` (Python CLI, ★6) — too trivial, no Android.
- `sharofin45-netizen/Circuit-Calculator` (JS, ★47) — Ohm's law toy, web-only.
- `spurnik/Kirchhoff` (notebook, ★7), `DurianTheWAV/OhmCalc-cli` (Python, ★3) — CLI only.
- `probizi/awesome-engineering-calculators` (curated list, updated 2026-09) — useful
  reference list only, no reusable Android code.

Queries "offline construction measurement calculator android kotlin" and
"electrician wire sizing calculator offline open source" returned no directly
reusable Android (Kotlin/Compose) codebase with a compatible licence.

DECISION: independent clean-room implementation in Kotlin/Compose. No copied code,
no licence risk. Formulas used are standard physics (IEC 60364 / C 15-100 method):
I = P/(U·cosφ) mono, I = P/(√3·U·cosφ) tri; ΔU computed with ρ Cu 0.023 / Al 0.037
Ω·mm²/m; standard mm² series; breaker ratings series. Factual method, not expression.
