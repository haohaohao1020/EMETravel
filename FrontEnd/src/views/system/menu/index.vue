<template>
  <div class="menu-container">
    <div class="page-header">
      <h2 class="page-title">菜单管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增菜单
      </el-button>
    </div>

    <el-card class="table-card">
      <el-table :data="menuData" border stripe row-key="id" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
        <el-table-column prop="name" label="菜单名称" min-width="200" />
        <el-table-column prop="icon" label="图标" width="80">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路由地址" min-width="200" />
        <el-table-column prop="component" label="组件路径" min-width="200" />
        <el-table-column prop="permission" label="权限标识" width="150" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '1' ? 'success' : 'danger'">
              {{ row.status === '1' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="success" link size="small" @click="handleAddChild(row)">
              新增子菜单
            </el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="上级菜单" prop="parentId">
          <el-tree-select
            v-model="formData.parentId"
            :data="menuTreeData"
            :props="{ label: 'name', value: 'id', children: 'children' }"
            check-strictly
            placeholder="请选择上级菜单"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="formData.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item label="路由地址" prop="path">
          <el-input v-model="formData.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="formData.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item label="权限标识" prop="permission">
          <el-input v-model="formData.permission" placeholder="请输入权限标识" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="formData.sort" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="菜单状态" prop="status">
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
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const formRef = ref(null)
const dialogVisible = ref(false)
const isEdit = ref(false)
const isAddChild = ref(false)
const dialogTitle = computed(() => {
  if (isEdit.value) return '编辑菜单'
  if (isAddChild.value) return '新增子菜单'
  return '新增菜单'
})

const menuData = ref([
  {
    id: 1,
    name: '数据驾驶舱',
    icon: 'DataAnalysis',
    path: '/dashboard',
    component: 'dashboard/index',
    permission: 'dashboard:view',
    sort: 1,
    status: '1',
    children: []
  },
  {
    id: 2,
    name: '系统管理',
    icon: 'Setting',
    path: '/system',
    component: '',
    permission: '',
    sort: 2,
    status: '1',
    children: [
      { id: 21, name: '用户管理', icon: 'User', path: '/system/user', component: 'system/user/index', permission: 'system:user:list', sort: 1, status: '1' },
      { id: 22, name: '角色管理', icon: 'UserFilled', path: '/system/role', component: 'system/role/index', permission: 'system:role:list', sort: 2, status: '1' },
      { id: 23, name: '菜单管理', icon: 'Menu', path: '/system/menu', component: 'system/menu/index', permission: 'system:menu:list', sort: 3, status: '1' },
      { id: 24, name: '操作日志', icon: 'Document', path: '/system/log', component: 'system/log/index', permission: 'system:log:list', sort: 4, status: '1' }
    ]
  },
  {
    id: 3,
    name: '景点管理',
    icon: 'Location',
    path: '/scenic',
    component: '',
    permission: '',
    sort: 3,
    status: '1',
    children: [
      { id: 31, name: '景点管理', icon: 'Picture', path: '/scenic/spot', component: 'scenic/spot/index', permission: 'scenic:spot:list', sort: 1, status: '1' },
      { id: 32, name: '路线管理', icon: 'Guide', path: '/scenic/route', component: 'scenic/route/index', permission: 'scenic:route:list', sort: 2, status: '1' }
    ]
  },
  {
    id: 4,
    name: '票务管理',
    icon: 'Ticket',
    path: '/ticket',
    component: '',
    permission: '',
    sort: 4,
    status: '1',
    children: [
      { id: 41, name: '票务管理', icon: 'Tickets', path: '/ticket/list', component: 'ticket/list/index', permission: 'ticket:list', sort: 1, status: '1' },
      { id: 42, name: '库存管理', icon: 'Box', path: '/ticket/stock', component: 'ticket/stock/index', permission: 'ticket:stock:list', sort: 2, status: '1' }
    ]
  }
])

const menuTreeData = ref([
  { id: 0, name: '顶级菜单', children: menuData.value }
])

const formData = reactive({
  id: null,
  parentId: 0,
  name: '',
  icon: '',
  path: '',
  component: '',
  permission: '',
  sort: 1,
  status: '1'
})

const formRules = {
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  path: [{ required: true, message: '请输入路由地址', trigger: 'blur' }]
}

const handleAdd = () => {
  isEdit.value = false
  isAddChild.value = false
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'sort' ? 1 : key === 'status' ? '1' : key === 'parentId' ? 0 : ''
  })
  formData.id = null
  dialogVisible.value = true
}

const handleAddChild = (row) => {
  isEdit.value = false
  isAddChild.value = true
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'sort' ? 1 : key === 'status' ? '1' : ''
  })
  formData.id = null
  formData.parentId = row.id
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  isAddChild.value = false
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该菜单吗？删除后将无法恢复。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const deleteFromTree = (items) => {
      for (let i = 0; i < items.length; i++) {
        if (items[i].id === row.id) {
          items.splice(i, 1)
          return true
        }
        if (items[i].children && items[i].children.length > 0) {
          if (deleteFromTree(items[i].children)) {
            return true
          }
        }
      }
      return false
    }
    deleteFromTree(menuData.value)
    ElMessage.success('删除成功')
  }).catch(() => {})
}

const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const updateInTree = (items) => {
          for (let i = 0; i < items.length; i++) {
            if (items[i].id === formData.id) {
              items[i] = { ...items[i], ...formData }
              return true
            }
            if (items[i].children && items[i].children.length > 0) {
              if (updateInTree(items[i].children)) {
                return true
              }
            }
          }
          return false
        }
        updateInTree(menuData.value)
        ElMessage.success('编辑成功')
      } else {
        const newId = Date.now()
        formData.id = newId
        if (formData.parentId === 0) {
          menuData.value.push({ ...formData, children: [] })
        } else {
          const addChild = (items) => {
            for (let i = 0; i < items.length; i++) {
              if (items[i].id === formData.parentId) {
                if (!items[i].children) {
                  items[i].children = []
                }
                items[i].children.push({ ...formData })
                return true
              }
              if (items[i].children && items[i].children.length > 0) {
                if (addChild(items[i].children)) {
                  return true
                }
              }
            }
            return false
          }
          addChild(menuData.value)
        }
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
    }
  })
}
</script>

<style lang="scss" scoped>
.menu-container {
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
    .pagination-container {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
  }
}
</style>
