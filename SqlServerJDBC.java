package connnect_try;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

public class SqlServerJDBC {
	private String ip = "10.185.145.163";
	private int count = 17;
	private int countOrder = 16;
	private ResultSet rs;
	private int command;
	private String id, password;
	
	private boolean carOwner, gender, state, orderState, hasLogIn=false;
	private String name, phone, number_plate, carType, carID, PassengerID, date, startName, destName, comment,dateP,startNameP,destNameP,commentP;
	private int age, orderNumber, maxPassenger, orderId;
	private float reputation, price, orderReputation,reputationP;
	
	public void setCommand(int command) {
		this.command = command;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setNumber_plate(String number_plate) {
		this.number_plate = number_plate;
	}
	
	public void setCarType(String carType) {
		this.carType = carType;
	}
	
	public void setCarOwner(boolean carOwner) {
		this.carOwner = carOwner;
	}
	
	public void setGender(boolean gender) {
		this.gender = gender;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public void setMaxPassenger(int maxPassenger) {
		this.maxPassenger = maxPassenger;
	}
	
	public void setState(boolean state, String id){
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            stmt.executeUpdate("update account set state = '"+state+"' where account = '"+id+"'");
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
	}
	
	public void setOrderNumber(int orderNumber, String id){
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            stmt.executeUpdate("update account set orderNumber = "+orderNumber+" where account = '"+id+"'");
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
	}
	
	public void setReputation(float reputation, String id){
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            stmt.executeUpdate("update account set reputation = '"+reputation+"' where account = '"+id+"'");
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
	}
	
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public void setCarID(String carID) {
		this.carID = carID;
	}
	
	public void setPassengerID(String PassengerID) {
		this.PassengerID = PassengerID;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setDateP(String dateP) {
		this.dateP = dateP;
	}
	
	public void setStartName(String startName) {
		this.startName = startName;
	}
	
	public void setStartNameP(String startNameP) {
		this.startNameP = startNameP;
	}
	
	public void setDestName(String destName) {
		this.destName = destName;
	}
	
	public void setDestNameP(String destNameP) {
		this.destNameP = destNameP;
	}
	
	public void setComment(String comment, int orderID){
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            stmt.executeUpdate("update orders set comment = '"+comment+"' where orderID = '"+orderID+"'");
            stmt.executeUpdate("update orders set ifcomment = '1' where orderID = '"+orderID+"'");
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
	}
	
	public void setCommentP(String commentP, int orderID) {
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            stmt.executeUpdate("update orders set commentP = '"+commentP+"' where orderID = '"+orderID+"'");
            stmt.executeUpdate("update orders set ifcommentP = '1' where orderID = '"+orderID+"'");
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
	}
	
	public void setOrderReputation(float orderReputation, int orderID){
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            stmt.executeUpdate("update orders set reputation = '"+orderReputation+"' where orderID = '"+orderID+"'");
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
	}
	
	public void setReputationP(float reputationP, int orderID) {
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            stmt.executeUpdate("update orders set reputationP = '"+reputationP+"' where orderID = '"+orderID+"'");
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
	}
	
	public void setPrice(float price, int orderID){
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            stmt.executeUpdate("update orders set price = '"+price+"' where orderID = '"+orderID+"'");
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
	}
	
	public void setOrderState(boolean orderState, int orderID){
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            stmt.executeUpdate("update orders set state = '"+orderState+"' where orderID = '"+orderID+"'");
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
	}
	
	public void newId(){
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            count = count+1;
            stmt.executeUpdate("insert into login values("+count+",'"+id+"','"+password+"')");
            stmt.executeUpdate("insert into account values("+count+",'"+id+"','"+carOwner+"','"+name+"',null,null,'"+id+"','5','0','0',null,null,null)");
            //stmt.executeUpdate("insert into account values("+count+",'"+id+"','"+carOwner+"','"+name+"','"+gender+"','"+age+"','"+id+"','5','0','0','"+number_plate+"','"+carType+"','"+maxPassenger+"')");
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
	}
	
	public void newOrder(){
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            stmt.executeUpdate("insert into orders values("+orderId+",'"+carID+"','"+PassengerID+"','"+date+"','"+startName+"','"+destName+"',null,null,null,'0','"+dateP+"','"+startNameP+"','"+destNameP+"',null,null,0,0)");
            stmt.executeUpdate("update account set orderNumber = orderNumber+1 where account = '"+carID+"' or account = '"+PassengerID+"'");
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
	}
	
	public String PassengerOrder(String PassengerID){
		JSONObject pass = new JSONObject();
		JSONArray passary = new JSONArray();
		
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa";
        String userPwd = "123456";
        
        int count=0;
        String rst=null;

        try {
            Class.forName(driverName);
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            this.rs = stmt.executeQuery("select * from orders where passengerID like '"+PassengerID+"' or carID like '"+PassengerID+"'");
            while(rs.next()){
            	count = count+1;
            	JSONObject obj = new JSONObject();
            	JSONObject car = new JSONObject();
            	JSONObject passenger = new JSONObject();
            	
            	//TODO:judge ifcomment and then show the comment
            	car.put("id", rs.getString("carID"));
            	car.put("time", rs.getString("date"));
            	car.put("start_name", rs.getString("startName"));
            	car.put("dest_name", rs.getString("destName"));
            	car.put("if_comment", rs.getBoolean("ifcomment"));
            	car.put("reputation", rs.getString("reputation"));
            	car.put("comment", rs.getString("comment"));
            	
            	passenger.put("id", rs.getString("PassengerID"));
            	passenger.put("time", rs.getString("dataP"));
            	passenger.put("start_name", rs.getString("startNameP"));
            	passenger.put("dest_name", rs.getString("destNameP"));
            	passenger.put("if_comment", rs.getBoolean("ifcommentP"));
            	passenger.put("reputation", rs.getString("reputationP"));
            	passenger.put("comment", rs.getString("commentP"));
            	
            	obj.put("order_number", rs.getString("orderID"));
            	obj.put("carowner", car);
            	obj.put("passenger", passenger);
            	obj.put("price", rs.getString("price"));
            	
            	passary.put(obj);
            }
            pass.put("hasdone", passary);
            pass.put("hasdone_number", count);
            
            rst = pass.toString();
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
        return rst;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean getCarOwner() {
		return carOwner;
	}
	
	public boolean getGender() {
		return gender;
	}
	
	public boolean getState() {
		return state;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public String getNumber_plate() {
		return number_plate;
	}
	
	public String getCarType() {
		return carType;
	}
	
	public int getAge() {
		return age;
	}
	
	public int getOrderNumber() {
		return orderNumber;
	}
	
	public int getMaxPassenger() {
		return maxPassenger;
	}
	
	public float getReputation() {
		return reputation;
	}
	
	public String getCarID() {
		return carID;
	}
	
	public String getPassengerID() {
		return PassengerID;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getDateP() {
		return dateP;
	}
	
	public String getStartName() {
		return startName;
	}
	
	public String getStartNameP() {
		return startNameP;
	}
	
	public String getDestName() {
		return destName;
	}
	
	public String getDestNameP() {
		return destNameP;
	}
	
	public String getComment() {
		return comment;
	}
	
	public String getCommentP() {
		return commentP;
	}
	
	public float getPrice() {
		return price;
	}
	
	public float getOrderReputation() {
		return orderReputation;
	}
	
	public float getReputationP() {
		return reputationP;
	}
	
	public boolean getOrderState() {
		return orderState;
	}
	
	public int getMaxOrder() {
		this.countOrder = this.countOrder+1;
		return countOrder;
	}
	
	public boolean getHasLogIn() {
		boolean rst = this.hasLogIn;
		this.hasLogIn = false;
		return rst;
	}
	
    public void run(String args[]) {

        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // 加载JDBC驱动
        String dbURL = "jdbc:sqlserver://"+ip+";DatabaseName=Tahm"; 
    
        String userName = "sa"; // 用户名
        String userPwd = "123456"; // 密码

        try {
            // 加载SQLSERVER JDBC驱动程序
            Class.forName(driverName);
            //建立数据库连接
            Connection connect = DriverManager.getConnection(dbURL, userName,userPwd);
            Statement stmt = connect.createStatement();
            
            switch(command){
            case 0:
            	this.rs = stmt.executeQuery("select * from login where account like '"+id+"'");
                while (rs.next()) {
                	/*String id0 = rs.getString("account");
                	if(id0.equals(this.id)){
                		this.password = rs.getString("password");
                	}*/
                	this.password = rs.getString("password");
                	this.hasLogIn = true;
                }
                break;
            case 1:
            	this.rs = stmt.executeQuery("select * from account where account like '"+id+"'");
                while (rs.next()) {
                	this.carOwner = rs.getBoolean("carOwner");
                	this.gender = rs.getBoolean("gender");
                	this.state = rs.getBoolean("state");
                	this.name = rs.getString("name");
                	this.phone = rs.getString("phone");
                	this.number_plate = rs.getString("number_plate");
                	this.carType = rs.getString("carType");
                	this.age = rs.getInt("age");
                	this.orderNumber = rs.getInt("orderNumber");
                	this.maxPassenger = rs.getInt("maxPassenger");
                	this.reputation = rs.getFloat("reputation");
                }
                break;
            case 2:
            	this.rs = stmt.executeQuery("select * from orders where orderID like '"+orderId+"'");
                while (rs.next()) {
                	this.carID = rs.getString("carID");
                	this.PassengerID = rs.getString("PassengerID");
                	this.date = rs.getString("date");
                	this.startName = rs.getString("startName");
                	this.destName = rs.getString("destName");
                	this.comment = rs.getString("comment");
                	this.price = rs.getFloat("price");
                	this.orderReputation = rs.getFloat("reputation");
                	this.orderState = rs.getBoolean("state");
                	this.dateP = rs.getString("dataP");
                	this.startNameP = rs.getString("startNameP");
                	this.destNameP = rs.getString("destNameP");
                	this.commentP = rs.getString("commentP");
                	this.reputationP = rs.getFloat("reputationP");
                }
                break;
            }
            
            connect.close();
            stmt.close();
            rs.close();
        } catch (ClassNotFoundException e) {
            System.out.print("Error loading SQLServer Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.print("get data error!");
            e.printStackTrace();
        }
    }

	

}