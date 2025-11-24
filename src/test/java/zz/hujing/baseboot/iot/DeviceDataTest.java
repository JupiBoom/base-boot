package zz.hujing.baseboot.iot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import zz.hujing.baseboot.iot.domain.DeviceData;

import java.time.Instant;

@SpringBootTest
@AutoConfigureMockMvc
public class DeviceDataTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDeviceDataEndpoint() throws Exception {
        // 创建测试设备数据
        DeviceData deviceData = new DeviceData();
        deviceData.setDeviceId("device-123");
        deviceData.setTemperature(25.5);
        deviceData.setHumidity(60.0);
        deviceData.setStatus("online");
        deviceData.setTimestamp(Instant.now().toEpochMilli());

        // 发送POST请求到设备数据接口
        mockMvc.perform(MockMvcRequestBuilders.post("/api/device-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceData)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testMqttDeviceDataEndpoint() throws Exception {
        // 创建测试设备数据
        DeviceData deviceData = new DeviceData();
        deviceData.setDeviceId("device-456");
        deviceData.setTemperature(28.0);
        deviceData.setHumidity(65.0);
        deviceData.setStatus("online");

        // 发送POST请求到MQTT设备数据接口
        mockMvc.perform(MockMvcRequestBuilders.post("/api/mqtt/device-data")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deviceData)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDynamicConfigEndpoint() throws Exception {
        // 获取当前配置
        mockMvc.perform(MockMvcRequestBuilders.get("/api/config"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
