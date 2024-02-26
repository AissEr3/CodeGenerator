package com.aisser.maker.cli.pattern;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/26-15:26
 */
public class TurnOffCommand implements Command{
    private Device device;

    public TurnOffCommand(Device device){
        this.device = device;
    }

    @Override
    public void execute() {
        device.turnOff();;
    }
}
