<template>
  <div class="route-container">
    <div class="page-header">
      <h2 class="page-title">路线管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        新增路线
      </el-button>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="路线类型">
          <el-select v-model="searchForm.type" placeholder="全部" clearable>
            <el-option label="经典路线" value="classic" />
            <el-option label="徒步路线" value="hiking" />
            <el-option label="亲子路线" value="family" />
            <el-option label="摄影路线" value="photo" />
          </el-select>
        </el-form-item>
        <el-form-item label="推荐等级">
          <el-select v-model="searchForm.level" placeholder="全部" clearable>
            <el-option label="★★★★★" value="5" />
            <el-option label="★★★★" value="4" />
            <el-option label="★★★" value="3" />
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
    </el-card>

    <div class="route-list mt-20">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="12" :lg="8" v-for="route in routeList" :key="route.id">
          <el-card class="route-card" shadow="hover">
            <template #header>
              <div class="card-header">
                <span class="route-name">{{ route.name }}</span>
                <el-tag :type="getTypeTagType(route.type)" size="small">
                  {{ getTypeText(route.type) }}
                </el-tag>
              </div>
            </template>

            <div class="route-image">
              <img :src="route.coverImage" :alt="route.name" />
            </div>

            <div class="route-info">
              <div class="info-item">
                <el-icon><Timer /></el-icon>
                <span>{{ route.duration }}</span>
              </div>
              <div class="info-item">
                <el-icon><Position /></el-icon>
                <span>{{ route.distance }}</span>
              </div>
              <div class="info-item">
                <el-icon><TrendCharts /></el-icon>
                <span>{{ route.difficulty }}</span>
              </div>
            </div>

            <div class="route-desc">
              {{ route.description }}
            </div>

            <div class="route-spots">
              <span class="spots-label">途经景点：</span>
              <el-tag v-for="spot in route.spots" :key="spot" size="small" class="spot-tag">
                {{ spot }}
              </el-tag>
            </div>

            <div class="route-footer">
              <div class="level">
                <el-rate v-model="route.rating" disabled show-score />
              </div>
              <div class="views">
                <el-icon><View /></el-icon>
                {{ route.views }}次浏览
              </div>
            </div>

            <div class="route-actions">
              <el-button type="primary" link size="small" @click="handleEdit(route)">
                编辑
              </el-button>
              <el-button type="success" link size="small" @click="handlePublish(route)">
                {{ route.status === '1' ? '已发布' : '发布' }}
              </el-button>
              <el-button type="danger" link size="small" @click="handleDelete(route)">
                删除
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="800px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="路线名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入路线名称" />
        </el-form-item>
        <el-form-item label="路线类型" prop="type">
          <el-select v-model="formData.type" placeholder="请选择路线类型" style="width: 100%">
            <el-option label="经典路线" value="classic" />
            <el-option label="徒步路线" value="hiking" />
            <el-option label="亲子路线" value="family" />
            <el-option label="摄影路线" value="photo" />
          </el-select>
        </el-form-item>
        <el-form-item label="封面图片" prop="coverImage">
          <el-input v-model="formData.coverImage" placeholder="请输入图片URL" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="预计时长" prop="duration">
              <el-input v-model="formData.duration" placeholder="如：4小时" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="全程距离" prop="distance">
              <el-input v-model="formData.distance" placeholder="如：8公里" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="难度等级" prop="difficulty">
              <el-select v-model="formData.difficulty" placeholder="请选择" style="width: 100%">
                <el-option label="简单" value="简单" />
                <el-option label="中等" value="中等" />
                <el-option label="困难" value="困难" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="路线描述" prop="description">
          <el-input v-model="formData.description" type="textarea" :rows="3" placeholder="请输入路线描述" />
        </el-form-item>
        <el-form-item label="途经景点" prop="spots">
          <el-select v-model="formData.spots" multiple placeholder="请选择途经景点" style="width: 100%">
            <el-option label="报国寺" value="报国寺" />
            <el-option label="清音阁" value="清音阁" />
            <el-option label="万年寺" value="万年寺" />
            <el-option label="洗象池" value="洗象池" />
            <el-option label="接引殿" value="接引殿" />
            <el-option label="金顶" value="金顶" />
            <el-option label="万佛顶" value="万佛顶" />
            <el-option label="洪椿坪" value="洪椿坪" />
            <el-option label="九十九道拐" value="九十九道拐" />
            <el-option label="一线天" value="一线天" />
          </el-select>
        </el-form-item>
        <el-form-item label="推荐等级" prop="rating">
          <el-rate v-model="formData.rating" show-score />
        </el-form-item>
        <el-form-item label="路线状态" prop="status">
          <el-radio-group v-model="formData.status">
            <el-radio value="1">已发布</el-radio>
            <el-radio value="0">草稿</el-radio>
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
const dialogTitle = computed(() => isEdit.value ? '编辑路线' : '新增路线')

