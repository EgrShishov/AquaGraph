package repository

import (
	"aquaGraph/models"
	"encoding/json"

	"github.com/Rosto4eks/loggify"
)

func (r *Repository) SaveWorks(works models.Works) error {
    bytes, err := json.Marshal(works)
    if err != nil {
        loggify.ERROR(err.Error())
        return err
    }
    _, err = r.db.Exec("Update works set value = $1", string(bytes))
    if err != nil {
        loggify.ERROR(err.Error())
    }
    return err
}

func (r *Repository) GetWorks() (models.Works, error) {
    var works string
    err := r.db.QueryRow("SELECT * FROM works").Scan(&works)
    if err != nil {
        loggify.ERROR(err.Error())
        return nil, err
    }
    var wrks models.Works
    err = json.Unmarshal([]byte(works), &wrks)
    if err != nil {
        loggify.ERROR(err.Error())
    }
    return wrks, err
}
