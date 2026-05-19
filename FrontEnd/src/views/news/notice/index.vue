<template>
  <div class="notice-container">
    <div class="page-header">
      <h2 class="page-title">公告管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        发布公告
      </el-button>
    </div>

    <div class="search-form">
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="公告标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable />
        </el-form-item>
        <el-form-item label="发布状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="已发布" value="published" />
            <el-option label="草稿" value="draft" />
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
        <el-table-column prop="title" label="公告标题" min-width="200" />
        <el-table-column prop="type" label="公告类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === 'important' ? 'danger' : 'primary'">
              {{ row.type === 'important' ? '重要' : '普通' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'published' ? 'success' : 'info'">
              {{ row.status === 'published' ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publisher" label="发布人" width="100" />
        <el-table-column prop="publishTime" label="发布时间" min-width="160" />
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="success" link size="small" @click="handleView(row)">
              <el-icon><View /></el-icon>
              预览
            </el-button>
            <el-button v-if="row.status === 'draft'" type="warning" link size="small" @click="handlePublish(row)">
              <el-icon><Promotion /></el-icon>
              发布
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="900px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入公告标题" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="公告类型" prop="type">
          <el-radio-group v-model="formData.type">
            <el-radio value="normal">普通公告</el-radio>
            <el-radio value="important">重要公告</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input
            v-model="formData.content"
            type="textarea"
            :rows="12"
            placeholder="请输入公告内容，支持HTML格式"
          />
        </el-form-item>
        <el-form-item label="是否立即发布" prop="publishNow">
          <el-switch v-model="formData.publishNow" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="previewVisible" title="公告预览" width="800px">
      <div v-if="previewData" class="preview-content">
        <h2 class="preview-title">{{ previewData.title }}</h2>
        <div class="preview-meta">
          <span>发布人：{{ previewData.publisher }}</span>
          <span>发布时间：{{ previewData.publishTime }}</span>
          <el-tag :type="previewData.type === 'important' ? 'danger' : 'primary'">
            {{ previewData.type === 'important' ? '重要公告' : '普通公告' }}
          </el-tag>
        </div>
        <div class="preview-body" v-html="previewData.content"></div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const formRef = ref(null)
const dialogVisible = ref(false)
const previewVisible = ref(false)
const isEdit = ref(false)
const dialogTitle = ref('发布公告')
const previewData = ref(null)

const searchForm = reactive({
  title: '',
  status: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const tableData = ref([
  { id: 1, title: '关于2024年五一假期景区开放安排的公告', type: 'important', status: 'published', publisher: '管理员', publishTime: '2024-04-25 10:00:00', viewCount: 1256, content: '<p>各位游客：</p><p>2024年五一假期期间，峨眉山景区将正常开放...</p>' },
  { id: 2, title: '索道设备维护停运通知', type: 'important', status: 'published', publisher: '管理员', publishTime: '2024-04-20 14:30:00', viewCount: 892, content: '<p>各位游客：</p><p>为保障索道安全运行，接引殿索道将于...</p>' },
  { id: 3, title: '关于景区观光车运营时间调整的通知', type: 'normal', status: 'published', publisher: '管理员', publishTime: '2024-04-15 09:00:00', viewCount: 567, content: '<p>各位游客：</p><p>自2024年4月20日起，观光车运营时间调整为...</p>' },
  { id: 4, title: '景区门票价格调整公告', type: 'important', status: 'draft', publisher: '管理员', publishTime: '-', viewCount: 0, content: '<p>各位游客：</p><p>根据相关规定，景区门票价格调整为...</p>' },
  { id: 5, title: '关于金顶区域游客承载量限制的通知', type: 'normal', status: 'published', publisher: '管理员', publishTime: '2024-04-10 16:20:00', viewCount: 423, content: '<p>各位游客：</p><p>为保障游览安全，金顶区域将实施承载量限制...</p>' }
])

pagination.total = tableData.value.length

const formData = reactive({
  id: null,
  title: '',
  type: 'normal',
  content: '',
  publishNow: true
})

const formRules = {
  title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
  type: [{ required: true, message: '请选择公告类型', trigger: 'change' }],
  content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }]
}

const handleSearch = () => {
  ElMessage.success('搜索功能开发中')
}

const handleReset = () => {
  searchForm.title = ''
  searchForm.status = ''
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '发布公告'
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'type' ? 'normal' : key === 'publishNow' ? true : ''
  })
  formData.id = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑公告'
  Object.assign(formData, row)
  formData.publishNow = row.status === 'published'
  dialogVisible.value = true
}

const handleView = (row) => {
  previewData.value = row
  previewVisible.value = true
}

const handlePublish = (row) => {
  ElMessageBox.confirm('确定要发布该公告吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = 'published'
    row.publishTime = new Date().toLocaleString()
    ElMessage.success('发布成功')
  }).catch(() => {})
}

const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const index = tableData.value.findIndex(item => item.id === formData.id)
        if (index > -1) {
          tableData.value[index] = {
            ...tableData.value[index],
            title: formData.title,
            type: formData.type,
            content: formData.content,
            status: formData.publishNow ? 'published' : 'draft',
            publishTime: formData.publishNow ? new Date().toLocaleString() : '-'
          }
        }
        ElMessage.success('编辑成功')
      } else {
        tableData.value.unshift({
          id: tableData.value.length + 1,
          title: formData.title,
          type: formData.type,
          content: formData.content,
          status: formData.publishNow ? 'published' : 'draft',
          publisher: '当前操作员',
          publishTime: formData.publishNow ? new Date().toLocaleString() : '-',
          viewCount: 0
        })
        pagination.total++
        ElMessage.success('发布成功')
      }
      dialogVisible.value = false
    }
  })
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该公告吗?', '提示', {
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
.notice-container {
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

.preview-content {
  .preview-title {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    margin-bottom: 20px;
    color: #333;
  }

  .preview-meta {
    text-align: center;
    padding-bottom: 20px;
    border-bottom: 1px solid #eee;
    margin-bottom: 20px;
    color: #999;
    font-size: 14px;

    span {
      margin: 0 10px;
    }
  }

  .preview-body {
    line-height: 1.8;
    color: #333;
    font-size: 14px;

    :deep(p) {
      margin-bottom: 15px;
      text-indent: 2em;
    }
  }
}
</style>
