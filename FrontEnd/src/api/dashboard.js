import request from '@/utils/request'

export function getDashboardStats() {
  return request({
    url: '/dashboard/stats',
    method: 'get'
  })
}

export function getPassengerTrend(params) {
  return request({
    url: '/dashboard/passengerTrend',
    method: 'get',
    params
  })
}

export function getTicketSalesRatio() {
  return request({
    url: '/dashboard/ticketSalesRatio',
    method: 'get'
  })
}

export function getScenicHeatMap() {
  return request({
    url: '/dashboard/scenicHeatMap',
    method: 'get'
  })
}

export function getCableCarStock() {
  return request({
    url: '/dashboard/cableCarStock',
    method: 'get'
  })
}

export function getBusStatus() {
  return request({
    url: '/dashboard/busStatus',
    method: 'get'
  })
}

export function getWeatherAlert() {
  return request({
    url: '/dashboard/weatherAlert',
    method: 'get'
  })
}
