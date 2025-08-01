// src/services/api.ts

import type {
    CategoryDto,
    CategoryCreateDto,
    BookDto,
    BookCreateDto
} from '@/types'

const API_BASE = import.meta.env.VITE_API_BASE_URL

async function handleResponse<T>(res: Response): Promise<T> {
    if (!res.ok) throw new Error(`API error: ${res.status}`)
    return res.json()
}

// — Categorias —
export async function getCategories(name?: string): Promise<CategoryDto[]> {
    const params = name ? `?name=${encodeURIComponent(name)}` : ''
    const res = await fetch(`${API_BASE}/categories${params}`)
    return handleResponse(res)
}

export async function createCategory(payload: CategoryCreateDto): Promise<CategoryDto> {
    const res = await fetch(`${API_BASE}/categories`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    return handleResponse(res)
}

export async function updateCategory(id: string, payload: CategoryCreateDto): Promise<CategoryDto> {
    const res = await fetch(`${API_BASE}/categories/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    return handleResponse(res)
}

export async function deleteCategory(id: string): Promise<void> {
    const res = await fetch(`${API_BASE}/categories/${id}`, { method: 'DELETE' })
    if (!res.ok) throw new Error(`Failed to delete category ${id}`)
}

// — Livros —
export async function getBooks(
    filters?: { title?: string; author?: string; isbn?: string; category?: string }
): Promise<BookDto[]> {
    const qs = filters
        ? '?' + new URLSearchParams(
        Object.entries(filters).filter(([, v]) => v != null) as any
    ).toString()
        : ''
    const res = await fetch(`${API_BASE}/books${qs}`)
    return handleResponse(res)
}

export async function createBook(payload: BookCreateDto): Promise<BookDto> {
    const res = await fetch(`${API_BASE}/books`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    return handleResponse(res)
}

export async function updateBook(id: string, payload: BookCreateDto): Promise<BookDto> {
    const res = await fetch(`${API_BASE}/books/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    return handleResponse(res)
}

export async function deleteBook(id: string): Promise<void> {
    const res = await fetch(`${API_BASE}/books/${id}`, { method: 'DELETE' })
    if (!res.ok) throw new Error(`Failed to delete book ${id}`)
}
