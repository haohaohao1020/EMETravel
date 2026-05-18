import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useScenicStore = defineStore('scenic', () => {
  const spots = ref([
    {
      id: 1,
      name: '金顶',
      subtitle: '峨眉之巅，云海之上',
      description: '金顶是峨眉山的最高峰，海拔3077米。这里有世界上最高的金佛——十方普贤菩萨像，以及壮观的云海、日出、佛光四大奇观。金顶也是索道的终点站。',
      image: 'https://picsum.photos/800/600?random=10',
      images: [
        'https://picsum.photos/800/600?random=11',
        'https://picsum.photos/800/600?random=12',
        'https://picsum.photos/800/600?random=13'
      ],
      altitude: '3077米',
      playTime: '2-3小时',
      openTime: '06:00 - 18:00',
      ticket: '包含在大门票内',
      tags: ['必游', '摄影', '云海', '索道终点'],
      location: { lat: 29.5333, lng: 103.3333 },
      tips: [
        '建议早上前往，观看云海和日出的最佳时间',
        '山顶气温较低，请携带保暖衣物',
        '索道运营时间：07:00-17:30'
      ],
      isCableEnd: true
    },
    {
      id: 2,
      name: '接引殿',
      subtitle: '索道起点，观光车换乘处',
      description: '接引殿是峨眉山重要的交通枢纽，也是金顶索道的起点站。从山底游客中心乘坐观光车约2小时可达这里，然后换乘索道前往金顶。',
      image: 'https://picsum.photos/800/600?random=20',
      images: [
        'https://picsum.photos/800/600?random=21',
        'https://picsum.photos/800/600?random=22'
      ],
      altitude: '2540米',
      playTime: '30分钟',
      openTime: '06:00 - 18:00',
      ticket: '包含在大门票内',
      tags: ['交通枢纽', '索道起点', '观光车终点'],
      location: { lat: 29.5222, lng: 103.3222 },
      tips: [
        '观光车终点站，需在此换乘索道上山',
        '此处有餐厅和休息区',
        '索道上行票每日限量1000张'
      ],
      isCableStart: true,
      isBusEnd: true
    },
    {
      id: 3,
      name: '万年寺',
      subtitle: '峨眉山最古老的寺庙之一',
      description: '万年寺是峨眉山历史最悠久的寺庙之一，始建于东晋时期。寺内有著名的普贤铜像，高7.35米，重62吨，是国家级文物保护单位。',
      image: 'https://picsum.photos/800/600?random=30',
      images: [
        'https://picsum.photos/800/600?random=31',
        'https://picsum.photos/800/600?random=32'
      ],
      altitude: '1020米',
      playTime: '1-2小时',
      openTime: '06:30 - 18:00',
      ticket: '包含在大门票内',
      tags: ['历史古迹', '佛教文化'],
      location: { lat: 29.5111, lng: 103.3111 },
      tips: [
        '寺内有素斋可供品尝',
        '建议参观普贤铜像',
        '可乘坐万年索道到达'
      ]
    },
    {
      id: 4,
      name: '清音阁',
      subtitle: '双桥清音，峨眉十景之一',
      description: '清音阁位于牛心岭下，是峨眉山的核心景区之一。这里有双桥清音的美景，黑白二水在此交汇，景色如画，是峨眉山十景之一。',
      image: 'https://picsum.photos/800/600?random=40',
      images: [
        'https://picsum.photos/800/600?random=41',
        'https://picsum.photos/800/600?random=42'
      ],
      altitude: '710米',
      playTime: '1-2小时',
      openTime: '全天开放',
      ticket: '包含在大门票内',
      tags: ['自然风光', '峨眉十景'],
      location: { lat: 29.5000, lng: 103.3000 },
      tips: [
        '建议夏季前往，避暑胜地',
        '有猴群出没，请注意安全',
        '可沿着栈道徒步'
      ]
    },
    {
      id: 5,
      name: '报国寺',
      subtitle: '峨眉山门户，入山第一寺',
      description: '报国寺是峨眉山的第一座寺庙，也是峨眉山佛教协会所在地。寺庙始建于明代，是游客入山的必经之地。',
      image: 'https://picsum.photos/800/600?random=50',
      images: [
        'https://picsum.photos/800/600?random=51',
        'https://picsum.photos/800/600?random=52'
      ],
      altitude: '533米',
      playTime: '1小时',
      openTime: '06:00 - 18:30',
      ticket: '包含在大门票内',
      tags: ['入山门户', '佛教文化'],
      location: { lat: 29.4888, lng: 103.2888 },
      tips: [
        '游客中心就在附近',
        '可在此了解峨眉山历史',
        '有素斋餐厅'
      ]
    },
    {
      id: 6,
      name: '洗象池',
      subtitle: '普贤洗象传说地',
      description: '洗象池因传说普贤菩萨曾在此洗象而得名。这里是观赏峨眉山月色的最佳地点，"象池夜月"是峨眉山十景之一。',
      image: 'https://picsum.photos/800/600?random=60',
      images: [
        'https://picsum.photos/800/600?random=61',
        'https://picsum.photos/800/600?random=62'
      ],
      altitude: '2070米',
      playTime: '1小时',
      openTime: '06:00 - 18:00',
      ticket: '包含在大门票内',
      tags: ['峨眉十景', '传说圣地'],
      location: { lat: 29.5444, lng: 103.3444 },
      tips: [
        '建议住宿一晚观赏月色',
        '有提供住宿',
        '徒步路线上的重要休息点'
      ]
    }
  ])

  const news = ref([
    {
      id: 1,
      title: '峨眉山景区"五一"假期游客接待公告',
      summary: '为确保"五一"假期景区安全有序，峨眉山景区将实行预约制入园，建议游客提前预约门票。',
      content: '峨眉山景区2024年"五一"假期（5月1日-5月5日）将实行门票预约制。游客可通过官方微信公众号、小程序等渠道提前预约门票。每日接待游客量不超过最大承载量的75%。索道票实行分时预约，请合理安排行程。',
      image: 'https://picsum.photos/800/400?random=n1',
      publishTime: '2024-04-28 09:00',
      type: '公告',
      isTop: true
    },
    {
      id: 2,
      title: '金顶索道每日限量1000张，售完即止',
      summary: '为提升游客体验，金顶索道即日起实行每日限量发售，上行票每日限量1000张，请提前预约。',
      content: '尊敬的游客：为确保索道安全运行和游客体验，金顶索道上行票即日起实行每日限量发售。每日限量1000张，分5个时段预约：07:00-09:00、09:00-11:00、11:00-13:00、13:00-15:00、15:00-17:00。建议游客提前1-2天预约。',
      image: 'https://picsum.photos/800/400?random=n2',
      publishTime: '2024-04-25 14:30',
      type: '重要通知',
      isTop: true
    },
    {
      id: 3,
      title: '峨眉山迎来最佳观赏期，云海佛光频现',
      summary: '近期峨眉山天气晴好，云海、佛光等自然奇观频繁出现，正是游览的最佳时机。',
      content: '进入春季以来，峨眉山天气持续晴好，金顶区域云海、佛光等自然奇观频繁出现。据气象部门预测，未来一周天气晴好，适合登山游览。建议游客选择上午乘坐索道上山，有较大概率观赏到云海奇观。',
      image: 'https://picsum.photos/800/400?random=n3',
      publishTime: '2024-04-22 10:15',
      type: '旅游资讯',
      isTop: false
    },
    {
      id: 4,
      title: '观光车运营时间调整通知',
      summary: '自4月20日起，观光车运营时间调整为06:00-17:00，每30分钟一班。',
      content: '随着旅游旺季的到来，为方便游客出行，景区观光车运营时间自2024年4月20日起调整为06:00-17:00。山底游客中心至接引殿往返，每30分钟一班，高峰期将根据客流情况增加班次。',
      image: 'https://picsum.photos/800/400?random=n4',
      publishTime: '2024-04-18 16:00',
      type: '运营公告',
      isTop: false
    },
    {
      id: 5,
      title: '峨眉山荣获"中国最美十大名山"称号',
      summary: '在最新发布的中国最美名山评选中，峨眉山成功入选，位列前五。',
      content: '在刚刚结束的"2024中国最美十大名山"评选活动中，峨眉山凭借其独特的自然风光和深厚的佛教文化底蕴，成功入选并位列前五。这是峨眉山连续第五年获此殊荣。',
      image: 'https://picsum.photos/800/400?random=n5',
      publishTime: '2024-04-15 11:30',
      type: '景区动态',
      isTop: false
    }
  ])

  const selectedSpot = ref(null)

  const getSpotById = (id) => {
    return spots.value.find(s => s.id === id)
  }

  const getNewsById = (id) => {
    return news.value.find(n => n.id === id)
  }

  const selectSpot = (spot) => {
    selectedSpot.value = spot
  }

  return {
    spots,
    news,
    selectedSpot,
    getSpotById,
    getNewsById,
    selectSpot
  }
})
