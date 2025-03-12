import java.util.Set;
import java.util.HashSet;
import java.io.Serializable;
public class CaveData implements Serializable{

	private Room start, end;

	private Set<Room> rooms;

	public CaveData(){
		rooms = new HashSet<Room>();
	}

	public void addRoom(Room r){
		rooms.add(r);
	}

	public void setStart(Room r){
		start = r;
	}

	public void setEnd(Room r){
		end = r;
	}

	public Room getStart(){
		return start;
	}

	public Room getEnd(){
		return end;
	}

}