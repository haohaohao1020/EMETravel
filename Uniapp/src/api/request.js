import { useUserStore } from '@/stores/user'

const BASE_URL = 'http://localhost:8081'

const request = (options) => {
  return new Promise((resolve, reject) => {
    const userStore = useUserStore()
    
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': userStore.token ? 'Bearer ' + userStore.token : '',
        ...options.header
      },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 401) {
            uni.showToast({
              title: '登录已过期，请重新登录',
              icon: 'none'
            })
            userStore.logout()
            setTimeout(() => {
              uni.redirectTo({
                url: '/pages/login/login'
              })
            }, 1500)
            reject(res.data)
          } else {
            resolve(res.data)
          }
        } else {
          uni.showToast({
            title: '网络请求失败',
            icon: 'none'
          })
          reject(res)
        }
      },
      fail: (err) => {
        console.error('请求失败:', err)
        uni.showToast({
          title: '网络连接失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

export const get = (url, data) => {
  return request({
    url,
    method: 'GET',
    data
  })
}

export const post = (url, data) => {
  return request({
    url,
    method: 'POST',
    data
  })
}

export const put = (url, data) => {
  return request({
    url,
    method: 'PUT',
    data
  })
}

export const del = (url, data) => {
  return request({
    url,
    method: 'DELETE',
    data
  })
}

export default request
