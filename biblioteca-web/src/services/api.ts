// src/services/api.ts
import type { CategoryDto, CategoryCreateDto } from '@/types'

const API_BASE = import.meta.env.VITE_API_BASE_URL

async function handleResponse<T>(res: Response): Promise<T> {
    if (!res.ok) throw new Error(`API error: ${res.status}`)
    return res.json()
}

export async function getCategories(name?: string): Promise<CategoryDto[]> {
    const params = name ? `?name=${encodeURIComponent(name)}` : ''
    const res = await fetch(`${API_BASE}/categories${params}`)
    return handleResponse<CategoryDto[]>(res)
}

export async function createCategory(payload: CategoryCreateDto): Promise<CategoryDto> {
    const res = await fetch(`${API_BASE}/categories`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    return handleResponse<CategoryDto>(res)
}

export async function updateCategory(id: string, payload: CategoryCreateDto): Promise<CategoryDto> {
    const res = await fetch(`${API_BASE}/categories/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    return handleResponse<CategoryDto>(res)
}

export async function deleteCategory(id: string): Promise<void> {
    const res = await fetch(`${API_BASE}/categories/${id}`, {
        method: 'DELETE'
    })
    if (!res.ok) throw new Error(`Failed to delete category ${id}`)
}
