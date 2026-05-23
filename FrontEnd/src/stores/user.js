import { defineStore } from 'pinia'
import { login, logout, getUserInfo } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    userInfo: null,
    roles: [],
    permissions: [],
    menus: []
  }),

  actions: {
    login(userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(res => {
          this.token = res.data.token
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    getUserInfo() {
      return new Promise((resolve, reject) => {
        getUserInfo().then(res => {
          this.userInfo = res.data.user || res.data.adminInfo
          this.roles = res.data.roles || ['admin']
          this.permissions = res.data.permissions || ['*']
          this.menus = res.data.menus || []
          resolve(res.data)
        }).catch(error => {
          reject(error)
        })
      })
    },

    logout() {
      return new Promise((resolve, reject) => {
        logout().then(() => {
          this.token = ''
          this.userInfo = null
          this.roles = []
          this.permissions = []
          this.menus = []
          resolve()
        }).catch(error => {
          reject(error)
        })
      })
    },

    resetToken() {
      return new Promise(resolve => {
        this.token = ''
        resolve()
      })
    }
  }
})
