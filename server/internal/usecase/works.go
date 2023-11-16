package usecase

import (
	"io"
	"net/http"
	"regexp"
	"strings"
)

type Works []map[string]interface{}


func (u *Usecase) GetDataWorks() (Works, error) {
    page, err := getHTMLWorks()
    if err != nil {
        return nil, err
    }

    reg, _ := regexp.Compile("quot;,serif\">.[^<]+")

    data := reg.FindAllString(string(page), 1000)
    for i, elem := range data {
        data[i] = strings.ReplaceAll(elem[13:], "&nbsp;", "")
        
    }
    
    works := make(Works, len(data) / 4)
    for i := 0; i < len(data); i += 4 {
        works[i / 4] = make(map[string]interface{})
        works[i / 4]["Time"] = data[i + 2]
        works[i / 4]["Addresses"] = getAddresses(data[i + 3])
        works[i / 4]["Data"] = data[i + 3]
    } 

    return works, nil
}

func getAddresses(str string) []string {
    reg, _ := regexp.Compile("адресам:.[^.]+")
    addrs := reg.FindString(str)
    if addrs == "" {
        reg, _ = regexp.Compile("адресу:.[^.]+")
        addrs = reg.FindString(str)[14:]
    } else {
        addrs = addrs[16:]
    }
    return strings.Split(addrs, ";")
}


func getHTMLWorks() (string, error) {
    res, err := http.Get("http://minskvodokanal.by/about/planovyie-rabotyi")
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
