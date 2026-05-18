
const BASE_URL = process.env.NODE_ENV === 'development' 
  ? '' 
  : 'https://api.emei-tour.com'

class Request {
  constructor() {
    this.baseURL = BASE_URL
    this.timeout = 30000
  }

  request(options) {
    return new Promise((resolve, reject) => {
      const { url, method = 'GET', data, header = {} } = options
      
      const token = uni.getStorageSync('token')
      if (token) {
        header['Authorization'] = `Bearer ${token}`
      }
      header['Content-Type'] = header['Content-Type'] || 'application/json'

      uni.request({
        url: this.baseURL + url,
        method,
        data,
        header,
        timeout: this.timeout,
        success: (res) => {
          const { statusCode, data: responseData } = res
          
          if (statusCode === 200) {
            if (responseData.code === 200) {
              resolve(responseData.data)
            } else if (responseData.code === 401) {
              uni.removeStorageSync('token')
              uni.removeStorageSync('userInfo')
              uni.showToast({
                title: '登录已过期，请重新登录',
                icon: 'none'
              })
              reject(responseData)
            } else {
              uni.showToast({
                title: responseData.msg || '请求失败',
                icon: 'none'
              })
              reject(responseData)
            }
          } else if (statusCode === 404) {
            reject(new Error('接口不存在'))
          } else {
            uni.showToast({
              title: '网络错误',
              icon: 'none'
            })
            reject(new Error('网络错误'))
          }
        },
        fail: (err) => {
          uni.showToast({
            title: '网络连接失败',
            icon: 'none'
          })
          reject(err)
        }
      })
    })
  }

  get(url, params = {}) {
    return this.request({
      url,
      method: 'GET',
      data: params
    })
  }

  post(url, data = {}) {
    return this.request({
      url,
      method: 'POST',
      data
    })
  }

  put(url, data = {}) {
    return this.request({
      url,
      method: 'PUT',
      data
    })
  }

  delete(url, data = {}) {
    return this.request({
      url,
      method: 'DELETE',
      data
    })
  }
}

export default new Request()
