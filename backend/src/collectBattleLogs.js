import fs from "node:fs";
import path from "node:path";
import { getBattleLog } from "./supercellClient.js";
const targetsFile = process.env.BRAWL_TARGETS_FILE || "data/targets.json";
const output = process.env.BRAWL_MATCHES_FILE || "data/battlelogs.json";
const targets = fs.existsSync(targetsFile) ? JSON.parse(fs.readFileSync(targetsFile, "utf8")) : [];
if (!Array.isArray(targets) || targets.length === 0) { console.log("No player targets configured; nothing collected."); process.exit(0); }
const logs = [];
for (const tag of [...new Set(targets)]) {
  try {
    const battleLog = await getBattleLog(tag);
    logs.push({ playerTag: tag, collectedAt: new Date().toISOString(), items: battleLog.items || [] });
    console.log("Collected", (battleLog.items || []).length, "battles for", tag);
  } catch (error) { console.error("Skipping", tag + ":", error.message); }
}
fs.mkdirSync(path.dirname(output), { recursive: true });
fs.writeFileSync(output, JSON.stringify(logs, null, 2));
console.log("Saved", logs.length, "battle logs.");