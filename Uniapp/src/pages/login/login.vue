<template>
  <view class="login-container">
    <view class="login-header">
      <view class="logo-wrapper">
        <text class="logo-icon">🏔️</text>
      </view>
      <text class="app-title">峨眉山旅游</text>
      <text class="app-subtitle">世界文化与自然双重遗产</text>
    </view>

    <view class="login-form">
      <view class="form-item">
        <text class="form-label">手机号</text>
        <input 
          class="form-input" 
          type="number" 
          v-model="loginForm.phone" 
          placeholder="请输入手机号"
          maxlength="11"
        />
      </view>
      
      <view class="form-item">
        <text class="form-label">密码</text>
        <input 
          class="form-input" 
          type="password" 
          v-model="loginForm.password" 
          placeholder="请输入密码"
          password
        />
      </view>

      <view class="form-tip">
        <text class="tip-text">默认初始密码：123456</text>
      </view>

      <view class="login-btn" @tap="handleLogin">
        <text class="btn-text">登录</text>
      </view>

      <view class="register-link">
        <text class="link-text">没有账号？</text>
        <text class="link-text highlight">立即注册</text>
      </view>
    </view>

    <view class="login-footer">
      <text class="footer-text">登录即表示同意</text>
      <text class="footer-text highlight">《用户协议》</text>
      <text class="footer-text">和</text>
      <text class="footer-text highlight">《隐私政策》</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { useUserStore } from '@/stores/user'
import { login } from '@/api/user'

const userStore = useUserStore()

const loginForm = ref({
  phone: '',
  password: '123456'
})

const handleLogin = async () => {
  if (!loginForm.value.phone) {
    uni.showToast({
      title: '请输入手机号',
      icon: 'none'
    })
    return
  }
  
  if (loginForm.value.phone.length !== 11) {
    uni.showToast({
      title: '请输入正确的手机号',
      icon: 'none'
    })
    return
  }
  
  if (!loginForm.value.password) {
    uni.showToast({
      title: '请输入密码',
      icon: 'none'
    })
    return
  }

  uni.showLoading({
    title: '登录中...'
  })

  try {
    const res = await login(loginForm.value)
    
    if (res.code === 200) {
      userStore.setToken(res.data.token)
      userStore.setUserInfo(res.data.userInfo)
      
      uni.showToast({
        title: '登录成功',
        icon: 'success'
      })
      
      setTimeout(() => {
        uni.switchTab({
          url: '/pages/index/index'
        })
      }, 1000)
    } else {
      uni.showToast({
        title: res.msg || '登录失败',
        icon: 'none'
      })
    }
  } catch (error) {
    console.error('登录错误:', error)
    uni.showToast({
      title: '网络错误，请稍后重试',
      icon: 'none'
    })
  } finally {
    uni.hideLoading()
  }
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #2D5A4B 0%, #F5F5F0 40%);
  padding: 40rpx;
}

.login-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 80rpx 0 60rpx;
}

.logo-wrapper {
  width: 160rpx;
  height: 160rpx;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30rpx;
}

.logo-icon {
  font-size: 80rpx;
}

.app-title {
  font-size: 48rpx;
  font-weight: 600;
  color: #FFFFFF;
  margin-bottom: 16rpx;
}

.app-subtitle {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.8);
}

.login-form {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 50rpx 40rpx;
  box-shadow: 0 8rpx 32rpx rgba(45, 90, 75, 0.1);
}

.form-item {
  margin-bottom: 40rpx;
}

.form-label {
  display: block;
  font-size: 28rpx;
  color: #333333;
  margin-bottom: 16rpx;
  font-weight: 500;
}

.form-input {
  width: 100%;
  height: 88rpx;
  background: #F8F8F5;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
  color: #333333;
  box-sizing: border-box;
}

.form-tip {
  margin-bottom: 40rpx;
}

.tip-text {
  font-size: 24rpx;
  color: #999999;
}

.login-btn {
  width: 100%;
  height: 96rpx;
  background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);
  border-radius: 48rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 30rpx;
  
  &:active {
    opacity: 0.9;
    transform: scale(0.98);
  }
}

.btn-text {
  font-size: 32rpx;
  color: #FFFFFF;
  font-weight: 500;
}

.register-link {
  text-align: center;
  
  .link-text {
    font-size: 26rpx;
    color: #666666;
    
    &.highlight {
      color: #2D5A4B;
      margin-left: 8rpx;
    }
  }
}

.login-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 80rpx;
  flex-wrap: wrap;
  
  .footer-text {
    font-size: 22rpx;
    color: #999999;
    
    &.highlight {
      color: #2D5A4B;
      margin: 0 4rpx;
    }
  }
}
</style>
