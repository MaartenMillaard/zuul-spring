package nl.maarten.zuul;

import java.util.ArrayList;
import java.util.Random;

public class TransporterRoom extends Room {
	private ArrayList<Room> rooms;

	public TransporterRoom(String description, ArrayList<Room> rooms) {
		super(description);
		this.rooms = rooms;
	}

	public Room getExit(String direction) {
		Random random = new Random();
		return rooms.get(random.nextInt(rooms.size()));
	}

}
