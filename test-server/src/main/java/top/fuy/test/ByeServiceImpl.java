package top.fuy.test;

import top.fuy.rpc.annotation.Service;
import top.fuy.rpc.api.ByeService;

/**
 * @author fuy
 */
@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye, " + name;
    }
}
