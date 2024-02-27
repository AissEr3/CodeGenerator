package com.aisser.maker;

import com.aisser.maker.meta.Meta;
import com.aisser.maker.meta.MetaManger;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/25-19:42
 */
public class Main {
    public static void main(String[] args) {
//        CommandExecutor commandExecutor = new CommandExecutor();
//        commandExecutor.doExecute(args);
        Meta metaInstance = MetaManger.getMetaInstance();
        System.out.println(metaInstance);
    }
}
