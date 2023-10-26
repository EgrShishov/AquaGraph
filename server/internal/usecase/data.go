package usecase

import (
	"encoding/json"
	"os"
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
