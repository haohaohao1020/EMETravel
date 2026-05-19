<template>
  <div class="strategy-container">
    <div class="page-header">
      <h2 class="page-title">攻略管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增攻略
      </el-button>
    </div>

    <el-card class="table-card">
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="攻略标题">
            <el-input v-model="searchForm.title" placeholder="请输入攻略标题" clearable style="width: 200px" />
          </el-form-item>
          <el-form-item label="发布状态">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable>
              <el-option label="已发布" value="1" />
              <el-option label="草稿" value="0" />
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

      <el-table :data="strategyList" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="title" label="攻略标题" min-width="200" />
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="category" label="分类" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="views" label="浏览量" width="100" />
        <el-table-column prop="likes" label="点赞数" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '1' ? 'success' : 'info'">
              {{ row.status === '1' ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="publishTime" label="发布时间" width="180" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button :type="row.status === '1' ? 'warning' : 'success'" link size="small" @click="handleToggleStatus(row)">
              {{ row.status === '1' ? '下架' : '发布' }}
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
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const searchForm = reactive({
  title: '',
  status: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const strategyList = ref([
  { id: 1, title: '峨眉山一日游经典路线攻略', author: 'admin', category: '一日游', views: 12580, likes: 856, status: '1', publishTime: '2024-05-01 10:00:00' },
  { id: 2, title: '金顶日出最佳观赏时间指南', author: 'admin', category: '景点推荐', views: 8960, likes: 623, status: '1', publishTime: '2024-04-28 14:30:00' },
  { id: 3, title: '峨眉山徒步登山注意事项', author: 'admin', category: '实用指南', views: 6580, likes: 456, status: '1', publishTime: '2024-04-25 09:15:00' },
  { id: 4, title: '峨眉山佛教文化深度游', author: 'admin', category: '文化游', views: 5230, likes: 389, status: '1', publishTime: '2024-04-20 16:20:00' },
  { id: 5, title: '峨眉山四季风光摄影指南', author: 'admin', category: '摄影攻略', views: 7890, likes: 568, status: '0', publishTime: '-' }
])

pagination.total = strategyList.value.length

const handleSearch = () => {
  ElMessage.success('搜索功能开发中')
}

const handleReset = () => {
  searchForm.title = ''
  searchForm.status = ''
}

const handleAdd = () => {
  ElMessage.info('新增功能开发中')
}

const handleEdit = (row) => {
  ElMessage.info('编辑功能开发中')
}

const handleToggleStatus = (row) => {
  const action = row.status === '1' ? '下架' : '发布'
  ElMessageBox.confirm(`确定要${action}该攻略吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = row.status === '1' ? '0' : '1'
    ElMessage.success(`${action}成功`)
  }).catch(() => {})
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该攻略吗？删除后将无法恢复。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = strategyList.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      strategyList.value.splice(index, 1)
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
.strategy-container {
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
