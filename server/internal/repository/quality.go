package repository

import (
	"aquaGraph/models"

	"github.com/Rosto4eks/loggify"
	_ "github.com/mattn/go-sqlite3"
)


func (r* Repository) GetQuality() (models.Quality, error) {
    var quality models.Quality
    err := r.db.QueryRow("SELECT * FROM quality ORDER BY id DESC LIMIT 1").Scan(&quality.Id, &quality.Time, &quality.Data)
    if err != nil {
        loggify.ERROR(err.Error())
        return models.Quality{}, err
    }
    return quality, nil
}

func (r* Repository) GetQualities() ([]models.Quality, error) {
    var quality []models.Quality
    rows, err := r.db.Query("SELECT * FROM quality ORDER BY id LIMIT 12")
    if err != nil {
        loggify.ERROR(err.Error())
        return nil, err
    } 
    
    var q models.Quality
    for rows.Next() {
        err = rows.Scan(&q.Id, &q.Time, &q.Data)
        if err != nil {
            loggify.ERROR(err.Error())
            continue
        }
        quality = append(quality, q)
    }
    return quality, nil
}
