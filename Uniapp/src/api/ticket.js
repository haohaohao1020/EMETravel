import { get, post } from './request'

export const getTicketList = (params) => {
  return get('/ticket/list', params)
}

export const getTicketDetail = (id) => {
  return get(`/ticket/${id}`)
}

export const getTicketStock = (params) => {
  return get('/ticket/stock', params)
}

export const getTimeSlots = (params) => {
  return get('/ticket/timeSlots', params)
}

export const createOrder = (data) => {
  return post('/order/create', data)
}

export const getOrderList = (params) => {
  return get('/order/list', params)
}

export const getOrderDetail = (id) => {
  return get(`/order/${id}`)
}

export const cancelOrder = (id) => {
  return post(`/order/cancel/${id}`)
}

export const refundOrder = (id) => {
  return post(`/order/refund/${id}`)
}

export const verifyTicket = (data) => {
  return post('/order/verify', data)
}
