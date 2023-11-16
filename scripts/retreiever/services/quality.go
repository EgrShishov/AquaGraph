package services

import (
	"encoding/json"
	"io"
	"net/http"
	"regexp"
)

type Quality struct {
    Link int `json:"link"` 
    Name string `json:"name"`
    Params [][]params `json:"params"`
    Titles string `json:"titles"`
}


type CleanQuality struct {
    Link int `json:"link"` 
    Name string `json:"name"`
    Params []params `json:"params"`
}

type params struct {
    MIGX_id string `json:"MIGX_id"`
    Help string `json:"help"`
    Metric string `json:"metric"`
    Name string `json:"name"`
    Pdk string `json:"pdk"`
    Value string `json:"value"`
}


func GetData() (string, error) {
    page, err := getHTML()
    if err != nil {
        return "", err
    }

    reg, _ := regexp.Compile("information = .*\n")
    reg2, _ := regexp.Compile("{.*")

    rawJson := reg.FindString(string(page))
    preparedJson := reg2.FindString(rawJson)
    preparedJson = preparedJson[:len(preparedJson)-1]
    return removeTrash(preparedJson), nil
}

func removeTrash(str string) string {
    data := make(map[string]Quality)
    cleanData := make(map[string]CleanQuality)
    
    json.Unmarshal([]byte(str), &data)
    for key, elem := range data {
        cleanData[key] = CleanQuality{
            Name: elem.Name,
            Link: elem.Link,
            Params: elem.Params[0],
        }
    }
    bytes, err := json.Marshal(cleanData)
    if err != nil {
        panic(err)
    }
    return string(bytes)
}

func getHTML() (string, error) {
    res, err := http.Get("http://minskvodokanal.by/water/home")
    if err != nil {
        return "", err
    }
    defer res.Body.Close()
    page, err := io.ReadAll(res.Body)
    if err != nil {
        return "", err
    }
    return string(page), nil
}
