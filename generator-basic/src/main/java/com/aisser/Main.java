package com.aisser;

import com.aisser.cli.CommandExecutor;
import com.aisser.generator.StaticGenerator;

import java.io.File;

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
