// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import CategoriesView from '@/views/CategoriesView.vue'
import BooksView      from '@/views/BooksView.vue'
import MembersView    from '@/views/MembersView.vue'
import LoansView      from '@/views/LoansView.vue'

const routes = [
    { path: '/',             redirect: '/categories' },
    { path: '/categories',   component: CategoriesView },
    { path: '/books',        component: BooksView },
    { path: '/members',      component: MembersView },
    { path: '/loans',        component: LoansView },
]

export const router = createRouter({
    history: createWebHistory(),
    routes
})

