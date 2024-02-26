package com.aisser.maker.cli;

import com.aisser.maker.cli.command.ConfigCommand;
import com.aisser.maker.cli.command.GenerateCommand;
import com.aisser.maker.cli.command.ListCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/26-15:42
 */
@Command(name = "aisser", mixinStandardHelpOptions = true)
public class CommandExecutor implements Runnable{
    private final CommandLine commandLine;

    {
        commandLine = new CommandLine(this)
                .addSubcommand(new GenerateCommand())
                .addSubcommand(new ConfigCommand())
                .addSubcommand(new ListCommand());
    }

    @Override
    public void run() {
        System.out.println("请输入具体命令，或输入 --help查看命令提示。");
    }

    public Integer doExecute(String[] args){
        return commandLine.execute(args);
    }
}
