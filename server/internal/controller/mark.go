package controller

import (
	"aquaGraph/models"

	"github.com/Rosto4eks/grapes"
)

func (c *Controller) GetMarks(ctx grapes.Context) {
    data, err := c.usecase.GetMarks()
    if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
    }
    ctx.SendJson(grapes.Obj{"Marks": data})
}


func (c *Controller) NewMark(ctx grapes.Context) {
	x := ctx.GetQueryParam("x")
	y := ctx.GetQueryParam("y")
	data := ctx.GetQueryParam("data")
    m := models.Mark{
        Data: data,
        X: x,
        Y: y,
    }
    err :=  c.usecase.NewMark(m)
    if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
    } else {
        ctx.SendJson(grapes.Obj{"error": nil})
    }

}
