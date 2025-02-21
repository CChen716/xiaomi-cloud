package entity;

import lombok.Data;

/** 发送请求 服务信息类
 * @author cg
 * @Date 2025/1/9 16:28
 */
@Data
public class ServerInfo {
    /**
     *服务IP
     */
    private Integer port;
    /**
     * 服务端口
     */
    private String host;
}
