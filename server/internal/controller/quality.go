package controller

import (
	"aquaGraph/internal/usecase"
	"strconv"

	"github.com/Rosto4eks/grapes"
)

func (c *Controller) GetQuality(ctx grapes.Context) {
	x, err := strconv.ParseFloat(ctx.GetQueryParam("x"), 64)
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
        return
	}
	y, err := strconv.ParseFloat(ctx.GetQueryParam("y"), 64)
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
        return
	}
	id, err := c.usecase.Locate(usecase.Point{x, y})
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
        return
	}
    data, err := c.usecase.GetQuality(id)
    if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
        return
    }
    ctx.SendJson(grapes.Obj{"Quality": data})
}


func (c *Controller) GetQualities(ctx grapes.Context) {
	x, err := strconv.ParseFloat(ctx.GetQueryParam("x"), 64)
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
	}
	y, err := strconv.ParseFloat(ctx.GetQueryParam("y"), 64)
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
	}
	id, err := c.usecase.Locate(usecase.Point{x, y})
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
	}
    data, err := c.usecase.GetQualities(id)
    if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
    }
    ctx.SendJson(grapes.Obj{"Qualities": data})
}
