import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'
import path from "path"
import tailwindcss from '@tailwindcss/vite'
import { qrcode } from "vite-plugin-qrcode"

const API_ORIGIN = process.env.VITE_API_ORIGIN
console.log(API_ORIGIN)
// https://vite.dev/config/
export default defineConfig(({ mode }) => {

  const env = loadEnv(mode, process.cwd())
  const API_ORIGIN = env.VITE_API_ORIGIN
  console.log(API_ORIGIN)

  return {
    plugins: [react(), tailwindcss(), qrcode()],
    resolve: {
      alias: {
        "@": path.resolve(__dirname, "./src"),
      },
    },
    server: {
      allowedHosts: true,
      host: '0.0.0.0',
      port: 5173,
      proxy: {
        '/api': {
          target: API_ORIGIN,
          changeOrigin: true,
          //rewrite: (path) => path.replace(/^\/api/, '')
        }
      }
    }
  }
}
)
