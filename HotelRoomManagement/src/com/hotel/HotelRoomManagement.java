package com.hotel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("serial")
public class HotelRoomManagement implements Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<Integer, List<Boolean>> roomsMap = null; // Represents Hotel Room Number (key) with Days (value)
	private static HotelRoomManagement objHotelRoomMgt;
	private int noOfRooms = 3; // Mention no of Rooms in Hotel. Requirement - Less Than or Equal to 1000
	private static final int noOfDays = 365; // Mention no of Days that are common to each Room. Requirement - 365 Days ( 0 to 364 ) 
	private static final boolean saveRoomsBookingStatus = false; // Make it true if you wanted to Serialize this Class 
	private static final boolean readRoomsBookingStatus = false; // Make it true if you wanted to Deserialize this Class
	private static final String fileName = "RoomsBookingStatus.txt"; // File Name to Serialize (or) Deserialize this Class
	private static final String accepted = "Accepted"; // Booking Request Status
	private static final String declined = "Declined"; // Booking Request Status
	public boolean showRoomStatusOnBookingReq = true;  // To show Room Status on each Room Booking Request
	
	public HotelRoomManagement() {
		
		initializeHotelRooms();
		
	}
	
	public HotelRoomManagement(int noOfRooms) {
		
		this.noOfRooms = noOfRooms;
		initializeHotelRooms();	
		
	}
	
	private void initializeHotelRooms(){
		
		roomsMap = new HashMap<Integer, List<Boolean>>(){{
			for(int i=1;i<=noOfRooms;i++) {
				put(i, new ArrayList<Boolean>() {{
					for(int j=0;j<noOfDays;j++) {
						add(false);
					}}
				});
			}
		}};
		
		System.out.println("Welcome to Hotel Room Management System. How Are You Today !");
		System.out.println();
		
	}

	public static void main(String[] args) {
		if(!readRoomsBookingStatus) {
			objHotelRoomMgt = new HotelRoomManagement();
		}else {
			readRoomsBookingStatus();
		}
		
		objHotelRoomMgt.takeBookingRequest();
	}
	
	private void takeBookingRequest() {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean closeBooking = false;
		boolean showRoomsStatus = false;
		boolean roomBookingRequest = false;
		
		while(!closeBooking) {
			
			closeBooking = false;
			showRoomsStatus = false;
			roomBookingRequest = false;
			
			boolean roomsFull = checkAllRoomsBooked();
			
			System.out.println("Please make a chioce :");
			System.out.println();
			
			if(!roomsFull) {
				System.out.println("1) For Room Booking : Provide Booking Days in the range 0 to "+(noOfDays-1)+" seperated by comma ( Ex : 0,1 )");
				System.out.println("2) To See Rooms Booking Status : Provide Days in the range 0 to "+(noOfDays-1)+" seperated by hiphen ( Ex : 0-6 )");
				System.out.println("3) To Close Room Bookings : close");
			}else {
				System.out.println("1) To See Rooms Booking Status : Provide Days in the range 0 to "+(noOfDays-1)+" seperated by hiphen ( Ex : 0-6 )");
				System.out.println("2) To Close Room Bookings : close");
			}
			
			System.out.println();
			System.out.print("Your Choice : ");
			
			try {
				
				String input = br.readLine();
				System.out.println();
				
				if(null != input && !input.isEmpty()) {
					
					if(input.equalsIgnoreCase("close"))
						closeBooking = true;
					else if(input.contains("-"))
						showRoomsStatus = true;
					else if(input.contains(","))
						roomBookingRequest = true;
					
				}
				
				if(closeBooking){

					System.out.println("Closing Room Bookings. Good Bye...");
					System.out.println();
					
					if(saveRoomsBookingStatus) {
						while(!saveRoomsBookingStatus()) {
							saveRoomsBookingStatus();
						}
					}
					
				}else if(showRoomsStatus){
					
					String[] bookingStatusDays = input.split("-");
					
					int fromDay = Integer.parseInt(bookingStatusDays[0]);
					int toDay = Integer.parseInt(bookingStatusDays[1]);
				
					if(validDays(fromDay, toDay)) {
						System.out.println("Showing Rooms Booking Status :");
						System.out.println();
						showRoomsBookingStatus(fromDay, toDay);
					}else {
						throw new InvalidInputException("Invalid Input Range Found ( "+fromDay+"-"+toDay+" ).");
					}
					
				}else if(roomBookingRequest && !roomsFull) {
					
					String[] bookingDays = input.split(",");
				
					int startDay = Integer.parseInt(bookingDays[0]);
					int endDay = Integer.parseInt(bookingDays[1]);
				
					if(validDays(startDay, endDay)) {
						
						boolean roomBooked = processBookingRequest(startDay, endDay);
						
						if(roomBooked)
							System.out.println("Room Booking Request Status : "+accepted);
						else
							System.out.println("Room Booking Request Status : "+declined);
						
						System.out.println();
						
					}else {
						throw new InvalidInputException("Invalid Input Range Found ( "+startDay+", "+endDay+" ).");
					}
					
				}else {
					throw new InvalidInputException("Invalid Input Found : "+input);
				}
				
			}catch(InvalidInputException ex) {
				System.out.println(ex.getExceptionDesc());
				System.out.println();
			}catch(Exception ex1) {
				System.out.println("Following Exception Occured : ");
				System.out.println();
				ex1.printStackTrace(System.out);
				System.out.println();
				System.out.println("Please provide a Valid Input.");
				System.out.println();
			}
		}
		
	}
	
	public boolean processBookingRequest(int startDay, int endDay) {
		
		boolean roomBooked = false;
		boolean bookingClash = false;
		Set<Integer> keySet = roomsMap.keySet();
		
		for(Integer key : keySet) {
			
			bookingClash = false;
			List<Boolean> totalRoomDays = roomsMap.get(key);
			List<Boolean> showRoomDays = totalRoomDays.subList(startDay, (endDay+1));
			
			for(int i=startDay;i<=endDay;i++) {
				if(totalRoomDays.get(i) == true) {
					bookingClash = true;
					System.out.println();
					
					boolean roomFull = checkRoomFull(totalRoomDays);
					if(roomFull) {
						System.out.println("There is Booking Clash for Room No : "+key+" - Reason : Room FULL");
					}else {
						System.out.println("There is Booking Clash for Room No : "+key+" - Reason : Booking Days Not Available");
					}
					
					if(showRoomStatusOnBookingReq) {
						printRoomDaysHeader(startDay, endDay);
						printRoomDaysStatus(showRoomDays);
					}
					break;
				}
			}
			
			if(!bookingClash) {
				for(int j=startDay;j<=endDay;j++) {
					totalRoomDays.set(j, true);
				}
				
				boolean roomFull = checkRoomFull(totalRoomDays);
				if(roomFull) {
					System.out.println("Booking Done Successfully for Room No : "+key+" ( FULL )");
				}else {
					System.out.println("Booking Done Successfully for Room No : "+key);
				}
				
				roomBooked = true;
				
				if(showRoomStatusOnBookingReq) {
					printRoomDaysHeader(startDay, endDay);
					printRoomDaysStatus(showRoomDays);
				}
				break;
			}
		}
		
		return roomBooked;
	}
	
	private void printRoomDaysHeader(int fromDay, int toDay) {
		
		String roomDaysHeader = "";
		for(int r=fromDay; r<=toDay; r++) {
			if((r+1) > toDay) {
				roomDaysHeader = roomDaysHeader + "    Day "+ r;
				break;
			}else {
				if(r < 10) {
					roomDaysHeader = roomDaysHeader + "    Day "+ r + "    |";
				}else if(r < 100) {
					roomDaysHeader = roomDaysHeader + "    Day "+ r + "   |";
				}else if(r < 1000) {
					roomDaysHeader = roomDaysHeader + "    Day "+ r + "  |";
				}
			}
		}
		
		StringBuffer horizontalLine = new StringBuffer();
		for(int i=-2;i<roomDaysHeader.length();i++) {
			horizontalLine.append("=");
		}
		
		System.out.println(horizontalLine);
		System.out.println(roomDaysHeader);
		System.out.println(horizontalLine);
	}
	
	private void printRoomDaysStatus(List<Boolean> showRoomDaysList) {
		
		String roomDaysStatus = "";
		for(int r=0; r<showRoomDaysList.size();r++) {
			
			boolean roomStatus = showRoomDaysList.get(r);
			
			String roomStatusDesc;
			if(roomStatus) {
				roomStatusDesc = "Booked";
			}else {
				roomStatusDesc = "Available";
			}
			
			if((r+1) == showRoomDaysList.size()) {
				roomDaysStatus = roomDaysStatus + "  " + roomStatusDesc;
			}else if(roomStatus) {
				roomDaysStatus = roomDaysStatus + "   " + roomStatusDesc +"    |";
			}else {
				roomDaysStatus = roomDaysStatus + "  " +roomStatusDesc +"  |";
			}
		}
		
		System.out.println(roomDaysStatus);
		System.out.println();
	}
	
	private void showRoomsBookingStatus(int fromDay, int toDay) {
		
		Set<Integer> roomNumbers = roomsMap.keySet();
		for(Integer roomNum : roomNumbers) {
			List<Boolean> totalRoomDays = roomsMap.get(roomNum);
			List<Boolean> showRoomDays = totalRoomDays.subList(fromDay, (toDay+1));
			
			boolean roomFull = checkRoomFull(totalRoomDays);
			
			if(roomFull) {
				System.out.println("Room "+roomNum+" ( FULL )");
			}else {
				System.out.println("Room "+roomNum);
			}
			
			printRoomDaysHeader(fromDay, toDay);
			printRoomDaysStatus(showRoomDays);
			System.out.println();
		}
	}
	
	private boolean checkAllRoomsBooked() {
		
		boolean roomsFull = true;
		
		Set<Integer> roomNumbers = roomsMap.keySet();
		for(Integer roomNum : roomNumbers) {
			List<Boolean> totalRoomDays = roomsMap.get(roomNum);
			
			for(Boolean roomStatus : totalRoomDays) {
				if(!roomStatus) {
					roomsFull = false;
					break;
				}
			}
		}
		
		if(roomsFull) {
			System.out.println("All Rooms Booked, No more Bookings Allowed.");
			System.out.println();
		}
		
		return roomsFull;
	}
	
	private boolean checkRoomFull(List<Boolean> roomDays) {
		boolean roomFull = true;
		
		for(Boolean roomStatus : roomDays) {
			if(!roomStatus) {
				roomFull = false;
				break;
			}
		}
		
		return roomFull;
	}
	
	private boolean saveRoomsBookingStatus() {
		
		boolean saveStatus = false;
		
		System.out.println("Please provide path to Store Rooms Booking Status :");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			String savePath = br.readLine();
			String fileToWrite = savePath+"\\"+fileName;
			
			if(new File(savePath).exists()) {
				
				FileOutputStream fileOut = new FileOutputStream(fileToWrite);
				ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
				objOut.writeObject(this);
				objOut.flush();
				objOut.close();
				saveStatus = true;
				
				System.out.println();
				System.out.println("Successfully Saved Rooms Booking Status under Path : "+fileToWrite);
				System.out.println();
				
			}else {
				throw new InvalidPathException("Invalid Path : "+savePath);
			}
			
		} catch(InvalidPathException ex) {
			System.out.println(ex.getExceptionDesc());
			System.out.println();
		}catch(Exception ex1) {
			System.out.println("Following Exception Occured : ");
			System.out.println();
			ex1.printStackTrace(System.out);
			System.out.println();
		}
		
		return saveStatus;
	}
	
	private static boolean readRoomsBookingStatus() {
		
		boolean readStatus = false;
		
		System.out.println("Welcome to Hotel Room Management System. How Are You Today !");
		System.out.println();
		System.out.println("Please provide path to Read Rooms Booking Status :");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		try {
			String readPath = br.readLine();
			String fileToRead = readPath+"\\"+fileName;
			
			if(new File(fileToRead).exists()) {
				
				FileInputStream fileIn = new FileInputStream(fileToRead);
				ObjectInputStream objIn = new ObjectInputStream(fileIn);
				objHotelRoomMgt = (HotelRoomManagement) objIn.readObject();
				objIn.close();
				readStatus = true;
				
				System.out.println();
				System.out.println("Successfully Read Rooms Booking Status under Path : "+fileToRead);
				System.out.println();
				
			}else {
				throw new InvalidPathException("Invalid File Path : "+fileToRead);
			}
			
		} catch(InvalidPathException ex) {
			System.out.println(ex.getExceptionDesc());
			System.out.println();
		}catch(Exception ex1) {
			System.out.println("Following Exception Occured : ");
			System.out.println();
			ex1.printStackTrace(System.out);
			System.out.println();
		}
		
		return readStatus;
	}
	
	public boolean validDays(int startDay, int endDay) {
		if(startDay >= 0 && startDay <= (noOfDays-1) && endDay >= 0 && endDay <= (noOfDays-1) && endDay >= startDay)
			return true;
		else
			return false;
	}

}
