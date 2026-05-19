import request from '@/utils/request'

export function getOrderList(params) {
  return request({
    url: '/order/list',
    method: 'get',
    params
  })
}

export function getOrder(id) {
  return request({
    url: `/order/${id}`,
    method: 'get'
  })
}

export function verifyTicket(code) {
  return request({
    url: '/order/verify',
    method: 'post',
    data: { code }
  })
}

export function getRefundList(params) {
  return request({
    url: '/order/refundList',
    method: 'get',
    params
  })
}

export function approveRefund(id, remark) {
  return request({
    url: `/order/${id}/approveRefund`,
    method: 'put',
    data: { remark }
  })
}

export function rejectRefund(id, remark) {
  return request({
    url: `/order/${id}/rejectRefund`,
    method: 'put',
    data: { remark }
  })
}

export function exportOrder(params) {
  return request({
    url: '/order/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

export function getVerifyRecord(params) {
  return request({
    url: '/order/verifyRecord',
    method: 'get',
    params
  })
}
