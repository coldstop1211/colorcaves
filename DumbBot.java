import java.io.*;
import java.util.*;

@SuppressWarnings("unused")

public class DumbBot {
    
    private SerialLoader sl;
    private String path;
    private Room curr;

    public void load(String path){
        sl = new SerialLoader();
        sl.deserialize(path);
        curr = sl.getStart();
    }

public void createPath(){

   
    //starting with current, until you reach sl.getEnd()
    //keep chosing random doors until and adding to  a String representation of the path
    //at teh end, print the path shwowing the full way to get frpom the start to the end
    //something like: GREEN, RED, PINK, GREEN, BLUE
    //print how many steps it took you to get there
    //test using ColorCave

    List<String> path = new ArrayList<>();
        int steps = 0;

        while (curr != sl.getEnd()) {
            Door nextDoor = randomDoor();
            path.add(nextDoor.name());
            curr = curr.enter(nextDoor);
            steps++;
        }

        System.out.println(path);
        System.out.println("Steps: " + steps);
}

    private Door randomDoor(){
        Set<Door> doors = curr.getDoors();
        int rand = (int)(Math.random() * doors.size());
        int i = 0;
        Door nextDoor = Door.GREEN;
        for(Door d: doors){
            nextDoor = d;
            if(i == rand){
                break;
            }
            i++;
            
        }
        return nextDoor;
    }

    public static void main(String[] args) {
        DumbBot bot = new DumbBot();
        bot.load("/Users/ashumittal/Downloads/programs/2024/2025/CaveStarter/CaveData/L1.ser");
        bot.createPath();
    }


}
