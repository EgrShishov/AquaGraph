package repository

import (
	"aquaGraph/models"
	"fmt"
	_ "github.com/mattn/go-sqlite3"
)

func (r* Repository) NewMark(m models.Mark) error {
    _, err := r.db.Exec("INSERT INTO mark (data, time, x, y) VALUES ($1, $2, $3, $4)", m.Data, m.Time, m.X, m.Y)
    return err
}

func (r* Repository) DeleteMark(id int) error {
    _, err := r.db.Exec("DELETE FROM mark WHERE id=$1", id)
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
