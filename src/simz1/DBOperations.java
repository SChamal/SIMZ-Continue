
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simz1;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import sun.misc.JavaxSecurityAuthKerberosAccess;

/**
 *
 * @author CHAM PC
 */
public class DBOperations {

    //String url = "jdbc:mysql://localhost:3306/simz";
    private static String url = "jdbc:mysql://localhost:3306/simz?zeroDateTimeBehavior=convertToNull";
    String username = "root";
    String password = "";
    Connection con = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    Statement st = null;

    /////////////////////////////////////////USER DETAILS/////////////////////////////////////////////////////
    boolean addEmployee(EmployeeDetails ed) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "INSERT INTO employeedetails VALUES(?,?,?,?,?,?,?,?,?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setInt(1, ed.getEmpId());//add values to the sql query
            pst.setString(2, ed.getFirstName());//add values to the sql query
            pst.setString(3, ed.getLastName());//add values to the sql query
            pst.setString(4, ed.getDesignation());//add values to the sql query
            pst.setString(5, ed.getUserName());//add values to the sql query
            pst.setString(6, ed.getPassword());//add values to the sql query
            pst.setString(7, ed.getPropic());//add values to the sql query
            pst.setString(8, ed.getHint());//add values to the sql query
            pst.setString(9, ed.getNIC()); //add values to the sql query

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }

    }

    ResultSet viewUser() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT emp_id,first_name,last_name,Nic_No,designation FROM employeedetails";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    int removeUser(int empID) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "DELETE FROM employeedetails WHERE emp_id = ?";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setInt(1, empID);

            pst.executeUpdate();
            return 1; //true: successfully removed
        } catch (SQLException e) {
            System.out.println(e);
            return 0; //false: user didn't remove successfully
        }
    }

    int checkUserName(String userName) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT user_name FROM employeedetails";

            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                if (userName.equals(rs.getString(1))) {
                    return 0;//the provided username already exists in the db
                }
            }
            return 1;//the provided username does not exist in the db

        } catch (SQLException e) {
            System.out.println(e);
            return 2;//an error occured while executing

        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
    }

    int checkLogin(String userName, String pswrd) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT user_name,password,designation FROM employeedetails";

            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                if (userName.equals(rs.getString(1)) && pswrd.equals(rs.getString(2)) && rs.getString(3).equals("Manager")) {
                    return 1;//the provided username & password matched
                } else if (userName.equals(rs.getString(1)) && pswrd.equals(rs.getString(2)) && rs.getString(3).equals("Sales Person")) {
                    return 11;//the provided username & password matched
                } else if (userName.equals(rs.getString(1)) && !(pswrd.equals(rs.getString(2)))) {
                    return 2;//the provided password does not exist in the db
                }
            }
            return 0;//the provided username & password do not exist in the db

        } catch (SQLException e) {
            System.out.println(e);
            return 3;//an error occured while executing

        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
    }

    String getPropic(String userName) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT user_name,profile_pic FROM employeedetails";

            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                if (userName.equals(rs.getString(1))) {
                    return rs.getString(2);//the provided username matched
                }
                /*else if(!userName.equals(rs.getString(1))){
                 return null;//the provided username does not exist in the db
                 }*/
            }
        } catch (SQLException x) {
            System.out.println(x);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
        return null;
    }

    String getHint(String userName) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT user_name,password_hint FROM employeedetails";

            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                if (userName.equals(rs.getString(1))) {
                    return rs.getString(2);//the provided username matched
                }
                /*else if(!userName.equals(rs.getString(1))){
                 return null;//the provided username does not exist in the db
                 }*/
            }
        } catch (SQLException x) {
            System.out.println(x);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
        return null;
    }

    boolean addProPic(String pic, String userName) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "UPDATE employeedetails SET profile_pic = ? WHERE user_name = ?";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setString(1, pic);//add values to the sql query
            pst.setString(2, userName);//add values to the sql query

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }

    }

    EmployeeDetails getDetails(EmployeeDetails ed, String userName) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT first_name,last_name,emp_id,user_name FROM employeedetails";

            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                if (userName.equals(rs.getString(4))) {
                    ed.setFirstName(rs.getString(1));
                    ed.setLastName(rs.getString(2));
                    ed.setEmpId(rs.getInt(3));
                    ed.setUserName(rs.getString(4));

                    return ed;
                }
            }
            return null;

        } catch (SQLException x) {
            System.out.println(x);
            return null;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
    }

    boolean updateName(String name1, String name2, int id) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "UPDATE employeedetails SET first_name = ?, last_name = ? WHERE emp_id = ?";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setString(1, name1);//add values to the sql query
            pst.setString(2, name2);//add values to the sql query
            pst.setInt(3, id);//add values to the sql query

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
    }

    String getName(String uName) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT first_name,last_name FROM employeedetails WHERE user_name = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setString(1, uName);//add values to the sql query
            rs = pst.executeQuery();//execute the sql query and get the result
            while (rs.next()) {
                String tmpName = rs.getString(1) + " " + rs.getString(2);
                return tmpName;
            }

        } catch (SQLException e) {
            System.out.println(e);
            return "";
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
        return "";
    }

    String getNic(String uName) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT Nic_No FROM employeedetails WHERE user_name = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setString(1, uName);//add values to the sql query
            rs = pst.executeQuery();//execute the sql query and get the result
            while (rs.next()) {
                String tmpNic = rs.getString(1);
                return tmpNic;
            }

        } catch (SQLException e) {
            System.out.println(e);
            return "";
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
        return "";
    }

    int getID(String uName) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT emp_id FROM employeedetails WHERE user_name = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setString(1, uName);//add values to the sql query
            rs = pst.executeQuery();//execute the sql query and get the result
            while (rs.next()) {
                int tmpID = rs.getInt(1);
                return tmpID;
            }

        } catch (SQLException e) {
            System.out.println(e);
            return -1;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
        return -1;
    }
    

    boolean updateUserName(String name, int id) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "UPDATE employeedetails SET user_name = ? WHERE emp_id = ?";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setString(1, name);//add values to the sql query
            pst.setInt(2, id);//add values to the sql query

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
    }

    boolean updatePswrd(String ps, String hnt, int id) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "UPDATE employeedetails SET password = ?, password_hint = ? WHERE emp_id = ?";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setString(1, ps);//add values to the sql query
            pst.setString(2, hnt);//add values to the sql query
            pst.setInt(3, id);//add values to the sql query

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
    }

    int checkPasswrd(String pswrd, int id) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT emp_id,password FROM employeedetails";

            pst = (PreparedStatement) con.prepareStatement(query);

            rs = pst.executeQuery();

            while (rs.next()) {
                if (id == (rs.getInt(1)) && pswrd.equals(rs.getString(2))) {
                    return 1;//the provided password matches with the id
                } else if (id == (rs.getInt(1)) && !pswrd.equals(rs.getString(2))) {
                    return 2; // the provided password mismatched with the id
                }
            }
            return 0;//the provided password isn't in the db

        } catch (SQLException e) {
            System.out.println(e);
            return 3;//an error occured while executing

        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
    }

    boolean checkPassword(String pswd) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT designation, password  FROM employeedetails ";

            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (("Manager".equals(rs.getString(1))) && pswd.equals(rs.getString(2))) {
                    return true;//the provided password matches with the id
                }
            }
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;

        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
    }

    boolean promoteUser(String desig, int id) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "UPDATE employeedetails SET designation = ? WHERE emp_id = ?";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setString(1, desig);//add values to the sql query
            pst.setInt(2, id);//add values to the sql query

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
    }
    
    /*boolean addToUsersTable(){
     try{
     con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
     String query = "SELECT emp_id,first_name,last_name,NICno FROM employeedetails ";
            
     st = con.createStatement();
     rs = st.executeQuery(query);
     ResultSetMetaData rsmt = rs.getMetaData();
            
     int columnCount = rsmt.getColumnCount();
     Vector column = new Vector(columnCount);
            
     for(int i=1;i<=columnCount;i++){
     column.add(rsmt.getColumnName(i));
     }

     Vector data = new Vector();
     Vector row =new Vector();
            
     while(rs.next()){
     row = new Vector();
     for(int i =1;i<=columnCount;i++){
     row.add(rs.getString(i));
     }
     data.add(row);
            
     //JTable table = new JTable(data,column);
     res.column=column;
     res.data = data;
     }return true;
     }catch(Exception e){
     JOptionPane.showMessageDialog(null, "Error !!");
     return false;
     }finally{
     try{
     st.close();
     rs.close();
     con.close();
     }catch(Exception e){
     JOptionPane.showMessageDialog(null, "Error in the process");
     }
     }
     }*/
    //////////////////////////////////////////STOCK DETAILS////////////////////////////////////////
    boolean addProduct(ProductDetails pd) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "INSERT INTO productdetails VALUES(?,?,?,?,?,?,?,?,?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setInt(1, pd.getProductID());
            pst.setString(2, pd.getProductType());
            pst.setString(3, pd.getProductName());
            pst.setDouble(4, pd.getReceivingPrice());
            pst.setDouble(5, pd.getSellingPrice());
            pst.setString(6, pd.getDate());
            pst.setString(7, pd.getProductIndicator());
            pst.setInt(8, pd.getQtyLimit());
            pst.setInt(9, 0);

            pst.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    int removeProduct(int productID, String productType) {

        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query1 = "SELECT productID FROM productdetails WHERE productID = ? AND productType = ?";
            pst = (PreparedStatement) con.prepareStatement(query1);
            pst.setInt(1, productID);
            pst.setString(2, productType);
            rs = pst.executeQuery();

            while (rs.next()) {
                String query = "DELETE FROM productdetails WHERE productID = ?  AND productType = ? ";
                pst = (PreparedStatement) con.prepareStatement(query);

                pst.setInt(1, productID);
                pst.setString(2, productType);

                pst.executeUpdate();
                return 1; //true: successfully removed
            }

        } catch (SQLException e) {
            System.out.println(e);
            return 0; //false: user didn't remove successfully
        }
        return 2;
    }

    ResultSet viewStock() { // getting values changed by me
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT productID,productName,sellingPrice,expiryDate,Quantity FROM productdetails ORDER BY productName";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    boolean setTodayStock(int id, String date, int totl, int crnt, String dte, int tot) { // getting values changed by me
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "INSERT INTO today_stock VALUES(?,?,?,?,?,?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setInt(1, id);
            pst.setString(2, date);
            pst.setInt(3, totl);
            pst.setInt(4, crnt);
            pst.setString(5, dte);
            pst.setInt(6, tot);
            
            pst.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    boolean deleteTodayStock() { // getting values changed by me
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT expiryDate,productID,currentQuantity FROM today_stock";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (!(rs.getString(1)==null) && rs.getInt(3) != 0) {
                    //System.out.println(rs.getString(1));
                } else {
                    String query1 = "DELETE FROM today_stock WHERE productID = ?";
                    pst = (PreparedStatement) con.prepareStatement(query1);
                    pst.setInt(1, rs.getInt(2));
                    pst.executeUpdate();
                }
            }
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    ResultSet getTodayStockQty(int id) { // getting values changed by me
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT currentQuantity,totalreceivedQuantity,expiryDate,firstReceivedQty FROM today_stock WHERE productID = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.isFirst()) {
                    return rs;
                } else {
                    return null;
                }
            }
            //return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }
        return null;
    }

    boolean updateTodayStockQty(int id, String date, int totl, int crnt, String dte, int tot) { // getting values changed by me
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "UPDATE today_stock SET currentDate = ?, totalreceivedQuantity = ?,expiryDate = ?, currentQuantity = ?, firstReceivedQty = ? WHERE productID = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setString(1, date);
            pst.setInt(2, totl);
            pst.setString(3, dte);
            pst.setInt(4, crnt);
            pst.setInt(5, tot);
            pst.setInt(6, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }
    
    boolean updateTodayStockQty2(int id, String date, int totl, int crnt, String dte) { // getting values changed by me
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "UPDATE today_stock SET currentDate = ?, totalreceivedQuantity = ?,expiryDate = ?, currentQuantity = ? WHERE productID = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setString(1, date);
            pst.setInt(2, totl);
            pst.setString(3, dte);
            pst.setInt(4, crnt);
            pst.setInt(5, id);
            pst.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.out.println(ex);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }
    
    boolean updateProductPrice(double rPrice, double sPrice,int Qlimit, int id) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "UPDATE productdetails SET receivingPrice = ?, sellingPrice = ?,quantityLimit=? WHERE productID = ?";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setDouble(1, rPrice);//add values to the sql query
            pst.setDouble(2, sPrice);//add values to the sql query
            pst.setInt(3, Qlimit);
            pst.setInt(4, id);//add values to the sql query

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
    }

    ResultSet searchTodayStock() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT productID FROM today_stock";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    ResultSet combineTwoTables(int id, String crnt) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query2 = "UPDATE today_stock set currentDate = ? WHERE productID = ?";
            pst = (PreparedStatement) con.prepareStatement(query2);
            pst.setString(1, crnt);
            pst.setInt(2, id);
            pst.executeUpdate();
            
            String query = "SELECT p.productName,p.sellingPrice,t.expiryDate,t.currentQuantity,t.totalreceivedQuantity,t.currentDate from today_stock t ,productdetails p where (p.productID=t.productID) and p.productID = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }
    
    ResultSet combineTwoTablesForSP(int id){
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT p.productName,p.sellingPrice,t.expiryDate,t.currentQuantity from today_stock t ,productdetails p where (p.productID=t.productID) and p.productID = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }
    
    ResultSet combineMorningStockAndStock() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT p.productID,p.productName,p.sellingPrice,m.expiryDate,m.currentQuantity,m.totalreceivedQuantity from morning_stock m ,productdetails p where (p.productID=m.productID) ";
            pst = (PreparedStatement) con.prepareStatement(query);           
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }

    }
    ResultSet combineAfternoonStockAndStock() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT a.productID,p.productName,a.totalreceivedQuantity from afternoon_stock a ,productdetails p where (p.productID=a.productID) ";
            pst = (PreparedStatement) con.prepareStatement(query);           
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }

    }
    
    ResultSet getProducts() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT productName FROM productdetails ORDER BY productName";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    ResultSet getTodayProducts() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT p.productName from today_stock t ,productdetails p where (p.productID=t.productID) Order By productName";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();
            System.out.println(rs);

            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    ResultSet viewStock2(String productName) { // getting values changed by me
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT productID,productType,productName,receivingPrice, sellingPrice,expiryDate,quantityLimit FROM productdetails WHERE productName = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setString(1, productName);
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            
            return null;
        }
        
    }

    ResultSet getPID(String productName) { // getting values changed by me
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT productID,productName,sellingPrice FROM productdetails WHERE productName = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setString(1, productName);
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }
    
    ResultSet getCurrentQandExpiryDate(int id) { 
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT currentQuantity,expiryDate FROM today_stock WHERE productID = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }
        
    boolean addTransaction(String time, String date) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "INSERT INTO transaction_main  VALUES(?,?,?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setInt(1, 0);//add values to the sql query
            pst.setString(2, time);//add values to the sql query
            pst.setString(3, date);

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }

    }

    int updateTodayStockByTransactions(int id, int qty) { // getting values changed by me
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT currentQuantity FROM today_stock WHERE productID=?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.isFirst()) {
                    String query1 = "UPDATE today_stock SET currentQuantity = ? WHERE productID = ?";
                    pst = (PreparedStatement) con.prepareStatement(query1);
                    int current = rs.getInt(1);
                    pst.setInt(1, current - qty);
                    pst.setInt(2, id);
                    pst.executeUpdate();

                    String query2 = "SELECT p.quantityLimit,t.currentQuantity from today_stock t ,productdetails p where (p.productID=t.productID) and p.productID = ?";
                    pst = (PreparedStatement) con.prepareStatement(query2);
                    pst.setInt(1, id);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        if (rs.isFirst()) {
                            if (rs.getInt(2) <= rs.getInt(1)) {
                                return 11;
                            }
                        }
                    }
                    return 1;
                } else {
                    return 0;
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex);
            return 0;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return 0;
    }

    int getBillID(String time, String date) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT Bill_no FROM transaction_main WHERE Time=? AND Date = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setString(1, time);//add values to the sql query
            pst.setString(2, date);
            rs = pst.executeQuery();//execute the sql query and get the result
            while (rs.next()) {
                int BID = rs.getInt(1);
                return BID;
            }

        } catch (SQLException e) {
            System.out.println(e);
            return -1;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
        return -1;
    }

    boolean addTransaction_2(int billNo, int pId, int quantity) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "INSERT INTO transaction_2  VALUES(?,?,?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setInt(1, billNo);//add values to the sql query
            pst.setInt(2, pId);//add values to the sql query
            pst.setInt(3, quantity);

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }

    }
    
    int getMaxBillID() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT max(Bill_no) FROM transaction_main";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();//execute the sql query and get the result
            while (rs.next()) {
                int BID = rs.getInt(1);
                return BID;
            }

        } catch (SQLException e) {
            System.out.println(e);
            return -1;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
        return -1;
    }
    
    int getPrdctQty(String name) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT t.currentQuantity FROM today_stock t ,productdetails p WHERE (p.productID=t.productID) and p.productName = ?";
            pst = (PreparedStatement) con.prepareStatement(query);
            pst.setString(1, name);
            rs = pst.executeQuery();//execute the sql query and get the result
            while (rs.next()) {
                int qty = rs.getInt(1);
                return qty;
            }

        } catch (SQLException e) {
            System.out.println(e);
            return -1;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
        return -1;
    }
    
    boolean addToIncomeAndExpenditure(int id, String descript,float credit,float debit) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "INSERT INTO income_expenditure  VALUES(?,?,?,?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setInt(1, id);//add values to the sql query
            pst.setString(2, descript);//add values to the sql query
            pst.setFloat(3, credit);
            pst.setFloat(4, debit);

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }

    }

