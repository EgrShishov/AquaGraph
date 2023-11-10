package models

type Quality struct {
    Id int
    Time string
    Data string
}

type QualityJson struct {
    Time string
    Data interface{}
}

type Mark struct {
    Id int
    Data string
    Time string
    X string
    Y string
}
