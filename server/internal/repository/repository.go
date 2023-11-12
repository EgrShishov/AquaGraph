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

func (r* Repository) GetQuality() (models.Quality, error) {
    var quality models.Quality
    err := r.db.QueryRow("SELECT * FROM quality ORDER BY id DESC LIMIT 1").Scan(&quality.Id, &quality.Time, &quality.Data)
    if err != nil {
        return models.Quality{}, err
    }
    return quality, nil
}

func (r* Repository) GetQualities() ([]models.Quality, error) {
    var quality []models.Quality
    rows, err := r.db.Query("SELECT * FROM quality ORDER BY id LIMIT 12")
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

func (r* Repository) NewMark(m models.Mark) error {
    _, err := r.db.Exec("INSERT INTO mark (data, time, x, y) VALUES ($1, $2, $3, $4)", m.Data, m.Time, m.X, m.Y)
    return err
}

func (r* Repository) GetMarks() ([]models.Mark, error) {
    var marks []models.Mark
    rows, err := r.db.Query("SELECT * FROM mark ORDER BY id")
    if err != nil {
        return nil, err
    } 
    
    var m models.Mark
    for rows.Next() {
        err = rows.Scan(&m.Id, &m.Data, &m.Time, &m.X, &m.Y)
        if err != nil {
            fmt.Println(err.Error())
            continue
        }
        marks = append(marks, m)
    }
    return marks, nil
}
