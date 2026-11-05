const BASE_URL = process.env.BRAWL_API_BASE_URL || "https://api.brawlstars.com/v1";
function normalizeTag(tag) { return encodeURIComponent(tag.startsWith("#") ? tag : "#" + tag); }
export async function getBattleLog(playerTag) {
  const token = process.env.BRAWL_API_TOKEN;
  if (!token) throw new Error("BRAWL_API_TOKEN is not configured");
  const response = await fetch(BASE_URL + "/players/" + normalizeTag(playerTag) + "/battlelog", { headers: { Authorization: "Bearer " + token, Accept: "application/json" } });
  if (!response.ok) throw new Error("Battlelog request failed for " + playerTag + ": HTTP " + response.status);
  return response.json();
}