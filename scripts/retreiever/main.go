package main

import (
	"fmt"
	"os"
	"retriever/services"
	"time"
)

func main() {
    data, err := services.GetData()
    if err != nil {
        fmt.Println(err.Error())
    }
    if len(data) < 80000 {
        fmt.Println(data)
    } else {
        fmt.Println(len(data), "symbols")
    }

    date := time.Now().UTC().Format("2006-01-02")
    fmt.Print("- ", date, " - insert this data? (Y/n): ")
    symb := make([]byte, 1)
    os.Stdin.Read(symb)
    if symb[0] == 110 {
        return
    }

    err = services.Insert(date, data)
    if err != nil {
        fmt.Println(err.Error())
    } else {
        fmt.Println("Successfully inserted")
    }
}

