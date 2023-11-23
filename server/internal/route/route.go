package route

import (
	"aquaGraph/internal/controller"

	"github.com/Rosto4eks/grapes"
)

func Init(router *grapes.Router, c *controller.Controller) {
	router.Get("/quality", c.GetQuality)
	router.Get("/qualities", c.GetQualities)
	router.Get("/marks", c.GetMarks)
	router.Get("/new-mark", c.NewMark)
	router.Get("/delete-mark", c.DeleteMark)
    router.Get("/works", c.GetWorks)
}
