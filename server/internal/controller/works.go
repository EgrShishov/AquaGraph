package controller

import (
	"github.com/Rosto4eks/grapes"
)

func (c *Controller) GetWorks(ctx grapes.Context) {
    data, err := c.usecase.GetDataWorks()
    if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
    
    }
    ctx.SendJson(grapes.Obj{"Works": data})
}
