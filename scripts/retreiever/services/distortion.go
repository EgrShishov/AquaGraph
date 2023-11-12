package services

import (
	"encoding/json"
	"fmt"
	"math"
	"math/rand"
	"strconv"
	"strings"
)

func Distort(str string) string {
    data := make(map[string]CleanQuality)
    json.Unmarshal([]byte(str), &data)
    for k,v := range data {
        for i, p := range v.Params {
            val := strings.ReplaceAll(p.Value, ",", ".")
            f, err := strconv.ParseFloat(val, 64)
            if err == nil {
                f += rand.Float64() * f / 5 * math.Pow(-1.0, float64(rand.Intn(2)))
                data[k].Params[i].Value = fmt.Sprintf("%.3f", f)
                println(p.Value, "->", data[k].Params[i].Value)
            }
        }
    }
    res, _ := json.Marshal(data)
    return string(res)
}

