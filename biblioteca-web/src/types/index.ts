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

export interface MemberDto {
    id: string;
    name: string;
    email: string;
    phone?: string;
    registration: number;
}

export interface MemberCreateDto {
    name: string;
    email: string;
    phone?: string;
}

export interface LoanDto {
    id: string;
    loanDate: string;
    dueDate: string;
    returnDate?: string;
    status: string;
    member: MemberDto;
    book: BookDto;
}

export interface CreateLoanDto {
    memberId: string;
    bookId: string;
}