const searchForm = reactive({
  type: '',
  level: ''
})

const routeList = ref([
  {
    id: 1,
    name: '金顶朝圣经典路线',
    type: 'classic',
    coverImage: 'https://picsum.photos/400/200?random=1',
    duration: '4-5小时',
    distance: '约8公里',
    difficulty: '中等',
    description: '从接引殿乘坐索道上山，步行至金顶，体验峨眉山最震撼的云海、佛光奇观。',
    spots: ['接引殿', '索道上站', '华藏寺', '金顶', '十方普贤', '舍身崖'],
    rating: 5,
    views: 12580,
    status: '1'
  },
  {
    id: 2,
    name: '徒步登山挑战路线',
    type: 'hiking',
    coverImage: 'https://picsum.photos/400/200?random=2',
    duration: '8-10小时',
    distance: '约25公里',
    difficulty: '困难',
    description: '从报国寺出发，全程徒步至金顶，体验峨眉山最完整的登山路线，适合资深徒步爱好者。',
    spots: ['报国寺', '清音阁', '洪椿坪', '九十九道拐', '洗象池', '接引殿', '金顶'],
    rating: 5,
    views: 8960,
    status: '1'
  },
  {
    id: 3,
    name: '亲子休闲轻松路线',
    type: 'family',
    coverImage: 'https://picsum.photos/400/200?random=3',
    duration: '3-4小时',
    distance: '约5公里',
    difficulty: '简单',
    description: '适合家庭出游的轻松路线，以观光车为主，配合短途步行，轻松游览峨眉山核心景点。',
    spots: ['报国寺', '清音阁', '万年寺', '生态猴区'],
    rating: 4,
    views: 15680,
    status: '1'
  },
  {
    id: 4,
    name: '摄影日出日落路线',
    type: 'photo',
    coverImage: 'https://picsum.photos/400/200?random=4',
    duration: '5-6小时',
    distance: '约10公里',
    difficulty: '中等',
    description: '专为摄影爱好者设计的路线，涵盖峨眉山最佳日出日落观赏点，捕捉最美光影瞬间。',
    spots: ['接引殿', '金顶', '万佛顶', '舍身崖', '观景台'],
    rating: 5,
    views: 7850,
    status: '1'
  },
  {
    id: 5,
    name: '佛教文化深度路线',
    type: 'classic',
    coverImage: 'https://picsum.photos/400/200?random=5',
    duration: '6-7小时',
    distance: '约12公里',
    difficulty: '中等',
    description: '深入探访峨眉山各大寺庙，感受千年佛教文化底蕴，适合对佛教文化感兴趣的游客。',
    spots: ['报国寺', '伏虎寺', '清音阁', '万年寺', '洗象池', '华藏寺'],
    rating: 4,
    views: 6320,
    status: '1'
  },
  {
    id: 6,
    name: '雪景雾凇专线',
    type: 'photo',
    coverImage: 'https://picsum.photos/400/200?random=6',
    duration: '4-5小时',
    distance: '约8公里',
    difficulty: '中等',
    description: '冬季限定路线，欣赏峨眉山绝美雪景和雾凇奇观，仿佛进入童话般的冰雪世界。',
    spots: ['接引殿', '索道上站', '金顶', '云海步道', '雾凇林'],
    rating: 5,
    views: 5890,
    status: '0'
  }
])

