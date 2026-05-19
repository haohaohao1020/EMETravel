<template>
  <div class="user-container">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增用户
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="用户名">
          <el-input v-model="searchForm.username" placeholder="请输入用户名" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="启用" value="1" />
            <el-option label="禁用" value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="table-container">
      <el-table :data="tableData" border stripe>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" min-width="120" />
        <el-table-column prop="realName" label="真实姓名" min-width="100" />
        <el-table-column prop="phone" label="手机号" min-width="120" />
        <el-table-column prop="email" label="邮箱" min-width="150" />
        <el-table-column prop="role" label="角色" min-width="100">
          <template #default="{ row }">
            <el-tag :type="getRoleType(row.role)">{{ getRoleName(row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '1' ? 'success' : 'danger'">
              {{ row.status === '1' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="warning" link size="small" @click="handleResetPwd(row)">
              <el-icon><Lock /></el-icon>
              重置密码
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="formData.username" placeholder="请输入用户名" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="formData.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item v-if="!isEdit" label="密码" prop="password">
          <el-input v-model="formData.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="formData.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="formData.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="超级管理员" value="admin" />
            <el-option label="票务管理员" value="ticket" />
            <el-option label="核销员" value="verify" />
            <el-option label="财务统计员" value="finance" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio value="1">启用</el-radio>
            <el-radio value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import * as XLSX from 'xlsx'

const formRef = ref(null)
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = ref('新增用户')

const searchForm = reactive({
  username: '',
  phone: '',
  status: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([
  { id: 1, username: 'admin', realName: '管理员', phone: '13800138000', email: 'admin@emei.com', role: 'admin', status: '1', createTime: '2024-01-01 00:00:00' },
  { id: 2, username: 'ticket01', realName: '张三', phone: '13800138001', email: 'zhangsan@emei.com', role: 'ticket', status: '1', createTime: '2024-01-02 10:00:00' },
  { id: 3, username: 'verify01', realName: '李四', phone: '13800138002', email: 'lisi@emei.com', role: 'verify', status: '1', createTime: '2024-01-03 14:30:00' },
  { id: 4, username: 'finance01', realName: '王五', phone: '13800138003', email: 'wangwu@emei.com', role: 'finance', status: '1', createTime: '2024-01-04 09:15:00' },
  { id: 5, username: 'ticket02', realName: '赵六', phone: '13800138004', email: 'zhaoliu@emei.com', role: 'ticket', status: '0', createTime: '2024-01-05 16:45:00' }
])

pagination.total = tableData.value.length

const formData = reactive({
  id: null,
  username: '',
  realName: '',
  password: '',
  phone: '',
  email: '',
  role: '',
  status: '1'
})

const formRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const getRoleName = (role) => {
  const roleMap = {
    admin: '超级管理员',
    ticket: '票务管理员',
    verify: '核销员',
    finance: '财务统计员'
  }
  return roleMap[role] || role
}

const getRoleType = (role) => {
  const typeMap = {
    admin: 'danger',
    ticket: 'primary',
    verify: 'success',
    finance: 'warning'
  }
  return typeMap[role] || ''
}

const handleSearch = () => {
  ElMessage.success('搜索功能开发中')
}

const handleReset = () => {
  searchForm.username = ''
  searchForm.phone = ''
  searchForm.status = ''
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增用户'
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'status' ? '1' : ''
  })
  formData.id = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑用户'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const index = tableData.value.findIndex(item => item.id === formData.id)
        if (index > -1) {
          tableData.value[index] = { ...tableData.value[index], ...formData }
        }
        ElMessage.success('编辑成功')
      } else {
        formData.id = tableData.value.length + 1
        formData.createTime = new Date().toLocaleString()
        tableData.value.push({ ...formData })
        pagination.total++
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该用户吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = tableData.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      tableData.value.splice(index, 1)
      pagination.total--
    }
    ElMessage.success('删除成功')
  }).catch(() => {})
}

const handleResetPwd = (row) => {
  ElMessageBox.prompt('请输入新密码', '重置密码', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    inputPattern: /^.{6,}$/,
    inputErrorMessage: '密码长度不能少于6位'
  }).then(({ value }) => {
    ElMessage.success(`用户 ${row.username} 密码重置成功`)
  }).catch(() => {})
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
}
</script>

<style lang="scss" scoped>
.user-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.search-form {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  padding: 20px;
  border-radius: 8px;

  .pagination-container {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
