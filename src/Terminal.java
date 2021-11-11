
import java.io.*;
<<<<<<< HEAD
import java.nio.file.*;

//import java.nio.file.Files;
//import java.nio.file.Path; 
//import java.nio.file.Paths;
=======
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
>>>>>>> 155e196676100a9ce037fb3bf7c578f565be3455
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class Parser {

    String commandName;
    String[] args;

    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input) {

        if (input != null) {
            String[] tokens = input.split(" ");
            commandName = tokens[0];

            int i = 1, j;

            if (tokens.length >= 2 && tokens[1].contains("-")) {
                commandName += tokens[1];
                i = 2;
            }

            args = new String[tokens.length - i];
            for (j = 0; i < tokens.length; i++) {
                args[j] = tokens[i];
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
<<<<<<< HEAD
=======
}

public class Terminal {

    Parser parser;
    Path currentPath;

    String currentDirectory = "C:\\Users\\" + System.getProperty("user.name") + "\\";

    Terminal() {
        this.parser = new Parser();
        // parser = new Parser();
        currentPath = Paths.get("").toAbsolutePath();
    } // constructor

    public void run(String input) {
        parser.parse(input);
    }

    public void updateCurrentPath(Path path) {
        currentPath = path;
    }

    public void newFile(String des) {
        try {
            File f = new File(des);
            f.createNewFile();
        } catch (IOException ex) {
            System.out.println("ERROR!");
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * private void check(String file){
     *
     * }
     */
    private void copyFile(File src, File des) throws IOException {

        InputStream in = new FileInputStream(src);

        OutputStream out = new FileOutputStream(des);

        int size;

        byte[] st = new byte[1024];

        while ((size = in.read(st)) > 0) {

            out.write(st, 0, size);
        }

        in.close();

        out.close();
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

    public String mkdir(String file) {
        File f = new File(file);
        if (!f.exists()) {
            f.mkdir();
        } else {
            System.out.println("There is already a directory with the same name");
        }

        return "";
    }

    public String cp(String src, String des) throws FileNotFoundException, IOException {

        if (!src.contains(":") && !src.equals("")) {
            src = currentDirectory + src + "\\";
        }
        if (!des.contains(":") && !des.equals("")) {
            des = currentDirectory + des + "\\";
        }
        File source = new File(src);
        File destination = new File(des);
        copyFile(source, destination);

        return "";
    }

    public String cpReverse(String src, String des) throws IOException{
        File source = new File(src);
        File destination = new File(des);
        if(source.isDirectory()){
            if(!destination.exists()){
                destination.mkdir();
            }
            String[] f = source.list();
            for(int i =0 ; i<f.length ;i++){
                cpReverse(src,des);
            }
        }else{
            copyFile(source,destination);
        }
        return "";
    }

    public String rm(String file) {

        File f = new File(file);
        if (!f.isFile()) {
            System.out.println(file + " is not a file");
        }
        if (f.delete()) {
            System.out.println(f.getName() + " is deleted");
        }

        return "";
    }

    public void exit() {
        System.exit(0);
    }

    public void chooseCommandAction() throws IOException {
        switch (parser.getCommandName()) {
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
                mkdir(parser.getArgs()[0]);
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

            case "exit":

                exit();
                break;

            default :
                System.out.println( "WRONG COMMAND ! TRY AGAIN");

        }

    } //This method will choose the suitable command method to be called

    // main function
    public static void main(String[] args) throws IOException {

        //Parser pars = new Parser();
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);
        String input = " ";
        while (input.length() != 0) {
            input = sc.nextLine();
            terminal.run(input);
            terminal.chooseCommandAction();
        }

    }

  /*  String mkdir() throws IOException {

        Terminal t = new Terminal();
        int size = parser.getArgs().length;
        String in = "";
        for (int i = 0; i < size - 1; i++) {
            in = t.mkdir(parser.args[i]) + in;
        }
        return in;
    }

    String cp() throws IOException {
        Terminal t = new Terminal();
        int size = parser.getArgs().length;
        String in = "";
        for (int i = 0; i < size - 1; i++) {
            in = t.cp(parser.args[i], parser.args[size - 1]) + in;
        }
        return in;
    }

    String cpReverse() throws IOException {
        Terminal t = new Terminal();
        int size = parser.getArgs().length;
        String in = "";
        for (int i = 0; i < size - 1; i++) {
            in = t.cpReverse(parser.args[i], parser.args[size - 1]) + in;
        }
        return in;
    }

    String rm() throws IOException {

        Terminal t = new Terminal();
        int size = parser.getArgs().length;
        String in = "";
        for (int i = 0; i < size - 1; i++) {
            in = t.rm(parser.args[i]) + in;
        }
        return in;
    }*/

>>>>>>> 155e196676100a9ce037fb3bf7c578f565be3455
}

public class Terminal {

    Parser parser;
    Path currentPath;

    public static String currentDirectory = "C:\\Users\\" + System.getProperty("user.name") + "\\";

    Terminal() {
        this.parser = new Parser();
        // parser = new Parser();
        currentPath = Paths.get("").toAbsolutePath();
    } // constructor

    public void run(String input) {
        parser.parse(input);
    }

    public void updateCurrentPath(Path path) {
        currentPath = path;
    }

    public void newFile(String des) {
        try {
            File f = new File(des);
            f.createNewFile();
        } catch (IOException ex) {
            System.out.println("ERROR!");
            Logger.getLogger(Terminal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static String check(String Path) throws IOException {
        Path currentDirect = Paths.get(currentDirectory);
        Path pathGiven = Paths.get(Path);
        String handledPath = currentDirect.resolve(pathGiven).toFile().getCanonicalPath();

        if (Files.exists(Paths.get(handledPath))) {
            return handledPath;
        } else if (Files.exists(Paths.get(handledPath.substring(0, handledPath.lastIndexOf("\\"))))) {
            return handledPath;
        }

        return "";
    }
//    private void copyFile(File src, File des) throws IOException {
//
//        InputStream in = new FileInputStream(src);
//
//        OutputStream out = new FileOutputStream(des);
//
//        int size;
//
//        byte[] st = new byte[1024];
//
//        while ((size = in.read(st)) > 0) {
//
//            out.write(st, 0, size);
//        }
//
//        in.close();
//
//        out.close();
//    }

    public void echo(String[] args) {
        System.out.println(args.toString());
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

    public String mkdir(String file) {
        File f = new File(file);
        if (!f.exists()) {
            if (f.mkdir()) {
                System.out.println("File Created !");
            } else {
                System.out.println("File is not Created !");
            }

        } else {
            System.out.println("There is already a directory with the same name");
        }

        return "";
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

    public String cp(String src, String des) throws FileNotFoundException, IOException {

        if (!src.contains(":") && !src.equals("")) {
            src = currentDirectory + src + "\\";
        }
        if (!des.contains(":") && !des.equals("")) {
            des = currentDirectory + des + "\\";
        }
        File source = new File(src);
        File destination = new File(des);

        InputStream in = new FileInputStream(source);

        OutputStream out = new FileOutputStream(destination);

        int size;

        byte[] st = new byte[1024];

        while ((size = in.read(st)) > 0) {

            out.write(st, 0, size);
        }

        in.close();

        out.close();

        return "";
    }

    public String cpReverse(String src, String des) throws IOException {

//        Path source = Paths.get(src);
//        Path destination = Paths.get(des);
        File source = new File(src);
        File destination = new File(des);
        //FileUtils.copyDirectory(source, destination);
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }
            String[] f = source.list();
            for (int i = 0; i < f.length; i++) {
                cpReverse(src, des);
            }
        } else {
            InputStream in = new FileInputStream(source);

            OutputStream out = new FileOutputStream(destination);

            int size;

            byte[] st = new byte[1024];

            while ((size = in.read(st)) > 0) {

                out.write(st, 0, size);
            }

            in.close();

            out.close();
        }
        return "";
    }

    public String rm(String file) {

        if (!file.contains(":") && !file.equals("")) {
            file = currentDirectory + file + "\\";
        }

        File f = new File(file);

        if (f.isDirectory()) {
            File[] ds = f.listFiles();
            for (File d : ds) {
                rm(d.toString());
            }
        }

        f.delete();
        return "";
    }

    public String cat(String[] paths) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (String path : paths) {
            Scanner input = new Scanner(new File(check(path)));

            while (input.hasNextLine()) {
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

    public void chooseCommandAction() throws IOException {
        switch (parser.getCommandName()) {
            case "echo":
                echo(parser.getArgs());
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
                mkdir(parser.getArgs()[0]);
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
            input = sc.nextLine();
            terminal.run(input);
            terminal.chooseCommandAction();
        }

    }

    /*  String mkdir() throws IOException {

        Terminal t = new Terminal();
        int size = parser.getArgs().length;
        String in = "";
        for (int i = 0; i < size - 1; i++) {
            in = t.mkdir(parser.args[i]) + in;
        }
        return in;
    }

    String cp() throws IOException {
        Terminal t = new Terminal();
        int size = parser.getArgs().length;
        String in = "";
        for (int i = 0; i < size - 1; i++) {
            in = t.cp(parser.args[i], parser.args[size - 1]) + in;
        }
        return in;
    }

    String cpReverse() throws IOException {
        Terminal t = new Terminal();
        int size = parser.getArgs().length;
        String in = "";
        for (int i = 0; i < size - 1; i++) {
            in = t.cpReverse(parser.args[i], parser.args[size - 1]) + in;
        }
        return in;
    }

    String rm() throws IOException {

        Terminal t = new Terminal();
        int size = parser.getArgs().length;
        String in = "";
        for (int i = 0; i < size - 1; i++) {
            in = t.rm(parser.args[i]) + in;
        }
        return in;
    }*/
}