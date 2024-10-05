package cn.tedu.tmall.common.ex;

import cn.tedu.tmall.common.enumerator.ServiceCode;
import lombok.Getter;

/**
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 14:05
 *
 * 业务异常,当我们的业务代码出问题返回自定义的异常,该异常对象包含错误码(枚举值)和错误信息
 */
public class ServiceException extends RuntimeException {

    @Getter
    private ServiceCode serviceCode;

    /**
     * 创建业务异常对象
     *
     * @param serviceCode 业务状态码
     * @param message     描述文本
     */
    public ServiceException(ServiceCode serviceCode, String message) {
        super(message);
        this.serviceCode = serviceCode;
    }

}
