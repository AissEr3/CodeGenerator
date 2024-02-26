package com.aisser.maker;

import com.aisser.maker.cli.CommandExecutor;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/25-19:42
 */
public class Main {
    public static void main(String[] args) {
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}
