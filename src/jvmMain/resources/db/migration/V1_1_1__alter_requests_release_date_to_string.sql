ALTER TABLE "requests"
    ALTER COLUMN "release_date" TYPE TEXT USING "release_date"::TEXT;