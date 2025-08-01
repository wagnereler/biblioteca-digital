// src/types/index.ts

export interface CategoryDto {
    id: string
    name: string
}

export interface CategoryCreateDto {
    name: string
}

export interface BookDto {
    id: string
    title: string
    author: string
    availableCopies: number
    isbn: string
    category: CategoryDto
}
export interface BookCreateDto {
    title: string
    author: string
    availableCopies: number
    isbn: string
    category: { id: string }
}

