package com.aisser.cli.pattern;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/26-15:34
 */
public class Client {
    public static void main(String[] args) {
        Device tv = new Device("TV");
        Device stereo = new Device("Stereo");

        TurnOnCommand turnOn = new TurnOnCommand(tv);
        TurnOffCommand turnOff = new TurnOffCommand(stereo);

        RemoteControl remote = new RemoteControl();
        remote.setCommand(turnOn).pressButton();
        remote.setCommand(turnOff).pressButton();
    }
}
