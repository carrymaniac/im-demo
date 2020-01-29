package com.gdou.im.protocol.data.response;

import lombok.Data;

/**
 * @ProjectName: demo
 * @Package: com.gdou.im.protocol.data.response
 * @ClassName: LoginResponse
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/1/28 1:37 下午
 * @Version:
 */
@Data
public class LoginResponse implements com.gdou.im.protocol.data.Data {
    private String userId;

    private String userName;

    private boolean success;

    private String reason;
}
