import request from '@/utils/request'

export function getScenicSpotList(params) {
  return request({
    url: '/scenicSpot/list',
    method: 'get',
    params
  })
}

export function getScenicSpot(id) {
  return request({
    url: `/scenicSpot/${id}`,
    method: 'get'
  })
}

export function createScenicSpot(data) {
  return request({
    url: '/scenicSpot',
    method: 'post',
    data
  })
}

export function updateScenicSpot(data) {
  return request({
    url: '/scenicSpot',
    method: 'put',
    data
  })
}

export function deleteScenicSpot(id) {
  return request({
    url: `/scenicSpot/${id}`,
    method: 'delete'
  })
}

export function toggleScenicSpotStatus(id, status) {
  return request({
    url: `/scenicSpot/${id}/status`,
    method: 'put',
    data: { status }
  })
}

export function getRouteList(params) {
  return request({
    url: '/route/list',
    method: 'get',
    params
  })
}

export function createRoute(data) {
  return request({
    url: '/route',
    method: 'post',
    data
  })
}

export function updateRoute(data) {
  return request({
    url: '/route',
    method: 'put',
    data
  })
}

export function deleteRoute(id) {
  return request({
    url: `/route/${id}`,
    method: 'delete'
  })
}
