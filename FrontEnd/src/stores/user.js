import { defineStore } from 'pinia'
import { login, logout, getUserInfo } from '@/api/user'
import { setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: {},
    roles: [],
    permissions: []
  }),

  actions: {
    login(userInfo) {
      const { username, password } = userInfo
      return new Promise((resolve, reject) => {
        login({ username: username.trim(), password: password }).then(response => {
          const { data } = response
          this.token = data.token
          setToken(data.token)
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    getUserInfo() {
      return new Promise((resolve, reject) => {
        getUserInfo().then(response => {
          const { data } = response
          this.userInfo = data.user
          this.roles = data.roles
          this.permissions = data.permissions
          localStorage.setItem('roles', JSON.stringify(data.roles))
          localStorage.setItem('permissions', JSON.stringify(data.permissions))
          resolve(data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    logout() {
      return new Promise((resolve, reject) => {
        logout().then(() => {
          this.token = ''
          this.userInfo = {}
          this.roles = []
          this.permissions = []
          removeToken()
          localStorage.removeItem('roles')
          localStorage.removeItem('permissions')
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    resetToken() {
      return new Promise(resolve => {
        this.token = ''
        this.roles = []
        this.permissions = []
        removeToken()
        resolve()
      })
    }
  }
})
