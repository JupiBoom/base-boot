package zz.hujing.baseboot.core.tenant.util;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 租户加密工具
 * 用于加密租户的敏感信息（如数据库密码）
 * @author hujing
 */
@Component
public class TenantEncryptionUtil {

    /**
     * 加密密钥
     */
    @Value("${base.boot.tenant.encrypt.key}")
    private String encryptKey;

    /**
     * 加密文本
     * @param text 待加密文本
     * @return 加密后的文本
     */
    public String encrypt(String text) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(encryptKey);
        return encryptor.encrypt(text);
    }

    /**
     * 解密文本
     * @param encryptedText 加密后的文本
     * @return 解密后的文本
     */
    public String decrypt(String encryptedText) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(encryptKey);
        return encryptor.decrypt(encryptedText);
    }
}