import java.sql.*;

public class jdbc_example {

    // The instance variables for the class
    private Connection connection;
    private Statement statement;

    boolean empty = true;	// boolean to tell Menu if query result is empty

    // The constructor for the class
    public jdbc_example() {
        connection = null;
        statement = null;
    }

    // The main program that tests the methods
    public static void main(String[] args) throws SQLException {
        String Username = "";              // Change to your own username
        String mysqlPassword = "";    // Change to your own mysql Password

        //jdbc_example test = new jdbc_example();
	Menu m = new Menu();
        m.test.connect(Username, mysqlPassword);
        m.test.initDatabase(Username, mysqlPassword, Username);

	m.menu();

        //String query1 = "SELECT guestNo from Guest WHERE guestNo = 3";
        //String query2 = "SELECT hotelName, COUNT(Room.hotelNo) AS TOTALNUMBEROFROOM, " +
        //        "AVG (Room.price) AS AVGPRICEPERROOM " +
        //        "FROM Room, Hotel " +
        //        "WHERE Room.hotelNo=Hotel.hotelID " +
        //        "GROUP BY hotelNo";

        //m.test.query(query1);
        //test.query(query2);

        m.test.disConnect();
    }

    // Connect to the database
    public void connect(String Username, String mysqlPassword) throws SQLException {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/" + Username + "?" +
                    "user=" + Username + "&password=" + mysqlPassword);
            //connection = DriverManager.getConnection("jdbc:mysql://localhost/" + Username +
             //       "?user=" + Username + "&password=" + mysqlPassword);
        }
        catch (Exception e) {
            throw e;
        }
    }

    // Disconnect from the database
    public void disConnect() throws SQLException {
        connection.close();
        statement.close();
    }

    // Execute an SQL query passed in as a String parameter
    // and print the resulting relation
    public void query(String q) {
        try {
            ResultSet resultSet = statement.executeQuery(q);
            System.out.println("\n---------------------------------");
            System.out.println("Query: \n" + q + "\n\nResult: ");
            print(resultSet);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Print the results of a query with attribute names on the first line
    // Followed by the tuples, one per line
    public void print(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numColumns = metaData.getColumnCount();

        printHeader(metaData, numColumns);
        printRecords(resultSet, numColumns);
    }

    // Print the attribute names
    public void printHeader(ResultSetMetaData metaData, int numColumns) throws SQLException {
        for (int i = 1; i <= numColumns; i++) {
            if (i > 1)
                System.out.print(",  ");
            System.out.print(metaData.getColumnName(i));
        }
        System.out.println();
    }

    // Print the attribute values for all tuples in the result
    public void printRecords(ResultSet resultSet, int numColumns) throws SQLException {
        String columnValue;
	if(resultSet.next())
	{
		resultSet.previous();
		empty = false;
	}
	else
		empty = true;
        while (resultSet.next()) {
            for (int i = 1; i <= numColumns; i++) {
                if (i > 1)
                    System.out.print(",  ");
                columnValue = resultSet.getString(i);
                System.out.print(columnValue);
            }
            System.out.println("");
        }
    }

    // Insert into any table, any values from data passed in as String parameters
    public void insert(String table, String values) {
        String query = "INSERT into " + table + " values (" + values + ")" ;
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove all records and fill them with values for testing
    // Assumes that the tables are already created
    public void initDatabase(String Username, String Password, String SchemaName) throws SQLException {
        statement = connection.createStatement();
        statement.executeUpdate("DELETE from Booking");
        statement.executeUpdate("DELETE from Room");
        statement.executeUpdate("DELETE from Hotel");
        statement.executeUpdate("DELETE from Guest");

        insert("Guest", "1, 'Cam Newton', '800 S Mint St., Charlotte, NC'");
        insert("Guest", "4, 'Dark Prescott', '1 ATT Way, Arlington, TX'");
        insert("Guest", "11, 'Alex Smith', '1 Arrowhead Dr., Kansas City, MO'");
        insert("Guest", "0, 'Johnny Manziel', 'Texas A&M'");

        insert("Hotel", "0, 'Fleabag', 'Detroit'");
        insert("Hotel", "8, 'Lap of Luxury', 'Las Vegas'");
        insert("Hotel", "3, 'Razorback Heave', 'Fayetteville'");

        insert("Room", "13, 0, 'tp', 75");
        insert("Room", "222, 0, 'db', 60");
        insert("Room", "107, 0, 'sg', 50");
        insert("Room", "107, 3, 'sg', 80");
        insert("Room", "109, 3, 'db', 100");
        insert("Room", "207, 3, 'tp', 125");
        insert("Room", "307, 3, 'db', 110");
        insert("Room", "101, 8, 'db', 400");
        insert("Room", "107, 8, 'tp', 600");
        insert("Room", "201, 8, 'db', 400");
        insert("Room", "301, 8, 'db', 400");

        insert("Booking", "0, 101, 8, 1, STR_TO_DATE('2017-06-20', '%Y-%m-%d'), STR_TO_DATE('2017-06-24', '%Y-%m-%d')");
        insert("Booking", "1, 101, 8, 1, STR_TO_DATE('2017-07-01', '%Y-%m-%d'), STR_TO_DATE('2017-07-03', '%Y-%m-%d')");
        insert("Booking", "2, 201, 8, 1, STR_TO_DATE('2017-07-01', '%Y-%m-%d'), STR_TO_DATE('2017-07-03', '%Y-%m-%d')");
        insert("Booking", "3, 301, 8, 11, STR_TO_DATE('2017-07-01', '%Y-%m-%d'), STR_TO_DATE('2017-07-03', '%Y-%m-%d')");
        insert("Booking", "4, 107, 3, 4, STR_TO_DATE('2017-07-01', '%Y-%m-%d'), STR_TO_DATE('2017-07-03', '%Y-%m-%d')");
        insert("Booking", "5, 107, 0, 0, STR_TO_DATE('2017-07-01', '%Y-%m-%d'), STR_TO_DATE('2017-07-03', '%Y-%m-%d')");
        insert("Booking", "6, 101, 8, 4, STR_TO_DATE('2017-09-28', '%Y-%m-%d'), STR_TO_DATE('2017-10-03', '%Y-%m-%d')");

    }
}
