# Market Analysis — ElectroDZ Chantier (Algeria)

## Problem
Independent electricians and small BTP electrical lots in Algeria do cable sizing
(voltage drop, section, breaker) with mental math, paper tables, or foreign apps
(NEC/AWG-centric, English-only, subscription paywalled). On chantier, network is
often absent. Paperwork (intervention fiches) is on cahier, easily lost.

## Target customers (verified existence, assumption: willingness to pay TBD by field test)
- Independent electricians (électriciens bâtiment/industriel), all 58 wilayas.
- Small contractors doing lots électricité in BTP projects.
- Maintenance technicians (usines, hôtels, cliniques).
- CFPA vocational training centres (students need IEC metric tooling in French/Arabic).

## Demand evidence (verified, fetched 2026-10-10)
- MouqawilPro (mouqawilpro.com): invoicing SaaS for Algerian auto-entrepreneurs —
  proves artisans pay for FR/AR admin tools, but it is billing, not field engineering.
- Dolisoft BTP Manager DZ / BTP Manager Pro 2026, Dinateck BTP Manager Pro 2026,
  Almawarid, Rochemère: all target BTP gestion/facturation, several 4 500 DA/month+.
  Conclusion: **facturation/BTP-ERP is saturated** — excluded from MVP scope.
- ElectroCalc (electrocalc.arabvolt.com, 15 languages, offline, freemium), Ampero
  (IEC 60364, iOS-first, Pro paywall), Voltix (NEC/BS/AS-NZS, Pro subscription),
  Voltage Drop Calc NEC (SAR 4.99, NEC-only): prove global demand for offline
  electrical calculators, but none is Algeria-first (Sonelgaz 220/380V, mm², FR/AR RTL,
  intervention paperwork). ASSUMPTION: Algerian electricians prefer a free offline
  FR/AR tool over English NEC apps — to validate with first 10 users.

## Offline requirement: YES — chantiers often without network. 100% on-device (Room).
## Hardware/APIs: none exotic. No camera/GPS/OBD in MVP. Min SDK 26.
## Costs: zero operational cost (no backend). GitHub Actions free tier for builds.
## Legal/regulatory: calculation results are indicative; app displays disclaimer to
verify against norme C 15-100 / IEC 60364 and local Sonelgaz rules before installation.
No personal data leaves the device. No DGI/fiscal logic in MVP (avoids facturation law risk).
