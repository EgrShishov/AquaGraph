package usecase

import (
	"io"
	"net/http"
	"regexp"
	"strings"
	"time"
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
    
    works := make(Works, 0)
    for i := 0; i < len(data); i += 4 {
        temp := make(map[string]interface{})
        temp["Time"] = data[i + 2]
        temp["Addresses"] = getAddresses(data[i + 3])
        temp["Data"] = data[i + 3]
        if !isPast(data[i+2]) {
            works = append(works, temp)
        }
    } 

    return works, nil
}

func isPast(data string) bool {
    rg, _ := regexp.Compile("до .*")
    data = rg.FindString(data)
    if data == "" {
        return false
    }
    t, err := time.Parse("02.01.2006", data[11:])
    if err != nil {
        print(err.Error())
        return true
    }
    return time.Now().After(t)
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
