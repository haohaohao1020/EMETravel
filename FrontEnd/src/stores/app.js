import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    sidebar: {
      opened: true
    },
    device: 'desktop'
  }),

  actions: {
    toggleSideBar() {
      this.sidebar.opened = !this.sidebar.opened
    },
    closeSideBar() {
      this.sidebar.opened = false
    },
    toggleDevice(device) {
      this.device = device
    }
  }
})
