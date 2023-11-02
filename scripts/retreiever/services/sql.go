package services

import (
	"database/sql"
    _ "github.com/mattn/go-sqlite3"
)

func Insert(date string, data string) error {
    db, err := sql.Open("sqlite3", "../../server/data/data.db")
    if err != nil {
        return err
    }
    defer db.Close()

    _, err = db.Exec("INSERT INTO quality (date, json) VALUES ($1, $2)", date, data)
    return err
}
