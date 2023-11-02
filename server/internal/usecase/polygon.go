package usecase

import (
	"errors"
	"math"
)

type Point []float64
type crossRay func(Point, Point, Point) float64

type Polygons struct {
	Polygons []Polygon `json:"polygons"`
}

type Polygon struct {
	Coords     []Point    `json:"coordinates"`
	Properties Properties `json:"properties"`
}

type Properties struct {
	Id   string `json:"id"`
	Name string `json:"name"`
}

// returns id of point's polygon
func (u *Usecase) Locate(polygons Polygons, point Point) (string, error) {
	for _, pl := range polygons.Polygons {
		if locatePolygon(pl.Coords, point) {
			return pl.Properties.Id, nil
		}
	}
	return "", errors.New("not found")
}

// determines whether the point is inside the polygon
func locatePolygon(polygon []Point, point Point) bool {
	// direct two rays up and down
	return inPolygon(polygon, point, crossRayUp) && inPolygon(polygon, point, crossRayDown)
}

// count all unique crossed dots on polygon sides
// if number is odd, it means the point is in polygon
func inPolygon(polygon []Point, point Point, fn crossRay) bool {
	dots := make(map[float64]bool, len(polygon))
	for i := 0; i < len(polygon); i++ {
		if i == len(polygon)-1 {
			dots[fn(point, polygon[i], polygon[0])] = true
		} else {
			dots[fn(point, polygon[i], polygon[i+1])] = true
		}
	}
	return (len(dots)-1)%2 == 1
}

func crossRayUp(point Point, dot1 Point, dot2 Point) float64 {
	if (point[0] > dot1[0] && point[0] > dot2[0]) || (point[0] < dot1[0] && point[0] < dot2[0]) {
		return -1
	}
	tan := math.Abs((dot1[1] - dot2[1]) / (dot1[0] - dot2[0]))

	var xmin float64
	if dot1[1] < dot2[1] {
		xmin = dot1[0]
	} else {
		xmin = dot2[0]
	}

	y := math.Abs(point[0]-xmin)*tan + math.Min(dot1[1], dot2[1])
	if point[1] <= y {
		return y
	} else {
		return -1
	}
}

func crossRayDown(point Point, dot1 Point, dot2 Point) float64 {
	if (point[0] > dot1[0] && point[0] > dot2[0]) || (point[0] < dot1[0] && point[0] < dot2[0]) {
		return -1
	}
	tan := math.Abs((dot1[1] - dot2[1]) / (dot1[0] - dot2[0]))

	var xmax float64
	if dot1[1] > dot2[1] {
		xmax = dot1[0]
	} else {
		xmax = dot2[0]
	}

	y := math.Max(dot1[1], dot2[1]) - math.Abs(point[0]-xmax)*tan
	if point[1] >= y {
		return y
	} else {
		return -1
	}
}
