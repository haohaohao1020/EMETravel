import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getScenicList, getNewsList } from '@/api/scenic'

export const useScenicStore = defineStore('scenic', () => {
  const spots = ref([])
  const news = ref([])
  const selectedSpot = ref(null)
  const loading = ref(false)

  const loadSpots = async () => {
    if (spots.value.length > 0) return
    loading.value = true
    try {
      const data = await getScenicList()
      spots.value = data || []
    } catch (error) {
      console.error('加载景点失败:', error)
    } finally {
      loading.value = false
    }
  }

  const loadNews = async () => {
    if (news.value.length > 0) return
    loading.value = true
    try {
      const data = await getNewsList()
      news.value = data?.records || []
    } catch (error) {
      console.error('加载资讯失败:', error)
    } finally {
      loading.value = false
    }
  }

  const getSpotById = (id) => {
    return spots.value.find(s => s.id === id)
  }

  const getNewsById = (id) => {
    return news.value.find(n => n.id === id)
  }

  const selectSpot = (spot) => {
    selectedSpot.value = spot
  }

  return {
    spots,
    news,
    selectedSpot,
    loading,
    loadSpots,
    loadNews,
    getSpotById,
    getNewsById,
    selectSpot
  }
})
