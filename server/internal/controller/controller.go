package controller

import (
	"aquaGraph/internal/usecase"

	"github.com/Rosto4eks/grapes"
)

type Controller struct {
	usecase *usecase.Usecase
}

func New(usecase *usecase.Usecase) *Controller {
	return &Controller{
		usecase,
	}
}

func (c *Controller) Get(context grapes.Context) {
	context.SendString("Work in progress...")
}
