package main

import (
	"aquaGraph/internal/controller"
	"aquaGraph/internal/repository"
	"aquaGraph/internal/route"
	"aquaGraph/internal/usecase"

	"github.com/Rosto4eks/grapes"
	// "github.com/Rosto4eks/loggify"
)

func main() {
    // loggify.SetLevel(loggify.LEVEL_ERROR)
	router := grapes.NewRouter()

	repository := repository.New(repository.Connect("data/data.db"))
    defer repository.Close()

	usecase := usecase.New(repository)
    go usecase.UpdateWorks()

	controller := controller.New(usecase)

	route.Init(router, controller)

	router.Run(1337)
}
