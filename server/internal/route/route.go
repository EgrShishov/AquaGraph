package route

import (
	"aquaGraph/internal/controller"

	"github.com/Rosto4eks/grapes"
)

func Init(router *grapes.Router, c *controller.Controller) {
	router.Get("/", c.Get)
}
