package com.gdou.im.protocol.data.request;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: demo
 * @Package: com.gdou.im.dataobject
 * @ClassName: MessagePacket
 * @Author: carrymaniac
 * @Description: 消息包
 * @Date: 2020/1/28 12:09 下午
 * @Version:
 */
@Data
@NoArgsConstructor
public class MessageRequest implements com.gdou.im.protocol.data.Data {
    private String message;
    private String toId;
    private String fromId;
}
