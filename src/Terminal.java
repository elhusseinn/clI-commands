import java.lang.reflect.Array;
import java.util.Arrays;

class Parser {
    String commandName;
    String[] args;
    int argSize;
    Parser(){
        commandName = "";
        args = new String[100];
    }

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
            for (j = 0; i < tokens.length; i++) {
                args[j] = tokens[i];
                j++;
            }
            argSize = j;

            return true;
        }
        return false;
    }
    public String getCommandName(){
        return commandName;
    }
    public String[] getArgs(){
            return Arrays.copyOfRange(args, 0, argSize);  // return the elements of the array and ignores the null

    }
}


public class Terminal {
    Parser parser;
    Terminal(){
        parser = new Parser();
    }
    public void run(String input){

        parser.parse(input);
        System.out.println(parser.getCommandName());
        System.out.println(Arrays.toString(parser.getArgs()));

    }
    //Implement each command in a method, for example:
  //  public String pwd(){...}
    //public void cd(String[] args){...}
    // ...
//This method will choose the suitable command method to be called
    //public void chooseCommandAction(){...}
    public static void main(String[] args){
        Terminal terminal = new Terminal();
        terminal.run("cd -r hello world");


    }


}



