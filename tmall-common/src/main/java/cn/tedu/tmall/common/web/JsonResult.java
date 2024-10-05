package cn.tedu.tmall.common.web;

import cn.tedu.tmall.common.enumerator.ServiceCode;
import cn.tedu.tmall.common.ex.ServiceException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author YiRunDong
 * @version 1.0
 * @date 2024/10/5 13:49
 *
 * 服务器端的统一响应类型(json字符串对应的类),有状态码,提示信息,数据
 * 要么成功,要么失败,成功就是ok,可以选择带不带返回数据,失败就是fail肯定就不带数据了
 */

@Data
public class JsonResult {
    /**
     * 操作结果的状态码（状态标识）
     */
    @ApiModelProperty("业务状态码")
    private Integer state;

    /**
     * 操作失败时的提示文本
     */
    @ApiModelProperty("提示文本")
    private String message;

    /**
     * 操作成功时响应的数据
     */
    @ApiModelProperty("数据")
    private Object data;

    /**
     * 生成表示"成功"的响应对象
     *
     * @return 表示"成功"的响应对象
     */
    public static JsonResult ok() {
        return ok(null);
    }

    /**
     * 生成表示"成功"的响应对象，此对象中将包含响应到客户端的数据
     *
     * @param data 响应到客户端的数据
     * @return 表示"成功"的响应对象
     */
    public static JsonResult ok(Object data) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.state = ServiceCode.OK.getValue();
        jsonResult.data = data;
        return jsonResult;
    }

    /**
     * 生成表示"失败"的响应对象
     *
     * @param e 业务异常
     * @return 表示"失败"的响应对象
     */
    public static JsonResult fail(ServiceException e) {
        return fail(e.getServiceCode(), e.getMessage());
    }

    /**
     * 生成表示"失败"的响应对象
     *
     * @param serviceCode 业务状态码
     * @param message     提示文本
     * @return 表示"失败"的响应对象
     */
    public static JsonResult fail(ServiceCode serviceCode, String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.state = serviceCode.getValue();
        jsonResult.message = message;
        return jsonResult;
    }
}
