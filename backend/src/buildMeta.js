import { aggregateMeta } from "./aggregateMeta.js";
import { normalizeBattle } from "./normalizeBattle.js";

/**
 * Construye el documento que consumirá la APK.
 * Puede ejecutarse por cron/GitHub Actions cuando exista una fuente de battle logs.
 */
export function buildMeta(rawBattleLogs, now = Date.now()) {
  const normalized = rawBattleLogs
    .flatMap(log => Array.isArray(log?.items) ? log.items : [])
    .map(normalizeBattle)
    .filter(Boolean);
  const result = aggregateMeta(normalized, now);
  return {
    ...result,
    methodology: "Aggregated from normalized recent battle logs",
    totalMatches: normalized.length
  };
}