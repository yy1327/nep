-- ================================================
-- 东软环保公众监督系统 数据库初始化脚本
-- 数据库名称: nep_system
-- ================================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS nep_system DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE nep_system;

-- ================================================
-- 1. 空气质量指数级别表 (aqi)
-- ================================================
DROP TABLE IF EXISTS aqi;
CREATE TABLE aqi (
    aqi_id INT PRIMARY KEY COMMENT 'AQI等级ID',
    aqi_level VARCHAR(10) COMMENT 'AQI等级名称',
    aqi_value_min INT COMMENT 'AQI最小值',
    aqi_value_max INT COMMENT 'AQI最大值',
    aqi_color VARCHAR(20) COMMENT '颜色代码',
    aqi_desc VARCHAR(100) COMMENT '等级描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='空气质量指数级别表';

-- 插入AQI等级数据
INSERT INTO aqi (aqi_id, aqi_level, aqi_value_min, aqi_value_max, aqi_color, aqi_desc) VALUES
(1, '优', 0, 50, '#00E400', '空气质量令人满意，基本无空气污染'),
(2, '良', 51, 100, '#FFFF00', '空气质量可接受，某些污染物对极少数人群有微弱影响'),
(3, '轻度污染', 101, 150, '#FF7E00', '易感人群症状有轻度加剧，健康人群出现刺激症状'),
(4, '中度污染', 151, 200, '#FF0000', '进一步加剧易感人群症状，可能对心脏、呼吸系统有影响'),
(5, '重度污染', 201, 300, '#99004C', '心脏病和肺病患者症状显著加剧，运动耐受力降低'),
(6, '严重污染', 301, 500, '#7E0023', '健康人群运动耐受力降低，有明显强烈症状');

-- ================================================
-- 2. 系统网格覆盖省区域表 (grid_province)
-- ================================================
DROP TABLE IF EXISTS grid_province;
CREATE TABLE grid_province (
    province_id INT PRIMARY KEY COMMENT '省ID',
    province_name VARCHAR(50) COMMENT '省名称'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统网格覆盖省区域表';

-- 插入省份数据
INSERT INTO grid_province (province_id, province_name) VALUES
(1, '北京市'),
(2, '天津市'),
(3, '河北省'),
(4, '山西省'),
(5, '内蒙古自治区'),
(6, '辽宁省'),
(7, '吉林省'),
(8, '黑龙江省'),
(9, '上海市'),
(10, '江苏省'),
(11, '浙江省'),
(12, '安徽省'),
(13, '福建省'),
(14, '江西省'),
(15, '山东省'),
(16, '河南省'),
(17, '湖北省'),
(18, '湖南省'),
(19, '广东省'),
(20, '广西壮族自治区'),
(21, '海南省'),
(22, '重庆市'),
(23, '四川省'),
(24, '贵州省'),
(25, '云南省'),
(26, '西藏自治区'),
(27, '陕西省'),
(28, '甘肃省'),
(29, '青海省'),
(30, '宁夏回族自治区'),
(31, '新疆维吾尔自治区');

-- ================================================
-- 3. 系统网格覆盖市区域表 (grid_city)
-- ================================================
DROP TABLE IF EXISTS grid_city;
CREATE TABLE grid_city (
    city_id INT PRIMARY KEY COMMENT '市ID',
    city_name VARCHAR(50) COMMENT '市名称',
    province_id INT COMMENT '所属省ID',
    FOREIGN KEY (province_id) REFERENCES grid_province(province_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统网格覆盖市区域表';

-- 插入部分城市数据（示例）
INSERT INTO grid_city (city_id, city_name, province_id) VALUES
(1, '北京市', 1),
(2, '天津市', 2),
(3, '石家庄市', 3),
(4, '唐山市', 3),
(5, '秦皇岛市', 3),
(6, '太原市', 4),
(7, '大同市', 4),
(8, '沈阳市', 6),
(9, '大连市', 6),
(10, '长春市', 7),
(11, '吉林市', 7),
(12, '哈尔滨市', 8),
(13, '齐齐哈尔市', 8),
(14, '上海市', 9),
(15, '南京市', 10),
(16, '苏州市', 10),
(17, '杭州市', 11),
(18, '宁波市', 11),
(19, '合肥市', 12),
(20, '福州市', 13),
(21, '厦门市', 13),
(22, '南昌市', 14),
(23, '济南市', 15),
(24, '青岛市', 15),
(25, '郑州市', 16),
(26, '武汉市', 17),
(27, '长沙市', 18),
(28, '广州市', 19),
(29, '深圳市', 19),
(30, '南宁市', 20),
(31, '海口市', 21),
(32, '重庆市', 22),
(33, '成都市', 23),
(34, '贵阳市', 24),
(35, '昆明市', 25),
(36, '拉萨市', 26),
(37, '西安市', 27),
(38, '兰州市', 28),
(39, '西宁市', 29),
(40, '银川市', 30),
(41, '乌鲁木齐市', 31);

-- ================================================
-- 4. 公众监督员表 (supervisor)
-- ================================================
DROP TABLE IF EXISTS supervisor;
CREATE TABLE supervisor (
    supervisor_id VARCHAR(20) PRIMARY KEY COMMENT '监督员编号',
    supervisor_tel VARCHAR(11) COMMENT '手机号码',
    supervisor_pwd VARCHAR(50) COMMENT '登录密码',
    supervisor_name VARCHAR(50) COMMENT '姓名',
    supervisor_sex VARCHAR(2) COMMENT '性别',
    supervisor_age INT COMMENT '年龄',
    supervisor_addr VARCHAR(200) COMMENT '详细地址',
    province_id INT COMMENT '所属省ID',
    city_id INT COMMENT '所属市ID',
    create_time DATETIME COMMENT '注册时间',
    FOREIGN KEY (province_id) REFERENCES grid_province(province_id),
    FOREIGN KEY (city_id) REFERENCES grid_city(city_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公众监督员表';

-- 插入测试数据
INSERT INTO supervisor (supervisor_id, supervisor_tel, supervisor_pwd, supervisor_name, supervisor_sex, supervisor_age, supervisor_addr, province_id, city_id, create_time) VALUES
('SP001', '13800001001', '123456', '张三', '男', 35, '北京市朝阳区XX路XX号', 1, 1, NOW()),
('SP002', '13800001002', '123456', '李四', '女', 28, '上海市浦东新区XX路XX号', 9, 14, NOW()),
('SP003', '13800001003', '123456', '王五', '男', 42, '广州市天河区XX路XX号', 19, 28, NOW());

-- ================================================
-- 5. 空气质量监测网格员表 (grid_member)
-- ================================================
DROP TABLE IF EXISTS grid_member;
CREATE TABLE grid_member (
    gm_id INT PRIMARY KEY COMMENT '网格员ID',
    gm_code VARCHAR(20) COMMENT '登录账号（工号）',
    gm_pwd VARCHAR(50) COMMENT '登录密码',
    gm_name VARCHAR(50) COMMENT '姓名',
    gm_tel VARCHAR(11) COMMENT '联系电话',
    gm_province_id INT COMMENT '负责省ID',
    gm_city_id INT COMMENT '负责市ID',
    gm_status VARCHAR(2) COMMENT '状态（01-可用，02-不可用）',
    create_time DATETIME COMMENT '创建时间',
    FOREIGN KEY (gm_province_id) REFERENCES grid_province(province_id),
    FOREIGN KEY (gm_city_id) REFERENCES grid_city(city_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='空气质量监测网格员表';

-- 插入测试数据
INSERT INTO grid_member (gm_id, gm_code, gm_pwd, gm_name, gm_tel, gm_province_id, gm_city_id, gm_status, create_time) VALUES
(1, 'GM001', '123456', '赵网格', '13900001001', 1, 1, '01', NOW()),
(2, 'GM002', '123456', '钱网格', '13900001002', 9, 14, '01', NOW()),
(3, 'GM003', '123456', '孙网格', '13900001003', 19, 28, '01', NOW());

-- ================================================
-- 6. 空气质量公众监督反馈表 (aqi_feedback)
-- ================================================
DROP TABLE IF EXISTS aqi_feedback;
CREATE TABLE aqi_feedback (
    af_id INT PRIMARY KEY COMMENT '反馈ID',
    af_code VARCHAR(50) COMMENT '反馈编号（唯一标识）',
    supervisor_id VARCHAR(20) COMMENT '监督员ID',
    af_type VARCHAR(2) COMMENT '反馈类型（01-空气有异味、02-雾霾严重、03-工业排放、04-其他）',
    af_level VARCHAR(10) COMMENT '预估AQI等级',
    af_desc VARCHAR(500) COMMENT '问题描述',
    af_address VARCHAR(200) COMMENT '问题发生地点',
    af_longitude DECIMAL(10,6) COMMENT '经度',
    af_latitude DECIMAL(10,6) COMMENT '纬度',
    af_province_id INT COMMENT '所属省ID',
    af_city_id INT COMMENT '所属市ID',
    af_photo VARCHAR(200) COMMENT '现场照片路径',
    af_state VARCHAR(2) COMMENT '状态（01-待指派、02-待确认、03-已完成）',
    af_assign_gm_id INT COMMENT '指派的网格员ID',
    af_assign_time DATETIME COMMENT '指派时间',
    af_assign_desc VARCHAR(200) COMMENT '指派备注',
    af_confirm_gm_id INT COMMENT '确认的网格员ID',
    af_confirm_time DATETIME COMMENT '确认时间',
    af_confirm_aqi INT COMMENT '确认的AQI值',
    af_confirm_desc VARCHAR(200) COMMENT '确认备注',
    create_time DATETIME COMMENT '创建时间',
    FOREIGN KEY (supervisor_id) REFERENCES supervisor(supervisor_id),
    FOREIGN KEY (af_assign_gm_id) REFERENCES grid_member(gm_id),
    FOREIGN KEY (af_confirm_gm_id) REFERENCES grid_member(gm_id),
    FOREIGN KEY (af_province_id) REFERENCES grid_province(province_id),
    FOREIGN KEY (af_city_id) REFERENCES grid_city(city_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='空气质量公众监督反馈表';

-- 插入测试数据
INSERT INTO aqi_feedback (af_id, af_code, supervisor_id, af_type, af_level, af_desc, af_address, af_longitude, af_latitude, af_province_id, af_city_id, af_state, create_time) VALUES
(1, 'AF20240001', 'SP001', '02', '轻度污染', '今天早上雾霾比较严重，能见度较低', '北京市朝阳区国贸附近', 116.427428, 39.908626, 1, 1, '01', NOW()),
(2, 'AF20240002', 'SP002', '01', '良', '附近工厂有异味飘出', '上海市浦东新区张江高科', 121.564516, 31.220654, 9, 14, '02', NOW()),
(3, 'AF20240003', 'SP003', '03', '中度污染', '工业区烟囱排放明显', '广州市天河区珠江新城', 113.324581, 23.119420, 19, 28, '03', NOW());

-- ================================================
-- 7. 空气质量监测数据统计表 (statistics)
-- ================================================
DROP TABLE IF EXISTS statistics;
CREATE TABLE statistics (
    statistics_id INT PRIMARY KEY COMMENT '统计ID',
    af_id INT COMMENT '反馈ID',
    gm_id INT COMMENT '网格员ID',
    aqi_value INT COMMENT 'AQI值',
    aqi_level VARCHAR(10) COMMENT 'AQI等级',
    pm25_value DECIMAL(5,2) COMMENT 'PM2.5浓度(μg/m³)',
    pm10_value DECIMAL(5,2) COMMENT 'PM10浓度(μg/m³)',
    so2_value DECIMAL(5,2) COMMENT 'SO2浓度(μg/m³)',
    no2_value DECIMAL(5,2) COMMENT 'NO2浓度(μg/m³)',
    co_value DECIMAL(5,2) COMMENT 'CO浓度(mg/m³)',
    o3_value DECIMAL(5,2) COMMENT 'O3浓度(μg/m³)',
    province_id INT COMMENT '省份ID',
    city_id INT COMMENT '城市ID',
    statistics_date DATE COMMENT '统计日期',
    create_time DATETIME COMMENT '创建时间',
    FOREIGN KEY (af_id) REFERENCES aqi_feedback(af_id),
    FOREIGN KEY (gm_id) REFERENCES grid_member(gm_id),
    FOREIGN KEY (province_id) REFERENCES grid_province(province_id),
    FOREIGN KEY (city_id) REFERENCES grid_city(city_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='空气质量监测数据统计表';

-- 插入测试数据
INSERT INTO statistics (statistics_id, af_id, gm_id, aqi_value, aqi_level, pm25_value, pm10_value, so2_value, no2_value, co_value, o3_value, province_id, city_id, statistics_date, create_time) VALUES
(1, 3, 3, 185, '中度污染', 138.50, 185.20, 25.30, 68.50, 1.25, 156.80, 19, 28, CURDATE(), NOW()),
(2, 2, 2, 78, '良', 45.30, 82.60, 12.50, 38.20, 0.65, 125.40, 9, 14, CURDATE(), NOW());

-- ================================================
-- 8. 系统管理员表 (admins)
-- ================================================
DROP TABLE IF EXISTS admins;
CREATE TABLE admins (
    admin_id INT PRIMARY KEY COMMENT '管理员ID',
    admin_code VARCHAR(20) COMMENT '登录账号',
    admin_pwd VARCHAR(50) COMMENT '登录密码',
    admin_name VARCHAR(50) COMMENT '姓名',
    admin_tel VARCHAR(11) COMMENT '联系电话',
    create_time DATETIME COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统管理员表';

-- 插入测试数据
INSERT INTO admins (admin_id, admin_code, admin_pwd, admin_name, admin_tel, create_time) VALUES
(1, 'admin', '123456', '系统管理员', '13700001000', NOW());

-- ================================================
-- 创建索引优化查询性能
-- ================================================
CREATE INDEX idx_supervisor_tel ON supervisor(supervisor_tel);
CREATE INDEX idx_aqi_feedback_state ON aqi_feedback(af_state);
CREATE INDEX idx_aqi_feedback_supervisor ON aqi_feedback(supervisor_id);
CREATE INDEX idx_statistics_province ON statistics(province_id);
CREATE INDEX idx_statistics_date ON statistics(statistics_date);
CREATE INDEX idx_grid_city_province ON grid_city(province_id);

-- ================================================
-- 创建视图方便查询
-- ================================================
-- 待处理反馈视图
CREATE OR REPLACE VIEW v_pending_feedback AS
SELECT 
    af.af_id,
    af.af_code,
    af.af_type,
    af.af_level,
    af.af_address,
    af.af_state,
    s.supervisor_name,
    s.supervisor_tel,
    gp.province_name,
    gc.city_name,
    af.create_time
FROM aqi_feedback af
LEFT JOIN supervisor s ON af.supervisor_id = s.supervisor_id
LEFT JOIN grid_province gp ON af.af_province_id = gp.province_id
LEFT JOIN grid_city gc ON af.af_city_id = gc.city_id;

-- AQI统计汇总视图
CREATE OR REPLACE VIEW v_aqi_statistics AS
SELECT 
    statistics_date,
    COUNT(*) as total_count,
    SUM(CASE WHEN aqi_level = '优' THEN 1 ELSE 0 END) as excellent_count,
    SUM(CASE WHEN aqi_level = '良' THEN 1 ELSE 0 END) as good_count,
    SUM(CASE WHEN aqi_level IN ('轻度污染', '中度污染', '重度污染', '严重污染') THEN 1 ELSE 0 END) as polluted_count,
    AVG(aqi_value) as avg_aqi
FROM statistics
GROUP BY statistics_date;

-- ================================================
-- 数据库初始化完成
-- ================================================
