<script setup>
import { onLaunch, onShow, onHide } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

onLaunch(() => {
  console.log('App Launch - 峨眉山旅游系统启动')
  userStore.initUserInfo()
})

onShow(() => {
  console.log('App Show')
})

onHide(() => {
  console.log('App Hide')
})
</script>

<style lang="scss">
@import '@/styles/theme.scss';

page {
  background-color: #F5F5F0;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', sans-serif;
  color: #333333;
}

.emei-container {
  padding: 20rpx;
  min-height: 100vh;
}

.emei-card {
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 30rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(45, 90, 75, 0.08);
}

.emei-btn {
  background: linear-gradient(135deg, #2D5A4B 0%, #3D7A6B 100%);
  color: #FFFFFF;
  border-radius: 50rpx;
  height: 88rpx;
  line-height: 88rpx;
  font-size: 32rpx;
  font-weight: 500;
  border: none;
  &:active {
    opacity: 0.9;
    transform: scale(0.98);
  }
}

.emei-btn-outline {
  background: transparent;
  color: #2D5A4B;
  border: 2rpx solid #2D5A4B;
  border-radius: 50rpx;
  height: 88rpx;
  line-height: 88rpx;
  font-size: 32rpx;
  font-weight: 500;
}

.emei-title {
  font-size: 36rpx;
  font-weight: 600;
  color: #2D5A4B;
  margin-bottom: 20rpx;
}

.emei-subtitle {
  font-size: 28rpx;
  color: #666666;
  margin-bottom: 16rpx;
}

.emei-text {
  font-size: 26rpx;
  color: #999999;
  line-height: 1.6;
}

.emei-divider {
  height: 1rpx;
  background: linear-gradient(90deg, transparent, #E0E0E0, transparent);
  margin: 30rpx 0;
}

.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-between {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.flex-start {
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.text-ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.safe-area-bottom {
  padding-bottom: env(safe-area-inset-bottom);
}
</style>
