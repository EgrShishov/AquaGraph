package repository

import (
	"aquaGraph/models"
	"database/sql"
	"fmt"

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

func (r* Repository) Get() (models.Quality, error) {
    var quality models.Quality
    err := r.db.QueryRow("SELECT * FROM quality ORDER BY id DESC LIMIT 1").Scan(&quality.Id, &quality.Time, &quality.Data)
    if err != nil {
        return models.Quality{}, err
    }
    return quality, nil
}

func (r* Repository) GetAll() ([]models.Quality, error) {
    var quality []models.Quality
    rows, err := r.db.Query("SELECT * FROM quality ORDER BY id")
    if err != nil {
        return nil, err
    } 
    
    var q models.Quality
    for rows.Next() {
        err = rows.Scan(&q.Id, &q.Time, &q.Data)
        if err != nil {
            fmt.Println(err.Error())
            continue
        }
        quality = append(quality, q)
    }
    return quality, nil
}
