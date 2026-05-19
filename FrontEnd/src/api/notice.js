import request from '@/utils/request'

export function getNoticeList(params) {
  return request({
    url: '/notice/list',
    method: 'get',
    params
  })
}

export function getNotice(id) {
  return request({
    url: `/notice/${id}`,
    method: 'get'
  })
}

export function createNotice(data) {
  return request({
    url: '/notice',
    method: 'post',
    data
  })
}

export function updateNotice(data) {
  return request({
    url: '/notice',
    method: 'put',
    data
  })
}

export function deleteNotice(id) {
  return request({
    url: `/notice/${id}`,
    method: 'delete'
  })
}

export function toggleNoticeStatus(id, status) {
  return request({
    url: `/notice/${id}/status`,
    method: 'put',
    data: { status }
  })
}
