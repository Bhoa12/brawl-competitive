/**
 * Normaliza una batalla de la API oficial a un formato estable para el agregador.
 * Solo conserva datos necesarios para estadísticas; las credenciales nunca llegan aquí.
 */
export function normalizeBattle(item) {
  const battle = item?.battle;
  const event = item?.event;
  if (!battle || !event || !Array.isArray(battle.teams)) return null;
  const winner = battle.result === "victory" ? 0 : battle.result === "defeat" ? 1 : null;
  const players = [];
  battle.teams.forEach((team, teamIndex) => {
    if (!Array.isArray(team)) return;
    team.forEach(member => {
      const brawler = member?.brawler;
      if (!brawler?.id || !brawler?.name) return;
      players.push({
        brawlerId: brawler.id,
        brawlerName: brawler.name,
        won: winner === null ? false : teamIndex === winner
      });
    });
  });
  if (!players.length) return null;
  return {
    battleTime: item.battleTime || null,
    mode: event.mode || "Unknown",
    mapName: event.map || "Unknown",
    players
  };
}