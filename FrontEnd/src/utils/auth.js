import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function hasPermission(permission) {
  const permissions = JSON.parse(localStorage.getItem('permissions') || '[]')
  return permissions.includes(permission)
}

export function hasRole(role) {
  const roles = JSON.parse(localStorage.getItem('roles') || '[]')
  return roles.includes(role)
}
