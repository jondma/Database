import java.util.Scanner;

class Menu
{
	// Class instantiations
	jdbc_example test;
	Scanner sc;

	int choice;	// Menu choice

	// Menu constructor
	public Menu()
	{
		test = new jdbc_example();
		sc = new Scanner(System.in);
		choice = 0;
	}

	// display menu
	public void menu()
	{
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
					this.bookHotel();
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
		String query	 = "";
		String checkin	 = "";
		String checkout  = "";

		// Prompt user for date of checkin
		System.out.println("Checkin Date:");
		checkin = getDate();
		System.out.println("checkin:" + checkin);

		// Prompt user for date of checkout
		System.out.println("Checkout Date:");
		checkout = getDate();
		System.out.println("checkout:" + checkout);
		
		// MySQL query that displays hotels available
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

	// Prompts user for date and returns date as string in 'yyyy-mm-dd' format
	public String getDate()
	{
		int year		  = -1;
		int month		  = -1;
		int day			  = -1;

		// Error handling check valid month
		do
		{
			System.out.println("Month:");
			month = sc.nextInt();
		}
		while(month < 1 || month > 12);

		// Error handling check valid day
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

		// Error handling check for valid year
		do
		{
			System.out.println("Year:");
			year = sc.nextInt();
		}
		while(year < 2016 || year > 2018);

		String date = "'" + year + "-";

		// Error handling add 0 to month less than two digits
		if(month > 0 && month < 10)
		{
			date = date + "0" + month + "-";
			//System.out.println("date:" + date);
		}
		else
			date += month + "-";

		// Error handling add 0 to day less than two digits
		if(day > 0 && day < 10)
			date = date + "0" + day + "'";
		else
			date += day + "'";
		return date;
	}

	// book Hotel
	public void bookHotel()
	{
		System.out.println("Enter your name:");
		sc.nextLine();
		String name = sc.nextLine();

		// MySQL query that displays guest information based on guestName
		String query1 = "SELECT guestNo, guestAddress " +
				"FROM Guest " +
				"WHERE guestName = '" + name + "'";
		test.query(query1);

		// Name not found
		if(test.empty)
		{
			// Prompt user for address
			System.out.println("Enter address:");
			String address = sc.nextLine();
			System.out.println("address:" + address + ":");
			
			// Insert new guest record
			String query2 = "'" + name + "', '" + address + "'";
			System.out.println("\n----------------------------------------------n");
			System.out.println("query2:" + query2);
			System.out.println("\n----------------------------------------------n");
			test.insert("Guest(guestName, guestAddress)", query2);
			
			// Displays guestNo of new gues
			System.out.println("\n----------------------------------------------n");
			String query3 = "SELECT guestNo FROM Guest WHERE guestName = '" +
					name + "' AND guestAddress = '" + address + "';";
			System.out.println("query3:" + query3);
			System.out.println("\n----------------------------------------------n");
			test.query(query3);

			// Prompt user for Booking info
			System.out.println("Enter Guest Number:");
			int guestNo = sc.nextInt();
			System.out.println("Enter Hotel name:");
			sc.nextLine();
			String hotel = sc.nextLine();
			System.out.println("Enter Room Number:");
			int room = sc.nextInt();
			String checkin = getDate();
			String checkout = getDate();
		
			// Search for hotelID from hotelName
			String query4 = "SELECT hotelID FROM Hotel WHERE hotelName = '" + 
					hotel + "';";
			System.out.println("\n----------------------------------------------n");
			System.out.println("query4:" + query4);
			System.out.println("\n----------------------------------------------n");
			test.query(query4);

			// Prompt user for hotelNo
			System.out.println("Enter hotelID:");
			int hotelID = sc.nextInt();
			

///////////////////////////////////////////ERROR//////////////////////////////////////////////////////

			// Search for unique bookingNo
			int i = -1;
			do
			{
				i++;
				// MySQL query that displays existing bookingNo 
				String query5 = "SELECT bookingNo FROM Booking WHERE bookingNo = " + i + ";";

				System.out.println(query5);
				test.query(query5);
				//++i;
			}
			while(!test.empty);

			// MySQL query that inserts new Booking information
			String query6 = i + ", " + room + ", " +
					hotelID + ", " + guestNo +
					", " + checkin + ", " +
					checkout;
			System.out.println(query6);
			test.insert("Booking", query6);
		}

///////////////////////////////////////////ERROR//////////////////////////////////////////////////////
		//int i = 0;
		
		//do
		//{
		//	test.query(query1);
		
		//	if(test.empty)
		//	{
		//		i++;
		//	}
		//}
		//while(test.query(query1).resultSet.next());
	}


}

