import { post, get } from './request'

export const login = (data) => {
  return post('/auth/login', data)
}

export const getUserInfo = () => {
  return get('/user/info')
}

export const updateUserInfo = (data) => {
  return post('/user/update', data)
}

export const logout = () => {
  return post('/auth/logout')
}
