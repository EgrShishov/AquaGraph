package controller

import (
	"aquaGraph/internal/usecase"
	"aquaGraph/models"
	"strconv"

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

func (c *Controller) GetQuality(ctx grapes.Context) {
	polygons, err := c.usecase.GetPolygons()
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
	}
	x, err := strconv.ParseFloat(ctx.GetQueryParam("x"), 64)
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
	}
	y, err := strconv.ParseFloat(ctx.GetQueryParam("y"), 64)
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
	}
	id, err := c.usecase.Locate(polygons, usecase.Point{x, y})
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
	}
    data, err := c.usecase.GetQuality(id)
    if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
    }
    ctx.SendJson(grapes.Obj{"Quality": data})
}


func (c *Controller) GetQualities(ctx grapes.Context) {
	polygons, err := c.usecase.GetPolygons()
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
	}
	x, err := strconv.ParseFloat(ctx.GetQueryParam("x"), 64)
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
	}
	y, err := strconv.ParseFloat(ctx.GetQueryParam("y"), 64)
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
	}
	id, err := c.usecase.Locate(polygons, usecase.Point{x, y})
	if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
	}
    data, err := c.usecase.GetQualities(id)
    if err != nil {
        ctx.SendJson(grapes.Obj{"error": err.Error()})
    }
    ctx.SendJson(grapes.Obj{"Qualities": data})
}


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
