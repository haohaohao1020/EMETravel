import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: 'mock-token',
    userInfo: {
      id: 1,
      username: 'admin',
      nickname: '超级管理员',
      avatar: ''
    },
    roles: ['admin'],
    permissions: ['*']
  }),

  actions: {
    login(userInfo) {
      return new Promise((resolve) => {
        this.token = 'mock-token'
        resolve()
      })
    },

    getUserInfo() {
      return new Promise((resolve) => {
        resolve({
          user: this.userInfo,
          roles: this.roles,
          permissions: this.permissions
        })
      })
    },

    logout() {
      return new Promise((resolve) => {
        this.token = ''
        resolve()
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
