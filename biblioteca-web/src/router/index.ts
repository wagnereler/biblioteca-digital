// src/router/index.ts

import { createRouter, createWebHistory } from 'vue-router'
import CategoriesView from '@/views/CategoriesView.vue'

const routes = [
    { path: '/', redirect: '/categories' },
    { path: '/categories', component: CategoriesView },
]

export const router = createRouter({
    history: createWebHistory(),
    routes
})
