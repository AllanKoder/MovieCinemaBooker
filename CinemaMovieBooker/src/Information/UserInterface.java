
import java.io.*;
import java.util.*;

class UserInterface {
    public static void main(String[] args) {
        //Starts at -1 because it is automatically added a value. 
        int TimeLength = -1;

        //List of all the times because they can add more values to it. 
        List<String> Times = new ArrayList<String>();

        Scanner ConsoleInput = new Scanner(System.in);

        System.out.println("What is the name of the Cinema?");
        String CinemaName = ConsoleInput.nextLine();

        System.out.println("What are the amount of seats in length?");
        int InputX = ConsoleInput.nextInt();
        System.out.println("What are the amount of seats in width?");
        int InputY = ConsoleInput.nextInt();

        //create a cinema
        CinemaData Cinema = new CinemaData(InputX, InputY, CinemaName);
        Cinema.ClientGUI();
        String ready;
        do{
            System.out.println("Is the pricing ready?(yes/no)");
            ready = ConsoleInput.next();
        }
        while(!ready.equalsIgnoreCase("yes"));

        ConsoleInput.nextLine();
        do {
            //repeat until person is done
            System.out.println("Set the timings of the day, need to have at least one time, ex:[6:00PM]?('done' if done)");
            ready = ConsoleInput.nextLine();
            TimeLength++;
            if(!ready.equalsIgnoreCase("done"))
                Times.add(ready);
        }
        while(!ready.equalsIgnoreCase("done"));

        String[] TimeArray = new String[Times.size()];
        Times.toArray(TimeArray);
        //convert the list to an array.

        Cinema.SetTime(TimeArray,TimeLength);

        //the pricing is now complete and the buttons will now serve as booking rather than pricing. 
        Cinema.CustomerReady = true;
        Cinema.CinemaGUI();
        ready = "";
        while(!ready.equalsIgnoreCase("done")){
            //repeat until person is done the program
            System.out.println("say 'done' when the program is finished and you want the status report in CinemaData.txt");
            ready = ConsoleInput.next();
        }
        Cinema.WriteFinalCinemaData();

    }
}