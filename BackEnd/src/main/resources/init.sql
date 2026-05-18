-- 峨眉山旅游系统数据库初始化脚本
CREATE DATABASE IF NOT EXISTS emei_tourism DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE emei_tourism;

-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    phone VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    password VARCHAR(100) NOT NULL DEFAULT 'e10adc3949ba59abbe56e057f20f883e' COMMENT '密码（默认123456的MD5）',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像',
    gender TINYINT DEFAULT 0 COMMENT '性别：0未知1男2女',
    level INT DEFAULT 1 COMMENT '会员等级',
    points INT DEFAULT 0 COMMENT '积分',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0否1是'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 票种表
CREATE TABLE IF NOT EXISTS t_ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '票种ID',
    name VARCHAR(100) NOT NULL COMMENT '票种名称',
    type TINYINT NOT NULL COMMENT '类型：1大门票2索道票3观光车票',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT DEFAULT -1 COMMENT '库存（-1不限）',
    valid_days INT DEFAULT 1 COMMENT '有效天数',
    use_rule TEXT COMMENT '使用规则',
    description TEXT COMMENT '描述',
    image VARCHAR(255) COMMENT '图片',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用1启用',
    time_slots VARCHAR(255) COMMENT '可预约时段',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='票种表';

-- 库存表
CREATE TABLE IF NOT EXISTS t_ticket_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '库存ID',
    ticket_id BIGINT NOT NULL COMMENT '票种ID',
    use_date DATE NOT NULL COMMENT '使用日期',
    time_slot VARCHAR(50) COMMENT '时段',
    total_stock INT NOT NULL DEFAULT 0 COMMENT '总库存',
    used_stock INT NOT NULL DEFAULT 0 COMMENT '已使用库存',
    locked_stock INT NOT NULL DEFAULT 0 COMMENT '已锁定库存',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    version INT DEFAULT 0 COMMENT '乐观锁版本',
    UNIQUE KEY uk_ticket_date_slot (ticket_id, use_date, time_slot)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='票种库存表';

