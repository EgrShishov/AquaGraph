package usecase

import "math"

type Point struct {
	X float64
	Y float64
}

type crossRay func(Point, Point, Point) float64

// determines whether the point is inside the polygon
func locate(polygon []Point, point Point) bool {
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
	if (point.X > dot1.X && point.X > dot2.X) || (point.X < dot1.X && point.X < dot2.X) {
		return -1
	}
	tan := math.Abs((dot1.Y - dot2.Y) / (dot1.X - dot2.X))

	var xmin float64
	if dot1.Y < dot2.Y {
		xmin = dot1.X
	} else {
		xmin = dot2.X
	}

	y := math.Abs(point.X-xmin)*tan + math.Min(dot1.Y, dot2.Y)
	if point.Y <= y {
		return y
	} else {
		return -1
	}
}

func crossRayDown(point Point, dot1 Point, dot2 Point) float64 {
	if (point.X > dot1.X && point.X > dot2.X) || (point.X < dot1.X && point.X < dot2.X) {
		return -1
	}
	tan := math.Abs((dot1.Y - dot2.Y) / (dot1.X - dot2.X))

	var xmax float64
	if dot1.Y > dot2.Y {
		xmax = dot1.X
	} else {
		xmax = dot2.X
	}

	y := math.Max(dot1.Y, dot2.Y) - math.Abs(point.X-xmax)*tan
	if point.Y >= y {
		return y
	} else {
		return -1
	}
}
