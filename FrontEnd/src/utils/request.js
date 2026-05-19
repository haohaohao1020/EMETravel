import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 30000
})

service.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers['Authorization'] = 'Bearer ' + userStore.token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage({
        message: res.msg || '请求失败',
        type: 'error',
        duration: 5 * 1000
      })

      if (res.code === 401) {
        ElMessageBox.confirm('登录状态已过期，请重新登录', '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          const userStore = useUserStore()
          userStore.logout()
          router.push('/login')
        })
      }
      return Promise.reject(new Error(res.msg || '请求失败'))
    } else {
      return res
    }
  },
  error => {
    console.log('err' + error)
    let message = error.message
    if (error.response?.data?.msg) {
      message = error.response.data.msg
    } else if (error.response?.status === 404) {
      message = '接口不存在'
    } else if (error.response?.status === 500) {
      message = '服务器错误'
    } else if (error.response?.status === 401) {
      message = '未授权，请重新登录'
      const userStore = useUserStore()
      userStore.logout()
      router.push('/login')
    }
    ElMessage({
      message: message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
