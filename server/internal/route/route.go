package route

import (
	"aquaGraph/internal/controller"

	"github.com/Rosto4eks/grapes"
)

func Init(router *grapes.Router, c *controller.Controller) {
	router.Get("/quality", c.GetQuality)
	router.Get("/qualities", c.GetQualities)
}
