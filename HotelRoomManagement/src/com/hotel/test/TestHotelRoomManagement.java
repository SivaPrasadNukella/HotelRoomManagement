package com.hotel.test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hotel.HotelRoomManagement;

public class TestHotelRoomManagement {

	private static HotelRoomManagement objHotelRoomMgt;
	private static final String accept = "Accept";
	private static final String decline = "Decline";
	
	public void createTestClsInstance(int noOfRooms) {
		objHotelRoomMgt = new HotelRoomManagement(noOfRooms);
		objHotelRoomMgt.showRoomStatusOnBookingReq = false;
	}
	
	@BeforeClass
	public static void beforeTestCaseBegin() {
		System.out.println("<<<<<<<<<<<<<<<<<<<<<< Test Cases Execution - Start >>>>>>>>>>>>>>>>>>>>>>");
		System.out.println();
	}
	
	@Before  
    public void beforeEachTestCase() { 
        System.out.println("---------- Test Case - Start ----------");  
        System.out.println();
    }
	
	@After  
    public void afterEachTestCase() { 
		System.out.println();
        System.out.println("---------- Test Case - End ----------");  
        System.out.println();
    }
	
	@AfterClass
	public static void afterTestCaseComplete() {
		System.out.println();
		System.out.println("<<<<<<<<<<<<<<<<<<<<<< Test Cases Execution - End >>>>>>>>>>>>>>>>>>>>>>");
	}
	
	@Test
	public void testCase1() {
		
		System.out.println("Test Case ID : 1");
		System.out.println();
		createTestClsInstance(1);
		
		assertEquals(decline, printTestResult(-4, 2));
		assertEquals(decline, printTestResult(200, 400));
	}
	
	@Test
	public void testCase2() {
		
		System.out.println("Test Case ID : 2");
		System.out.println();
		createTestClsInstance(3);
		
		assertEquals(accept, printTestResult(0, 5));
		assertEquals(accept, printTestResult(7, 13));
		assertEquals(accept, printTestResult(3, 9));
		assertEquals(accept, printTestResult(5, 7));
		assertEquals(accept, printTestResult(6, 6));
		assertEquals(accept, printTestResult(0, 4));
	}
	
	@Test
	public void testCase3() {
		
		System.out.println("Test Case ID : 3");
		System.out.println();
		createTestClsInstance(3);
		
		assertEquals(accept, printTestResult(1, 3));
		assertEquals(accept, printTestResult(2, 5));
		assertEquals(accept, printTestResult(1, 9));
		assertEquals(decline, printTestResult(0, 15));
	}
	
	@Test
	public void testCase4() {
		
		System.out.println("Test Case ID : 4");
		System.out.println();
		createTestClsInstance(3);
		
		assertEquals(accept, printTestResult(1, 3));
		assertEquals(accept, printTestResult(0, 15));
		assertEquals(accept, printTestResult(1, 9));
		assertEquals(decline, printTestResult(2, 5));
		assertEquals(accept, printTestResult(4, 9));
	}
	
	@Test
	public void testCase5() {
		
		System.out.println("Test Case ID : 5");
		System.out.println();
		createTestClsInstance(2);
		
		assertEquals(accept, printTestResult(1, 3));
		assertEquals(accept, printTestResult(0, 4));
		assertEquals(decline, printTestResult(2, 3));
		assertEquals(accept, printTestResult(5, 5));
		assertEquals(decline, printTestResult(4, 10));
		assertEquals(accept, printTestResult(10, 10));
		assertEquals(accept, printTestResult(6, 7));
		assertEquals(accept, printTestResult(8, 10));
		assertEquals(accept, printTestResult(8, 9));
	}
	
	private String printTestResult(int startDay, int endDay) {
		
		String bookingStatus;
		
		boolean result = false;
		
		if(objHotelRoomMgt.validDays(startDay, endDay))
			result = objHotelRoomMgt.processBookingRequest(startDay, endDay);
		
		if(result) {
			
			bookingStatus = accept;
			
			System.out.println("######");
			System.out.println(accept);
			System.out.println("######");
		}
		else {
			
			bookingStatus = decline;
			
			System.out.println("#######");
			System.out.println(decline);
			System.out.println("#######");
		}
		
		return bookingStatus;
	}
	
}
