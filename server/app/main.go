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
	repository := repository.New(repository.Connect("data/data.db"))
    defer repository.Close()
	usecase := usecase.New(repository)
	controller := controller.New(usecase)
	route.Init(router, controller)
    go usecase.UpdateWorks()
	router.Run(1337)
}
