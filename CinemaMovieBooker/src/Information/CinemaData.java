
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
//Program to custom build a theatre system using the length and width, pricing of the client. 
//Green means available seats, yellow means at least 1 seat is booked, red means all seats are booked. 
public class CinemaData {
    //Setting the basic variables needed for the Cinema 
    String name = "Cinema";
    int xSize, ySize;
    boolean TakenSeat[];
    String SeatPerson_Time[];
    JButton AllSeatButtons[];
    JButton PriceButtons[];
    double SeatCosts[];
    //All the times of a seat, 2D array because a single seat can have many times to book. 
    String SeatTimes[][];
    double Profit = 0;
    int AllBookingTimes = 0;
    public boolean CustomerReady = false;
    public boolean CinemaFinished = false;
    int CustomerAmount = 0;
    public CinemaData(int x, int y, String name)
    {
        //Setting the stage to the right values and etting the arrys to right sizes.
        xSize = MinStage(x);
        ySize = MinStage(y);
        TakenSeat = new boolean[xSize * ySize];
        AllSeatButtons = new JButton[ySize * xSize];
        SeatPerson_Time = new String[ySize * xSize];
        PriceButtons = new JButton[ySize * (xSize + 1)];
        SeatCosts = new double[ySize * (xSize + 1)];
        this.name = name;
    }
    public int MinStage(int value)
    {
        //If the value is less than 1, it is set to 1.
        if(value <= 0){
            value = 1;
        }
        return value;
    }

