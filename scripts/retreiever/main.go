package main

import (
	"fmt"
	"retriever/services"
	"time"
)

func main() {
    data, err := services.GetData()
    if err != nil {
        fmt.Println(err.Error())
    }


    fmt.Print("distort data? (Y/n): ")
    var ans string
    fmt.Scanln(&ans)
    data = services.Prepare(data, ans != "n")

    if len(data) < 80000 {
        fmt.Println(data)
    } else {
        fmt.Println(len(data), "symbols")
    }

    date := time.Now().UTC().Format("2006-01-02")
    fmt.Print("- ", date, " - insert this data? (Y/n): ")
    ans = ""
    fmt.Scanln(&ans)
    if ans == "n" {
        return
    }

    err = services.Insert(date, data)
    if err != nil {
        fmt.Println(err.Error())
    } else {
        fmt.Println("Successfully inserted")
    }
}

