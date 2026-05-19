<template>
  <div class="feedback-container">
    <div class="page-header">
      <h2 class="page-title">游客反馈</h2>
    </div>

    <el-row :gutter="20" class="stats-row">
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ totalFeedback }}</div>
          <div class="stat-label">总反馈数</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ pendingFeedback }}</div>
          <div class="stat-label">待处理</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ processedFeedback }}</div>
          <div class="stat-label">已处理</div>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6">
        <el-card class="stat-card">
          <div class="stat-value">{{ avgScore }}分</div>
          <div class="stat-label">平均评分</div>
        </el-card>
      </el-col>
    </el-row>

    <el-card class="table-card">
      <div class="search-form">
        <el-form :inline="true" :model="searchForm">
          <el-form-item label="反馈类型">
            <el-select v-model="searchForm.type" placeholder="请选择" clearable>
              <el-option label="建议" value="suggestion" />
              <el-option label="投诉" value="complaint" />
              <el-option label="咨询" value="consultation" />
            </el-select>
          </el-form-item>
          <el-form-item label="处理状态">
            <el-select v-model="searchForm.status" placeholder="请选择" clearable>
              <el-option label="待处理" value="0" />
              <el-option label="处理中" value="1" />
              <el-option label="已处理" value="2" />
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

      <el-table :data="feedbackList" border stripe>
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="nickname" label="用户昵称" width="120" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTagType(row.type)">
              {{ getTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="反馈内容" min-width="200" show-overflow-tooltip />
        <el-table-column prop="score" label="评分" width="100">
          <template #default="{ row }">
            <el-rate v-model="row.score" disabled show-score />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleReply(row)">
              回复
            </el-button>
            <el-button :type="row.status === '2' ? 'info' : 'warning'" link size="small" @click="handleMark(row)">
              {{ row.status === '2' ? '查看' : '标记处理' }}
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

    <el-dialog v-model="dialogVisible" title="回复反馈" width="500px">
      <el-form :model="replyForm" label-width="100px">
        <el-form-item label="反馈内容">
          <el-input v-model="replyForm.content" type="textarea" :rows="3" readonly />
        </el-form-item>
        <el-form-item label="回复内容" prop="reply">
          <el-input v-model="replyForm.reply" type="textarea" :rows="4" placeholder="请输入回复内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitReply">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'

const dialogVisible = ref(false)

const totalFeedback = ref('128')
const pendingFeedback = ref('36')
const processedFeedback = ref('92')
const avgScore = ref('4.8')

const searchForm = reactive({
  type: '',
  status: ''
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const replyForm = reactive({
  id: null,
  content: '',
  reply: ''
})

const feedbackList = ref([
  { id: 1, nickname: '游客001', type: 'suggestion', content: '建议景区增加休息座椅数量，特别是在登山途中。', score: 4, status: '2', createTime: '2024-05-01 10:20:00' },
  { id: 2, nickname: '游客002', type: 'complaint', content: '观光车排队时间太长，建议增加车辆班次。', score: 2, status: '1', createTime: '2024-05-01 11:35:00' },
  { id: 3, nickname: '游客003', type: 'consultation', content: '请问金顶索道最晚运营时间是几点？', score: 5, status: '2', createTime: '2024-05-01 14:10:00' },
  { id: 4, nickname: '游客004', type: 'suggestion', content: '希望景区能提供更多的导览讲解服务。', score: 4, status: '0', createTime: '2024-05-01 15:45:00' },
  { id: 5, nickname: '游客005', type: 'complaint', content: '部分卫生间清洁不及时，希望改进。', score: 3, status: '1', createTime: '2024-05-01 16:20:00' }
])

pagination.total = feedbackList.value.length

const getTypeText = (type) => {
  const map = { suggestion: '建议', complaint: '投诉', consultation: '咨询' }
  return map[type] || type
}

const getTypeTagType = (type) => {
  const map = { suggestion: 'success', complaint: 'danger', consultation: 'primary' }
  return map[type] || 'info'
}

const getStatusText = (status) => {
  const map = { 0: '待处理', 1: '处理中', 2: '已处理' }
  return map[status] || status
}

const getStatusTagType = (status) => {
  const map = { 0: 'warning', 1: 'primary', 2: 'success' }
  return map[status] || 'info'
}

const handleSearch = () => {
  ElMessage.success('搜索功能开发中')
}

const handleReset = () => {
  searchForm.type = ''
  searchForm.status = ''
}

const handleReply = (row) => {
  replyForm.id = row.id
  replyForm.content = row.content
  replyForm.reply = ''
  dialogVisible.value = true
}

const handleMark = (row) => {
  if (row.status !== '2') {
    row.status = '2'
    ElMessage.success('已标记为已处理')
  }
}

const handleSubmitReply = () => {
  if (!replyForm.reply) {
    ElMessage.warning('请输入回复内容')
    return
  }
  const item = feedbackList.value.find(item => item.id === replyForm.id)
  if (item) {
    item.status = '2'
  }
  dialogVisible.value = false
  ElMessage.success('回复成功')
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
}
</script>

<style lang="scss" scoped>
.feedback-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;

    .page-title {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
      margin: 0;
    }
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      text-align: center;

      .stat-value {
        font-size: 32px;
        font-weight: bold;
        color: #667eea;
        margin-bottom: 10px;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
      }
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
