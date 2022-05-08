# Adenn Requests

Repo for managing requests for my Plex server. Inspired by Overseerr and my own previous request management project.

Required environment values:

```
POSTGRES_USER
POSTGRES_PASSWORD
POSTGRES_DATABASE
POSTGRES_HOST
POSTGRES_PORT

MOVIE_DB_API_KEY
MOVIE_DB_REQUEST_TOKEN
MOVIE_DB_SESSION_TOKEN

// Must be 16 byte hex string
SESSION_SECRET_ENCRYPT_KEY 
SESSION_SIGN_KEY
```