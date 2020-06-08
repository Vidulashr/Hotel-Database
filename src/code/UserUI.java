package code;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Scanner;
import java.sql.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UserUI {
    //Class to load Driver
    public static String schema = "test";
    public static String user = "lmalo034";
    public static String password = "";

    private static Connection loadDriver() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection("jdbc:postgresql://web0.site.uottawa.ca:15432/lmalo034",user,password);
    }
    private static boolean createGuestReviewSQL(int userid,int writerid, String review, int rating,Date created, String status) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("INSERT INTO "+schema+".guestreview VALUES(DEFAULT,"+"'"+userid+"','"+writerid+"','"+review+"','"+rating+"','"+created+"','"+status+"');");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;
    }

    private static boolean createHostReviewSQL(int userid,int writerid, int propertyid,String review, int location,int accuracy,int checkin,int communication,int cleanliness, int value,Date created, String status) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("INSERT INTO test.hostreview VALUES("+"DEFAULT"+",'"+userid+"','"+writerid+"','"+propertyid+"','"+review+"','"+location+"','"+accuracy+"','"+checkin+"','"+communication+"','"+cleanliness+"','"+value+"','"+created+"','"+status+"');");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;
    }

    private static boolean createPaymentSQL(int payee,int receivee, String type, float amount,Date created, String status) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("INSERT INTO test.payment VALUES("+"DEFAULT"+",'"+payee+"','"+receivee+"','"+type+"','"+amount+"','"+created+"','"+status+"');");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;
    }

    private static boolean cancelPaymentSQL(int paymentid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("UPDATE test.payment SET status= 'Refunded' WHERE paymentid="+"'"+paymentid+"';");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;
    }

    private static boolean cancelBookingSQL(int bookingid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("UPDATE test.booking SET status = 'Cancelled' WHERE bookingid="+"'"+bookingid+"';");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;

    }

    private static int[] showGuestsForOwner(int ownerid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT platformuser.firstname,platformuser.lastname,platformuser.userid,booking.booking_date,booking.bookingid FROM test.platformuser,test.booking,test.property WHERE platformuser.userid = booking.userid AND booking.propertyid = property.propertyid  AND property.userid ="+"'"+ownerid+"'");
        int count = 0;
        while (rs.next()){
            count++;
            System.out.println("__________________________________________________________________");
            System.out.println("Booking ID: "+rs.getString(5));
            System.out.println("UserID: "+ rs.getString(3));
            System.out.println("Name: "+rs.getString(1)+" "+ rs.getString(2));
            System.out.println("Booking Date: "+ rs.getString(4));
        }
        System.out.println("__________________________________________________________________");
        rs.close();
        int[] result = new int[count];
        ResultSet ps = st.executeQuery("SELECT platformuser.firstname,platformuser.lastname,platformuser.userid FROM test.platformuser,test.booking,test.property WHERE platformuser.userid = booking.userid AND booking.propertyid=property.propertyid  AND property.userid="+"'"+ownerid+"'");
        int test=0;
        while (ps.next()&&(test!=count)){
            result[test++]=ps.getInt(3);
        }
        ps.close();st.close();db.close();
        return result;
    }

    private static int propertyCountForUser(int userid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT propertyid FROM test.property WHERE userid="+"'"+userid+"'");
        int count=0;
        while (rs.next()){
            count++;
        }
        rs.close();st.close();db.close();
        return count;
    }

    private static boolean createBookingSQL(int userid,int propertyid,int paymentid, Date checkin, Date checkout, Date bookingdate, String status) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("INSERT INTO test.booking VALUES("+"DEFAULT"+",'"+userid+"','"+propertyid+"','"+paymentid+"','"+checkin+"','"+checkout+"','"+bookingdate+"','"+status+"');");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;
    }

    private static boolean createRenterRateSQL(int propertyid,float price, int guestnum, String type, Date created, String status) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("INSERT INTO test.renterrate VALUES("+"DEFAULT"+","+"'"+propertyid+"','"+price+"','"+guestnum+"','"+type+"','"+created+"','"+status+"');");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;
    }

    private static boolean createPropertyAmenitySQL(int amenityid,int propertyid,Date created, String status) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("INSERT INTO test.propertyamenities VALUES("+"DEFAULT"+",'"+amenityid+"','"+propertyid+"','"+created+"','"+status+"');");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;
    }

    private static boolean createRentalAgreementSQL(int propertyid, Date startdate, Date enddate, String signing, Date created, String status) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("INSERT INTO test.rentalagreement VALUES("+"DEFAULT"+",'"+propertyid+"','"+startdate+"','"+enddate+"','"+signing+"','"+created+"','"+status+"');");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;
    }

    //Checks if a booking can be made
    private static boolean checkBookingAvailable(Date start,Date end,int propertyid) throws SQLException, ClassNotFoundException, ParseException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT StartDate,EndDate,status FROM test.rentalagreement WHERE propertyid="+"'"+propertyid+"'");
        boolean result = true;
        while(rs.next()) {
            if (rs.getString(3).equals("Active")) {
                Date newsdate = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(1));
                Date newedate = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(2));
                System.out.println(newedate);System.out.println(newsdate);
                if (start.after(newsdate) && end.before(newedate)) {
                    result = result;
                } else {
                    result = false;
                }
            }
            else{
                result = result;
            }
        }
        rs.close();st.close();db.close();
        return result;
    }

    private static int bookedPropertiesforUser(int userid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT booking.bookingid,booking.propertyid,booking.paymentid,booking.booking_date,booking.check_in_date,booking.check_out_date,payment.amount FROM test.booking,test.payment WHERE booking.paymentid = payment.paymentid AND booking.userid='"+userid+"'");
        int count = 0;
        while(rs.next()){
            count++;
            System.out.println("_______________________________________________________");
            System.out.println("Booking ID:"+ rs.getString(1));
            System.out.println("Property ID:"+ rs.getString(2));
            System.out.println("Amount:"+ rs.getString(7));
            System.out.println("Transaction Date:"+ rs.getString(4));
            System.out.println("Checkin: "+rs.getDate(5));
            System.out.println("Checkout: "+rs.getDate(6));
        }
        rs.close();st.close();db.close();
        return count;
    }

    private static int getPropertyID(int userid,String branchid,int addnum,String addstreet, String addpostal,
                                     String addcity, String countryid,int rooms,int bathrooms, Date created,String status) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT propertyid FROM test.property WHERE userid="+"'"+userid+"' AND branchid='"+branchid+"' AND addressnum='"+addnum+"' AND addressstreet='"+addstreet+"' AND addresscity='"+addcity+"' AND addresspostal='"+addpostal+"'" +
                "AND countryid='"+countryid+"' AND rooms='"+rooms+"' AND bathrooms='"+bathrooms+"' AND created='"+created+"' AND status='"+status+"'");
        int resullt = 0;
        while(rs.next()){
            resullt = rs.getInt(1);}
        rs.close();st.close();db.close();
        return resullt;
    }

    private static boolean PropertyExists(int propertyid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT propertyid FROM test.property WHERE propertyid="+"'"+propertyid+"'");
        int count = 0;
        while (rs.next()){
            count++;
        }
        rs.close();st.close();db.close();
        if (count>0){
            return true;
        }
        return false;
    }

    private static int getPaymentID(int payeeid,int recid,float amount,String type,Date created,String status) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT paymentid FROM test.payment WHERE payeeid="+"'"+payeeid+"' AND receiveeid='"+recid+"' AND type='"+type+"' AND amount='"+amount+"'" +
                "AND created='"+created+"' AND status='"+status+"'");
        rs.next();
        int resullt = rs.getInt(1);
        rs.close();st.close();db.close();
        return resullt;
    }

    private static int getPaymentIDfromBooking(int bookingid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT paymentid FROM test.booking WHERE bookingid="+"'"+bookingid+"'");
        rs.next();
        int result = rs.getInt(1);
        rs.close();st.close();db.close();
        return result;
    }

    private static int getUserIDfromPropertyID(int propertyid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT userid FROM test.property WHERE propertyid="+"'"+propertyid+"'");
        rs.next();
        int user = rs.getInt(1);
        rs.close();st.close();db.close();
        return user;
    }

    private static int getPropertyIDfromBookingID(int bookingid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT propertyid FROM test.booking WHERE bookingid="+"'"+bookingid+"'");
        rs.next();
        int user = rs.getInt(1);
        rs.close();st.close();db.close();
        return user;
    }

    //Function to add new property to database
    private static boolean createPropertySQL(int userid, String branchid, int addressnum, String addressstreet,
                                             String addresscity, String addresspostal, String countryid, String description,
                                             int rooms, int bathrooms, int minimumstay, String refundtype, Date created, String status) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("INSERT INTO test.Property VALUES("+"DEFAULT"+","+"'"+userid+"'"+","+"'"+branchid+"'"+","+"'"+addressnum+"'"+","+"'"+addressstreet+"'"+","+"'"+
                addresscity+"'"+","+"'"+addresspostal+"'"+","+"'"+countryid+"'"+","+"'"+description+"'"+","+"'"+rooms+"'"+","+"'"+bathrooms+"'"+","+"'"+minimumstay+"'"+","+"'"+refundtype+"'"+","+"'"+created+"'"+","+"'"+status+"'"+");");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;
    }

    private static boolean createUserSQL(String pass, String fname,String lname,String dob, int addressnum, String addressstreet,
                                         String addresscity, String addresspostal, String countryid, String email, String phone,String about, String usertype, Date created, String status) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("INSERT INTO test.Platformuser VALUES("+"DEFAULT,"+"'"+pass+"'"+","+"'"+fname+"'"+","+"'"+lname+"'"+","+"'"+dob+"'"+","+"'"+addressnum+"'"+","+"'"+addressstreet+"'"+","+"'"+
                addresscity+"'"+","+"'"+addresspostal+"'"+","+"'"+countryid+"'"+","+"'"+email+"'"+","+"'"+phone+"'"+","+"'"+about+"'"+","+"'"+usertype+"'"+","+"'"+created+"'"+","+"'"+status+"'"+");");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;
    }

    private static boolean deletePropertySQL(int propertyid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        int rs = st.executeUpdate("UPDATE test.property SET status='Deleted' WHERE propertyid="+"'"+propertyid+"';");
        if (rs!=0){
            st.close();
            db.close();
            return true;
        }
        st.close();
        db.close();
        return false;
    }

    private static void retrieveALLPropertySQL(int userid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT propertyid,addressnum,addressstreet,addresscity,addresspostal,rooms,bathrooms,refundtype,created FROM test.property WHERE status='Active' AND userid="+"'"+userid+"'");
        while(rs.next()){
            System.out.println("______________________________________________________________________________________________________________");
            System.out.println("ID: "+rs.getString(1));
            System.out.println("Address: "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+" "+rs.getString(5));
            System.out.println("Rooms: "+ rs.getString(6));
            System.out.println("Bathrooms: "+rs.getString(7));
            System.out.println("Refund Type: "+ rs.getString(8));
            System.out.println("Posted On: "+ rs.getString(9));
        }
        rs.close();
        st.close();
        db.close();
    }

    private static String getUserTypeFromUser(int userid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT usertype FROM test.platformuser WHERE userid="+"'"+userid+"'");
        rs.next();
        String type = rs.getString(1);
        rs.close();st.close();db.close();
        return type;
    }

    private static boolean alreadyAdded(int amid,int propertyid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT amenityid FROM test.propertyamenities WHERE propertyid="+"'"+propertyid+"'");
        boolean result = false;
        while(rs.next()){
            if (rs.getInt(1)==amid){
                result= true;
            }
        }
        rs.close();st.close();db.close();
        return result;
    }

    private static int[] showAvailableAmenities() throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT amenityid,type,name FROM test.amenity");
        int count = 0;
        while (rs.next()){
            count++;
            System.out.println("______________________________________________________________");
            System.out.println("Amenity ID: "+rs.getInt(1));
            System.out.println("Amenity Type: "+rs.getString(2));
            System.out.println("Amenity name: "+ rs.getString(3));
        }
        System.out.println("__________________________________________________________________");
        rs.close();
        int[] result = new int[count];
        ResultSet ps = st.executeQuery("SELECT amenityid,type,name FROM test.amenity");
        int test=0;
        while (ps.next()&&(test!=count)){
            result[test++]=ps.getInt(1);
        }
        ps.close();st.close();db.close();
        return result;
    }

    private static void showAmenitiesFromProperty(int propertyid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT amenity.name,amenity.type FROM test.propertyamenities,test.amenity WHERE amenity.amenityid= propertyamenities.amenityid AND propertyid="+"'"+propertyid+"'");
        int count = 1;
        while(rs.next()){
            System.out.println(count+ ". "+rs.getString(1)+" Type: "+rs.getString(2));
        }
        System.out.println("_______________________________________________________________________________");
        rs.close();st.close();db.close();
    }

    private static void listPropertyINFOFromID(int propertyid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT property.addressnum,property.addressstreet,property.addresscity,property.addresspostal,property.rooms,property.bathrooms," +
                "property.minimumstay,property.refundtype,renterrate.price,renterrate.guestnumber,renterrate.type FROM test.property,test.renterrate WHERE property.propertyid = renterrate.propertyid AND renterrate.propertyid="+"'"+propertyid+"'");
        while(rs.next()){
            System.out.println("______________________________________________________________________________________________________________");
            System.out.println("ID: "+propertyid);
            System.out.println("Address: "+rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4));
            System.out.println("Price per day: "+rs.getFloat(9));
            System.out.println("Property Type: "+rs.getString(11));
            System.out.println("Rooms: "+ rs.getString(5));
            System.out.println("Bathrooms: "+rs.getString(6));
            System.out.println("Total Guest: "+rs.getInt(10));
            System.out.println("Minimum Stay: "+ rs.getString(7));
            System.out.println("Refund Type: "+ rs.getString(8));
        }
        rs.close();st.close();db.close();

    }

    private static float getPropertyPrice(int propertyid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT price FROM test.renterrate WHERE propertyid='"+propertyid+"'");
        rs.next();
        float result = rs.getFloat(1);
        rs.close();st.close();db.close();
        return result;
    }

    private static boolean BookingHasNotPassed(int bookingid) throws SQLException, ClassNotFoundException, ParseException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT check_in_date,check_out_date FROM test.booking WHERE bookingid="+"'"+bookingid+"'");
        rs.next();
        Date newsdate = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(1));
        Date newedate = new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString(2));
        rs.close();st.close();db.close();
        if (newsdate.after(getCurrentDate())){
            return true;
        }
        else{
            return false;
        }

    }

    private static boolean bookingExists(int bookingid,int userid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT bookingid FROM test.booking WHERE bookingid="+"'"+bookingid+"'"+" AND userid="+"'"+userid+"'");
        int count = 0;
        while (rs.next()){
            count++;
        }
        rs.close();st.close();db.close();
        if (count>0){
            return true;
        }
        return false;
    }

    private static void showHostReviews(int userid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT platformuser.firstname,platformuser.lastname,hostreview.propertyid,hostreview.review,hostreview.location,hostreview.accuracy,hostreview.cleanliness,hostreview.checkin,hostreview.communication,hostreview.value,hostreview.created FROM test.property,test.platformuser,test.hostreview WHERE hostreview.writerid = platformuser.userid AND hostreview.userid="+"'"+userid+"'");
        while(rs.next()){
            System.out.println("__________________________________________________________");
            System.out.println("Reviewer Name: "+rs.getString(1)+" "+rs.getString(2));
            System.out.println("PropertyID: "+rs.getString(3));
            System.out.println("Review: "+rs.getString(4));
            System.out.println("Location Rating: "+rs.getString(5));
            System.out.println("Accuracy Rating: "+rs.getString(6));
            System.out.println("Checkin Rating: "+rs.getString(7));
            System.out.println("Communication Rating: "+rs.getString(8));
            System.out.println("Cleanliness Rating: "+rs.getString(9));
            System.out.println("Value Rating: "+rs.getString(10));
            System.out.println("Posted Date: "+rs.getString(11));
        }
        rs.close();st.close();db.close();
    }

    //SEARCH FUNCTIONS
    private static int[] searchPropertyByCity(String city) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT propertyid FROM test.property WHERE status='Active' AND addresscity="+"'"+city+"'");
        ResultSetMetaData md =rs.getMetaData();
        int count = 0;
        while (rs.next()){
            count +=1;
        }
        rs.close();
        ResultSet rs2 = st.executeQuery("SELECT propertyid FROM test.property WHERE status='Active' AND addresscity="+"'"+city+"'");
        int[] setofprop = new int[count];
        int count2 = 0;
        while (rs2.next()){
            setofprop[count2++]=rs2.getInt(1);
            if (count2==count){
                break;
            }
        }
        rs2.close();
        st.close();
        db.close();
        return setofprop;
    }
    private static int[] searchPropertyByRoom(int minrooms,int maxrooms) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT propertyid FROM test.property WHERE status='Active' AND rooms BETWEEN "+minrooms+" AND "+ maxrooms);
        ResultSetMetaData md =rs.getMetaData();
        int count = 0;
        while (rs.next()){
            count +=1;
        }
        rs.close();
        ResultSet rs2 = st.executeQuery("SELECT propertyid FROM test.property WHERE status='Active' AND rooms BETWEEN "+ minrooms+ " AND "+ maxrooms);
        int[] setofprop = new int[count];
        int count2 = 0;
        while (rs2.next()){
            setofprop[count2++]=rs2.getInt(1);
            if (count2==count){
                break;
            }
        }
        rs2.close();
        st.close();
        db.close();
        return setofprop;}
    private static int[] searchPropertyByBathroom(int minbath,int maxbath) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT propertyid FROM test.property WHERE status='Active' AND bathrooms BETWEEN "+minbath+" AND "+ maxbath);
        ResultSetMetaData md =rs.getMetaData();
        int count = 0;
        while (rs.next()){
            count +=1;
        }
        rs.close();
        ResultSet rs2 = st.executeQuery("SELECT propertyid FROM test.property WHERE status='Active' AND bathrooms BETWEEN "+ minbath+ " AND "+ maxbath);
        int[] setofprop = new int[count];
        int count2 = 0;
        while (rs2.next()){
            setofprop[count2++]=rs2.getInt(1);
            if (count2==count){
                break;
            }
        }
        rs2.close();
        st.close();
        db.close();
        return setofprop;
    }
    private static int[] searchPropertyByMinStay(int maxstay) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT propertyid FROM test.property WHERE status='Active' AND minimumstay BETWEEN "+0+" AND "+ maxstay);
        ResultSetMetaData md =rs.getMetaData();
        int count = 0;
        while (rs.next()){
            count +=1;
        }
        rs.close();
        ResultSet rs2 = st.executeQuery("SELECT propertyid FROM test.property WHERE status='Active' AND minimumstay BETWEEN "+0+ " AND "+ maxstay);
        int[] setofprop = new int[count];
        int count2 = 0;
        while (rs2.next()){
            setofprop[count2++]=rs2.getInt(1);
            if (count2==count){
                break;
            }
        }
        rs2.close();
        st.close();
        db.close();
        return setofprop;
    }

    private static int makePayment(int propertyid,int payerid,int receiveeid,Date sdate,Date edate) throws SQLException, ClassNotFoundException {
        Scanner in = new Scanner(System.in);
        String paytype= null;
        boolean a = true;
        while(a) {
            System.out.println("1. Debit");
            System.out.println("2. Credit");
            System.out.print("Select Type of Payment: ");
            String cho = in.nextLine();
            if (cho.equals("1")) {
                paytype = "Debit";
                break;
            }
            else if(cho.equals("2")){
                paytype = "Credit";
                break;
            }
            else{
                continue;
            }
        }
        int totaldays = getDifferenceDays(sdate,edate);
        float total = (getPropertyPrice(propertyid)*totaldays);
        boolean a5 = true;
        while(a5) {
            System.out.println("1. Accept");
            System.out.println("2. Decline");
            System.out.println("Total cost: "+ total);
            String choi = in.nextLine();
            if (choi.equals("1")) {
                Date day = getCurrentDate();
                createPaymentSQL(payerid,receiveeid,paytype,total,day,"Active");
                System.out.println("Payment Successful");
                return getPaymentID(payerid,receiveeid,total,paytype,day,"Active");
            }
            else if(choi.equals("2")){
                break;
            }
            else{
                continue;
            }
        }
        return -1;
    }

    private static boolean signRentalAgreement(int propertyid,Date s, Date e){
        Scanner agree = new Scanner(System.in);
        System.out.println("---------------------------------COMPLETE RENTAL AGREEMENT-----------------------------------");
        System.out.println("Before making the booking you have to Esign this agreement.");
        System.out.println("Do you agree to book property "+propertyid);
        System.out.println("from "+s+" to "+e+"? (Y/N): ");
        String agre = agree.nextLine();
        while (!(agre.equals("Y")||agre.equals("y")||agre.equals("N")||agre.equals("n"))){
            System.out.println("Do you agree to book from "+s+" to "+e+"? (Y/N): ");
            agre = agree.nextLine();
        }
        if ((agre.equals("Y")||agre.equals("y"))){
            return true;
        }
        else{
            return false;
        }
    }

    private static int getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return (int)(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
    }

    private static Date getCurrentDate(){
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate localDate = LocalDate.now();
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }

    private static void makeAccount() throws SQLException, ClassNotFoundException {
        Scanner in = new Scanner(System.in);
        String pass;
        String fname;String lname;
        String dob;int addressnum;String addressstreet;
        String addresspostal;String addresscity;String countryid=null;
        String email;String phone;
        String about;String usertype = "";

        System.out.print("Enter Email: ");
        email = in.nextLine();

        System.out.print("Enter Password: ");
        pass = in.nextLine();

        System.out.print("Enter First name: ");
        fname = in.nextLine();

        System.out.print("Enter Last name: ");
        lname = in.nextLine();

        System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
        dob = in.nextLine();

        System.out.print("Enter Street Number: ");
        addressnum = in.nextInt();
        in.nextLine();
        System.out.print("Enter Street Address: ");
        addressstreet = in.nextLine();

        System.out.print("Enter Postal Code: ");
        addresspostal = in.nextLine();

        System.out.print("Enter City: ");
        addresscity = in.nextLine();

        boolean a = true;
        while(a) {
            System.out.println("1. Canada");
            System.out.println("2. The United States of America");
            System.out.println("3. France");
            System.out.print("Enter Country from Available (1/2/3): ");
            String choi = in.nextLine();
            if (choi.equals("1")) {
                countryid = "CA";
                break;
            }
            else if(choi.equals("2")){
                countryid = "US";
                break;
            }
            else if(choi.equals("3")){
                countryid = "FR";
                break;
            }
            else{
                continue;
            }
        }

        System.out.print("Enter Phone Number: ");
        phone = in.nextLine();

        System.out.print("Enter About you: ");
        about = in.nextLine();

        boolean a2 = true;
        while(a2) {
            System.out.println("1. For Renting");
            System.out.println("2. For Owning");
            System.out.print("Enter usertype (1/2): ");
            String choi = in.nextLine();
            if (choi.equals("1")) {
                usertype = "Renter";
                break;
            }
            else if(choi.equals("2")){
                usertype = "Owner";
                break;
            }
            else{
                continue;
            }
        }

        Boolean inserted = (createUserSQL(pass,fname,lname,dob,addressnum,addressstreet,addresscity,addresspostal,countryid,email,phone,about,usertype,getCurrentDate(),"Active"));
        if (inserted){
            System.out.println("Successfully Created Account");

        }
    }

    private static boolean checkAccount(String email) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM test.platformuser WHERE email="+"'"+email+"'");
        ResultSetMetaData check = rs.getMetaData();
        rs.next();
        if (rs.getString(1).equals("1")){
            rs.close();
            st.close();
            db.close();
            return true;
        }
        rs.close();
        st.close();
        db.close();
        return false;
    }

    private static int getUserIDfromEmail(String email) throws SQLException, ClassNotFoundException{
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT userid FROM test.platformuser WHERE email="+"'"+email+"'");
        rs.next();
        int result = rs.getInt(1);
        rs.close();
        st.close();
        db.close();
        return result;
    }

    private static String getPass(int userid) throws SQLException, ClassNotFoundException{
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT pword FROM test.platformuser WHERE userid="+"'"+userid+"'");
        rs.close();
        st.close();
        db.close();
        return rs.getString(1);
    }

    private static Boolean checkPass(int userid, String pass) throws SQLException, ClassNotFoundException{
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT pword FROM test.platformuser WHERE userid="+"'"+userid+"'");
        rs.next();
        String correctpass = rs.getString(1);
        if (!(correctpass.equals(pass))){
            return false;
        }
        rs.close();
        st.close();
        db.close();
        return true;

    }

    private static String getNamefromUser(int userid) throws SQLException, ClassNotFoundException{
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT firstname,lastname FROM test.platformuser WHERE userid="+"'"+userid+"'");
        rs.next();
        String fullname = rs.getString(1)+" "+rs.getString(2);
        rs.close();st.close();db.close();
        return fullname;
    }

    //Function that lets you login
    private static int login() throws SQLException, ClassNotFoundException {
        Scanner in = new Scanner(System.in);
        Boolean active = true;
        while (active) {
            System.out.print("Enter your email: ");
            String email = in.nextLine();
            if (!checkAccount(email)) {
                System.out.println("There is no account associated with that email.");
            } else {
                System.out.print("Enter your password: ");
                String pass = in.nextLine();
                if (!checkPass(getUserIDfromEmail(email), pass)) {
                    System.out.println("Invalid Password. Try Again");
                } else {
                    System.out.println("Successful Login");
                    System.out.println("------------------------------------------------------------------");
                    return getUserIDfromEmail(email);
                }
            }
        }
        return 0;
    }

    private static String getBranchFromCountry(String countryid) throws SQLException, ClassNotFoundException {
        Connection db = loadDriver();
        Statement st = db.createStatement();
        ResultSet rs = st.executeQuery("SELECT BranchID FROM test.Branch WHERE CountryID="+"'"+countryid+"'");
        rs.next();
        String bid = rs.getString(1);
        rs.close();st.close();db.close();
        return bid;
    }

    private static boolean listContains(int[] list, int value){
        boolean result = false;
        for(int i =0;i<list.length;i++){
            if (list[i]==value){
                result=true;
            }
            else{
                result=result;
            }
        }
        return result;
    }

    private static void runRENTERUI(int userid) throws SQLException, ClassNotFoundException, ParseException {
        Scanner in = new Scanner(System.in);
        System.out.println("----------------RENTER ACCOUNT----------------");
        System.out.println("1. Search for Property"); //complete
        System.out.println("2. Rent a Property"); //complete
        System.out.println("3. Post Review"); //complete
        System.out.println("4. Cancel Booking");//complete
        System.out.println("5. View Property Amenities"); //complete
        System.out.println("6. Log Out");//complete
        System.out.println("---------------------------------------------");
        System.out.print("What do you want to do? (1/2/3/4/5/6): ");
        int choice = in.nextInt();
        in.nextLine();
        if (choice==1) {
            boolean act = true;
            while(act){
                System.out.println("Search By: ");
                System.out.println("1. City"); //completed
                System.out.println("2. # of Rooms"); //completed
                System.out.println("3. # of Bathrooms"); //completed
                System.out.println("4. Minimum Stay"); //completed
                System.out.print("Choice (1/2/3/4): ");
                int choic = in.nextInt();in.nextLine();
                if (choic==1){
                    System.out.print("Type a city: ");
                    String city = in.nextLine();
                    int[] prop = searchPropertyByCity(city);
                    for (int i =0;i<prop.length;i++){
                        listPropertyINFOFromID(prop[i]);
                    }
                    break;
                }
                else if (choic==2){
                    System.out.print("What is the minimum # of rooms:  ");
                    int minroom = in.nextInt();in.nextLine();
                    System.out.print("What is the maximum # of rooms:  ");
                    int maxroom = in.nextInt();in.nextLine();
                    int[] prop = searchPropertyByRoom(minroom,maxroom);
                    for (int i =0;i<prop.length;i++){
                        listPropertyINFOFromID(prop[i]);
                    }
                    break;
                }
                else if (choic==3) {
                    System.out.print("What is the minimum # of bathrooms:  ");
                    int minbath = in.nextInt();
                    in.nextLine();
                    System.out.print("What is the maximum # of bathrooms:  ");
                    int maxbath = in.nextInt();
                    in.nextLine();
                    int[] prop = searchPropertyByBathroom(minbath, maxbath);
                    for (int i = 0; i < prop.length; i++) {
                        listPropertyINFOFromID(prop[i]);
                    }
                    break;
                }
                else if (choic==4){
                    System.out.print("How many days would you like to stay:  ");
                    int maxbath = in.nextInt();
                    in.nextLine();
                    int[] prop = searchPropertyByMinStay(maxbath);
                    for (int i = 0; i < prop.length; i++) {
                        listPropertyINFOFromID(prop[i]);
                    }
                    break;
                }



            }
            boolean x= true;
            while(x){
                System.out.println("CONTINUE? (Y/N)");
                String ans = in.nextLine();
                if ((ans.equals("Y")||ans.equals("y"))){
                    runRENTERUI(userid);
                    break;}
                else if ((ans.equals("N"))||ans.equals("n")){
                    break;}
                else {
                    System.out.println("Invalid Entry. Try Again.");
                    return; }

            }

        }
        else if (choice==2){
            System.out.print("Type Property ID to Start Booking: ");
            int propertyid = in.nextInt();in.nextLine();
            System.out.println("Type Start Date (YYYY-MM-DD): ");
            String sdate = in.nextLine();
            Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(sdate);
            System.out.println("Type End Date (YYYY-MM-DD):");
            String edate = in.nextLine();
            Date date2=new SimpleDateFormat("yyyy-MM-dd").parse(edate);
            if (checkBookingAvailable(date1,date2,propertyid)){
                boolean agree = signRentalAgreement(propertyid,date1,date2);
                if (agree){
                    System.out.println("SIGN HERE: ");
                    String sign = in.nextLine();
                    boolean signed = createRentalAgreementSQL(propertyid,date1,date2,sign,getCurrentDate(),"Active");
                    System.out.println("Agreement Signed. Proceed to payment...");
                    int payID = makePayment(propertyid,userid,getUserIDfromPropertyID(propertyid),date1,date2);
                    createBookingSQL(userid,propertyid,payID,date1,date2,getCurrentDate(),"Active");
                    System.out.println("Successfully Booked Property "+propertyid+" from "+sdate+" to "+edate);
                    boolean x= true;
                    while(x){
                        System.out.println("CONTINUE? (Y/N)");
                        String ans = in.nextLine();
                        if ((ans.equals("Y")||ans.equals("y"))){
                            runRENTERUI(userid);
                            break;}
                        else if ((ans.equals("N"))||ans.equals("n")){
                            break;}
                        else {
                            System.out.println("Invalid Entry. Try Again.");
                            return; }}}
                else {
                    System.out.println("Booking Cancelled");
                    runRENTERUI(userid);
                }
            }
            else{
                System.out.println("Not Available Sorry. ");
                boolean x= true;
                while(x){
                    System.out.println("CONTINUE? (Y/N)");
                    String ans = in.nextLine();
                    if ((ans.equals("Y")||ans.equals("y"))){
                        runRENTERUI(userid);
                        break;}
                    else if ((ans.equals("N"))||ans.equals("n")){
                        break;}
                    else {
                        System.out.println("Invalid Entry. Try Again.");
                        return; }

                }
            }

        }
        else if (choice==4){
            System.out.println("Below are Properties You have Booked: ");
            int test = bookedPropertiesforUser(userid);
            boolean test2 = false;
            if (test==0){
                System.out.println("You havent Booked any Properties Yet.");
                boolean x= true;
                while(x){
                    System.out.println("CONTINUE? (Y/N)");
                    String ans = in.nextLine();
                    if ((ans.equals("Y")||ans.equals("y"))){
                        runRENTERUI(userid);
                        test2 = true;
                        break;}
                    else if ((ans.equals("N"))||ans.equals("n")){
                        test2 = true;
                        break;}
                    else {
                        System.out.println("Invalid Entry. Try Again.");
                        return; }
                }
            }
            if (test2){
                return;
            }
            boolean confirm = false;
            System.out.print("Type booking ID to cancel that booking: ");
            int bookid = in.nextInt();in.nextLine();
            while (!bookingExists(bookid,userid)){
                System.out.println("That ID does not exist under your account.");
                System.out.println("CONTINUE? (Y/N): ");
                String agre = in.nextLine();
                while (!(agre.equals("Y")||agre.equals("y")||agre.equals("N")||agre.equals("n"))){
                    System.out.println("CONTINUE? (Y/N): ");
                    agre = in.nextLine();
                }
                if ((agre.equals("Y")||agre.equals("y"))){
                    System.out.println("Type booking ID to cancel that booking: ");
                    bookid = in.nextInt();in.nextLine();
                }
                else{
                    confirm = true;
                    break;
                }
            }
            if (confirm){
                return;
            }
            boolean notpassed = BookingHasNotPassed(bookid);
            if (notpassed){
                cancelBookingSQL(bookid);
                cancelPaymentSQL(getPaymentIDfromBooking(bookid));
                System.out.println("Booking Cancelled Successfully.");
            }
            else{
                System.out.println("Booking Cancellation Denied.");
                System.out.println("Booking has passed or not active. Cannot Cancel anyone. ");
            }

            boolean x= true;
            while(x){
                System.out.println("CONTINUE? (Y/N)");
                String ans = in.nextLine();
                if ((ans.equals("Y")||ans.equals("y"))){
                    runRENTERUI(userid);
                    break;}
                else if ((ans.equals("N"))||ans.equals("n")){
                    break;}
                else {
                    System.out.println("Invalid Entry. Try Again.");
                    return; }

            }
        }
        //REVIEW
        else if (choice==3){
            System.out.println("Below are Properties You have Booked: ");
            int test = bookedPropertiesforUser(userid);
            boolean test2= false;
            if (test==0){
                System.out.println("You havent Booked any Properties Yet.");
                boolean x= true;
                while(x){
                    System.out.println("CONTINUE? (Y/N)");
                    String ans = in.nextLine();
                    if ((ans.equals("Y")||ans.equals("y"))){
                        runRENTERUI(userid);
                        test2 = true;
                        break;}
                    else if ((ans.equals("N"))||ans.equals("n")){
                        test2 = true;
                        break;}
                    else {
                        System.out.println("Invalid Entry. Try Again.");
                        return; }
                }
            }
            if (test2){
                return;
            }
            boolean confirm = false;
            System.out.print("Type BookingID of Property you would like to review: ");
            int bookid = in.nextInt();in.nextLine();
            while (!bookingExists(bookid,userid)){
                System.out.println("That booking ID does not exist under your account.");
                System.out.println("CONTINUE? (Y/N): ");
                String agre = in.nextLine();
                while (!(agre.equals("Y")||agre.equals("y")||agre.equals("N")||agre.equals("n"))){
                    System.out.println("CONTINUE? (Y/N): ");
                    agre = in.nextLine();
                }
                if ((agre.equals("Y")||agre.equals("y"))){
                    System.out.println("Type BookingID of Property you would like to review: ");
                    bookid = in.nextInt();in.nextLine();
                }
                else{
                    confirm = true;
                    break;
                }
            }
            if (confirm){
                return;
            }
            System.out.println("Write your Review below (180 characters): ");
            String review = in.nextLine();
            System.out.println("Type value from 1-10 for Location: ");
            int loca = in.nextInt();in.nextLine();
            System.out.println("Type value from 1-10 for Accuracy: ");
            int acc = in.nextInt();in.nextLine();
            System.out.println("Type value from 1-10 for Check-in: ");
            int chein = in.nextInt();in.nextLine();
            System.out.println("Type value from 1-10 for Communication: ");
            int comm = in.nextInt();in.nextLine();
            System.out.println("Type value from 1-10 for Cleaniness: ");
            int clea = in.nextInt();in.nextLine();
            System.out.println("Type value from 1-10 for Value: ");
            int valu = in.nextInt();in.nextLine();
            Boolean rev = createHostReviewSQL(getUserIDfromPropertyID(getPropertyIDfromBookingID(bookid)),userid,getPropertyIDfromBookingID(bookid),review,loca,acc,chein,comm,clea,valu,getCurrentDate(),"Active");
            System.out.println("Review Successfully Created.");
            boolean x= true;
            while(x){
                System.out.println("CONTINUE? (Y/N)");
                String ans = in.nextLine();
                if ((ans.equals("Y")||ans.equals("y"))){
                    runRENTERUI(userid);
                    break;}
                else if ((ans.equals("N"))||ans.equals("n")){
                    break;}
                else {
                    System.out.println("Invalid Entry. Try Again.");
                    return; }
            }
        }
        else if (choice==5){
            System.out.println("Enter PropertyID to view Amenities: ");
            int propid = in.nextInt();in.nextLine();
            while (!PropertyExists(propid)){
                System.out.println("Please use valid propertyID");
                System.out.println("Enter PropertyID to view Amenities: ");
                propid = in.nextInt();in.nextLine();
            }
            showAmenitiesFromProperty(propid);
            boolean x= true;
            while(x){
                System.out.println("CONTINUE? (Y/N)");
                String ans = in.nextLine();
                if ((ans.equals("Y")||ans.equals("y"))){
                    runRENTERUI(userid);
                    break;}
                else if ((ans.equals("N"))||ans.equals("n")){
                    break;}
                else {
                    System.out.println("Invalid Entry. Try Again.");
                    return; }
            }

        }
        //LOGOUT
        else if (choice==6){
            return;
        }
    }

    private static void runOWNERUI(int userid) throws SQLException, ClassNotFoundException, ParseException {
        Scanner in = new Scanner(System.in);
        System.out.println("----------------OWNER ACCOUNT----------------");
        System.out.println("1. Create Property"); //complete
        System.out.println("2. Delete Property"); //complete
        System.out.println("3. Read Your Reviews"); //complete
        System.out.println("4. Review a Host"); //complete
        System.out.println("5. List Your Properties"); //complete
        System.out.println("6. Log Out"); //complete
        System.out.println("---------------------------------------------");
        System.out.print("What do you want to do? (1/2/3/4/5/6): ");
        int choice = in.nextInt();
        in.nextLine();
        if (choice==1){
            String branchid;int addressnum;
            String addressstreet;String addresscity;
            String addresspostal;String countryid = null;
            String description;int rooms;
            int bathrooms;int minimumstay;
            String refundtype;

            System.out.print("Enter Street Number: ");
            addressnum = in.nextInt();
            in.nextLine();
            System.out.print("Enter Street Name: ");
            addressstreet = in.nextLine();

            System.out.print("Enter City: ");
            addresscity = in.nextLine();

            System.out.print("Enter Postal Code: ");
            addresspostal = in.nextLine();

            boolean a = true;
            while(a) {
                System.out.println("1. Canada");
                System.out.println("2. The United States of America");
                System.out.println("3. France");
                System.out.print("Enter Country from Available: ");
                String choi = in.nextLine();
                if (choi.equals("1")) {
                    countryid = "CA";
                    break;
                }
                else if(choi.equals("2")){
                    countryid = "US";
                    break;
                }
                else if(choi.equals("3")){
                    countryid = "FR";
                    break;
                }
                else{
                    continue;
                }
            }

            System.out.print("Enter Description: ");
            description = in.nextLine();

            System.out.print("Enter Room Number: ");
            rooms = in.nextInt();

            System.out.print("Enter Bathroom Number: ");
            bathrooms = in.nextInt();

            System.out.print("Enter Minimum Stay: ");
            minimumstay = in.nextInt();
            in.nextLine();
            System.out.print("Enter Refund Type: ");
            refundtype = in.nextLine();

            branchid = getBranchFromCountry(countryid);

            //Inserts property into DB and returns boolean if successful
            Boolean inserted = (createPropertySQL(userid,branchid,
                    addressnum,addressstreet,addresscity,addresspostal,countryid,
                    description,rooms,bathrooms,minimumstay,refundtype,getCurrentDate(),"Active"));

            System.out.print("Add any amenities to your property? (Y/N): ");
            String add = in.nextLine();
            while ((add.equals("Y")||add.equals("y"))){
                int[] availableam = showAvailableAmenities();
                System.out.print("Enter amenity ID: ");
                int amm = in.nextInt();in.nextLine();
                while(!(listContains(availableam,amm))){
                    System.out.println("Enter Valid amenity ID");
                    System.out.print("Enter amenity ID: ");
                    amm = in.nextInt();in.nextLine();
                }
                int propid = getPropertyID(userid,branchid,
                        addressnum,addressstreet,addresspostal,addresscity,countryid,rooms,bathrooms,getCurrentDate(),"Active");
                System.out.println("Property ID: "+ propid);
                boolean amaddded = false;
                if (!alreadyAdded(amm,propid)){
                    amaddded = createPropertyAmenitySQL(amm,propid,getCurrentDate(),"Active");
                }
                if (amaddded){System.out.println("Amenity Successfully Added.");}
                else{
                    System.out.println("Amenity was not added. Either already added or not available.");
                }
                System.out.println("Add more amenities to your property? (Y/N): ");
                add = in.nextLine();
            }

            if (inserted){
                System.out.println("Successfully Added Property");
                System.out.println("-------Complete Renter Rate Below-------");
                System.out.println("What is the Price per day?: ");
                float price = in.nextFloat();in.nextLine();
                System.out.println("What is the total Guest Number: ");
                int guest = in.nextInt();in.nextLine();
                String type = null;
                boolean a3 = true;
                while(a3) {
                    System.out.println("1. House");
                    System.out.println("2. Room");
                    System.out.print("What type of Property (1/2): ");
                    String choi = in.nextLine();
                    if (choi.equals("1")) {
                        type = "House";
                        break;
                    }
                    else if(choi.equals("2")){
                        type = "Room";
                        break;
                    }
                    else{
                        continue;
                    }
                }
                int propertyid = getPropertyID(userid,branchid,
                        addressnum,addressstreet,addresspostal,addresscity,countryid,
                        rooms,bathrooms,getCurrentDate(),"Active");
                boolean result = createRenterRateSQL(propertyid,price,guest,type,getCurrentDate(),"Active");
                if (result){
                    System.out.println("Renter Rate Successfully Added");
                }
                System.out.print("CONTINUE? (Y/N): ");
                String continu = in.next();
                if ((continu.equals("Y"))||(continu.equals("y"))){
                    runOWNERUI(userid);
                }
                else if ((continu.equals("N"))||(continu.equals("n"))){
                    System.out.println();
                }
            }
        }

        else if (choice==2){
            int test = propertyCountForUser(userid);
            boolean test2= false;
            if (test==0){
                System.out.println("You do not currently have any Property Posted.");
                boolean x= true;
                while(x){
                    System.out.println("CONTINUE? (Y/N)");
                    String ans = in.nextLine();
                    if ((ans.equals("Y")||ans.equals("y"))){
                        runOWNERUI(userid);
                        test2 = true;
                        break;}
                    else if ((ans.equals("N"))||ans.equals("n")){
                        test2 = true;
                        break;}
                    else {
                        System.out.println("Invalid Entry. Try Again.");
                        return; }
                }
            }
            if (test2){
                return;
            }
            Scanner inp = new Scanner(System.in);
            System.out.print("Enter Property ID: ");
            int id = inp.nextInt();
            Boolean deleted = (deletePropertySQL(id));
            if (deleted){
                System.out.println("Successfully Deleted Property");
                boolean x= true;
                while(x){
                    System.out.println("CONTINUE? (Y/N)");
                    String ans = in.nextLine();
                    if ((ans.equals("Y")||ans.equals("y"))){
                        runOWNERUI(userid);
                        break;}
                    else if ((ans.equals("N"))||ans.equals("n")){
                        break;}
                    else {
                        System.out.println("Invalid Entry. Try Again.");
                        return; }
                }
            }
            else{
                boolean x= true;
                while(x){
                    System.out.println("CONTINUE? (Y/N)");
                    String ans = in.nextLine();
                    if ((ans.equals("Y")||ans.equals("y"))){
                        runOWNERUI(userid);
                        break;}
                    else if ((ans.equals("N"))||ans.equals("n")){
                        break;}
                    else {
                        System.out.println("Invalid Entry. Try Again.");
                        return; }
                }
            }
        }

        else if (choice==3){
            showHostReviews(userid);
            System.out.println("----------------------------------------------");
            System.out.println("CONTINUE? (Y/N): ");
            String agre = in.nextLine();
            while (!(agre.equals("Y")||agre.equals("y")||agre.equals("N")||agre.equals("n"))){
                System.out.println("CONTINUE? (Y/N): ");
                agre = in.nextLine();
            }
            if ((agre.equals("Y")||agre.equals("y"))){
                runOWNERUI(userid);
            }
            else{
                return;
            }
        }

        else if (choice==4){
            System.out.println("Here are renters who have stayed at your properties: ");
            int[] list = showGuestsForOwner(userid);
            System.out.print("Select UserID to review: ");
            int usertoreview= in.nextInt();in.nextLine();
            while (!(listContains(list,usertoreview))){
                System.out.println("User has not stayed at your Properties.");
                System.out.print("Select UserID to review: ");
                usertoreview= in.nextInt();in.nextLine();
            }
            System.out.println("Write your Review below (180 characters): ");
            String review = in.nextLine();
            System.out.println("Type value from 1-10 for Rating: ");
            int loca = in.nextInt();in.nextLine();
            Boolean rev = createGuestReviewSQL(usertoreview,userid,review,loca,getCurrentDate(),"Active");
            System.out.println("Guest Review Successfully Created.");
            System.out.println("CONTINUE? (Y/N): ");
            String agre = in.nextLine();
            while (!(agre.equals("Y")||agre.equals("y")||agre.equals("N")||agre.equals("n"))){
                System.out.println("CONTINUE? (Y/N): ");
                agre = in.nextLine();
            }
            if ((agre.equals("Y")||agre.equals("y"))){
                runOWNERUI(userid);
            }
            else{
                return;
            }
        }

        else if (choice==5){
            retrieveALLPropertySQL(userid);
            System.out.println("----------------------------------------------");
            System.out.println("CONTINUE? (Y/N): ");
            String agre = in.nextLine();
            while (!(agre.equals("Y")||agre.equals("y")||agre.equals("N")||agre.equals("n"))){
                System.out.println("CONTINUE? (Y/N): ");
                agre = in.nextLine();
            }
            if ((agre.equals("Y")||agre.equals("y"))){
                runOWNERUI(userid);
            }
            else{
                return;
            }
        }

        else if (choice==6){
            return;
        }
        else{
            System.out.println("Invalid Entry");
            runOWNERUI(userid);
        }
    }

    //Main Program
    public static void main(String[] args) throws SQLException, ClassNotFoundException, ParseException {
        Scanner x = new Scanner(System.in);
        int user = -1;
        boolean active = true;
        while(active){
            System.out.println("------------------------------HOTEL 2.0------------------------------");
            System.out.print("Do you already have an Account? (Y/N): ");
            String accYoN = x.nextLine();
            if ((accYoN.equals("N"))||(accYoN.equals("n"))) { //if no account, option to create account
                boolean active2= true;
                while(active2) {
                    System.out.println("----------------------------------------------------------------------");
                    System.out.print("Create Account? (Y/N): ");
                    String createacc = x.nextLine();
                    if ((createacc.equals("Y")) || (createacc.equals("y"))) {
                        makeAccount();
                        break;
                    } else if ((createacc.equals("N")) || (createacc.equals("n"))) {
                        System.out.println("Thanks for using HOTEL 2.0!");
                        active2 = false;
                        break;

                    } else {
                        System.out.println("Please enter valid key");
                    }
                }
                if (!active2){
                    break;
                }
                System.out.println("--------------------------------LOG IN--------------------------------");
                user = login();
                break;
            }
            else if ((accYoN.equals("Y"))||(accYoN.equals("y"))) {
                System.out.println("--------------------------------LOG IN--------------------------------");
                user = login();
                break;
            }
            else {
                System.out.println("Please enter valid key");
            } }

        if (user!=-1){
            if (getUserTypeFromUser(user).equals("Owner")){ //if user is logged in as owner
                System.out.println("Welcome "+getNamefromUser(user));
                runOWNERUI(user);

            }
            else{
                System.out.println("Welcome "+getNamefromUser(user));
                runRENTERUI(user);
            }
        }
    }
}
