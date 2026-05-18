<template>
  <view class="detail-container">
    <view class="news-header">
      <image :src="newsDetail.image" mode="aspectFill" class="header-image" />
      <view class="header-overlay">
        <text class="news-type" v-if="newsDetail.type">{{ newsDetail.type }}</text>
      </view>
    </view>

    <view class="news-content">
      <text class="news-title">{{ newsDetail.title }}</text>
      <text class="news-time">{{ newsDetail.publishTime }}</text>
      
      <view class="content-body">
        <text class="content-text">{{ newsDetail.content }}</text>
      </view>

      <view class="content-images" v-if="newsDetail.images && newsDetail.images.length > 0">
        <view class="image-item" v-for="(img, index) in newsDetail.images" :key="index">
          <image :src="img" mode="aspectFill" class="detail-image" @tap="previewImage(index)" />
        </view>
      </view>
    </view>

    <view class="footer-section">
      <view class="related-title">
        <text class="title-text">相关推荐</text>
      </view>
      <view class="related-list">
        <view class="related-item" v-for="item in relatedNews" :key="item.id" @tap="goToDetail(item.id)">
          <image :src="item.image" mode="aspectFill" class="related-image" />
          <view class="related-info">
            <text class="related-title-text">{{ item.title }}</text>
            <text class="related-time">{{ item.publishTime }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useScenicStore } from '@/stores/scenic'

const scenicStore = useScenicStore()
const newsId = ref(0)
const newsDetail = ref({})

onMounted(() => {
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  const options = currentPage.options
  if (options.id) {
    newsId.value = parseInt(options.id)
    loadNewsDetail()
  }
})

const loadNewsDetail = () => {
  const detail = scenicStore.getNewsById(newsId.value)
  if (detail) {
    newsDetail.value = detail
  }
}

const relatedNews = computed(() => {
  return scenicStore.news.filter(n => n.id !== newsId.value).slice(0, 3)
})

const previewImage = (index) => {
  const urls = newsDetail.value.images || [newsDetail.value.image]
  uni.previewImage({
    urls: urls,
    current: index
  })
}

const goToDetail = (id) => {
  uni.navigateTo({ url: `/pages/news/detail?id=${id}` })
}
</script>

<style lang="scss" scoped>
.detail-container {
  min-height: 100vh;
  background: #F5F5F0;
}

.news-header {
  position: relative;
  height: 400rpx;
}

.header-image {
  width: 100%;
  height: 100%;
}

.header-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.3));
  display: flex;
  align-items: flex-end;
  padding: 30rpx;
}

.news-type {
  font-size: 24rpx;
  color: #FFFFFF;
  background: #2D5A4B;
  padding: 8rpx 20rpx;
  border-radius: 20rpx;
}

.news-content {
  background: #FFFFFF;
  margin: -40rpx 20rpx 20rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  position: relative;
  z-index: 10;
}

.news-title {
  display: block;
  font-size: 36rpx;
  font-weight: 600;
  color: #333333;
  line-height: 1.5;
  margin-bottom: 20rpx;
}

.news-time {
  font-size: 24rpx;
  color: #999999;
  margin-bottom: 30rpx;
  display: block;
}

.content-body {
  border-top: 2rpx solid #F0F0E8;
  padding-top: 30rpx;
}

.content-text {
  font-size: 28rpx;
  color: #666666;
  line-height: 1.8;
  text-align: justify;
}

.content-images {
  margin-top: 30rpx;
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.image-item {
  border-radius: 12rpx;
  overflow: hidden;
}

.detail-image {
  width: 100%;
  height: 400rpx;
}

.footer-section {
  padding: 0 20rpx 40rpx;
}

.related-title {
  margin-bottom: 20rpx;
}

.title-text {
  font-size: 30rpx;
  font-weight: 600;
  color: #333333;
}

.related-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.related-item {
  display: flex;
  gap: 20rpx;
  background: #FFFFFF;
  border-radius: 16rpx;
  padding: 20rpx;
}

.related-image {
  width: 180rpx;
  height: 130rpx;
  border-radius: 12rpx;
  flex-shrink: 0;
}

.related-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.related-title-text {
  font-size: 26rpx;
  color: #333333;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.related-time {
  font-size: 22rpx;
  color: #999999;
}
</style>
