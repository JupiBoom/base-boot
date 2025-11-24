### 基于SpringBoot的脚手架工程
- 基本的通用返回值 [√]
- 全局异常返回 [√]
- mybatis-plus整合 [√]
- 参数校验 [√]
- redis的相关配置 [√]
- HttpClient整合连接池配置 [√]
- 通用日志logback+Slf4j [√]
- SpringContext依赖查找工具 [√]
- 简陋版多数据源,支持application.yml属性控制开关 [√]

### 物联网设备数据采集与实时分析系统
- 设备数据采集：支持HTTP和MQTT两种方式接收设备上报的数据 [√]
- 实时数据处理：使用RabbitMQ实现异步数据处理，提高系统吞吐量 [√]
- 时序数据存储：使用InfluxDB存储设备数据和聚合数据 [√]
- 数据聚合：定时聚合设备数据，计算平均值等统计指标 [√]
- 异常检测：基于Redis实现设备异常检测，包括温度、湿度和状态异常 [√]
- 离线检测：基于Redis实现设备离线检测，及时发现离线设备 [√]
- 动态配置：支持动态调整系统配置，如数据采集频率、异常阈值等 [√]

## 技术栈

- **Spring Boot**：项目框架
- **Spring AMQP**：RabbitMQ集成
- **InfluxDB**：时序数据库
- **Redis**：缓存和状态存储
- **Lombok**：简化Java代码
- **Hutool**：工具类库

## 接口说明

### 设备数据接口

- **POST /api/device-data**：接收设备上报的数据
  - 请求体：DeviceData对象
  - 响应：无

### MQTT设备数据接口

- **POST /api/mqtt/device-data**：接收MQTT设备上报的数据
  - 请求体：DeviceData对象
  - 响应：无

### 动态配置接口

- **GET /api/config**：获取当前配置
  - 响应：DynamicConfig对象

- **PUT /api/config**：更新配置
  - 请求体：DynamicConfig对象
  - 响应：无

## 配置说明

### RabbitMQ配置

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
    virtual-host: /
    listener:
      simple:
        concurrency: 5
        max-concurrency: 10
        prefetch: 1
```

### InfluxDB配置

```yaml
influxdb:
  url: http://localhost:8086
  token: my-token
  org: my-org
  bucket: my-bucket
```

### Redis配置

```yaml
spring:
  redis:
    host: localhost
    password: bdth-redis@2019
    port: 16379
```

### 动态配置

```yaml
base:
  boot:
    iot:
      data-collection-frequency: 5000
      anomaly-threshold: 3
      alert-cooldown: 60
      offline-threshold: 300
```

## 运行说明

1. 启动RabbitMQ服务
2. 启动InfluxDB服务
3. 启动Redis服务
4. 运行BaseBootApplication类
5. 使用Postman或其他工具测试接口

## 测试说明

运行DeviceDataTest类中的测试方法，测试系统功能。
