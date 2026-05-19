<template>
  <div class="role-container">
    <div class="page-header">
      <h2 class="page-title">角色管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增角色
      </el-button>
    </div>

    <el-card class="table-card">
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="角色名称">
            <el-input v-model="searchForm.name" placeholder="请输入角色名称" clearable />
          </el-form-item>
          <el-form-item label="角色状态">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable>
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

      <el-table :data="roleList" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="角色名称" width="150" />
        <el-table-column prop="code" label="角色编码" width="150" />
        <el-table-column prop="description" label="角色描述" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '1' ? 'success' : 'danger'">
              {{ row.status === '1' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="warning" link size="small" @click="handlePermission(row)">
              分配权限
            </el-button>
            <el-button :type="row.status === '1' ? 'info' : 'success'" link size="small" @click="handleToggleStatus(row)">
              {{ row.status === '1' ? '禁用' : '启用' }}
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="formData.code" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入角色描述" />
        </el-form-item>
        <el-form-item label="角色状态" prop="status">
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

    <el-dialog v-model="permissionVisible" title="分配权限" width="500px">
      <el-tree
        ref="treeRef"
        :data="permissionTree"
        :props="{ label: 'label', children: 'children' }"
        show-checkbox
        node-key="id"
        v-model:checked-keys="checkedKeys"
        default-expand-all
      />
      <template #footer>
        <el-button @click="permissionVisible = false">取消</el-button>
        <el-button type="primary" @click="handlePermissionSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const formRef = ref(null)
const treeRef = ref(null)
const dialogVisible = ref(false)
const permissionVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = computed(() => isEdit.value ? '编辑角色' : '新增角色')

const searchForm = reactive({
  name: '',
  status: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const roleList = ref([
  { id: 1, name: '超级管理员', code: 'super_admin', description: '拥有系统所有权限', status: '1', createTime: '2024-01-01 00:00:00' },
  { id: 2, name: '票务管理员', code: 'ticket_admin', description: '负责票务相关管理', status: '1', createTime: '2024-01-02 10:00:00' },
  { id: 3, name: '核销员', code: 'verifier', description: '负责门票核销', status: '1', createTime: '2024-01-03 14:00:00' },
  { id: 4, name: '财务统计员', code: 'finance', description: '负责财务统计', status: '1', createTime: '2024-01-04 16:00:00' },
  { id: 5, name: '景区运营', code: 'operation', description: '负责景区日常运营', status: '0', createTime: '2024-01-05 09:00:00' }
])

pagination.total = roleList.value.length

const formData = reactive({
  id: null,
  name: '',
  code: '',
  description: '',
  status: '1'
})

const formRules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

const permissionTree = ref([
  {
    id: '1',
    label: '数据驾驶舱',
    children: [
      { id: '1-1', label: '查看统计' },
      { id: '1-2', label: '导出报表' }
    ]
  },
  {
    id: '2',
    label: '权限与日志',
    children: [
      { id: '2-1', label: '用户管理' },
      { id: '2-2', label: '角色管理' },
      { id: '2-3', label: '操作日志' }
    ]
  },
  {
    id: '3',
    label: '景点与路线',
    children: [
      { id: '3-1', label: '景点管理' },
      { id: '3-2', label: '路线管理' }
    ]
  },
  {
    id: '4',
    label: '票务管理',
    children: [
      { id: '4-1', label: '票种管理' },
      { id: '4-2', label: '库存管理' }
    ]
  },
  {
    id: '5',
    label: '订单与核销',
    children: [
      { id: '5-1', label: '订单管理' },
      { id: '5-2', label: '门票核销' }
    ]
  },
  {
    id: '6',
    label: '客流与资讯',
    children: [
      { id: '6-1', label: '预约配置' },
      { id: '6-2', label: '公告管理' }
    ]
  },
  {
    id: '7',
    label: '财务与配置',
    children: [
      { id: '7-1', label: '营收统计' },
      { id: '7-2', label: '系统配置' }
    ]
  }
])

const checkedKeys = ref([])

const handleSearch = () => {
  ElMessage.success('搜索功能开发中')
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.status = ''
}

const handleAdd = () => {
  isEdit.value = false
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'status' ? '1' : ''
  })
  formData.id = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handlePermission = (row) => {
  checkedKeys.value = ['1-1', '2-1', '3-1']
  permissionVisible.value = true
}

const handlePermissionSubmit = () => {
  ElMessage.success('权限分配成功')
  permissionVisible.value = false
}

const handleToggleStatus = (row) => {
  const action = row.status === '1' ? '禁用' : '启用'
  ElMessageBox.confirm(`确定要${action}该角色吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = row.status === '1' ? '0' : '1'
    ElMessage.success(`${action}成功`)
  }).catch(() => {})
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该角色吗？删除后将无法恢复。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = roleList.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      roleList.value.splice(index, 1)
    }
    ElMessage.success('删除成功')
  }).catch(() => {})
}

const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const index = roleList.value.findIndex(item => item.id === formData.id)
        if (index > -1) {
          roleList.value[index] = { ...roleList.value[index], ...formData }
        }
        ElMessage.success('编辑成功')
      } else {
        formData.id = roleList.value.length + 1
        formData.createTime = new Date().toLocaleString()
        roleList.value.push({ ...formData })
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
    }
  })
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
}
</script>

<style lang="scss" scoped>
.role-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .page-title {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
      margin: 0;
    }
  }

  .table-card {
    .search-form {
      background: #f5f7fa;
      padding: 15px;
      border-radius: 4px;
      margin-bottom: 20px;
    }

    .pagination-container {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
  }
}
</style>
