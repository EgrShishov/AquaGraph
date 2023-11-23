package services

import (
	"encoding/json"
	"fmt"
	"math"
	"math/rand"
	"regexp"
	"strconv"
	"strings"
	"unicode/utf8"
)

const epsilon = 0.01

func distort(f float64) float64 {
    if rand.Intn(100) > 30 {
        return f + rand.Float64() * f / 7 * math.Pow(-1.0, float64(rand.Intn(2)))
    } 
    return f
}

func clean(s string) string {
    val := strings.ReplaceAll(s, ",", ".")
    val = strings.ReplaceAll(val, "<", "")
    rg, _ := regexp.Compile("[0-9.]+")
    return rg.FindString(val)
}

func Prepare(str string, distortion bool) string {
    data := make(map[string]CleanQuality)
    json.Unmarshal([]byte(str), &data)

    for k,v := range data {
        for i, p := range v.Params {
            val := clean(p.Value)
            sp := strings.Repeat("-", 20 - utf8.RuneCountInString(p.Value))
            print(p.Value, " ", sp, "> ")
            f, err := strconv.ParseFloat(val, 64)
            if err == nil {
                if distortion {
                    f = distort(f)
                }
                if f == 0 {
                    f = epsilon
                }
                data[k].Params[i].Value = fmt.Sprintf("%.3f", f)
            } else {
                data[k].Params[i].Value = fmt.Sprintf("%.3f", epsilon)
            }
            println(data[k].Params[i].Value)
        }
    }
    res, _ := json.Marshal(data)
    return string(res)
}

