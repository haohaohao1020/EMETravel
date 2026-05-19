<template>
  <div class="dict-container">
    <div class="page-header">
      <h2 class="page-title">数据字典</h2>
      <el-button type="primary" @click="handleAddType">
        <el-icon><Plus /></el-icon>
        新增字典类型
      </el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="type-card">
          <template #header>
            <span>字典类型</span>
          </template>
          <el-tree
            ref="typeTreeRef"
            :data="dictTypeList"
            :props="{ label: 'name', children: 'children' }"
            node-key="code"
            highlight-current
            @node-click="handleTypeClick"
          />
        </el-card>
      </el-col>
      <el-col :span="18">
        <el-card class="table-card">
          <template #header>
            <div class="card-header">
              <span>字典数据 - {{ currentTypeName || '请选择字典类型' }}</span>
              <el-button type="primary" size="small" @click="handleAddData" :disabled="!currentType">
                <el-icon><Plus /></el-icon>
                新增字典数据
              </el-button>
            </div>
          </template>

          <el-table :data="dictDataList" border stripe>
            <el-table-column type="index" label="序号" width="60" />
            <el-table-column prop="label" label="字典标签" width="150" />
            <el-table-column prop="value" label="字典键值" width="150" />
            <el-table-column prop="sort" label="排序" width="100" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === '1' ? 'success' : 'danger'">
                  {{ row.status === '1' ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
            <el-table-column prop="createTime" label="创建时间" width="180" />
            <el-table-column label="操作" width="200">
              <template #default="{ row }">
                <el-button type="primary" link size="small" @click="handleEditData(row)">
                  编辑
                </el-button>
                <el-button :type="row.status === '1' ? 'info' : 'success'" link size="small" @click="handleToggleDataStatus(row)">
                  {{ row.status === '1' ? '禁用' : '启用' }}
                </el-button>
                <el-button type="danger" link size="small" @click="handleDeleteData(row)">
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
      </el-col>
    </el-row>

    <el-dialog v-model="typeDialogVisible" :title="typeDialogTitle" width="500px">
      <el-form ref="typeFormRef" :model="typeFormData" :rules="typeFormRules" label-width="100px">
        <el-form-item label="字典名称" prop="name">
          <el-input v-model="typeFormData.name" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典编码" prop="code">
          <el-input v-model="typeFormData.code" placeholder="请输入字典编码" />
        </el-form-item>
        <el-form-item label="字典状态" prop="status">
          <el-radio-group v-model="typeFormData.status">
            <el-radio value="1">启用</el-radio>
            <el-radio value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="typeFormData.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="typeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleTypeSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="dataDialogVisible" :title="dataDialogTitle" width="500px">
      <el-form ref="dataFormRef" :model="dataFormData" :rules="dataFormRules" label-width="100px">
        <el-form-item label="字典标签" prop="label">
          <el-input v-model="dataFormData.label" placeholder="请输入字典标签" />
        </el-form-item>
        <el-form-item label="字典键值" prop="value">
          <el-input v-model="dataFormData.value" placeholder="请输入字典键值" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="dataFormData.sort" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="字典状态" prop="status">
          <el-radio-group v-model="dataFormData.status">
            <el-radio value="1">启用</el-radio>
            <el-radio value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dataFormData.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dataDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleDataSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const typeTreeRef = ref(null)
const typeFormRef = ref(null)
const dataFormRef = ref(null)
const typeDialogVisible = ref(false)
const dataDialogVisible = ref(false)
const isTypeEdit = ref(false)
const isDataEdit = ref(false)
const currentType = ref(null)
const currentTypeName = ref('')

const typeDialogTitle = computed(() => isTypeEdit.value ? '编辑字典类型' : '新增字典类型')
const dataDialogTitle = computed(() => isDataEdit.value ? '编辑字典数据' : '新增字典数据')

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const dictTypeList = ref([
  {
    name: '通用状态',
    code: 'common_status',
    status: '1',
    children: []
  },
  {
    name: '票种类型',
    code: 'ticket_type',
    status: '1',
    children: []
  },
  {
    name: '订单状态',
    code: 'order_status',
    status: '1',
    children: []
  },
  {
    name: '支付方式',
    code: 'pay_type',
    status: '1',
    children: []
  },
  {
    name: '景区区域',
    code: 'scenic_area',
    status: '1',
    children: []
  }
])

const dictDataList = ref([])

const typeFormData = reactive({
  id: null,
  name: '',
  code: '',
  status: '1',
  remark: ''
})

const typeFormRules = {
  name: [{ required: true, message: '请输入字典名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入字典编码', trigger: 'blur' }]
}

const dataFormData = reactive({
  id: null,
  typeCode: '',
  label: '',
  value: '',
  sort: 0,
  status: '1',
  remark: ''
})

const dataFormRules = {
  label: [{ required: true, message: '请输入字典标签', trigger: 'blur' }],
  value: [{ required: true, message: '请输入字典键值', trigger: 'blur' }]
}

const handleTypeClick = (data) => {
  currentType.value = data.code
  currentTypeName.value = data.name
  loadDictData(data.code)
}

const loadDictData = (typeCode) => {
  const mockData = {
    common_status: [
      { id: 1, label: '启用', value: '1', sort: 1, status: '1', remark: '', createTime: '2024-01-01 00:00:00' },
      { id: 2, label: '禁用', value: '0', sort: 2, status: '1', remark: '', createTime: '2024-01-01 00:00:00' }
    ],
    ticket_type: [
      { id: 3, label: '大门票', value: 'gate', sort: 1, status: '1', remark: '', createTime: '2024-01-01 00:00:00' },
      { id: 4, label: '索道票', value: 'cable', sort: 2, status: '1', remark: '', createTime: '2024-01-01 00:00:00' },
      { id: 5, label: '观光车票', value: 'bus', sort: 3, status: '1', remark: '', createTime: '2024-01-01 00:00:00' }
    ],
    order_status: [
      { id: 6, label: '待支付', value: 'pending', sort: 1, status: '1', remark: '', createTime: '2024-01-01 00:00:00' },
      { id: 7, label: '已支付', value: 'paid', sort: 2, status: '1', remark: '', createTime: '2024-01-01 00:00:00' },
      { id: 8, label: '已核销', value: 'verified', sort: 3, status: '1', remark: '', createTime: '2024-01-01 00:00:00' },
      { id: 9, label: '已退款', value: 'refunded', sort: 4, status: '1', remark: '', createTime: '2024-01-01 00:00:00' },
      { id: 10, label: '已取消', value: 'cancelled', sort: 5, status: '1', remark: '', createTime: '2024-01-01 00:00:00' }
    ],
    pay_type: [
      { id: 11, label: '微信支付', value: 'wechat', sort: 1, status: '1', remark: '', createTime: '2024-01-01 00:00:00' },
      { id: 12, label: '支付宝', value: 'alipay', sort: 2, status: '1', remark: '', createTime: '2024-01-01 00:00:00' },
      { id: 13, label: '银联', value: 'unionpay', sort: 3, status: '1', remark: '', createTime: '2024-01-01 00:00:00' }
    ],
    scenic_area: [
      { id: 14, label: '高山区', value: 'high', sort: 1, status: '1', remark: '', createTime: '2024-01-01 00:00:00' },
      { id: 15, label: '低山区', value: 'low', sort: 2, status: '1', remark: '', createTime: '2024-01-01 00:00:00' }
    ]
  }
  dictDataList.value = mockData[typeCode] || []
  pagination.total = dictDataList.value.length
}

const handleAddType = () => {
  isTypeEdit.value = false
  Object.keys(typeFormData).forEach(key => {
    typeFormData[key] = key === 'status' ? '1' : ''
  })
  typeFormData.id = null
  typeDialogVisible.value = true
}

const handleAddData = () => {
  isDataEdit.value = false
  Object.keys(dataFormData).forEach(key => {
    dataFormData[key] = key === 'sort' ? 0 : key === 'status' ? '1' : ''
  })
  dataFormData.id = null
  dataFormData.typeCode = currentType.value
  dataDialogVisible.value = true
}

const handleEditData = (row) => {
  isDataEdit.value = true
  Object.assign(dataFormData, row)
  dataDialogVisible.value = true
}

const handleToggleDataStatus = (row) => {
  const action = row.status === '1' ? '禁用' : '启用'
  ElMessageBox.confirm(`确定要${action}该字典数据吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = row.status === '1' ? '0' : '1'
    ElMessage.success(`${action}成功`)
  }).catch(() => {})
}

const handleDeleteData = (row) => {
  ElMessageBox.confirm('确定要删除该字典数据吗？删除后将无法恢复。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = dictDataList.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      dictDataList.value.splice(index, 1)
    }
    ElMessage.success('删除成功')
  }).catch(() => {})
}

const handleTypeSubmit = () => {
  typeFormRef.value?.validate((valid) => {
    if (valid) {
      if (isTypeEdit.value) {
        ElMessage.success('编辑成功')
      } else {
        dictTypeList.value.push({
          name: typeFormData.name,
          code: typeFormData.code,
          status: typeFormData.status,
          children: []
        })
        ElMessage.success('新增成功')
      }
      typeDialogVisible.value = false
    }
  })
}

const handleDataSubmit = () => {
  dataFormRef.value?.validate((valid) => {
    if (valid) {
      if (isDataEdit.value) {
        const index = dictDataList.value.findIndex(item => item.id === dataFormData.id)
        if (index > -1) {
          dictDataList.value[index] = { ...dictDataList.value[index], ...dataFormData }
        }
        ElMessage.success('编辑成功')
      } else {
        dataFormData.id = dictDataList.value.length + 1
        dataFormData.createTime = new Date().toLocaleString()
        dictDataList.value.push({ ...dataFormData })
        ElMessage.success('新增成功')
      }
      dataDialogVisible.value = false
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
.dict-container {
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

  .type-card {
    .el-tree {
      height: calc(100vh - 200px);
      overflow-y: auto;
    }
  }

  .table-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .pagination-container {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
    }
  }
}
</style>
