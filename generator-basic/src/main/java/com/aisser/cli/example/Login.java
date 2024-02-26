package com.aisser.cli.example;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.sun.media.sound.SoftTuning;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.concurrent.Callable;

@Command(name = "ASCIIArt", version = "ASCIIArt 1.0", mixinStandardHelpOptions = true)
class Login implements Callable<Integer> {
    @Option(names = {"-u", "--user"}, description = "User name")
    String user;

    // interactive : 支持交互式输入；arity : 可接受的参数个数
    @Option(names = {"-p", "--password"}, arity = "0..1",description = "Passphrase", interactive = true)
    String password;

    @Option(names = {"-cp", "checkPassword"}, description = "checkPassword", interactive = true)
    String checkPassword;

    public Integer call(){
        System.out.println("username = " + user);
        System.out.println("password = " + password);
        System.out.println("checkPassword = " + checkPassword);
        return 0;
    }

    public static void main(String[] args){
        String[] ars = {"-u","root","-p","123"};
        Login login = new Login();
        int execute = new CommandLine(login).execute(ars);

        System.exit(execute);
    }

}