package com.aisser.maker.cli.pattern;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/26-15:29
 */
public class TurnOnCommand implements Command{
    private Device device;

    public TurnOnCommand(Device device){
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOn();
    }
}
