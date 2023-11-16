package controller

import (
	"aquaGraph/internal/usecase"
)

type Controller struct {
	usecase *usecase.Usecase
}

func New(usecase *usecase.Usecase) *Controller {
	return &Controller{
		usecase,
	}
}


