package connnect_try;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.concurrent.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import connnect_try.User;
import connnect_try.Order;
import connnect_try.Point;
import connnect_try.MatchQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;

import javax.management.RuntimeErrorException;
public class Server {
    private int port=54321;
    private ServerSocket serverSocket;
    private ExecutorService executorService;//线程池
    private final int POOL_SIZE=10;//单个CPU线程池大小

    private static final double DIS_PER_MIN = 250.0;    
    private static final double PI = 3.14159265358979323; 
    private static final double R = 6371229;             
    private static int Query_Number = 0;
    private static int Order_Number = 0;
    private static Map<Integer, MatchQuery> matchQueryMap = new HashMap<Integer, MatchQuery>();
    private static Map<String, Point> userLocMap = new HashMap<String, Point>();
    private static Map<Integer, Order> proceedingOrderMap = new HashMap<Integer, Order>();
    private static List<UserToInform> userInformList = new ArrayList<UserToInform>();
    
    private static final String queryStubFileName = "E:\\eclipse_project\\connnect_try\\src\\connnect_try\\query_stub.txt";
    private SqlServerJDBC jdbc = new SqlServerJDBC() ;
    
    public Server() throws IOException{
        serverSocket=new ServerSocket(port);
        //Runtime的availableProcessor()方法返回当前系统的CPU数目.
        executorService=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
        System.out.println("服务器启动");
    }

