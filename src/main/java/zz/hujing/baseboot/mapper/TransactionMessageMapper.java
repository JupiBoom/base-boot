package zz.hujing.baseboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import zz.hujing.baseboot.domain.entity.TransactionMessage;

/**
 * 事务消息Mapper
 */
@Mapper
public interface TransactionMessageMapper extends BaseMapper<TransactionMessage> {
}
