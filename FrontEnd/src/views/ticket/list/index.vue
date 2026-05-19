<template>
  <div class="ticket-container">
    <div class="page-header">
      <h2 class="page-title">票务管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增票种
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="票种名称">
          <el-input v-model="searchForm.name" placeholder="请输入票种名称" clearable />
        </el-form-item>
        <el-form-item label="票务类型">
          <el-select v-model="searchForm.type" placeholder="请选择类型" clearable>
            <el-option label="大门票" value="gate" />
            <el-option label="索道票" value="cable" />
            <el-option label="观光车票" value="bus" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="上架" value="1" />
            <el-option label="下架" value="0" />
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
        <el-table-column prop="name" label="票种名称" min-width="150" />
        <el-table-column prop="type" label="票务类型" width="120">
          <template #default="{ row }">
            <el-tag :type="getTicketType(row.type)">{{ getTicketTypeName(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="票价(元)" width="100" />
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="useRule" label="使用规则" min-width="200" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '1' ? 'success' : 'info'">
              {{ row.status === '1' ? '上架' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button :type="row.status === '1' ? 'warning' : 'success'" link size="small" @click="handleToggleStatus(row)">
              <el-icon><SwitchButton /></el-icon>
              {{ row.status === '1' ? '下架' : '上架' }}
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
        <el-form-item label="票种名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入票种名称" />
        </el-form-item>
        <el-form-item label="票务类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="大门票" value="gate" />
            <el-option label="索道票" value="cable" />
            <el-option label="观光车票" value="bus" />
          </el-select>
        </el-form-item>
        <el-form-item label="票价" prop="price">
          <el-input-number v-model="formData.price" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="formData.stock" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="使用规则" prop="useRule">
          <el-input v-model="formData.useRule" type="textarea" :rows="3" placeholder="请输入使用规则" />
        </el-form-item>
        <el-form-item label="退改手续费" prop="refundFee">
          <el-input-number v-model="formData.refundFee" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio value="1">上架</el-radio>
            <el-radio value="0">下架</el-radio>
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
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const formRef = ref(null)
const dialogVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = ref('新增票种')

const searchForm = reactive({
  name: '',
  type: '',
  status: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([
  { id: 1, name: '大门票-成人票', type: 'gate', price: 160.00, stock: 9999, useRule: '当日有效，限使用一次', status: '1', createTime: '2024-01-01 00:00:00', refundFee: 10 },
  { id: 2, name: '大门票-学生票', type: 'gate', price: 80.00, stock: 9999, useRule: '需持学生证验证，当日有效', status: '1', createTime: '2024-01-01 00:00:00', refundFee: 5 },
  { id: 3, name: '大门票-老人票', type: 'gate', price: 0.00, stock: 9999, useRule: '60岁以上免费，需持身份证', status: '1', createTime: '2024-01-01 00:00:00', refundFee: 0 },
  { id: 4, name: '索道票-上行', type: 'cable', price: 65.00, stock: 1000, useRule: '接引殿至金顶上行，当日有效', status: '1', createTime: '2024-01-01 00:00:00', refundFee: 5 },
  { id: 5, name: '索道票-下行', type: 'cable', price: 55.00, stock: 1000, useRule: '金顶至接引殿下行，当日有效', status: '1', createTime: '2024-01-01 00:00:00', refundFee: 5 },
  { id: 6, name: '观光车-单程', type: 'bus', price: 40.00, stock: 9999, useRule: '黄湾至雷洞坪单程', status: '1', createTime: '2024-01-01 00:00:00', refundFee: 5 },
  { id: 7, name: '观光车-往返', type: 'bus', price: 70.00, stock: 9999, useRule: '黄湾至雷洞坪往返，当日有效', status: '1', createTime: '2024-01-01 00:00:00', refundFee: 8 }
])

pagination.total = tableData.value.length

const formData = reactive({
  id: null,
  name: '',
  type: '',
  price: 0,
  stock: 0,
  useRule: '',
  refundFee: 0,
  status: '1'
})

const formRules = {
  name: [{ required: true, message: '请输入票种名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择票务类型', trigger: 'change' }],
  price: [{ required: true, message: '请输入票价', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }]
}

const getTicketTypeName = (type) => {
  const typeMap = {
    gate: '大门票',
    cable: '索道票',
    bus: '观光车票'
  }
  return typeMap[type] || type
}

const getTicketType = (type) => {
  const typeMap = {
    gate: 'primary',
    cable: 'success',
    bus: 'warning'
  }
  return typeMap[type] || ''
}

const handleSearch = () => {
  ElMessage.success('搜索功能开发中')
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.type = ''
  searchForm.status = ''
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增票种'
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'status' ? '1' : key === 'price' || key === 'stock' || key === 'refundFee' ? 0 : ''
  })
  formData.id = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑票种'
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
  ElMessageBox.confirm('确定要删除该票种吗?', '提示', {
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

const handleToggleStatus = (row) => {
  const newStatus = row.status === '1' ? '0' : '1'
  const action = newStatus === '1' ? '上架' : '下架'
  ElMessageBox.confirm(`确定要${action}该票种吗?`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = newStatus
    ElMessage.success(`${action}成功`)
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
.ticket-container {
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
