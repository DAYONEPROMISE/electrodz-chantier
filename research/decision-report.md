# Decision Report

## Scoring (max 100)
ElectroDZ Chantier — offline electrician toolkit (cable sizing + voltage drop +
intervention fiches + client register + PDF, FR/AR, 100% offline):
- Problem severity/frequency: 16/20 (daily sizing + paperwork pain; verified via
  competitor demand, assumption on DZ frequency — field validation needed)
- Algerian demand evidence: 11/15 (adjacent evidence: artisans pay for FR/AR tools;
  direct demand assumed, must validate with 10 users)
- Identifiable paying customer: 11/15 (independent electricians via quincailleries,
  WhatsApp groups, CFPA; monetisation later: one-time Pro PDF pack — NOT in MVP)
- MVP feasibility: 14/15 (pure local app, no backend, standard formulas)
- Technical reuse: 6/10 (clean-room; reuse = AndroidX/Room/Compose only)
- Differentiation: 8/10 (only DZ-first offline FR/AR field tool; calculators exist)
- First 10 customers: 8/10 (reachable: 2 quincailleries + 1 CFPA + WhatsApp groups)
- Licence/legal: 5/5 (clean-room, no copied code)
TOTAL: 79/100. Runner-up (generic devis/facturation app) rejected: saturated market.

## Go decision: BUILD ElectroDZ Chantier MVP as specified in docs/SPEC.md.
## Explicit non-goals: invoicing/TVA, cloud sync, AI, auth, subscriptions.
