package usecase

import (
	"aquaGraph/models"
	"io"
	"net/http"
	"regexp"
	"strings"
	"time"

	"github.com/Rosto4eks/loggify"
)

const (
    UPDATE_TIMESPAN_OK = time.Minute * 60 * 12
    UPDATE_TIMESPAN_ERR = time.Minute * 60
)

func (u *Usecase) GetWorks() (models.Works, error) {
    return u.repository.GetWorks()
}

func (u *Usecase) UpdateWorks() {
    loggify.INFO("Loading works data...")
    works, err := u.getDataWorks()
    if err == nil {
        u.repository.SaveWorks(works)
        loggify.INFO("Successfully loaded works")
        time.Sleep(UPDATE_TIMESPAN_OK)
    } else {
        loggify.ERROR(err.Error())
        time.Sleep(UPDATE_TIMESPAN_ERR)
    }
    u.UpdateWorks()
}

func (u *Usecase) getDataWorks() (models.Works, error) {
    page, err := getHTMLWorks()
    if err != nil {
        return nil, err
    }

    reg, _ := regexp.Compile("quot;,serif\">.[^<]+")

    data := reg.FindAllString(string(page), 1000)
    for i, elem := range data {
        data[i] = strings.ReplaceAll(elem[13:], "&nbsp;", "")
        
    }
    works := make(models.Works, 0)
    for i := 0; i < len(data); i += 4 {
        if data[i] == "" {
            i++
            continue
        }
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
        loggify.ERROR(err.Error())
        return true
    }
    return time.Now().After(t)
}

func getAddresses(str string) []string {
    reg, _ := regexp.Compile("адресам:.[^.]+")
    addrs := reg.FindString(str)
    if addrs == "" {
        reg, _ = regexp.Compile("адресу:.[^.]+")
        addrs = reg.FindString(str)
        addrs = addrs[14:]
    } else {
        addrs = addrs[16:]
    }
    return strings.Split(addrs, ";")
}


func getHTMLWorks() (string, error) {
    res, err := http.Get("http://minskvodokanal.by/about/planovyie-rabotyi/")
    if err != nil {
        loggify.ERROR(err.Error())
        return "", err
    }
    defer res.Body.Close()
    page, err := io.ReadAll(res.Body)
    if err != nil {
        loggify.ERROR(err.Error())
        return "", err
    }
    return string(page), nil
}
