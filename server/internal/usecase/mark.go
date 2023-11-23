package usecase

import (
	"aquaGraph/models"
	"time"
)


func (u *Usecase) GetMarks() ([]models.Mark, error) {
    return u.repository.GetMarks()
}


func (u *Usecase) NewMark(m models.Mark) error {
    m.Time = time.Now().Format("2006-01-02")
    return u.repository.NewMark(m)
}

func (u *Usecase) DeleteMark(id int) error {
    return u.repository.DeleteMark(id)
}
