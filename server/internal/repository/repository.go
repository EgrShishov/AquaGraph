package repository

import (
	"database/sql"

	_ "github.com/mattn/go-sqlite3"
)

type Repository struct {
    db *sql.DB
}

func New(db *sql.DB) *Repository {
	return &Repository{db}
}

func Connect(dbpath string) *sql.DB {
    db, err := sql.Open("sqlite3", dbpath)
    if err != nil {
        panic(err)
    }
    return db
}

func (r *Repository) Close() {
    r.db.Close()
}


