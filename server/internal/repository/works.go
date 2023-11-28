package repository

import (
	"aquaGraph/models"
	"encoding/json"
)

func (r *Repository) SaveWorks(works models.Works) error {
    bytes, err := json.Marshal(works)
    if err != nil {
        return err
    }
    _, err = r.db.Exec("Update works set value = $1", string(bytes))
    return err
}

func (r *Repository) GetWorks() (models.Works, error) {
    var works string
    err := r.db.QueryRow("SELECT * FROM works").Scan(&works)
    if err != nil {
        return nil, err
    }
    var wrks models.Works
    err = json.Unmarshal([]byte(works), &wrks)
    return wrks, err
}
