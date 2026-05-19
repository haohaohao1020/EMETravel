<template>
  <div class="login-container">
    <div class="login-box">
      <div class="title-container">
        <h1 class="title">峨眉山景区管理后台</h1>
        <p class="subtitle">Emei Mountain Scenic Area Management System</p>
      </div>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
        <el-form-item prop="username">
          <el-input ref="username" v-model="loginForm.username" placeholder="请输入用户名" name="username" type="text" tabindex="1" auto-complete="on">
            <template #prefix>
              <el-icon class="el-input__icon"><User /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" name="password" tabindex="2" auto-complete="on" @keyup.enter="handleLogin">
            <template #prefix>
              <el-icon class="el-input__icon"><Lock /></el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登 录</el-button>
        </el-form-item>
      </el-form>
      <div class="tips">
        <p>默认账号：admin / 123456</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const loginForm = reactive({
  username: 'admin',
  password: '123456'
})

const loginRules = {
  username: [{ required: true, trigger: 'blur', message: '请输入用户名' }],
  password: [{ required: true, trigger: 'blur', message: '请输入密码' }]
}

const loading = ref(false)
const username = ref(null)

onMounted(() => {
  username.value?.focus()
})

const handleLogin = () => {
  loading.value = true
  userStore.login(loginForm).then(() => {
    ElMessage.success('登录成功')
    router.push({ path: '/' })
  }).catch(() => {
    loading.value = false
  })
}
</script>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  width: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.title-container {
  text-align: center;
  margin-bottom: 40px;

  .title {
    font-size: 24px;
    color: #333;
    margin-bottom: 10px;
  }

  .subtitle {
    font-size: 12px;
    color: #999;
  }
}

.login-form {
  .el-form-item {
    margin-bottom: 25px;
  }
}

.tips {
  text-align: center;
  color: #999;
  font-size: 12px;

  p {
    margin: 5px 0;
  }
}
</style>
