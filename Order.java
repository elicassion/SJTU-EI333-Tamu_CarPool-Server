package connnect_try;
public class Order{
	private User carOwner;
	private User passengerUser;
	private int number;
	private String cStartName;
	private String cDestName;
	private String pStartName;
	private String pDestName;
	private String cTimeString;
	private String pTimeString;
	private int cPoolType;
	private int pPoolType;
	private boolean carOwnerArrived = false;
	private boolean passengerArrived = false;
	
	Order(User c, User p, String cSN, String cDN, String cTS, 
			String pSN, String pDN, String pTS, int cpooltype, int ppooltype){
		carOwner = c;
		passengerUser = p;
		cStartName = cSN;
		cDestName = cDN;
		pStartName = pSN;
		pDestName = pDN;
		cTimeString = cTS;
		pTimeString = pTS;
		cPoolType = cpooltype;
		pPoolType = ppooltype;
	}
	public void setNumber(int no){
		number = no;
	}
	public int getOrderNumber(){
		return number;
	}
	public User getCarOwner(){
		return carOwner;
	}
	public User getPassenger(){
		return passengerUser;
	}
	public String getCarStartName(){
		return cStartName;
	}
	public String getCarDestName(){
		return cDestName;
	}
	public String getPassengerStartName(){
		return pStartName;
	}
	public String getPassengerDestName(){
		return pDestName;
	}
	public String getCarTimeString(){
		return cTimeString;
	}
	public String getPassengerTimeString(){
		return pTimeString;
	}
	public boolean getCarOwnerArrived(){
		return carOwnerArrived;
	}
	public boolean getPassengerArrived(){
		return passengerArrived;
	}
	public void setCarOwnerArrived(){
		carOwnerArrived = true;
	}
	public void setPassengerArrived(){
		passengerArrived = true;
	}
	public boolean isDone(){
		return carOwnerArrived && passengerArrived;
	}
	public int getCPoolType(){
		return cPoolType;
	}
	public int getPPoolType(){
		return pPoolType;
	}
}