package usecase

import (
	"aquaGraph/models"
	"encoding/json"
	"fmt"
	"os"
	"time"
)

func (u *Usecase) GetPolygons() (Polygons, error) {
	file, err := os.ReadFile("data/polygons.json")
	if err != nil {
		return Polygons{}, err
	}
	var polygons Polygons
	json.Unmarshal(file, &polygons)
	return polygons, nil
}

func (u *Usecase) GetQuality(id string) (models.QualityJson, error) {
    data, err := u.repository.GetQuality()
    if err != nil {
        return models.QualityJson{}, err
    }
    datamap := make(map[string]interface{})
    err = json.Unmarshal([]byte(data.Data), &datamap)
    if err != nil {
        return models.QualityJson{}, err
    }
    return models.QualityJson{
        Time: data.Time,
        Data: datamap[id],
    }, nil
}


func (u *Usecase) GetQualities(id string) ([]models.QualityJson, error) {
    data, err := u.repository.GetQualities()
    if err != nil {
        return nil, err
    }
    arr := make([]models.QualityJson, 0)
    for _, elem := range data {
        datamap := make(map[string]interface{})
        err = json.Unmarshal([]byte(elem.Data), &datamap)
        if err != nil {
            fmt.Println(err.Error())
            continue
        }
        arr = append(arr, models.QualityJson{
            Time: elem.Time, 
            Data: datamap[id],
        })
    }
    return arr, nil
}


func (u *Usecase) GetMarks() ([]models.Mark, error) {
    return u.repository.GetMarks()
}


func (u *Usecase) NewMark(m models.Mark) error {
    m.Time = time.Now().Format("2006-01-02")
    return u.repository.NewMark(m)
}
