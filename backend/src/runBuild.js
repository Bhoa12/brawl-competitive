import fs from "node:fs";
import path from "node:path";
import { buildMeta } from "./buildMeta.js";

const input = process.env.BRAWL_MATCHES_FILE || "data/battlelogs.json";
const output = process.env.BRAWL_META_OUTPUT || "public/meta.json";
const logs = fs.existsSync(input) ? JSON.parse(fs.readFileSync(input, "utf8")) : [];
const meta = buildMeta(logs);
fs.mkdirSync(path.dirname(output), { recursive: true });
fs.writeFileSync(output, JSON.stringify(meta, null, 2));
console.log("Generated", output, "from", meta.totalMatches, "normalized matches.");
