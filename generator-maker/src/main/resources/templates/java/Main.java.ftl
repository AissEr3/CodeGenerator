package ${basePackage};

import ${basePackage}.cli.CommandExecutor;

/**
* @author ${author}
* @created by ${author}
*/
public class Main {
    public static void main(String[] args) {
        CommandExecutor commandExecutor = new CommandExecutor();
        commandExecutor.doExecute(args);
    }
}
