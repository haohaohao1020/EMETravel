<template>
  <div class="scenic-container">
    <div class="page-header">
      <h2 class="page-title">景点管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增景点
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="景点名称">
          <el-input v-model="searchForm.name" placeholder="请输入景点名称" clearable />
        </el-form-item>
        <el-form-item label="所属区域">
          <el-select v-model="searchForm.area" placeholder="请选择区域" clearable>
            <el-option label="高山区" value="high" />
            <el-option label="低山区" value="low" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="开放" value="open" />
            <el-option label="限流" value="limit" />
            <el-option label="维护" value="maintain" />
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
        <el-table-column prop="name" label="景点名称" min-width="120" />
        <el-table-column prop="area" label="所属区域" width="100">
          <template #default="{ row }">
            <el-tag :type="row.area === 'high' ? 'primary' : 'success'">
              {{ row.area === 'high' ? '高山区' : '低山区' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="简介" min-width="200" show-overflow-tooltip />
        <el-table-column prop="visitorLimit" label="承载人数" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.status === 'open'" type="success">开放</el-tag>
            <el-tag v-else-if="row.status === 'limit'" type="warning">限流</el-tag>
            <el-tag v-else type="danger">维护</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="更新时间" min-width="160" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="success" link size="small" @click="handleView(row)">
              <el-icon><View /></el-icon>
              详情
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
          :page-sizes="[10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="景点名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入景点名称" />
        </el-form-item>
        <el-form-item label="所属区域" prop="area">
          <el-select v-model="formData.area" placeholder="请选择区域" style="width: 100%">
            <el-option label="高山区" value="high" />
            <el-option label="低山区" value="low" />
          </el-select>
        </el-form-item>
        <el-form-item label="景点简介" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入景点简介" />
        </el-form-item>
        <el-form-item label="佛教文化介绍" prop="cultureIntro">
          <el-input v-model="formData.cultureIntro" type="textarea" :rows="4" placeholder="请输入佛教文化介绍" />
        </el-form-item>
        <el-form-item label="安全须知" prop="safetyNotice">
          <el-input v-model="formData.safetyNotice" type="textarea" :rows="4" placeholder="请输入安全须知" />
        </el-form-item>
        <el-form-item label="最大承载人数" prop="visitorLimit">
          <el-input-number v-model="formData.visitorLimit" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio value="open">开放</el-radio>
            <el-radio value="limit">限流</el-radio>
            <el-radio value="maintain">维护</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailVisible" title="景点详情" width="800px">
      <el-descriptions :column="2" border v-if="currentSpot">
        <el-descriptions-item label="景点名称">{{ currentSpot.name }}</el-descriptions-item>
        <el-descriptions-item label="所属区域">
          <el-tag>{{ currentSpot.area === 'high' ? '高山区' : '低山区' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="最大承载人数">{{ currentSpot.visitorLimit }}人</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag v-if="currentSpot.status === 'open'" type="success">开放</el-tag>
          <el-tag v-else-if="currentSpot.status === 'limit'" type="warning">限流</el-tag>
          <el-tag v-else type="danger">维护</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="景点简介" :span="2">{{ currentSpot.description }}</el-descriptions-item>
        <el-descriptions-item label="佛教文化介绍" :span="2">{{ currentSpot.cultureIntro }}</el-descriptions-item>
        <el-descriptions-item label="安全须知" :span="2">{{ currentSpot.safetyNotice }}</el-descriptions-item>
        <el-descriptions-item label="更新时间" :span="2">{{ currentSpot.updateTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const formRef = ref(null)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = ref('新增景点')
const currentSpot = ref(null)

const searchForm = reactive({
  name: '',
  area: '',
  status: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([
  { id: 1, name: '金顶', area: 'high', description: '峨眉山主峰，海拔3077米，是峨眉山的象征', cultureIntro: '金顶是峨眉山佛教文化的核心区域，有华藏寺、十方普贤等著名佛教建筑', safetyNotice: '高海拔地区，请注意高原反应，雷雨天气请勿在户外逗留', visitorLimit: 5000, status: 'open', updateTime: '2024-05-01 08:00:00' },
  { id: 2, name: '接引殿', area: 'high', description: '索道上站，前往金顶的必经之地', cultureIntro: '接引殿是峨眉山古刹之一，始建于宋代', safetyNotice: '注意台阶，小心滑倒', visitorLimit: 2000, status: 'open', updateTime: '2024-05-01 08:30:00' },
  { id: 3, name: '洗象池', area: 'high', description: '传说普贤菩萨曾在此洗象', cultureIntro: '洗象池是峨眉山著名古刹，有象池夜月等景观', safetyNotice: '夜间山路较暗，注意安全', visitorLimit: 1000, status: 'open', updateTime: '2024-05-01 09:00:00' },
  { id: 4, name: '报国寺', area: 'low', description: '峨眉山入山门户，峨眉山第一景', cultureIntro: '报国寺是峨眉山最大的寺院，始建于明代', safetyNotice: '寺院区域请保持安静，尊重宗教习俗', visitorLimit: 3000, status: 'open', updateTime: '2024-05-01 09:30:00' },
  { id: 5, name: '伏虎寺', area: 'low', description: '峨眉山著名古刹，以园林清幽著称', cultureIntro: '伏虎寺始建于唐代，有布金林等著名景观', safetyNotice: '注意蚊虫叮咬', visitorLimit: 1500, status: 'limit', updateTime: '2024-05-01 10:00:00' }
])

pagination.total = tableData.value.length

const formData = reactive({
  id: null,
  name: '',
  area: '',
  description: '',
  cultureIntro: '',
  safetyNotice: '',
  visitorLimit: 0,
  status: 'open'
})

const formRules = {
  name: [{ required: true, message: '请输入景点名称', trigger: 'blur' }],
  area: [{ required: true, message: '请选择所属区域', trigger: 'change' }],
  description: [{ required: true, message: '请输入景点简介', trigger: 'blur' }],
  visitorLimit: [{ required: true, message: '请输入最大承载人数', trigger: 'blur' }]
}

const handleSearch = () => {
  ElMessage.success('搜索功能开发中')
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.area = ''
  searchForm.status = ''
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增景点'
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'status' ? 'open' : key === 'visitorLimit' ? 0 : ''
  })
  formData.id = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑景点'
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleView = (row) => {
  currentSpot.value = row
  detailVisible.value = true
}

const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const index = tableData.value.findIndex(item => item.id === formData.id)
        if (index > -1) {
          tableData.value[index] = { ...tableData.value[index], ...formData, updateTime: new Date().toLocaleString() }
        }
        ElMessage.success('编辑成功')
      } else {
        formData.id = tableData.value.length + 1
        formData.updateTime = new Date().toLocaleString()
        tableData.value.push({ ...formData })
        pagination.total++
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该景点吗?', '提示', {
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

const handleSizeChange = (val) => {
  pagination.pageSize = val
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
}
</script>

<style lang="scss" scoped>
.scenic-container {
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
