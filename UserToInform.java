package connnect_try;

public class UserToInform {
	private String id;
	private String msg;
	private boolean marker;
	UserToInform(String id, String msg) {
		this.id = id;
		this.msg = msg;
		marker = false;
	}
	public String getId(){
		return id;
	}
	public String getMsg(){
		return msg;
	}
}
