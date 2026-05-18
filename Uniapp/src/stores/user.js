import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(null)
  const token = ref('')

  const isLoggedIn = computed(() => !!token.value)

  const initUserInfo = () => {
    const savedUserInfo = uni.getStorageSync('userInfo')
    const savedToken = uni.getStorageSync('token')
    
    if (savedUserInfo) {
      userInfo.value = JSON.parse(savedUserInfo)
    }
    if (savedToken) {
      token.value = savedToken
    }
  }

  const login = async (loginData) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        const mockUser = {
          id: 1,
          username: loginData.username || '游客',
          avatar: 'https://picsum.photos/200/200?random=user',
          phone: '138****8888',
          level: '普通会员',
          points: 1000
        }
        const mockToken = 'mock_token_' + Date.now()
        
        userInfo.value = mockUser
        token.value = mockToken
        
        uni.setStorageSync('userInfo', JSON.stringify(mockUser))
        uni.setStorageSync('token', mockToken)
        
        resolve(mockUser)
      }, 500)
    })
  }

  const logout = () => {
    userInfo.value = null
    token.value = ''
    uni.removeStorageSync('userInfo')
    uni.removeStorageSync('token')
    uni.showToast({
      title: '已退出登录',
      icon: 'success'
    })
  }

  const updateUserInfo = (newInfo) => {
    userInfo.value = { ...userInfo.value, ...newInfo }
    uni.setStorageSync('userInfo', JSON.stringify(userInfo.value))
  }

  return {
    userInfo,
    token,
    isLoggedIn,
    initUserInfo,
    login,
    logout,
    updateUserInfo
  }
})
