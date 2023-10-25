package main

import (
	"aquaGraph/internal/controller"
	"aquaGraph/internal/repository"
	"aquaGraph/internal/route"
	"aquaGraph/internal/usecase"

	"github.com/Rosto4eks/grapes"
)

func main() {
	router := grapes.NewRouter()
	repository := repository.New()
	usecase := usecase.New(repository)
	controller := controller.New(usecase)
	route.Init(router, controller)

	router.Run(1337)
}
