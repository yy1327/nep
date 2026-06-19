import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/nepm',
  timeout: 5000
})

api.interceptors.response.use(
  response => response.data,
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

export default api
