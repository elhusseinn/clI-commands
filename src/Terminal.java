import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

class Parser {

    String commandName;
    String[] args;

    public boolean parse(String input){

        if(input != null) {
            String[] tokens = input.split(" (?=(([^'\"]*['\"]){2})*[^'\"]*$)");
            commandName = tokens[0];

            int i = 1, j;

            if (tokens.length >= 2 && tokens[1].contains("-")) {
                commandName += tokens[1];
                i = 2;
            }

            args = new String[tokens.length - i];
            for (j = 0; i < tokens.length; i++) {
                String s = tokens[i].replaceAll("\"", "");
                args[j] = s;
                j++;
            }

            return true;
        }
        return false;
    }

    public String getCommandName() {
        return commandName;
    }

    public String[] getArgs() {
        return args;
    }
}

public class Terminal {

    Parser parser;
    Path currentPath;

    public static String currentDirectory = "C:\\Users\\" + System.getProperty("user.name") + "\\";

    Terminal() {
        this.parser = new Parser();
        currentPath = Paths.get("").toAbsolutePath();
    }

    public void run(String input) {
        parser.parse(input);
    }

    public void updateCurrentPath(Path path) {
        currentPath = path;
    }


    public void echo(String args) {
        System.out.println(args);
    }

    public String pwd() {
        return currentPath.toString();
    }

    public void cd(String[] args) {
        try {
            if (args.length == 0) {
                String homeDir = System.getProperty("user.home");
                Path home = Paths.get(homeDir);
                updateCurrentPath(home);
            } else if (args.length == 1) {
                Path resolvedPath;
                try {
                    resolvedPath = currentPath.resolve(args[0]).toRealPath();
                    if (resolvedPath.toFile().isFile()) {
                        throw new IOException();
                    }
                    updateCurrentPath(resolvedPath);
                } catch (IOException e) {
                    System.out.println("Not a directory");
                }

            } else {
                System.out.println("Too many arguments");
            }
        } catch (NullPointerException ignored) {

        }

    }

    public void ls() {
        String[] pathNames;
        File f = new File(String.valueOf(currentPath));
        pathNames = f.list();
        for (int i = 0; i < pathNames.length; i++) {
            System.out.println(pathNames[i]);
        }

    }

    public void lsReverse() {
        String[] pathNames;
        File f = new File(String.valueOf(currentPath));
        pathNames = f.list();
        for (int i = pathNames.length - 1; i >= 0; i--) {
            System.out.println(pathNames[i]);
        }

    }

    public void mkdir(String[] file) {

        for (int i = 0; i < file.length; i++) {

            File f = new File(currentPath.toString() + "\\" + file[i]);
            if (f.exists()) {
                System.out.println("File is not Created !");

                System.out.println("There is already a directory with the same name");

            } else {
                for (int j = i; j <= i; j++) {
                    f.mkdir();

                    System.out.println(" File Created !");
                }
            }
        }

    }

    public static void touch(String str) throws IOException {
        long timestamp = System.currentTimeMillis();
        File file = new File(str);
        touch(file, timestamp);
    }

    public static void touch(File file, long timestamp) throws IOException {
        if (!file.exists()) {
            new FileOutputStream(file).close();
        }

        file.setLastModified(timestamp);
    }


   public void cp(String src, String des) throws IOException {
       Path sourceDirectory = Paths.get(src);
       Path targetDirectory = Paths.get(des);
       Files.copy(sourceDirectory, targetDirectory,StandardCopyOption.REPLACE_EXISTING);
       System.out.println("File copied successfully");

   }


    public static void copyDir(Path source, Path destination) throws IOException {

        if (Files.isDirectory(source)) {
            if (!Files.exists(destination)) {
                Files.createDirectories(destination);
                System.out.println("Directory is created : " + destination);
            }

            try (Stream<Path> paths = Files.list(source)) {
                paths.forEach(p -> copyDirWrapper(p, destination.resolve(source.relativize(p))));
            }

        } else {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void copyDirWrapper(Path source, Path destination) {

        try {
            copyDir(source, destination);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public void cpReverse(String src, String des) throws IOException {

        String source = src;
        String destination = des;

        try {

            copyDir(Paths.get(source), Paths.get(destination));

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Directory is copied !");
    }

    public void rm(String file) throws IOException {

        String ff = currentPath.toString() + "\\" + file;
        File f = new File( ff);
        if(f.delete()){
            System.out.println(f.getName() +" is deleted!");
        }else{
            System.out.println("Operation failed!");
        }

    }

    public void cat(String[] paths) throws IOException
    {

        StringBuilder str = new StringBuilder();
        for (String path : paths) {

            Scanner input = new Scanner(new File(currentPath.toString() + "\\" + path));
            while (input.hasNextLine()) {
                String line = input.nextLine();
                str.append(line);

            }
        }
        System.out.println(str.toString());
    }

    public void exit() {
        System.exit(0);
    }

    public void chooseCommandAction() throws IOException {
        switch (parser.getCommandName()) {
            case "echo":
                echo(parser.getArgs()[0]);
                break;
            case "pwd":
                System.out.println(pwd());
                break;
            case "cd": {
                cd(parser.getArgs());
                System.out.println(currentPath);
                break;
            }
            case "ls":
                ls();
                break;
            case "ls-r":
                lsReverse();
                break;

            case "mkdir":
                mkdir(parser.getArgs());
                break;

            case "touch":
                touch(parser.getArgs()[0]);
                break;
            case "cp":
                cp(parser.getArgs()[0], parser.getArgs()[1]);
                break;

            case "cp-r":
                cpReverse(parser.getArgs()[0], parser.getArgs()[1]);
                break;

            case "rm":
                rm(parser.getArgs()[0]);
                break;
            case "cat":
                cat(parser.getArgs());
                break;

            case "exit":

                exit();
                break;

            default:
                System.out.println("WRONG COMMAND ! TRY AGAIN");

        }

    } //This method will choose the suitable command method to be called

    // main function
    public static void main(String[] args) throws IOException {

        //Parser pars = new Parser();
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);
        String input = " ";
        while (input.length() != 0) {
            System.out.print('>');
            input = sc.nextLine();
            terminal.run(input);
            terminal.chooseCommandAction();
        }

    }
}