    public void service(){
        while(true){
            Socket socket=null;
            try {
            	 //接收客户连接,只要客户进行了连接,就会触发accept();从而建立连接
                socket=serverSocket.accept();
                executorService.execute(new Handler(socket));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void initTestStub(){
    	File queryStubFile = new File(queryStubFileName);
    	try{
    		InputStreamReader reader = new InputStreamReader(new FileInputStream(queryStubFile), "GBK");
    		BufferedReader br = new BufferedReader(reader);
    		String line = br.readLine();
    		long idnumber = 18112378654L;
    		while (line != null){
    			JSONObject jMatchQuery = new JSONObject(line);
    			++Query_Number;
    			//++idnumber;
    			MatchQuery matchQuery = new MatchQuery(jMatchQuery, Query_Number);
    			matchQuery.time = new Date();
    			matchQuery.user = new User(Long.toString(idnumber), 2);
    			matchQuery.maxPsg = 3;
                matchQueryMap.put(Query_Number, matchQuery);
                line = br.readLine();
    		}
    	}catch(Exception e){throw new RuntimeException(e);}
    	
    }

    public static void main(String[] args) throws IOException {
    	initTestStub();
        new Server().service();
    }


    class Handler implements Runnable{
        private Socket socket;
        private String mId;
        public Handler(Socket socket){
            this.socket=socket;
        }
        private PrintWriter getWriter(Socket socket) throws IOException{
            OutputStream socketOut=socket.getOutputStream();
            return new PrintWriter(socketOut,true);
        }
        private BufferedReader getReader(Socket socket) throws IOException{
            InputStream socketIn=socket.getInputStream();
            return new BufferedReader(new InputStreamReader(socketIn, "UTF-8"));
        }

        public void run(){
            try {
                System.out.println("New connection accepted "+socket.getInetAddress()+":"+socket.getPort());
                BufferedReader br=getReader(socket);
                PrintWriter pw=getWriter(socket);
                String msg=null;
                while(true)
                {
                	msg = br.readLine();
                	if(msg!=null)
                	{
                		System.out.println(msg);
                        String reply = checkorder(msg);
                        if(reply!=null)
                        {
                        	System.out.println(reply);
                        	pw.println(reply);
                        }
                	}
                    if(msg.equals("bye"))
                        break;
                }             
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    if(socket!=null)
                    {
                    	socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        private String checkorder(String inData) {
            String answer = null;
            try {
                JSONTokener jsonParser = new JSONTokener(inData);
                // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。  
                // 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String） 
                JSONObject jsoncmd = (JSONObject) jsonParser.nextValue();
                // 接下来的就是JSON对象的操作了  
                int command = jsoncmd.getInt("command");
                switch(command)
                {
                    case 0:       		    	
                        String id0 = jsoncmd.getString("id");
                        String password = jsoncmd.getString("password");
                        JSONObject jIdPw = new JSONObject();
                        
                        jdbc.setCommand(0);
                        jdbc.setId(id0);
                        jdbc.run(null);
                        String truepass = jdbc.getPassword();
                        
                        jdbc.setCommand(1);
                        jdbc.setId(id0);
                        jdbc.run(null);
                        
                        int user_type =0;
                        if(jdbc.getHasLogIn() && password.equals(truepass)){
                        	jIdPw.put("success",true);
                        	
                        	if(jdbc.getCarOwner())
                            	user_type = 2;
                            else 
    							user_type = 1;
    						jIdPw.put("user_type",user_type);
                        	mId = id0;
                        }
                            
                        else
                        {
                        	jIdPw.put("success",false);
                        	jIdPw.put("user_type",user_type);
                        }
                        answer = jIdPw.toString();
                        break;

                    case 1:
                        String id1 = jsoncmd.getString("id");
                        JSONObject jType = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(id1);
                        jdbc.run(null);

                        boolean usertype = jdbc.getCarOwner();
                        if(usertype)
                            jType.put("type",2);
                        else
                            jType.put("type",1);
                        answer = jType.toString();
                        break;

                    case 2:
                        String id2 = jsoncmd.getString("id");
                        JSONObject jState = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(id2);
                        jdbc.run(null);

                        boolean userState = jdbc.getState();
                        jState.put("state",userState);
                        answer = jState.toString();
                        break;

                    case 3:
                        JSONObject person3 = jsoncmd.getJSONObject("user");
                        JSONObject jMaxPassenger = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(person3.getString("id"));
                        jdbc.run(null);

                        int carMaxPassenger = jdbc.getMaxPassenger();
                        jMaxPassenger.put("maxpassenger",carMaxPassenger);
                        answer = jMaxPassenger.toString();
                        break;

                    case 4:
                        String id4 = jsoncmd.getString("id");
                        JSONObject jReputation = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(id4);
                        jdbc.run(null);

                        float userreputation = jdbc.getReputation();
                        jReputation.put("reputation",userreputation);
                        answer = jReputation.toString();
                        break;

                    case 5:
                        String id5 = jsoncmd.getString("id");
                        JSONObject jGender = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(id5);
                        jdbc.run(null);

                        boolean usergender = jdbc.getGender();
                        jGender.put("gender",usergender);
                        answer = jGender.toString();
                        break;

                    case 6:
                        String id6 = jsoncmd.getString("id");
                        JSONObject jName = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(id6);
                        jdbc.run(null);

                        String username = jdbc.getName();
                        jName.put("name",username);
                        answer = jName.toString();
                        break;

                    case 7:
                        String id7 = jsoncmd.getString("id");
                        JSONObject jPhone = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(id7);
                        jdbc.run(null);

                        String userphone = jdbc.getPhone();
                        jPhone.put("phone",userphone);
                        answer = jPhone.toString();
                        break;

                    case 8:
                        String id8 = jsoncmd.getString("id");
                        JSONObject jNumber_Plate = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(id8);
                        jdbc.run(null);

                        String usernumber_plate = jdbc.getNumber_plate();
                        jNumber_Plate.put("number_plate",usernumber_plate);
                        answer = jNumber_Plate.toString();
                        break;

                    case 9:
                        String id9 = jsoncmd.getString("id");
                        JSONObject jCar_Type = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(id9);
                        jdbc.run(null);

                        String usercar_type = jdbc.getCarType();
                        jCar_Type.put("cartype",usercar_type);
                        answer = jCar_Type.toString();
                        break;

                    case 10:
                        String id10 = jsoncmd.getString("id");
                        JSONObject jAge = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(id10);
                        jdbc.run(null);

                        int age = jdbc.getAge();
                        jAge.put("age",age);
                        answer = jAge.toString();
                        break;

                    case 11:
                        String id11 = jsoncmd.getString("id");
                        JSONObject jOrder_Number = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(id11);
                        jdbc.run(null);

                        int order_number = jdbc.getOrderNumber();
                        jOrder_Number.put("order_number",order_number);
                        answer = jOrder_Number.toString();
                        break;

                    case 12:
                    	answer = solveMatch(jsoncmd);
                    	break;
                    
                    case 13: //确认
                    	
                    	JSONObject user = jsoncmd.getJSONObject("user");
                    	JSONObject target = jsoncmd.getJSONObject("target");
                        int self_query_number = jsoncmd.getInt("self_query_number");
                        int target_query_number = jsoncmd.getInt("target_query_number");
                        MatchQuery target_match_query = matchQueryMap.get(target_query_number);
                        MatchQuery self_match_query = matchQueryMap.get(self_query_number);
                        
                        ConfirmUser self_confirm = new ConfirmUser();
                        self_confirm.setId(user.getString("id"));
                        self_confirm.setQuerynumber(self_query_number);
                        
                        
                        
                        ConfirmUser target_confirm = new ConfirmUser();
                        target_confirm.setId(target.getString("id"));
                        target_confirm.setQuerynumber(target_query_number);
                        //self_match_query.getHaveConfirmed_User().contains(target_confirm)
                        if(checkInConfrimList(self_match_query.getHaveConfirmed_User(), target_confirm))
                        {
                        	MatchQuery nTargetMatchQuery = matchQueryMap.get(target_query_number);
                        	nTargetMatchQuery.incCurPsg();
                        	nTargetMatchQuery.addConfirmedUser(self_confirm);
                        	matchQueryMap.remove(target_query_number);
                        	if (nTargetMatchQuery.getCurPsg() < nTargetMatchQuery.getMaxPsg())
                        		matchQueryMap.put(target_query_number, nTargetMatchQuery);
                        	MatchQuery nSelfMatchQuery = matchQueryMap.get(self_query_number);
                        	nSelfMatchQuery.incCurPsg();
                        	matchQueryMap.remove(self_match_query);
                        	if (nSelfMatchQuery.getCurPsg() < nSelfMatchQuery.getMaxPsg())
                        		matchQueryMap.put(self_query_number, nSelfMatchQuery);
                        	JSONObject jConfirmedMatch = new JSONObject();
                        	jConfirmedMatch.put("code", 1);
                        	jConfirmedMatch.put("target", user);
                        	//TODO:askfor more info
                        	JSONObject jMyQuery = new JSONObject();
                        	jMyQuery.put("start_name", nTargetMatchQuery.getStartName());
                        	jMyQuery.put("dest_name", nTargetMatchQuery.getEndName());
                        	jMyQuery.put("time", nTargetMatchQuery.getTimeString());
                        	jMyQuery.put("query_number", target_query_number);
                        	jConfirmedMatch.put("my_query", jMyQuery);
                        	//broadcast info
                        	UserToInform uti = new UserToInform(target.getString("id"), jConfirmedMatch.toString());
                        	userInformList.add(uti);
                        	//add order
                        	User carowner = null;
                        	User passengerUser = null;
                        	
                        	if (user.getInt("user_type") == 1) {
                        		passengerUser = new User (user.getString("id"), user.getInt("user_type"));
                        		carowner = new User(target.getString("id"), target.getInt("user_type"));
                        		Order order = new Order(carowner, passengerUser, 
                        				target_match_query.getStartName(), target_match_query.getEndName(), 
                        				target_match_query.getTimeString(), 
                        				self_match_query.getStartName(), self_match_query.getEndName(), 
                        				self_match_query.getTimeString(), target_match_query.getPoolType(),
                        				self_match_query.getPoolType());
                        		Order_Number = jdbc.getMaxOrder();
                        		order.setNumber(Order_Number);
                        		proceedingOrderMap.put(Order_Number, order);
                        	}
                        	else{
                        		passengerUser = new User(target.getString("id"), target.getInt("user_type"));
                        		carowner = new User (user.getString("id"), user.getInt("user_type"));
                        		Order order = new Order(carowner, passengerUser, 
                        				self_match_query.getStartName(), self_match_query.getEndName(), 
                        				self_match_query.getTimeString(), 
                        				target_match_query.getStartName(), target_match_query.getEndName(), 
                        				target_match_query.getTimeString(), self_match_query.getPoolType(),
                        				target_match_query.getPoolType());
                        		Order_Number = jdbc.getMaxOrder();
                        		order.setNumber(Order_Number);
                        		proceedingOrderMap.put(Order_Number, order);
                        	}
                        }
                        else
                        {
                        	MatchQuery nTargetMatchQuery = matchQueryMap.get(target_query_number);
                        	nTargetMatchQuery.addConfirmedUser(self_confirm);
                        	matchQueryMap.remove(target_query_number);
                        	matchQueryMap.put(target_query_number, nTargetMatchQuery);
                        	JSONObject jConfirmedMatch = new JSONObject();
                        	jConfirmedMatch.put("code", 2);
                        	JSONObject jTarget = new JSONObject();
                        	jTarget.put("id", user.getString("id"));
                        	int targetUserType = user.getInt("user_type");
                        	jTarget.put("user_type", targetUserType);
                        	
                            jdbc.setCommand(1);
                            jdbc.setId(user.getString("id"));
                            jdbc.run(null);
                            
                        	jTarget.put("reputation", jdbc.getReputation());
                        	jTarget.put("finished_order_number", jdbc.getOrderNumber());
                        	jTarget.put("start_name", self_match_query.getStartName());
                        	jTarget.put("dest_name", self_match_query.getEndName());
                        	jTarget.put("time", self_match_query.getTimeString());
                        	jTarget.put("query_number", self_query_number);
                        	//TODO:show name reputation ordernumber number_plate and cerType
                        	if (targetUserType == 2){
                        		JSONObject userCarInfo = new JSONObject();
                                userCarInfo.put("number_plate",jdbc.getNumber_plate());
                                userCarInfo.put("car_color", "red");
                                userCarInfo.put("insurance", "$200000");
                                
                                userCarInfo.put("max_psg", self_match_query.getMaxPsg());
                                userCarInfo.put("cur_psg", self_match_query.getCurPsg());
                                jTarget.put("car_info", userCarInfo);
                        	}
                        	jConfirmedMatch.put("target", jTarget);
                        	//TODO:askfor more info
                        	JSONObject jMyQuery = new JSONObject();
                        	jMyQuery.put("start_name", nTargetMatchQuery.getStartName());
                        	jMyQuery.put("dest_name", nTargetMatchQuery.getEndName());
                        	jMyQuery.put("time", nTargetMatchQuery.getTimeString());
                        	jMyQuery.put("query_number", target_query_number);
                        	jConfirmedMatch.put("my_query", jMyQuery);
                        	//broadcast info
                        	UserToInform uti = new UserToInform(target.getString("id"), jConfirmedMatch.toString());
                        	userInformList.add(uti);
                        }
                    	break;

                    case 14:       
                    	String id = jsoncmd.getString("id");
                    	Point pt = new Point(jsoncmd.getDouble("lat"), jsoncmd.getDouble("lon"));
                    	userLocMap.remove("id");
                    	userLocMap.put(id, pt);
                    	break;
                    	
                    case 15:  // 询问
                        JSONObject juser15 = jsoncmd.getJSONObject("user");
                        String mid15 = juser15.getString("id");
                    	for (int i = 0; i < userInformList.size(); ++i){
                    		UserToInform utf = userInformList.get(i);
                    		if (utf.getId().equals(mid15)){
                    			answer = utf.getMsg();
                    			userInformList.remove(i);
                    			break;
                    		}
                    	}
                    	if (answer == null){
                    		JSONObject code0 = new JSONObject();
                    		code0.put("code", 0);
                    		answer = code0.toString();
                    	}
                    	break;   
                    	
                    case 16:  // rematch
                    	int queryNumber = jsoncmd.getInt("query_number");
                    	answer = reMatch(queryNumber);
                    	break;
                    	
                    case 17:  // 确认到达
                    	int orderNumber =jsoncmd.getInt("order_number");
                    	String uid = jsoncmd.getString("id");
                    	Order nOrder = proceedingOrderMap.get(orderNumber);
                    	if (nOrder.getCarOwner().getID().equals(uid))
                    		nOrder.setCarOwnerArrived();
                    	else
                    		nOrder.setPassengerArrived();
                    	if (nOrder.isDone()){
                    		jdbc.setOrderId(orderNumber);
                    		jdbc.setCarID(nOrder.getCarOwner().ID);
                    		jdbc.setPassengerID(nOrder.getPassenger().ID);
                    		jdbc.setDate(nOrder.getCarTimeString());
                    		jdbc.setStartName(nOrder.getCarStartName());
                    		jdbc.setDestName(nOrder.getCarDestName());
                    		jdbc.setDateP(nOrder.getPassengerTimeString());
                    		jdbc.setStartNameP(nOrder.getPassengerStartName());
                    		jdbc.setDestNameP(nOrder.getPassengerDestName());
                    		jdbc.newOrder();
                    		proceedingOrderMap.remove(orderNumber);
                    	}
                    	else {
							proceedingOrderMap.remove(orderNumber);
							proceedingOrderMap.put(orderNumber, nOrder);
						}
                    	
                    	break;
                    	
                    case 18:  // 所有订单
                    	String oid = jsoncmd.getString("id");
                    	int type = jsoncmd.getInt("user_type");
                    	answer = getOrderInfo(oid, type);
                    	break;
                    	
                    case 19: // 评论
                    	if(jsoncmd.getInt("user_type")==2){
                    		float reputation19 = (float)jsoncmd.getDouble("reputation");
                    		
                    		jdbc.setCommand(2);
                    		jdbc.setOrderId(jsoncmd.getInt("order_number"));
                    		jdbc.run(null);
                    		String id19 = jdbc.getPassengerID();
                    		
                    		jdbc.setCommand(1);
                    		jdbc.setId(id19);
                    		jdbc.run(null);
                    		float old_reputation19 = jdbc.getReputation();
                    		int old_order_number19 = jdbc.getOrderNumber();
                    		float new_reputation19;
                    		if(old_order_number19 ==0)
                    		{
                    			new_reputation19 = reputation19;
                    		}
                    		else {
                    			new_reputation19 = (old_reputation19*old_order_number19+reputation19)/(old_order_number19+1);
							}
                    			
                    		jdbc.setReputation(new_reputation19, id19);
                    		
                    		jdbc.setReputationP((float)jsoncmd.getDouble("reputation"), jsoncmd.getInt("order_number"));
                        	jdbc.setCommentP(jsoncmd.getString("comment"), jsoncmd.getInt("order_number"));
                    	}
                    	else{
                    		float reputation19 = (float)jsoncmd.getDouble("reputation");
                    		
                    		jdbc.setCommand(2);
                    		jdbc.setOrderId(jsoncmd.getInt("order_number"));
                    		jdbc.run(null);
                    		String id19 = jdbc.getCarID();
                    		
                    		jdbc.setCommand(1);
                    		jdbc.setId(id19);
                    		jdbc.run(null);
                    		float old_reputation19 = jdbc.getReputation();
                    		int old_order_number19 = jdbc.getOrderNumber();		
                    		float new_reputation19 = (old_reputation19*old_order_number19+reputation19)/(old_order_number19+1);
                    		
                    		jdbc.setReputation(new_reputation19, id19);
                    		
                    		jdbc.setOrderReputation((float)jsoncmd.getDouble("reputation"), jsoncmd.getInt("order_number"));
                        	jdbc.setComment(jsoncmd.getString("comment"), jsoncmd.getInt("order_number"));
                    	}
                    	
                    	break;
                    	
                    case 20: // 个人信息
                    	String id20 = jsoncmd.getString("id");
                    	JSONObject jowninfo20 = new JSONObject();

                        jdbc.setCommand(1);
                        jdbc.setId(id20);
                        jdbc.run(null);

                        jowninfo20.put("id",id20);
                        jowninfo20.put("name",jdbc.getName());
                        if(jdbc.getGender()){
                        	jowninfo20.put("gender","女");
                        }
                        else {
                        	jowninfo20.put("gender","男");
						}
                        jowninfo20.put("age",jdbc.getAge());
                        jowninfo20.put("reputation",jdbc.getReputation());
                        jowninfo20.put("state",jdbc.getState());
                        jowninfo20.put("finished_order_number",jdbc.getOrderNumber());
                        jowninfo20.put("number_plate",jdbc.getNumber_plate());
                        jowninfo20.put("car_type",jdbc.getCarType());
                        jowninfo20.put("max_psg",jdbc.getMaxPassenger());
                        if(jdbc.getCarOwner())
                        	jowninfo20.put("user_type",2);
                        else 
                        	jowninfo20.put("user_type",1);
                        answer = jowninfo20.toString();
                        break;
                        
                    case 21:  
                    	String id21 = jsoncmd.getString("phone_number");
                    	String password21 = jsoncmd.getString("password");
                    	int user_type21 = jsoncmd.getInt("user_type");
                    	String first_name = jsoncmd.getString("first_name");
                    	String last_name = jsoncmd.getString("last_name");
                    	
                    	jdbc.setCommand(0);
                    	jdbc.setId(id21);
                    	jdbc.setPassword("1");
                    	jdbc.run(null);
                    	
                    	String check_password21 = jdbc.getPassword();
                    	if(!check_password21.equals("1"))
                    	{
                    		JSONObject code021 = new JSONObject();
                    		code021.put("code", 0);
                    		answer = code021.toString();
                    	}
                    	else {
                    		jdbc.setId(id21);
                    		jdbc.setName(last_name+first_name);
                    		if(user_type21==2)
                    			jdbc.setCarOwner(true);
                    		else {
								jdbc.setCarOwner(false);
							jdbc.setPassword(password21);
							jdbc.newId();
							
							JSONObject code121 = new JSONObject();
                    		code121.put("code", 1);
                    		answer = code121.toString();
							}
						}
                    	break; 	
                        
                    default:
                        System.out.println("no this command");
                        break;
                }
            } catch (JSONException ex) {
                System.out.println("error in class Order");
            }
            return answer; 		
        }
        
        public String getOrderInfo(String uid, int type){
        	try{
				JSONArray jOrders = new JSONArray();
				int cnt_1 = 0;
				for (MatchQuery aQuery : matchQueryMap.values()){
					if (aQuery.getUser().getID().equals(uid)){
						cnt_1 = cnt_1 + 1;
						JSONObject jOrder = new JSONObject();
						jOrder.put("pool_type", aQuery.getPoolType());
						jOrder.put("query_number", aQuery.getQueryNumber());
						jOrder.put("time", aQuery.getTimeString());
						jOrder.put("start_name", aQuery.getStartName());
						jOrder.put("dest_name", aQuery.getEndName());
						jOrder.put("id", uid);
						jOrder.put("user_type", aQuery.getUser().getUserType());
						jOrders.put(jOrder);
					}
				}

				JSONArray jOrders2 = new JSONArray();
				int cnt_2 = 0;
				for (Order aOrder : proceedingOrderMap.values()){
					if (aOrder.getCarOwner().getID().equals(uid)
							|| aOrder.getPassenger().getID().equals(uid)){
						cnt_2 = cnt_2 + 1;
						JSONObject jOrder = new JSONObject();
						JSONObject jCarOwner = new JSONObject();
						JSONObject jPassenger = new JSONObject();
						jCarOwner.put("id", aOrder.getCarOwner().getID());
						jCarOwner.put("time", aOrder.getCarTimeString());
						jCarOwner.put("start_name", aOrder.getCarStartName());
						jCarOwner.put("dest_name", aOrder.getCarDestName());
						jOrder.put("carowner", jCarOwner);
						jPassenger.put("id", aOrder.getPassenger().getID());
						jPassenger.put("time", aOrder.getPassengerTimeString());
						jPassenger.put("start_name", aOrder.getPassengerStartName());
						jPassenger.put("dest_name", aOrder.getPassengerDestName());
						jOrder.put("passenger", jPassenger);
						jOrder.put("order_number", aOrder.getOrderNumber());
						jOrders2.put(jOrder);
					}
				}
				
				String total_order = jdbc.PassengerOrder(uid);
				JSONObject jOrderInfo_total = new JSONObject(total_order);
				
				
				jOrderInfo_total.put("unmatched", jOrders);
				jOrderInfo_total.put("unmatched_number", cnt_1);
				
				jOrderInfo_total.put("hasmatched", jOrders2);
				jOrderInfo_total.put("hasmatched_number", cnt_2);
				
				return jOrderInfo_total.toString();
					
        	}catch(Exception e){throw new RuntimeException(e);}
        }
        
        public String reMatch(int queryNumber){
        	try{
        		MatchQuery matchQuery = matchQueryMap.get(queryNumber);
        		Point qStartPoint = matchQuery.getStartPoint();
                Point qEndPoint  = matchQuery.getEndPoint();
                List<Point> qPoints = matchQuery.getPoints();
                Date qTime = matchQuery.getTime();
                int qPoolType = matchQuery.getPoolType();
                User qUser = matchQuery.getUser();

                JSONObject jMatchResult = new JSONObject();
                JSONArray jMatchUsers = new JSONArray();
                for (MatchQuery aQuery : matchQueryMap.values()) {
                	String matchUserId = null;
                    if (aQuery.getUser().getID() == qUser.getID()){
                        continue;
                    }
                    if (aQuery.getUser().getUserType() == qUser.getUserType()){
                        continue;
                    }
                    //passenger
                    if (qUser.getUserType() == 1) {
                        //realtime
                        if (qPoolType == 1){
                            if ((getDistance(qStartPoint, aQuery.getStartPoint()) < 500
                                    && checkTime(qTime, aQuery.getTime(), 5, false)
                                    ||
                                    getDistance(qStartPoint, userLocMap.get(aQuery.getUser().getID())) < 500)
                                    && checkOnPath(qEndPoint, aQuery.getPoints())
                                    ) {
                            	matchUserId = aQuery.getUser().getID();
                            }
                        }
                        //appointment
                        else{
                            if (( getDistance(qStartPoint, aQuery.getStartPoint()) < 500
                                    && checkTime(qTime, aQuery.getTime(), 5, false)
                                    ||
                                    checkOnPath(qStartPoint, aQuery.getPoints())
                                            && checkTime(qTime, aQuery.getTime(), getDistance(qStartPoint, aQuery.getStartPoint())/DIS_PER_MIN , true) )
                                    && checkOnPath(qEndPoint, aQuery.getPoints()) ){
                            	matchUserId = aQuery.getUser().getID();
                            }
                        }
                    }
                    //carowner
                    else{
                        if (qPoolType == 1 || qPoolType == 2){
                            if ( (getDistance(qStartPoint, aQuery.getStartPoint()) < 500
                                    && checkTime(qTime, aQuery.getTime(), 5, false)
                                    ||
                                    checkTime(aQuery.getTime(), qTime, getDistance(qStartPoint, aQuery.getStartPoint())/DIS_PER_MIN, true)
                                            && checkOnPath(aQuery.getStartPoint(), qPoints) ) 
                                    && checkOnPath(aQuery.getEndPoint(), qPoints) ) {
                            	matchUserId = aQuery.getUser().getID();
                            }
                        }
                    }
                    //finish judge
                    if (matchUserId == null) continue;
                    
                    jdbc.setCommand(1);
                    jdbc.setId(matchUserId);
                    jdbc.run(null);
                    
                    JSONObject jMatchUser = new JSONObject();
                    jMatchUser.put("match_query_number", aQuery.getQueryNumber());
                    jMatchUser.put("id", matchUserId);
                    jMatchUser.put("reputation", jdbc.getReputation());
                    jMatchUser.put("finished_order_number", jdbc.getOrderNumber());
                    jMatchUser.put("start_name", aQuery.getStartName());
                    jMatchUser.put("dest_name", aQuery.getEndName());
                    if(qUser.getUserType() == 1)
                    {
                    	JSONObject userCarInfo = new JSONObject();
                        userCarInfo.put("number_plate",jdbc.getNumber_plate());
                        userCarInfo.put("car_color", "red");
                        userCarInfo.put("insurance", "$200000");
                        userCarInfo.put("max_psg", aQuery.getMaxPsg());
                        userCarInfo.put("cur_psg", aQuery.getCurPsg());
                        jMatchUser.put("car_info", userCarInfo);
                    }
                    else
                    {
                    	jMatchUser.put("car_info","");
                    }
                    jMatchUsers.put(jMatchUser);
                }

                //JSONObject jMatchResult = generate_test_match_result();
                jMatchResult.put("users", jMatchUsers);
                jMatchResult.put("self_query_number", matchQuery.getQueryNumber());
                return jMatchResult.toString();
        	}catch(Exception e){throw new RuntimeException(e);}
        }
        
        public String solveMatch(JSONObject jMatchQuery){
            try {
                //JSONObject jMatchQuery = new JSONObject(matchQueryString);
            	++Query_Number;
                MatchQuery matchQuery = new MatchQuery(jMatchQuery, Query_Number);
                matchQueryMap.put(Query_Number, matchQuery);

                Point qStartPoint = matchQuery.getStartPoint();
                Point qEndPoint  = matchQuery.getEndPoint();
                List<Point> qPoints = matchQuery.getPoints();
                Date qTime = matchQuery.getTime();
                int qPoolType = matchQuery.getPoolType();
                User qUser = matchQuery.getUser();

                JSONObject jMatchResult = new JSONObject();
                JSONArray jMatchUsers = new JSONArray();
                for (MatchQuery aQuery : matchQueryMap.values()) {
                	String matchUserId = null;
                    if (aQuery.getUser().getID() == qUser.getID()){
                        continue;
                    }
                    if (aQuery.getUser().getUserType() == qUser.getUserType()){
                        continue;
                    }
                    //passenger
                    if (qUser.getUserType() == 1) {
                        //realtime
                        if (qPoolType == 1){
                            if ((getDistance(qStartPoint, aQuery.getStartPoint()) < 500
                                    && checkTime(qTime, aQuery.getTime(), 5, false)
                                    ||
                                    getDistance(qStartPoint, userLocMap.get(aQuery.getUser().getID())) < 500)
                                    && checkOnPath(qEndPoint, aQuery.getPoints())
                                    ) {
                            	matchUserId = aQuery.getUser().getID();
                            }
                        }
                        //appointment
                        else{
                            if (( getDistance(qStartPoint, aQuery.getStartPoint()) < 500
                                    && checkTime(qTime, aQuery.getTime(), 5, false)
                                    ||
                                    checkOnPath(qStartPoint, aQuery.getPoints())
                                            && checkTime(qTime, aQuery.getTime(), getDistance(qStartPoint, aQuery.getStartPoint())/DIS_PER_MIN , true) )
                                    && checkOnPath(qEndPoint, aQuery.getPoints()) ){
                            	matchUserId = aQuery.getUser().getID();
                            }
                        }
                    }
                    //carowner
                    else{
                        if (qPoolType == 1 || qPoolType == 2){
                            if ( (getDistance(qStartPoint, aQuery.getStartPoint()) < 500
                                    && checkTime(qTime, aQuery.getTime(), 5, false)
                                    ||
                                    checkTime(aQuery.getTime(), qTime, getDistance(qStartPoint, aQuery.getStartPoint())/DIS_PER_MIN, true)
                                            && checkOnPath(aQuery.getStartPoint(), qPoints) ) 
                                    && checkOnPath(aQuery.getEndPoint(), qPoints) ) {
                            	matchUserId = aQuery.getUser().getID();
                            }
                        }
                    }
                    //finish judge
                    
                    if (matchUserId == null) continue;
                    JSONObject jMatchUser = new JSONObject();
                    
                    jdbc.setCommand(1);
                    jdbc.setId(matchUserId);
                    jdbc.run(null);
                    
                    jMatchUser.put("match_query_number", aQuery.getQueryNumber());
                    jMatchUser.put("id", matchUserId);
                    jMatchUser.put("reputation", jdbc.getReputation());
                    jMatchUser.put("finished_order_number", jdbc.getOrderNumber());
                    jMatchUser.put("start_name", aQuery.getStartName());
                    jMatchUser.put("dest_name", aQuery.getEndName());
                    if(qUser.getUserType() == 1)
                    {
                    	JSONObject userCarInfo = new JSONObject();
                        userCarInfo.put("number_plate", jdbc.getNumber_plate());
                        userCarInfo.put("car_color", "red");
                        userCarInfo.put("insurance", "$200000");
                        userCarInfo.put("max_psg", aQuery.getMaxPsg());
                        userCarInfo.put("cur_psg", aQuery.getCurPsg());
                        jMatchUser.put("car_info", userCarInfo);
                    }
                    else
                    {
                    	jMatchUser.put("car_info","");
                    }
                    jMatchUsers.put(jMatchUser);
                }

                //JSONObject jMatchResult = generate_test_match_result();
                jMatchResult.put("users", jMatchUsers);
                jMatchResult.put("self_query_number", matchQuery.getQueryNumber());
                return jMatchResult.toString();
            }catch(Exception ex){
                throw new RuntimeException(ex);
            }

        }

        public boolean checkOnPath(Point qEnd, List<Point> points){
            for (int i = 0; i < points.size(); ++i){
                if (getDistance(qEnd, points.get(i)) < 300){
                    return true;
                }
            }
            return false;
        }



        public boolean checkMaxPsg(MatchQuery aQuery){
            return aQuery.getCurPsg() < aQuery.getMaxPsg();
        }

        public boolean checkTime(Date qTime, Date aTime, double scope, boolean greater){
            long signedDff = qTime.getTime() - aTime.getTime();
            if (greater){
                if (signedDff < 0) return false;
            }
            long dff = Math.abs(signedDff);
            long day = dff / (24*60*60*1000);
            long hour = (dff / (60*60*1000) - day*24);
            long minutes = ((dff / (60*1000)) - day*24*60- hour*60);

            return minutes < scope;
        }

        public double getDistance(Point x1, Point x2){
            if (x1 == null || x2 == null) return 1000;
            double lat1 = x1.lat, longt1 = x1.lon, lat2 = x2.lat, longt2 = x2.lon;
            double x, y, distance;
            x = (longt2-longt1)*PI*R*Math.cos( ((lat1+lat2)/2)*PI/180)/180;
            y = (lat2-lat1)*PI*R/180;
            distance = Math.hypot(x,y);
            return distance;
        }
        
        public boolean checkInConfrimList(Set<ConfirmUser> set, ConfirmUser user){
        	String userId = user.getId();
        	int queryNumber = user.getQuerynumber();
        	for (Iterator<ConfirmUser> iterator = set.iterator(); iterator.hasNext();){
        		ConfirmUser cUser = iterator.next();
        		if (cUser.getId().equals(userId) && queryNumber == cUser.getQuerynumber()){
        			return true;
        		}
        	}
        	return false;
        }
    }
}