    public void SetTime(String[] day_time, int Length) {
        //Sets the variables of the times of seat seat to the input day_time variables.
        SeatTimes = new String[ySize * xSize][Length];
        for (int x = 0; x < SeatTimes.length; x++)
        {
            //Access each seat
            for (int y = 0; y < Length; y++)
            {
                //Access each time from the seat
                SeatTimes[x][y] = day_time[y];
                AllBookingTimes++;
            }
        }
    }
    public void CinemaGUI(){
        //Create the Cinema Booking Program usnig the swing java library

        JFrame CinemaProgram = new JFrame(name);
        //set the defualt sixe of the program 
        CinemaProgram.setSize(500, 300);

        //The layout, or the order of the buttons in the GUI Program. 
        GridLayout gl = new GridLayout(ySize, xSize);
        CinemaProgram.setLayout(gl);
        // for the GUI
        for (int t = 0; t < ySize * xSize; t++) {
            //For each of the buttons in the program
            String ButtonText = "Seat number: " + t + ", $" + SeatCosts[t];
            if(ySize * xSize > 200){
                //shorten the button text if the GUI is really big.
                ButtonText = "id: " + t;
            }
            //Create a button in the empty spot in the array. Set the color to green to know it is still open. IN addition, set the action to the actin performed, which will send autmatically to the swing array. 
            AllSeatButtons[t] = new JButton(ButtonText);
            AllSeatButtons[t].setBackground(Color.BLACK);
            AllSeatButtons[t].setForeground(Color.GREEN);
            AllSeatButtons[t].setActionCommand(Integer.toString(t));
            AllSeatButtons[t].addActionListener(this::actionPerformed);
            CinemaProgram.add(AllSeatButtons[t]);
        }
        //Make sure that the program is visible.
        CinemaProgram.setVisible(true);
    }
    public void ClientGUI(){
        //GUI program to set the price of the program.
        JFrame CinemaProgram = new JFrame("Set Price");
        CinemaProgram.setSize(500, 300);

        GridLayout gl = new GridLayout(ySize, xSize + 1);
        CinemaProgram.setLayout(gl);
        // for the GUI
        for (int t = 0; t < ySize * (xSize + 1); t++) {
            //This is to find the real seat index due to the button layout having an extra button to price each row.
            int RealSeatPosition = (int)(t - 1 - Math.floor(t / (xSize + 1)));
            String ButtonText = "Seat: " + (RealSeatPosition + 1) + ", Cost: $" + SeatCosts[t];
            PriceButtons[t] = new JButton(ButtonText);
            if((t % (xSize + 1)) == 0)
            {
                //Those at the end are to price the row.
                PriceButtons[t].setText("Row Cost");
                PriceButtons[t].setActionCommand("Row" + Integer.toString((int)(t / (xSize + 1))));
            }
            else
            {
                //These are to price each one individually.
                PriceButtons[t].setActionCommand(Integer.toString(t));
            }
            if(ySize * (xSize + 1) > 200){
                //If the size of the GUI is too large, it will shorten the text.
                PriceButtons[t].setText("id: " + t);
            }
            //Set the color of the button to the defaalts green and black. As well as add the action  
            PriceButtons[t].setBackground(Color.BLACK);
            PriceButtons[t].setForeground(Color.GREEN);
            PriceButtons[t].addActionListener(this::actionPerformed);
            CinemaProgram.add(PriceButtons[t]);
        }
        CinemaProgram.setVisible(true);

    }
    public void SeatSelectGUI(int seat) {
        //This is to creat the promp that goes through the process of sending when the times are occupied to when the seat is booked.
        JFrame PromptInfo = new JFrame("Dates");
        PromptInfo.setSize(500, 300);
        String Times = "";
        String Options = "";
        int AvailableSeats = 0;
        //Get all the elements in the times of the seat.
        for (int x = 0; x < SeatTimes[seat].length; x++)
        {
            //Update the string that will prompt the user the options.
            if(!CinemaFinished){
                Times += (x + 1) + ". " + SeatTimes[seat][x] + "\n";
            }
            else
            {
                if(SeatTimes[seat][x].length() <= 8)
                {
                    Times += (x + 1) + ". Cinema CLOSED" + "\n";
                }
                else
                {
                    Times += (x + 1) + ". " + SeatTimes[seat][x] + "\n";
                }
            }
            if(x == SeatTimes[seat].length - 1)
            {
                //If the item is last in the list
                Options += (x + 1);
            }
            else{
                //if the item is not last in the list it will include a comma
                Options += (x + 1) + ", ";
            }
            if(SeatTimes[seat][x].length() < 9)
            {
                // If the seat does not say occupied then it is a good seat. Since time is formated like 12:00, it cannot exceed 9 characters in length
                AvailableSeats++;
            }

        }
        JOptionPane.showMessageDialog(PromptInfo,Times);

        if (!TakenSeat[seat] && !CinemaFinished) {
            //when the seat is not taken, it will send a prompt with a panel to choose the times.
            String ChooseSeat = JOptionPane.showInputDialog("Enter which time(options: " + Options + ")");
            //create new variables. 
            boolean ValidSeat = false;
            int ChosenSeat = 0;

            if(ChooseSeat != null && !ChooseSeat.equalsIgnoreCase("")) {
                ChosenSeat = Integer.valueOf(ChooseSeat) - 1;
                ValidSeat = ChosenSeat >= 0 && ChosenSeat < SeatTimes[seat].length;
                //check if the inputted value is in the right range to prevent a crash system from occuring. 
            }
            if (ChooseSeat == null || ChooseSeat.equalsIgnoreCase("null") || ChooseSeat.trim().equalsIgnoreCase("")) {
                return;
                //exit if nothing
            } else if (ValidSeat) {
                if (SeatTimes[seat][ChosenSeat].length() < 9) {
                    DateTimeFormatter datetime = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String Name = NameGUI(seat);
                    while (Name.equalsIgnoreCase("")) {
                        Name = NameGUI(seat);
                        //keep prompting name until it is not a empty string
                    }
                    AllSeatButtons[seat].setForeground(Color.yellow);
                    String NewData = "Person: " + Name + ", Seat:" + seat + ", Time:" + datetime.format(now) + ", Cost: $" + SeatCosts[seat] + ", WatchingSession: " + SeatTimes[seat][ChosenSeat];
                    SaveData(NewData);
                    SeatTimes[seat][ChosenSeat] = "Occupied: " + Name + ", " + SeatTimes[seat][ChosenSeat];

                    Profit += SeatCosts[seat];
                    if (AvailableSeats - 1 == 0) {
                        AllSeatButtons[seat].setForeground(Color.RED);
                        TakenSeat[seat] = true;
                        //set color to red if this is the last available seats
                    }
                }
            }
            else {
                //recursion if invalid
                SeatSelectGUI(seat);
            }
        }
    }
    public String NameGUI(int seat) {
        String Name = JOptionPane.showInputDialog("Please Enter Your Name: ");
        //Make sure that the new panel is valid.
        if (Name == null || Name.equalsIgnoreCase("null") || Name.trim().equalsIgnoreCase("")) {
            return "";
        }
        else {
            return Name;
        }
    }
    public void ChangeSeatPricing(int Actualseat, int ClientSeat) {
        String Price = JOptionPane.showInputDialog("Price of Seat");
        //Make sure that the new panel is valid.
        if (Price == null || Price.equalsIgnoreCase("null") || Price.trim().equalsIgnoreCase("")) {
            ;
        } else {
            //change the price of the seats.
            SeatCosts[Actualseat] = Double.valueOf(Price);
            PriceButtons[ClientSeat].setText("Seat: " + (Actualseat + 1) + ", Cost: $" + SeatCosts[Actualseat]);
        }

    }
    public void ChangeRowSeating(int row)
    {
        String PriceRow = JOptionPane.showInputDialog("Price of Row Seat:");
        if (PriceRow == null || PriceRow.equalsIgnoreCase("null") || PriceRow.trim().equalsIgnoreCase("")) {
            ;
        } else {
            //access all the seats in that row
            for (int x = row * xSize; x < row * xSize + xSize; x++) {
                SeatCosts[x] = Double.valueOf(PriceRow);
                //0,1,2,3,4
                //5,6,7,8,9
                //Input:

                //-,1,2,3,4,5
                //-,7,8,9,10,11
                //What we want:
                int ClientSeats = (int)(x + Math.floor(x / xSize + 1));
                PriceButtons[ClientSeats].setText("Seat: " + (x + 1)+ ", Cost: $" + SeatCosts[x]);
            }
        }
    }
    public void actionPerformed(ActionEvent evt) {

        //to check the input from the buttons, come from swing java.
        if(CustomerReady) {
            int index = Integer.valueOf(evt.getActionCommand());
            SeatSelectGUI(index);

        }
        else{
            if(evt.getActionCommand().length() >= 3) {
                //if the command is greater than 3 in legnth.
                if (evt.getActionCommand().substring(0, 3).equalsIgnoreCase("row")) {
                    ChangeRowSeating(Integer.valueOf(evt.getActionCommand().substring(3)));
                    //if the first bit of string is 'row', it will be sent to the row command. 
                } else {
                    int index = Integer.valueOf(evt.getActionCommand());
                    int RealSeatPosition = (int)(index - 1 - Math.floor(index / (xSize + 1)));
                    ChangeSeatPricing(RealSeatPosition,index);
                    //if it is to price the seats individually, it will get the actual position of the seats relative to the client pricing GUI and seat the price method. 
                }
            }
            else {
                int index = Integer.valueOf(evt.getActionCommand());
                int RealSeatPosition = (int)(index - 1 - Math.floor(index / (xSize + 1)));
                ChangeSeatPricing(RealSeatPosition,index);
                //If the length is less than 3, commonly now the row command unless we are reaching triple digit numbers. if it is to price the seats individually, it will get the actual position of the seats relative to the client pricing GUI and seat the price method. 
            }
        }
    }

