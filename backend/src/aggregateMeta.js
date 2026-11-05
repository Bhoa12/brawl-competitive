/*
 * Agregador deliberadamente independiente del proveedor.
 * Entrada: partidas normalizadas en JSON.
 * Salida: meta.json con muestras y tasas verificables.
 */
export function aggregateMeta(matches, now = Date.now()) {
  const buckets = new Map();
  for (const match of matches) {
    if (!match || !match.mapName || !Array.isArray(match.players)) continue;
    for (const player of match.players) {
      if (!player.brawlerId || !player.brawlerName) continue;
      const key = [match.mode || "Unknown", match.mapName, player.brawlerId].join("|");
      const current = buckets.get(key) || {
        brawlerId: player.brawlerId, brawlerName: player.brawlerName,
        mapName: match.mapName, mode: match.mode || "Unknown",
        picks: 0, wins: 0
      };
      current.picks += 1;
      if (player.won === true) current.wins += 1;
      buckets.set(key, current);
    }
  }
  const totals = new Map();
  for (const value of buckets.values()) {
    const key = [value.mode, value.mapName].join("|");
    totals.set(key, (totals.get(key) || 0) + value.picks);
  }
  return {
    version: 1,
    generatedAt: new Date(now).toISOString(),
    entries: [...buckets.values()].map(value => ({
      brawlerId: value.brawlerId,
      brawlerName: value.brawlerName,
      mapName: value.mapName,
      mode: value.mode,
      rank: "General",
      pickRate: totals.get([value.mode, value.mapName].join("|"))
        ? value.picks / totals.get([value.mode, value.mapName].join("|")) * 100 : 0,
      winRate: value.picks ? value.wins / value.picks * 100 : 0,
      sampleSize: value.picks,
      updatedAt: now
    }))
  };
}