package com.aisser.maker.cli.example;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;


// |2| 使用Command注解设置命令名称，版本号，指定是否有注释选项
@Command(name = "ASCIIArt", version = "ASCIIArt 1.0", mixinStandardHelpOptions = true)
public class ASCIIArt implements Runnable { // |1| 必须实现Runnable或Collable接口

    // |3| 注解将字段设置为命令选项，可以给选项设置名称和描述
    @Option(names = { "-s", "--font-size" }, description = "Font size")
    int fontSize;

    // |4| 注解将字段设置为命令行参数，可以指定默认值、描述等信息
    @Parameters(paramLabel = "<word>", defaultValue = "Hello, picocli",
               description = "Words to be translated into ASCII art.")
    // |5| Picocli会将命令行参数转换为强类型值，并自动注入到注解字段中
    private String[] words = { "Hello,", "picocli" };

    @Override
    // |6| 定义业务逻辑，即如何执行命令
    public void run() {
        System.out.println("fontSize = " + fontSize);
        System.out.println("words:" + String.join(",",words));
    }

    public static void main(String[] args) {
        // |7| 使用命令，接收的参数在args中
        int exitCode = new CommandLine(new ASCIIArt()).execute(args);
        System.exit(exitCode); // |8|
    }
}