ResultSet getIAndExpences() { // getting values changed by me
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT Description,Credit,	Debit FROM income_expenditure ";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

ResultSet expireDates() { // getting values changed by me
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT currentDate,expiryDate,productID,currentQuantity FROM today_stock WHERE expiryDate <> \"0000-00-00\" AND currentQuantity <> 0";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();
            return rs;

        } catch (SQLException ex) {
            //System.out.println(ex);
            return null;
        }
    }

//Process order - Order confirmation
    boolean addOrdersToDB(String orderPlacedTime, String orderPlacedDate) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "INSERT INTO order_main  VALUES(?,?,?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setInt(1, 0);//add values to the sql query
            pst.setString(2, orderPlacedTime);
            pst.setString(3, orderPlacedDate);

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }

    }
    boolean addOrder_2(int orderID, int productID, int quantity) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "INSERT INTO order_2  VALUES(?,?,?)";
            pst = (PreparedStatement) con.prepareStatement(query);

            pst.setInt(1, orderID);//add values to the sql query
            pst.setInt(2, productID);
            pst.setInt(3, quantity);

            pst.executeUpdate();//execute the sql query and insert the values to the db table
            return true;

        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }

    }
    //get the last orderID in the database
    int getMaxOrderID() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT max(Order_No) FROM order_main";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();//execute the sql query and get the result
            while (rs.next()) {
                int orderID = rs.getInt(1);
                return orderID;
            }

        } catch (SQLException e) {
            System.out.println(e);
            return -1;
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {

            }
        }
        return -1;

    }   
    ResultSet getTodayStock() {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT t.productID,p.productName,p.sellingPrice,t.expiryDate,t.currentQuantity from today_stock t,productdetails p where (t.productID=p.productID)";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }

    }
    //get order ID from order_main table
    ResultSet getOrderIDs(){
         try {
            con = (Connection) DriverManager.getConnection(url, username, password);//get the connection
            String query = "SELECT Order_No FROM order_main";
            pst = (PreparedStatement) con.prepareStatement(query);
            rs = pst.executeQuery();//execute the sql query and get the result
            return rs; 

        } catch (SQLException e) {
            System.out.println(e);            
        } 
        return null;
        
    }
    
    ResultSet combineProductDetailsAndOrder_2(int order_No) {
        try {
            con = (Connection) DriverManager.getConnection(url, username, password);
            String query = "SELECT o.productID,o.quantity,p.productName from order_2 o ,productdetails p where (Order_No = ? )and (o.productID=p.productID) ";
            pst = (PreparedStatement) con.prepareStatement(query);  
            pst.setInt(1,order_No);

            rs = pst.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
            return null;
        }

    }
}