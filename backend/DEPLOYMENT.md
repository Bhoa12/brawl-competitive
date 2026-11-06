# Competitive data pipeline deployment

## Required server environment

Configure these variables in the backend service. Do not commit the API token.

- `BRAWL_STARS_API_TOKEN`: official Brawl Stars API token.
- `COMPETITIVE_PLAYER_TAGS`: comma-separated player tags used as the server-managed sample.
- `DATABASE_URL`: persistent PostgreSQL connection string for production.

## Automatic sync

The backend exposes `POST /ingestion/sync/configured`. A scheduler can call this endpoint periodically after the server environment is configured. The mobile app never needs the official API token or player tags.

## Data integrity

The pipeline rejects samples without brawler data, avoids duplicate samples, and only attributes wins when the queried player's team can be identified. It deliberately avoids guessing missing results.

## Verification

After a successful sync, check `GET /analytics/summary`. A healthy populated pipeline should eventually show `sample_size` above zero and non-empty `rows`.
