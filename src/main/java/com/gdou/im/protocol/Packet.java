package com.gdou.im.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @ProjectName: demo
 * @Package: com.gdou.im.dataobject
 * @ClassName: porotocol
 * @Author: carrymaniac
 * @Description: 协议格式
 * @Date: 2020/1/28 12:04 下午
 * @Version:
 */
@Data
public class Packet {
    /**
     * 协议版本
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;
    /**
     * 指令
     */
    private int command;
    /**
     * 真正的内容正文，用json数据书写
     */
    private String data;

}
