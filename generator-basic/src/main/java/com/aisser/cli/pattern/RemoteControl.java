package com.aisser.cli.pattern;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/26-15:29
 */
public class RemoteControl {
    private Command command;

    public RemoteControl setCommand(Command command){
        this.command = command;
        return this;
    }

    public void pressButton(){
        command.execute();
    }
}
