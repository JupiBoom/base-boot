package zz.hujing.baseboot.core.tenant.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import zz.hujing.baseboot.core.config.BaseBootProperties;
import zz.hujing.baseboot.core.tenant.TenantContext;

import java.util.Properties;

/**
 * 租户SQL拦截器
 * 自动在SELECT语句中添加tenant_id过滤条件
 * @author hujing
 */
@Slf4j
@Component
@EnableConfigurationProperties(BaseBootProperties.class)
@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, org.apache.ibatis.cache.CacheKey.class, BoundSql.class})
})
public class TenantSqlInterceptor implements Interceptor {

    /**
     * 租户ID字段名
     */
    private String tenantIdField;
    
    @Autowired
    public void setTenantIdField(BaseBootProperties baseBootProperties) {
        this.tenantIdField = baseBootProperties.getTenantKeyColumn();
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取当前租户标识
        String tenantKey = TenantContext.getTenantKey();
        if (tenantKey == null || tenantKey.isEmpty()) {
            // 没有租户标识，直接执行原SQL
            return invocation.proceed();
        }

        // 获取MappedStatement
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if (mappedStatement.getSqlCommandType() != SqlCommandType.SELECT) {
            // 非SELECT语句，直接执行原SQL
            return invocation.proceed();
        }

        // 获取BoundSql
        BoundSql boundSql;
        if (invocation.getArgs().length == 4) {
            // query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler)
            boundSql = mappedStatement.getBoundSql(invocation.getArgs()[1]);
        } else {
            // query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql)
            boundSql = (BoundSql) invocation.getArgs()[5];
        }

        // 改写SQL，添加tenant_id过滤条件
        String originalSql = boundSql.getSql();
        String rewrittenSql = addTenantFilter(originalSql, tenantKey);

        // 创建新的BoundSql
        BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), rewrittenSql, boundSql.getParameterMappings(), boundSql.getParameterObject());

        // 设置原BoundSql的metaParameters到新BoundSql中
        if (boundSql instanceof org.apache.ibatis.mapping.BoundSql) {
            // 复制参数映射
            for (org.apache.ibatis.mapping.ParameterMapping mapping : boundSql.getParameterMappings()) {
                String prop = mapping.getProperty();
                if (boundSql.hasAdditionalParameter(prop)) {
                    newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
                }
            }
        }

        // 创建新的MappedStatement
        MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));

        // 替换原MappedStatement
        if (invocation.getArgs().length == 4) {
            invocation.getArgs()[0] = newMs;
        } else {
            invocation.getArgs()[0] = newMs;
            invocation.getArgs()[5] = newBoundSql;
        }

        return invocation.proceed();
    }

    /**
     * 为SQL添加租户过滤条件
     * @param originalSql 原始SQL
     * @param tenantKey 租户标识
     * @return 改写后的SQL
     */
    private String addTenantFilter(String originalSql, String tenantKey) {
        String sql = originalSql.trim();
        String lowerCaseSql = sql.toLowerCase();
        
        // 检查是否包含UNION、ORDER BY等需要特殊处理的关键字
        if (lowerCaseSql.contains("union") || lowerCaseSql.contains("order by")) {
            // TODO: 更复杂的SQL处理逻辑
            return sql;
        }
        
        // 检查是否已经包含租户ID过滤条件
        if (lowerCaseSql.contains(tenantIdField + " = ") || lowerCaseSql.contains(tenantIdField + " in ")) {
            return sql;
        }
        
        int whereIndex = lowerCaseSql.indexOf("where");
        int fromIndex = lowerCaseSql.indexOf("from");
        
        if (whereIndex != -1) {
            // 有WHERE条件，在WHERE后添加tenant_id过滤
            return sql.substring(0, whereIndex + 6) + " " + tenantIdField + " = '" + tenantKey + "' and " + sql.substring(whereIndex + 6);
        } else if (fromIndex != -1) {
            // 没有WHERE条件，在FROM前添加WHERE条件
            return sql.substring(0, fromIndex) + " where " + tenantIdField + " = '" + tenantKey + "' " + sql.substring(fromIndex);
        } else {
            // 无法处理的SQL格式
            return sql;
        }
    }

    /**
     * 复制MappedStatement
     * @param ms 原MappedStatement
     * @param newSqlSource 新的SqlSource
     * @return 新的MappedStatement
     */
    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    /**
     * 内部类：SqlSource实现
     */
    private static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以通过properties配置租户ID字段名等
    }
}