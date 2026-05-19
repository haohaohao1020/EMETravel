import request from '@/utils/request'

export function getTicketList(params) {
  return request({
    url: '/ticket/list',
    method: 'get',
    params
  })
}

export function getTicket(id) {
  return request({
    url: `/ticket/${id}`,
    method: 'get'
  })
}

export function createTicket(data) {
  return request({
    url: '/ticket',
    method: 'post',
    data
  })
}

export function updateTicket(data) {
  return request({
    url: '/ticket',
    method: 'put',
    data
  })
}

export function deleteTicket(id) {
  return request({
    url: `/ticket/${id}`,
    method: 'delete'
  })
}

export function toggleTicketStatus(id, status) {
  return request({
    url: `/ticket/${id}/status`,
    method: 'put',
    data: { status }
  })
}

export function getTicketStockList(params) {
  return request({
    url: '/ticketStock/list',
    method: 'get',
    params
  })
}

export function updateTicketStock(data) {
  return request({
    url: '/ticketStock',
    method: 'put',
    data
  })
}

export function lockTicketStock(id, lockedStock) {
  return request({
    url: `/ticketStock/${id}/lock`,
    method: 'put',
    data: { lockedStock }
  })
}
