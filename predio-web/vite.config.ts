/**
 * Configuración de Vite:
 * - Habilita alias '@' → 'src' para imports limpios: import X from '@/carpeta/archivo'
 */
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: { '@': path.resolve(__dirname, './src') }
  }
})
