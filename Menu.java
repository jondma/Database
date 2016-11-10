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
			System.out.println("-------------------------------------\n" +
					  "    Welcome to the hotel database.\n" +
					  "-------------------------------------\n" +
					  "1) Find a hotel room\n" +
					  "2) Book a hotel room\n" +
					  "3) Find a booking\n" +
					  "4) Cancel a booking\n" +
					  "5) List all guests\n" +
					  "6) Add a hotel\n" +
					  "7) Quit\n" +
					  "-------------------------------------\n" +
					  "Enter an option:");

			choice = sc.nextInt();

			switch (choice)
			{
				case 1:
					System.out.println("-------------------------------------\n" +
					"        Finding a hotel room.\n" +
					"-------------------------------------\n"); 
					this.findHotel();
					break;
				case 2:
					System.out.println("-------------------------------------\n" +
					"        Booking a hotel room.\n" +
					"-------------------------------------\n"); 
					this.bookHotel();
					break;
				case 3:
					System.out.println("-------------------------------------\n" +
					"         Finding a booking.\n" +
					"-------------------------------------\n"); 
					this.findBooking();
					break;
				case 4:
					System.out.println("-------------------------------------\n" +
					"        Canceling a booking.\n" +
					"-------------------------------------\n"); 
					this.cancelBooking();
					break;
				case 5:
					System.out.println("-------------------------------------\n" +
					"         Listing all guests.\n" +
					"-------------------------------------\n"); 
					this.listGuests();
					break;
				case 6:
					System.out.println("-------------------------------------\n" +
					"          Adding a hotel.\n" +
					"-------------------------------------\n"); 
					this.addHotel();
					break;
				case 7:
					System.out.println("-------------------------------------\n" +
					"        Exiting now. Goodbye.\n" +
					"-------------------------------------"); 
					break;
				default:
					System.out.println("-----------------------------------");
					System.out.println("          Invalid option");
					System.out.println("-----------------------------------");
					break;
			}
		}
		while(choice != 7);
	}

	// Search for available rooms at all hotel for duration of user's stay
	public void findHotel()
	{
		// Prompt user for date of checkin
		System.out.println("Checkin Date:");
		String checkin = getDate();
		//System.out.println("checkin:" + checkin);

		// Prompt user for date of checkout
		System.out.println("Checkout Date:");
		String checkout = getDate();
		//System.out.println("checkout:" + checkout);
		
		// MySQL query that displays hotels available
		String query1 = "SELECT hotelName, type, Room.roomNo, price " +
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
		//System.out.println(query1);

		test.query(query1);
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

	// Search if user has previously booked a hotel room before
	// Books a hotel room for user
	public void bookHotel()
	{
		// Prompt user for name
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
			//System.out.println("address:" + address + ":");
			
			// Search for unique guestNo
			int j = -1;
			do
			{
				j++;
				// MySQL query that displays existing bookingNo 
				String query2 = "SELECT guestNo FROM Guest WHERE guestNo = " + j + ";";

				//System.out.println(query2);
				test.query(query2);
				//++i;
			}
			while(!test.empty);

			// Insert new guest record
			String query3 = j + ", '" + name + "', '" + address + "'";
			//System.out.println("\n----------------------------------------------n");
			//System.out.println("query3:" + query3);
			//System.out.println("\n----------------------------------------------n");
			test.insert("Guest", query3);
			
			// Displays guestNo of new gues
			//System.out.println("\n----------------------------------------------n");
			String query4 = "SELECT guestNo FROM Guest WHERE guestName = '" +
					name + "' AND guestAddress = '" + address + "';";
			//System.out.println("query4:" + query4);
			//System.out.println("\n----------------------------------------------n");
			test.query(query4);

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
			String query5 = "SELECT hotelID FROM Hotel WHERE hotelName = '" + 
					hotel + "';";
			//System.out.println("\n----------------------------------------------n");
			//System.out.println("query5:" + query5);
			//System.out.println("\n----------------------------------------------n");
			test.query(query5);

			// Prompt user for hotelNo
			System.out.println("Enter hotelID:");
			int hotelID = sc.nextInt();
			
			// Search for unique bookingNo
			int i = -1;
			do
			{
				i++;
				// MySQL query that displays existing bookingNo 
				String query6 = "SELECT bookingNo FROM Booking WHERE bookingNo = " + i + ";";

				//System.out.println(query6);
				test.query(query6);
				//++i;
			}
			while(!test.empty);

			// MySQL query that inserts new Booking information
			String query6 = i + ", " + room + ", " +
					hotelID + ", " + guestNo +
					", " + checkin + ", " +
					checkout;
			//System.out.println(query6);
			test.insert("Booking", query6);
		}
	}

	// Search for booking details of user
	void findBooking()
	{
		// Prompt user for name
		System.out.println("Enter name:");
		sc.nextLine();
		String name = sc.nextLine();
		//System.out.println("name:" + name);

		// MySQL query that displays guest information based on guestName
		String query1 = "SELECT guestNo, guestAddress " +
				"FROM Guest " +
				"WHERE guestName = '" + name + "'";
		test.query(query1);
		
		// Name not found
		if(test.empty)
		{
			System.out.println("Name not found. Going back to main menu.");
			return;
		}

		// Prompt user for guest number
		System.out.println("Enter guest number:");
		int guestNum = sc.nextInt();
		//System.out.println("guest " + guestNum);

		// MySQL query that display booking information
		// for existing booking of user
		String query2 = "SELECT bookingNo, hotelName, dateFrom AS checkin, type " +
				"FROM Hotel, Room, Booking " +
				"WHERE hotelID = Booking.hotelNo " +
				"AND Booking.roomNo = Room.roomNo " +
				"AND Booking.hotelNo = Room.hotelNo " +
				"AND guestNo = " + guestNum + ";";
		//System.out.println("query2:" + query2);
		test.query(query2);
	}

	// Cancel a booking
	void cancelBooking()
	{
		// Prompt user for booking number
		System.out.println("Enter booking number:");
		int bookingNum = sc.nextInt();
		//System.out.println("Booking " + bookingNum);

		// MySQL query that delete booking record
		String query1 = "bookingNo = " + bookingNum;
		//System.out.println("query1:" + query1);
		test.remove("Booking", query1);
	}

	// List all guests at a hotel on a specified date
	void listGuests()
	{
		// MySQL query that displays name and ID for all hotels
		String query1 = "SELECT hotelName, hotelID " +
				"FROM Hotel;";
		//System.out.println("query1:" + query1);
		test.query(query1);

		// Prompt user for hotelID and date
		System.out.println("Enter hotelID:");
		int id = sc.nextInt();
		String date = getDate();
		
		// MySQL query that displays names of all guests staying
		// at a specific hotel on a specific date
		String query2 = "SELECT DISTINCT guestName " +
				"FROM Hotel, Guest, Booking " +
				"WHERE Guest.guestNo = Booking.guestNo " +
				"AND hotelNo = " + id + " " +
				"AND " + date + " BETWEEN dateFrom AND dateTo;";
		System.out.print("query2:" + query2);
		test.query(query2);
	}

	// Add a new hotel until user quits
	void addHotel()
	{
		// Prompt user for new hotel name
		System.out.println("Enter new hotel name:");
		sc.nextLine();
		String name = sc.nextLine();
		//System.out.println("Hotel " + name);

		// Prompt user for new hotel city
		System.out.println("Enter new hotel city:");
		String city = sc.nextLine();
		//System.out.println("city:" + city);
		
		// Search for unique hotelID
		int i = -1;
		do
		{
			i++;
			// MySQL query that displays existing hotelID 
			String query1 = "SELECT hotelID FROM Hotel WHERE hotelID = " + i + ";";
			//System.out.println("query1:" + query1);
			test.query(query1);
		}
		while(!test.empty);

		// Insert new hotel into table Hotel
		String query2 = i + ", '" + name + "', '" + city + "'";
		//System.out.println("query2:" + query2);
		test.insert("Hotel", query2);

		char answer;
		// Prompt user for room number, room type, and price
		do
		{
			System.out.println("Enter room number:");
			int roomNum = sc.nextInt();
			//System.out.println("Room " + roomNum);

			System.out.println("Enter room type:");
			String type = sc.next();
			//System.out.println("Type:" + type);

			System.out.println("Enter price:");
			float price = sc.nextFloat();
			//System.out.println("Float:" + price);

			System.out.println("Would you like to stop adding rooms?\n" +
					   "Y: Yes\n" +
					   "N: No\n");
			answer = sc.next().charAt(0);
		}
		while(answer != 'Y' && answer != 'y');
	} 
}
