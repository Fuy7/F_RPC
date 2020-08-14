package top.fuy.rpc.exception;

/**
 * 序列化异常
 *
 * @author fuy
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }
}
