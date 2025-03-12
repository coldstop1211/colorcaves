import java.util.*;
import java.io.Serializable;
public class Room implements Serializable {
	public static int next_id = 0;
	private static int numMoves = 0;
	private int id;
	private String name;
	private String description;
	private HashMap<Door, Room> doors;

  public Room(String name, String description){
		this.name = name;
		this.description = description;
		this.id = next_id;
		next_id++;
		this.doors = new HashMap<Door, Room>();
  }
	public void addDoor(Door door, Room room){
		this.doors.put(door, room);
		room.doors.put(door,this);
	}
	public Set<Door> getDoors(){
		return doors.keySet();
	}
	public Room enter(Door d){
		numMoves++;
		return doors.get(d);
	}
	public static int getNumMoves(){
		return numMoves;
	}
	public String getName(){
		return this.name;
	}
	public String getDescription(){
		return this.description;
	}
	public int getID(){
		return this.id;
	}
	public boolean equals(Room other){
		return this.id == other.id;
	}
	public String toString(){
		return this.id+" "+this.name;
	}

}