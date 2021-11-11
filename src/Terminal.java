import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Terminal {
    Parser parser = new Parser();
    Path currentPath = Paths.get("").toAbsolutePath();

    Terminal() {
    }

    public void run(String input) {
        this.parser.parse(input);
    }

    public void updateCurrentPath(Path path) {
        this.currentPath = path;
    }

    public void echo(String[] args) {
        System.out.println(args.toString());
    }

    public String pwd() {
        return this.currentPath.toString();
    }

    public void cd(String[] args) {
        try {
            if (args.length == 0) {
                String homeDir = System.getProperty("user.home");
                Path home = Paths.get(homeDir);
                this.updateCurrentPath(home);
            } else if (args.length == 1) {
                try {
                    Path resolvedPath = this.currentPath.resolve(args[0]).toRealPath();
                    if (resolvedPath.toFile().isFile()) {
                        throw new IOException();
                    }

                    this.updateCurrentPath(resolvedPath);
                } catch (IOException var4) {
                    System.out.println("Not a directory");
                }
            } else {
                System.out.println("Too many arguments");
            }
        } catch (NullPointerException var5) {
        }

    }

    public void ls() {
        File f = new File(String.valueOf(this.currentPath));
        String[] pathNames = f.list();

        for(int i = 0; i < pathNames.length; ++i) {
            System.out.println(pathNames[i]);
        }

    }

    public void lsReverse() {
        File f = new File(String.valueOf(this.currentPath));
        String[] pathNames = f.list();

        for(int i = pathNames.length - 1; i >= 0; --i) {
            System.out.println(pathNames[i]);
        }

    }

    public static void touch(File file) throws IOException{
        long timestamp = System.currentTimeMillis();
        touch(file, timestamp);
    }

    public static void touch(File file, long timestamp) throws IOException{
        if (!file.exists()) {
            new FileOutputStream(file).close();
        }

        file.setLastModified(timestamp);
    }

    public String cat(String[] paths) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (String path : paths)
        {
            Scanner input = new Scanner(new File(handlePath(path)));

            while (input.hasNextLine())
            {
                String line = input.nextLine();
                builder.append(line);
                System.out.println(line);
            }
        }
        return builder.toString();
    }

    public void exit() {
        System.exit(0);
    }

    public void chooseCommandAction() {
        String var1 = this.parser.getCommandName();
        byte var2 = -1;
        switch(var1.hashCode()) {
            case 3169:
                if (var1.equals("cd")) {
                    var2 = 1;
                }
                break;
            case 3463:
                if (var1.equals("ls")) {
                    var2 = 2;
                }
                break;
            case 111421:
                if (var1.equals("pwd")) {
                    var2 = 0;
                }
                break;
            case 3127582:
                if (var1.equals("exit")) {
                    var2 = 4;
                }
                break;
            case 3329452:
                if (var1.equals("ls-r")) {
                    var2 = 3;
                }
        }

        switch(var2) {
            case 0:
                System.out.println(this.pwd());
                break;
            case 1:
                this.cd(this.parser.getArgs());
                System.out.println(this.currentPath);
                break;
            case 2:
                this.ls();
                break;
            case 3:
                this.lsReverse();
                break;
            case 4:
                this.exit();
        }

    }

    private static String handlePath(String givenPath) throws IOException {
        Path currentDirectory = Paths.get(Main.currentDirectory);
        Path pathGiven = Paths.get(givenPath);
        String handledPath = currentDirectory.resolve(pathGiven).toFile().getCanonicalPath();

        if(Files.exists(Paths.get(handledPath)))
            return handledPath;

        else if(Files.exists(Paths.get(handledPath.substring(0, handledPath.lastIndexOf("\\"))))){
            return handledPath;
        }

        return "";
    }

    public static void main(String[] args) {
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);
        String input = " ";

        while(input.length() != 0) {
            input = sc.nextLine();
            terminal.run(input);
            terminal.chooseCommandAction();
        }

    }

}
