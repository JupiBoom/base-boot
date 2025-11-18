package zz.hujing.baseboot.core.tenant.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import zz.hujing.baseboot.core.tenant.model.Tenant;

/**
 * 租户信息Mapper
 * @author hujing
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
}
