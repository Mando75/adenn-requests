# Adenn Requests

Repo for managing requests for my Plex server. Inspired by Overseerr and my own previous request management project.

Required environment values:

```
POSTGRES_USER
POSTGRES_PASSWORD
POSTGRES_URL

MOVIE_DB_API_KEY
MOVIE_DB_REQUEST_TOKEN
MOVIE_DB_SESSION_TOKEN

// Must be 16 byte hex string
SESSION_SECRET_ENCRYPT_KEY 
SESSION_SIGN_KEY
JWT_TOKEN_SECRET
JWT_REALM
JWT_ISSUER
JWT_AUDIENCE
JWT_TOKEN_LIFETIME

PLEX_MACHINE_ID
```
