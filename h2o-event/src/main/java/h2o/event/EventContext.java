package h2o.event;

import h2o.common.lang.NBytes;

import java.time.Duration;

public interface EventContext {

    /**
     * 确认收到消息
     */
    void confirm();

    /**
     * 已收到但不会处理
     */
    void reject();

    /**
     * 已收到一定时间内不要再发送
     * @param timeout
     */
    void hide( Duration timeout );

    /**
     * 处理完成
     * @param r 返回结果
     */
    void reply( NBytes r );

    /**
     * 处理完成
     * @param ok 是否处理成功
     */
    void complete( boolean ok  );

}