-- 订单表
CREATE TABLE IF NOT EXISTS t_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    visitor_name VARCHAR(50) COMMENT '游客姓名',
    visitor_phone VARCHAR(20) COMMENT '游客电话',
    id_card VARCHAR(50) COMMENT '身份证号',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    status TINYINT DEFAULT 1 COMMENT '状态：0已取消1待支付2已支付3已核销4已退款',
    pay_time DATETIME COMMENT '支付时间',
    verify_code VARCHAR(20) COMMENT '核销码',
    verify_time DATETIME COMMENT '核销时间',
    refund_reason VARCHAR(255) COMMENT '退款原因',
    refund_time DATETIME COMMENT '退款时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除',
    INDEX idx_user_id(user_id),
    INDEX idx_order_no(order_no),
    INDEX idx_verify_code(verify_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单明细表
CREATE TABLE IF NOT EXISTS t_order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    ticket_id BIGINT NOT NULL COMMENT '票种ID',
    ticket_name VARCHAR(100) NOT NULL COMMENT '票种名称',
    ticket_type TINYINT NOT NULL COMMENT '票种类型',
    price DECIMAL(10,2) NOT NULL COMMENT '单价',
    quantity INT NOT NULL DEFAULT 1 COMMENT '数量',
    use_date DATE COMMENT '使用日期',
    time_slot VARCHAR(50) COMMENT '使用时段',
    verify_code VARCHAR(20) COMMENT '核销码',
    verify_status TINYINT DEFAULT 0 COMMENT '核销状态：0未核销1已核销',
    verify_time DATETIME COMMENT '核销时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_id(order_id),
    INDEX idx_verify_code(verify_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- 核销记录表
CREATE TABLE IF NOT EXISTS t_verify_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_item_id BIGINT NOT NULL COMMENT '订单明细ID',
    verify_code VARCHAR(20) NOT NULL COMMENT '核销码',
    ticket_id BIGINT NOT NULL COMMENT '票种ID',
    operator_id BIGINT COMMENT '操作员ID',
    operator_name VARCHAR(50) COMMENT '操作员姓名',
    verify_type TINYINT DEFAULT 1 COMMENT '核销类型：1正常核销2手动核销',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '核销时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='核销记录表';

-- 景点表
CREATE TABLE IF NOT EXISTS t_scenic_spot (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '景点ID',
    name VARCHAR(100) NOT NULL COMMENT '景点名称',
    subtitle VARCHAR(255) COMMENT '副标题',
    description TEXT COMMENT '详细描述',
    image VARCHAR(255) COMMENT '主图',
    images TEXT COMMENT '图片列表JSON',
    altitude VARCHAR(50) COMMENT '海拔',
    play_time VARCHAR(50) COMMENT '建议游玩时间',
    open_time VARCHAR(100) COMMENT '开放时间',
    ticket_info VARCHAR(255) COMMENT '门票信息',
    tags VARCHAR(255) COMMENT '标签，逗号分隔',
    longitude DECIMAL(10,6) COMMENT '经度',
    latitude DECIMAL(10,6) COMMENT '纬度',
    tips TEXT COMMENT '游玩提示',
    is_cable_end TINYINT DEFAULT 0 COMMENT '是否索道终点',
    is_cable_start TINYINT DEFAULT 0 COMMENT '是否索道起点',
    is_bus_end TINYINT DEFAULT 0 COMMENT '是否观光车终点',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='景点表';

-- 公告表
CREATE TABLE IF NOT EXISTS t_notice (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '公告ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    summary VARCHAR(500) COMMENT '摘要',
    content TEXT COMMENT '内容',
    image VARCHAR(255) COMMENT '封面图',
    type TINYINT DEFAULT 1 COMMENT '类型：1公告2资讯3攻略',
    is_top TINYINT DEFAULT 0 COMMENT '是否置顶：0否1是',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告/资讯表';

-- 插入测试票种数据
INSERT INTO t_ticket (name, type, price, stock, valid_days, use_rule, description, status, time_slots) VALUES
('峨眉山景区大门票', 1, 160.00, -1, 1, '凭身份证入园，当日有效', '峨眉山景区入园门票，包含全山游览权限', 1, ''),
('金顶索道上行票', 2, 65.00, 1000, 1, '接引殿到金顶上行索道，当日有效', '接引殿到金顶上行索道票，每日限量1000张，请提前预约', 1, '07:00-09:00,09:00-11:00,11:00-13:00,13:00-15:00,15:00-17:00'),
('金顶索道下行票', 2, 55.00, 1000, 1, '金顶到接引殿下行索道，当日有效', '金顶到接引殿下行索道票', 1, '07:00-09:00,09:00-11:00,11:00-13:00,13:00-15:00,15:00-17:00'),
('观光车往返票', 3, 90.00, -1, 1, '游客中心到接引殿往返，当日有效', '游客中心到接引殿往返观光车票', 1, '06:00-08:00,08:00-10:00,10:00-12:00,12:00-14:00,14:00-16:00'),
('观光车单程票', 3, 50.00, -1, 1, '单程有效', '游客中心到接引殿或接引殿到游客中心单程票', 1, '06:00-08:00,08:00-10:00,10:00-12:00,12:00-14:00,14:00-16:00'),
('门票+索道+观光车套票', 1, 370.00, -1, 1, '套票包含大门票+索道上行+观光车往返，当日有效', '套票优惠组合', 1, '');

-- 插入景点数据
INSERT INTO t_scenic_spot (name, subtitle, description, image, altitude, play_time, open_time, ticket_info, tags, longitude, latitude, tips, is_cable_end, is_cable_start, is_bus_end, sort, status) VALUES
('金顶', '峨眉之巅，云上佛国', '金顶是峨眉山的最高峰，海拔3077米，这里有世界上最高的金佛--十方普贤菩萨像，高48米，重达660吨。金顶是峨眉山的象征，也是观赏云海、日出、佛光、圣灯四大奇观的最佳地点。', 'https://picsum.photos/800/600?random=jinding1', '3077米', '2-3小时', '06:00-18:00', '包含在大门票内，索道票另行购买', '必游,摄影,云海,日出,索道终点', 103.3333, 29.5333, '建议早上前往，观看云海和日出的最佳时间；山顶气温较低，请携带保暖衣物；索道运营时间：07:00-17:30', 1, 0, 0, 1, 1),
('接引殿', '索道起点，观光车换乘枢纽', '接引殿是峨眉山重要的交通枢纽，也是金顶索道的起点站。从山底游客中心乘坐观光车约2小时可达这里，然后换乘索道前往金顶。接引殿始建于宋代，是峨眉山重要的佛教寺院之一。', 'https://picsum.photos/800/600?random=jieyindian1', '2540米', '30分钟', '06:00-18:00', '包含在大门票内', '交通枢纽,索道起点,观光车终点,寺院', 103.3222, 29.5222, '观光车终点站，需在此换乘索道上山；此处有餐厅和休息区；索道上行票每日限量1000张，请提前预约', 0, 1, 1, 2, 1),
('万年寺', '峨眉山最古老的寺院之一', '万年寺是峨眉山历史最悠久的寺庙之一，始建于东晋时期（公元401年），距今已有1600多年历史。寺内有著名的普贤铜像，高7.35米，重62吨，铸于宋代，是国家级文物保护单位。', 'https://picsum.photos/800/600?random=wanniansi1', '1020米', '1-2小时', '06:30-18:00', '包含在大门票内，可乘坐万年索道到达', '历史古迹,佛教文化,索道', 103.3111, 29.5111, '寺内有素斋可供品尝；建议参观普贤铜像；可乘坐万年索道到达', 0, 0, 0, 3, 1),
('清音阁', '双桥清音，峨眉十景之一', '清音阁位于牛心岭下，是峨眉山的核心景区之一。这里有双桥清音的美景，黑白二水在此交汇，冲击着中心的牛心石，景色如画，是峨眉山十景之一。清音阁始建于唐代，是峨眉山重点寺院。', 'https://picsum.photos/800/600?random=qingyinge1', '710米', '1-2小时', '全天开放', '包含在大门票内', '自然风光,峨眉十景,避暑,溪流', 103.3000, 29.5000, '建议夏季前往，避暑胜地；有猴群出没，请注意安全，不要投喂；可沿着栈道徒步观赏', 0, 0, 0, 4, 1),
('报国寺', '峨眉山门户，入山第一寺', '报国寺是峨眉山的第一座寺庙，也是峨眉山佛教协会所在地。寺庙始建于明代万历年间（公元1573-1619年），是游客入山的必经之地。报国寺也是峨眉山最大的寺院之一。', 'https://picsum.photos/800/600?random=baoguosi1', '533米', '1小时', '06:00-18:30', '包含在大门票内', '入山门户,佛教文化,历史建筑', 103.2888, 29.4888, '游客中心就在附近；可在此了解峨眉山历史；有素斋餐厅；是登山的起点', 0, 0, 0, 5, 1),
('洗象池', '普贤洗象传说地', '洗象池因传说普贤菩萨曾在此洗象而得名。这里是观赏峨眉山月色的最佳地点，"象池夜月"是峨眉山十景之一。洗象池也是徒步登山路线上的重要休息站。', 'https://picsum.photos/800/600?random=xixiangchi1', '2070米', '1小时', '06:00-18:00', '包含在大门票内', '峨眉十景,传说圣地,赏月,徒步休息站', 103.3444, 29.5444, '建议住宿一晚观赏月色；提供简单住宿；徒步路线上的重要休息点；夜晚较冷注意保暖', 0, 0, 0, 6, 1),
('洪椿坪', '洪椿晓雨，峨眉十景', '洪椿坪是峨眉山古刹之一，始建于宋代。洪椿晓雨是峨眉山十景之一，这里常有细雨蒙蒙，云雾缭绕，宛如仙境。洪椿坪也是徒步登山路线上的重要休息站。', 'https://picsum.photos/800/600?random=hongchunping1', '1120米', '1小时', '06:00-18:00', '包含在大门票内', '峨眉十景,古刹,徒步休息站', 103.3355, 29.5255, '徒步登山路线必经之地；有素斋和住宿；雨天较多，备好雨具', 0, 0, 0, 7, 1),
('仙峰寺', '九老仙府，洞天福地', '仙峰寺始建于宋代，是峨眉山主要寺院之一。寺周围猴群众多，是峨眉山观赏猴子的好地方。寺旁有九老洞，是著名的道教洞天福地。', 'https://picsum.photos/800/600?random=xianfengsi1', '1752米', '1-2小时', '06:00-18:00', '包含在大门票内', '寺院,猴群,道教圣地,徒步', 103.3344, 29.5344, '有猴群出没，请注意安全；可参观九老洞；提供住宿和素斋', 0, 0, 0, 8, 1);

-- 插入公告资讯
INSERT INTO t_notice (title, summary, content, image, type, is_top, sort, status) VALUES
('峨眉山景区"五一"假期游客接待公告', '为确保"五一"假期景区安全有序，峨眉山景区将实行预约制入园，建议游客提前预约门票。', '峨眉山景区2024年"五一"假期（5月1日-5月5日）将实行门票预约制。游客可通过官方微信公众号、小程序等渠道提前预约门票。每日接待游客量不超过最大承载量的75%。索道票实行分时预约，请合理安排行程。建议游客朋友错峰出行，提前关注景区天气和交通信息。', 'https://picsum.photos/800/400?random=notice1', 1, 1, 1, 1),
('金顶索道每日限量1000张，售完即止', '为提升游客体验，金顶索道即日起实行每日限量发售，上行票每日限量1000张，请提前预约。', '尊敬的游客：为确保索道安全运行和游客体验，金顶索道上行票即日起实行每日限量发售。每日限量1000张，分5个时段预约：07:00-09:00、09:00-11:00、11:00-13:00、13:00-15:00、15:00-17:00。建议游客提前1-2天预约。节假日期间索道票会比较紧张，请务必提前规划行程。', 'https://picsum.photos/800/400?random=notice2', 1, 1, 2, 1),
('峨眉山迎来最佳观赏期，云海佛光频现', '近期峨眉山天气晴好，云海、佛光等自然奇观频繁出现，正是游览的最佳时机。', '进入春季以来，峨眉山天气持续晴好，金顶区域云海、佛光等自然奇观频繁出现。据气象部门预测，未来一周天气晴好，适合登山游览。建议游客选择上午乘坐索道上山，有较大概率观赏到云海奇观。日出时间约为早上6:30，建议提前出发。', 'https://picsum.photos/800/400?random=info1', 2, 0, 1, 1),
('观光车运营时间调整通知', '自4月20日起，观光车运营时间调整为06:00-17:00，每30分钟一班。', '随着旅游旺季的到来，为方便游客出行，景区观光车运营时间自2024年4月20日起调整为06:00-17:00。山底游客中心至接引殿往返，每30分钟一班，高峰期将根据客流情况增加班次。请游客合理安排行程时间。', 'https://picsum.photos/800/400?random=notice3', 1, 0, 3, 1),
('峨眉山荣获"中国最美十大名山"称号', '在最新发布的中国最美名山评选中，峨眉山成功入选，位列前五。', '在刚刚结束的"2024中国最美十大名山"评选活动中，峨眉山凭借其独特的自然风光和深厚的佛教文化底蕴，成功入选并位列前五。这是峨眉山连续第五年获此殊荣。峨眉山素有"峨眉天下秀"的美誉，是世界文化与自然双重遗产地。', 'https://picsum.photos/800/400?random=info2', 2, 0, 2, 1),
('峨眉山徒步登山完整攻略', '本文为您详细介绍峨眉山徒步登山路线，包括路线选择、装备建议、住宿推荐、注意事项等。', '峨眉山徒步登山是很多登山爱好者的首选。推荐路线：报国寺→清音阁→洪椿坪→仙峰寺→洗象池→接引殿→金顶。全程约58公里，建议2-3天完成。装备建议：登山鞋、登山杖、雨具、保暖衣物、头灯、补给食品等。沿途寺院多提供住宿和素斋，无需担心食宿问题。徒步过程中注意猴群，不要投喂食物。', 'https://picsum.photos/800/400?random=guide1', 3, 0, 1, 1);

-- 插入测试用户
INSERT INTO t_user (phone, password, nickname, level, points) VALUES
('13800138000', 'e10adc3949ba59abbe56e057f20f883e', '测试用户001', 1, 1000),
('13800138001', 'e10adc3949ba59abbe56e057f20f883e', '测试用户002', 2, 2500);
