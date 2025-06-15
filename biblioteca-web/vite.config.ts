// vite.config.ts
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: { '@': path.resolve(__dirname, './src') }
  },
  server: {
    host: '0.0.0.0',       // aceita conexões externas
    port: 3000,           // porta desejada
    strictPort: true      // falha se a 3000 estiver ocupada
  }
})