const formData = reactive({
  id: null,
  name: '',
  type: '',
  coverImage: '',
  duration: '',
  distance: '',
  difficulty: '',
  description: '',
  spots: [],
  rating: 5,
  status: '1'
})

const formRules = {
  name: [{ required: true, message: '请输入路线名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择路线类型', trigger: 'change' }],
  coverImage: [{ required: true, message: '请输入封面图片URL', trigger: 'blur' }],
  duration: [{ required: true, message: '请输入预计时长', trigger: 'blur' }],
  distance: [{ required: true, message: '请输入全程距离', trigger: 'blur' }],
  difficulty: [{ required: true, message: '请选择难度等级', trigger: 'change' }],
  description: [{ required: true, message: '请输入路线描述', trigger: 'blur' }],
  spots: [{ required: true, message: '请选择途经景点', trigger: 'change' }]
}

const getTypeText = (type) => {
  const map = { classic: '经典路线', hiking: '徒步路线', family: '亲子路线', photo: '摄影路线' }
  return map[type] || type
}

const getTypeTagType = (type) => {
  const map = { classic: 'primary', hiking: 'success', family: 'warning', photo: 'danger' }
  return map[type] || 'info'
}

const handleSearch = () => {
  ElMessage.success('搜索功能已执行')
}

const handleReset = () => {
  searchForm.type = ''
  searchForm.level = ''
}

const handleAdd = () => {
  isEdit.value = false
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'rating' ? 5 : key === 'spots' ? [] : key === 'status' ? '1' : ''
  })
  formData.id = null
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handlePublish = (row) => {
  if (row.status === '1') {
    ElMessage.info('该路线已发布')
    return
  }
  ElMessageBox.confirm('确定要发布该路线吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = '1'
    ElMessage.success('发布成功')
  }).catch(() => {})
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该路线吗？删除后将无法恢复。', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = routeList.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      routeList.value.splice(index, 1)
    }
    ElMessage.success('删除成功')
  }).catch(() => {})
}

const handleSubmit = () => {
  formRef.value?.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const index = routeList.value.findIndex(item => item.id === formData.id)
        if (index > -1) {
          routeList.value[index] = { ...routeList.value[index], ...formData }
        }
        ElMessage.success('编辑成功')
      } else {
        formData.id = routeList.value.length + 1
        formData.views = 0
        routeList.value.push({ ...formData })
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
    }
  })
}
</script>

<style lang="scss" scoped>
.route-container {
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

  .mt-20 {
    margin-top: 20px;
  }

  .route-list {
    .route-card {
      height: 100%;
      margin-bottom: 20px;

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .route-name {
          font-weight: 600;
          font-size: 16px;
        }
      }

      .route-image {
        width: 100%;
        height: 180px;
        overflow: hidden;
        border-radius: 4px;
        margin-bottom: 15px;

        img {
          width: 100%;
          height: 100%;
          object-fit: cover;
          transition: transform 0.3s;
        }

        &:hover img {
          transform: scale(1.05);
        }
      }

      .route-info {
        display: flex;
        justify-content: space-between;
        margin-bottom: 15px;
        padding: 10px;
        background: #f5f7fa;
        border-radius: 4px;

        .info-item {
          display: flex;
          align-items: center;
          font-size: 13px;
          color: #606266;

          .el-icon {
            margin-right: 5px;
            color: #667eea;
          }
        }
      }

      .route-desc {
        font-size: 13px;
        color: #606266;
        line-height: 1.6;
        margin-bottom: 15px;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }

      .route-spots {
        margin-bottom: 15px;

        .spots-label {
          font-size: 13px;
          color: #909399;
          margin-right: 5px;
        }

        .spot-tag {
          margin: 3px;
        }
      }

      .route-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-top: 10px;
        border-top: 1px solid #ebeef5;

        .views {
          font-size: 13px;
          color: #909399;
          display: flex;
          align-items: center;

          .el-icon {
            margin-right: 5px;
          }
        }
      }

      .route-actions {
        display: flex;
        justify-content: flex-end;
        margin-top: 10px;
        padding-top: 10px;
        border-top: 1px solid #ebeef5;
      }
    }
  }
}
</style>
