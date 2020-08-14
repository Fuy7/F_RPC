package top.fuy.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 协议中的包状态字段
 * @author fuy
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;

}
