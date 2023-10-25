package usecase

import "aquaGraph/internal/repository"

type Usecase struct {
	repository *repository.Repository
}

func New(repository *repository.Repository) *Usecase {
	return &Usecase{
		repository,
	}
}
