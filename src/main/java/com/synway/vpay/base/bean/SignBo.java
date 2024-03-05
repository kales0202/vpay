package com.synway.vpay.base.bean;

import com.synway.vpay.base.exception.SignatureException;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Objects;

@Data
public abstract class SignBo {

    /**
     * 接口请求方计算出来的数据签名
     *
     * @since 0.1
     */
    @NotBlank
    protected String sign;

    /**
     * 调用此方法计算数据签名
     *
     * @param key 密钥
     * @return 计算出来的签名，用于进行验签，比较是否和“sign”值是否一致
     * @since 0.1
     */
    public abstract String calculateSign(String key);

    /**
     * 验签，比较计算出来的签名是否和“sign”值是否一致
     * 验证失败则抛出SignatureException异常
     *
     * @param key 密钥
     * @since 0.1
     */
    public final void verifySign(String key) {
        String checkSign = this.calculateSign(key);
        if (!Objects.equals(this.sign, checkSign)) {
            throw new SignatureException();
        }
    }
}
