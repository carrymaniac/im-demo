package com.gdou.im.protocol.command;

/**
 * @ProjectName: demo
 * @Package: com.gdou.im.protocol.command
 * @ClassName: Command
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/1/28 12:40 下午
 * @Version:
 */
public interface Command {
    Integer LOGIN_REQUEST = 1;

    Integer LOGIN_RESPONSE = 2;

    Integer MESSAGE_REQUEST = 3;

    Integer MESSAGE_RESPONSE = 4;

    Integer SYSTEM_MESSAGE_RESPONSE = -1;
}