    private void SaveData(String Data){
        //Save all the data onto the notepad file.
        try(FileWriter fw = new FileWriter("CinemaData.txt", true); PrintWriter NotePadOutput = new PrintWriter(fw)) {
            if(CustomerAmount == 0){
                NotePadOutput.println("\n NEW SESSION");
                NotePadOutput.println("----------------------------");
                //saves the trings and data to the notepad file CinemaData.txt
            }
            CustomerAmount++;
            NotePadOutput.println(Data);
            if(CheckProgramFinished(CustomerAmount)){
                WriteFinalCinemaData();
            }
        } catch (IOException e) {
            //occurs when there is no file to save to. This will not happen as a new file will always be created if there is none already.
            return;
        }
    }
    boolean CheckProgramFinished(int Customers)
    {
        if(Customers >= xSize * ySize * AllBookingTimes){
            return true;
            //the program is done, chose to not exit hte porgram due to the other custmers that might want to see the times and the occupied spaces. 
        }
        return false;
    }
    void WriteFinalCinemaData()
    {
        CinemaFinished = true;
        try(FileWriter fw = new FileWriter("CinemaData.txt", true); PrintWriter NotePadOutput = new PrintWriter(fw)) {
            NotePadOutput.println("Total Profits: $" + Profit);
            //print the total profits when the program is finished.
        } catch (IOException e) {
            return;
        }
    }

}