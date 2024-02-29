package com.aisser.maker.generator;

import java.io.*;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/27-20:07
 */
public class JarGenerator {

    public static void doGenerate(String projectDir) throws IOException, InterruptedException {
        // windows的clean命令
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true";
        // 其它操作系统的clean命令
        String otherMavenCommand = "mvn clean package -DskipTest=true";
        String mavenCommand = winMavenCommand;

        //
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectDir));

        Process process = processBuilder.start();

        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while((line = reader.readLine()) != null){
            System.out.println(line);
        }

        int exitCode = process.waitFor();
        System.out.println("退出码："+exitCode);
    }

}
