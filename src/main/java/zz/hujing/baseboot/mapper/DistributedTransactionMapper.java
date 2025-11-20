package zz.hujing.baseboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import zz.hujing.baseboot.domain.entity.DistributedTransaction;

/**
 * 分布式事务Mapper
 */
@Mapper
public interface DistributedTransactionMapper extends BaseMapper<DistributedTransaction> {
}
