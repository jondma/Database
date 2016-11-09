import java.util.Scanner;

class Menu
{
	jdbc_example test;
	Scanner sc;

	int choice;
	public Menu()
	{
		test = new jdbc_example();

		sc = new Scanner(System.in);
		choice = 0;
	/*	
	System.out.println("HERE");
		do
		{	
			System.out.println("Welcome to the hotel database.");
			System.out.println("1) Find a hotel room");
			System.out.println("2) Book a hotel room");
			System.out.println("3) Find a booking");
			System.out.println("4) Cancel a booking");
			System.out.println("5) List all guests");
			System.out.println("6) Add a hotel");
			System.out.println("7) Quit");
			System.out.println("-------------------------------------\n");
			System.out.println("Enter an option:");

			choice = sc.nextInt();

			switch (choice)
			{
				case 1:
					System.out.println("Finding a hotel room.");
					this.findHotel();
					break;
				case 2:
					System.out.println("Booking a hotel room.");
					break;
				case 3:
					System.out.println("Finding a booking.");
					break;
				case 4:
					System.out.println("Canceling a booking.");
					break;
				case 5:
					System.out.println("Listing all guests.");
					break;
				case 6:
					System.out.println("Adding a hotel.");
					break;
				case 7:
					System.out.println("Exiting now. Goodbye.");
					break;
				default:
					System.out.println("\n----------------------------------");
					System.out.println("Invalid option.");
					System.out.println("\n----------------------------------");
					break;
			}
		}
		while(choice != 7);*/
	}

	public void menu()
	{
		System.out.println("HERE");
		do
		{	
			System.out.println("Welcome to the hotel database.");
			System.out.println("1) Find a hotel room");
			System.out.println("2) Book a hotel room");
			System.out.println("3) Find a booking");
			System.out.println("4) Cancel a booking");
			System.out.println("5) List all guests");
			System.out.println("6) Add a hotel");
			System.out.println("7) Quit");
			System.out.println("-------------------------------------\n");
			System.out.println("Enter an option:");

			choice = sc.nextInt();

			switch (choice)
			{
				case 1:
					System.out.println("Finding a hotel room.");
					this.findHotel();
					break;
				case 2:
					System.out.println("Booking a hotel room.");
					break;
				case 3:
					System.out.println("Finding a booking.");
					break;
				case 4:
					System.out.println("Canceling a booking.");
					break;
				case 5:
					System.out.println("Listing all guests.");
					break;
				case 6:
					System.out.println("Adding a hotel.");
					break;
				case 7:
					System.out.println("Exiting now. Goodbye.");
					break;
				default:
					System.out.println("\n----------------------------------");
					System.out.println("Invalid option.");
					System.out.println("\n----------------------------------");
					break;
			}
		}
		while(choice != 7);
	}

	public void findHotel()
	{
		String query	  = "";
		String hotelName = "";
		String hotelID   = "";
		String checkin	  = "";
		String checkout  = "";
		int year			  = -1;
		int month		  = -1;
		int day			  = -1;

		System.out.println("Checkin:");
		checkin = getDate(month, day, year);
		System.out.println("checkin:" + checkin);
		checkout = getDate(month, day, year);
		System.out.println("checkout:" + checkout);
		
		query = "SELECT hotelName, type, Room.roomNo, price " +
				  "FROM Hotel, Room, Booking " +
				  "WHERE hotelID = Room.hotelNo " +
				  "AND Room.roomNo = Booking.roomNo " +
				  "AND Room.hotelNo = Booking.hotelNo " +
				  "AND ((" + checkin + " NOT BETWEEN dateFrom AND dateTo) " +
				  "AND (" + checkout + " NOT BETWEEN dateFrom AND dateTo)) " +
				  "UNION " + 
				  "SELECT hotelName, type, Room.roomNo, price " +
				  "FROM Hotel, Room LEFT JOIN Booking " +
				  "ON Room.roomNo = Booking.roomNo " + 
				  "AND Room.hotelNo = Booking.hotelNo " +
				  "WHERE hotelID = Room.hotelNo " +
				  "AND bookingNo IS NULL;";
		System.out.println(query);

		test.query(query);
	}

	public String getDate(int month, int day, int year)
	{
		do
		{
			System.out.println("Month:");
			month = sc.nextInt();
		}
		while(month < 1 || month > 12);
		if(month == 2)
		{
			do
			{
				System.out.println("Day:");
				day = sc.nextInt();
			}
			while(day < 1 || day > 28);
		}
		else if (month == 4 || month == 6 || month == 9 || month == 11)
		{
			do
			{
				System.out.println("Day:");
				day = sc.nextInt();
			}
			while(day < 1 || day > 30);
		}
		else
		{
			do
			{
				System.out.println("Day:");
				day = sc.nextInt();
			}
			while(day < 1 || day > 31);
		}
		do
		{
			System.out.println("Year:");
			year = sc.nextInt();
		}
		while(year < 2016 || year > 2018);

		String date = "'" + year + "-";
		if(month > 0 && month < 10)
		{
			date = date + "0" + month + "-";
			//System.out.println("date:" + date);
		}
		else
			date += month + "-";
		if(day > 0 && day < 10)
			date = date + "0" + day + "'";
		else
			date += day + "'";
		return date;
	}

/*	public void bookHotel()
	{
		System.out.println("Enter your name:");
		String name = sc.next();

		String query1 = "SELECT guestNo, guestAddress " +
							 "FROM Guest " +
							 "WHERE guestName = '" + name + "'";
		
		int i = 0;
		
		//do
		{
			test.query(query1);
		
			if(test.empty)
			{
				i++;
			}
		}
		//while(test.query(query1).resultSet.next());
	}
*/

}

