package repository

import (
	"database/sql"

	"github.com/Rosto4eks/loggify"
	_ "github.com/mattn/go-sqlite3"
)

type Repository struct {
    db *sql.DB
}

func New(db *sql.DB) *Repository {
	return &Repository{db}
}

func Connect(dbpath string) *sql.DB {
    loggify.INFO("Connecting to database: " + dbpath + "...")
    db, err := sql.Open("sqlite3", dbpath)
    if err != nil {
        loggify.ERROR(err.Error())
        panic(err)
    }
    loggify.INFO("Successfully connected to database")
    return db
}

func (r *Repository) Close() {
    r.db.Close()
}


