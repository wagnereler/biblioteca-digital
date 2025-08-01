// src/services/api.ts

import type {
    CategoryDto,
    CategoryCreateDto,
    BookDto,
    BookCreateDto,
    MemberDto,
    MemberCreateDto,
    LoanDto,
    CreateLoanDto
} from '@/types'

const API_BASE = import.meta.env.VITE_API_BASE_URL

async function handleResponse<T>(res: Response): Promise<T> {
    if (!res.ok) throw new Error(`API error: ${res.status}`)
    return res.json()
}



// — Membros —
export async function getMembers(
    name?: string,
    registration?: number
): Promise<MemberDto[]> {
    const params = new URLSearchParams()
    if (name)         params.set('name', name)
    if (registration != null) params.set('registration', String(registration))
    const query = params.toString() ? `?${params.toString()}` : ''
    const res = await fetch(`${API_BASE}/members${query}`)
    return handleResponse<MemberDto[]>(res)
}

export async function createMember(
    payload: MemberCreateDto
): Promise<MemberDto> {
    const res = await fetch(`${API_BASE}/members`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    return handleResponse<MemberDto>(res)
}

export async function updateMember(
    id: string,
    payload: MemberCreateDto
): Promise<MemberDto> {
    const res = await fetch(`${API_BASE}/members/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    return handleResponse<MemberDto>(res)
}

export async function deleteMember(id: string): Promise<void> {
    const res = await fetch(`${API_BASE}/members/${id}`, { method: 'DELETE' })
    if (!res.ok) throw new Error(`Failed to delete member ${id}`)
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
    filters?: {
        title?: string
        author?: string
        isbn?: string
        category?: string
    }
): Promise<BookDto[]> {
    let url = `${API_BASE}/books`
    if (filters) {
        const params = new URLSearchParams()
        if (filters.title)    params.set('title', filters.title)
        if (filters.author)   params.set('author', filters.author)
        if (filters.isbn)     params.set('isbn', filters.isbn)
        if (filters.category) params.set('category', filters.category)
        const qs = params.toString()
        if (qs) url += `?${qs}`
    }
    const res = await fetch(url)
    return handleResponse<BookDto[]>(res)
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

// — Empréstimos —
export async function getLoans(): Promise<LoanDto[]> {
    const res = await fetch(`${API_BASE}/loans`)
    return handleResponse<LoanDto[]>(res)
}

export async function createLoan(
    payload: CreateLoanDto
): Promise<LoanDto> {
    const res = await fetch(`${API_BASE}/loans`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    return handleResponse<LoanDto>(res)
}

export async function returnLoan(id: string): Promise<void> {
    const res = await fetch(`${API_BASE}/loans/${id}/return`, {
        method: 'PATCH'
    })
    if (!res.ok) throw new Error(`Failed to return loan ${id}`)
}
