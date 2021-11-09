import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

class Parser {
    String commandName;
    String[] args;

    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input){

        if(input != null) {
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
    public String getCommandName(){
        return commandName;
    }
    public String[] getArgs(){
            return args ;
    }
}


public class Terminal {
    Parser parser;
    Path currentPath;
    Terminal(){
        parser = new Parser();
        currentPath = Paths.get("").toAbsolutePath();
    } // constructor
    public void run(String input){
        parser.parse(input);
    }

    public void updateCurrentPath(Path path){
        currentPath = path;
    }

    public String  pwd() {
        return currentPath.toString();
    }

    public void cd(String[] args){
        try {
            if(args.length == 0){
                String homeDir = System.getProperty( "user.home" );
                Path home = Paths.get(homeDir);
                updateCurrentPath(home);
            }
            else if(args.length == 1){
                Path resolvedPath;
                try {
                    resolvedPath = currentPath.resolve(args[0]).toRealPath();
                    if(resolvedPath.toFile().isFile()){
                        throw new IOException();
                    }
                    updateCurrentPath(resolvedPath);
                } catch (IOException e) {
                    System.out.println("Not a directory");
                }

            }
            else {
                System.out.println("Too many arguments");
            }
        }
        catch (NullPointerException ignored){

        }

    }

    public void ls(){
        String[] pathNames;
        File f = new File(String.valueOf(currentPath));
        pathNames = f.list();
        for (int i =0; i < pathNames.length; i++) {
            System.out.println(pathNames[i]);
        }

    }
    public void lsReverse(){
        String[] pathNames;
        File f = new File(String.valueOf(currentPath));
        pathNames = f.list();
        for (int i =pathNames.length - 1; i >= 0; i--) {
            System.out.println(pathNames[i]);
        }

    }
    public void exit(){
        System.exit(0);
    }


    public void chooseCommandAction(){
        switch (parser.getCommandName()) {
            case "pwd" -> System.out.println(pwd());
            case "cd" -> {
                cd(parser.getArgs());
                System.out.println(currentPath);
            }
            case "ls" -> ls();
            case "ls-r" -> lsReverse();
            case "exit" -> exit();
        }

    } //This method will choose the suitable command method to be called

    // main function
    public static void main(String[] args){
        Terminal terminal = new Terminal();
        Scanner sc = new Scanner(System.in);
        String input = " ";
        while(input.length() != 0){
            input = sc.nextLine();
            terminal.run(input);
            terminal.chooseCommandAction();
        }



    }


}



