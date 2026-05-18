import { get } from './request'

export const getScenicList = (params) => {
  return get('/scenic/list', params)
}

export const getScenicDetail = (id) => {
  return get(`/scenic/${id}`)
}

export const getNewsList = (params) => {
  return get('/notice/list', params)
}

export const getNewsDetail = (id) => {
  return get(`/notice/${id}`)
}

export const getNoticeList = () => {
  return get('/notice/list')
}

export const getBusStatus = () => {
  return get('/bus/status')
}
