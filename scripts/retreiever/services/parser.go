package services

import (
	"io"
	"net/http"
	"regexp"
)


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
    return preparedJson, nil
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
