package pinetree.cra.bis.model;

public class BusTimeModel {
	private int time_srl;
	private String point1;
	private String point2;
	private String point3;
	private String point4;
	private String point5;
	private String start_time;
	private String destination;
	private String etc;
	
	public BusTimeModel(){}
	
	public BusTimeModel(int srl, String p1, String p2, String p3, String p4, String p5, String s_time, String dest, String e){
		time_srl = srl;
		point1 = p1;
		point2 = p2;
		point3 = p3;
		point4 = p4;
		point5 = p5;
		start_time = s_time;
		destination = dest;
		etc = e;		
	}
	
	public BusTimeModel setTimeSrl(int srl){
		time_srl = srl;
		return this;
	}
	
	public int getTimeSrl(){
		return time_srl;
	}

	public BusTimeModel setPoint1(String p1){
		point1 = p1;
		return this;
	}
	
	public String getPoint1(){
		return point1;
	}
	
	public BusTimeModel setPoint2(String p2){
		point2 = p2;
		return this;
	}
	
	public String getPoint2(){
		return point2;
	}
	
	public BusTimeModel setPoint3(String p3){
		point3 = p3;
		return this;
	}
	
	public String getPoint3(){
		return point3;
	}
	
	public BusTimeModel setPoint4(String p4){
		point4 = p4;
		return this;
	}
	
	public String getPoint4(){
		return point4;
	}
	
	public BusTimeModel setPoint5(String p5){
		point5 = p5;
		return this;
	}
	
	public String getPoint5(){
		return point5;
	}
	
	public BusTimeModel setStartTime(String s_time){
		start_time = s_time;
		return this;
	}
	
	public String getStartTime(){
		return start_time;
	}
	
	public BusTimeModel setDestination(String dest){
		destination = dest;
		return this;
	}
	
	public String getDestination(){
		return destination;
	}
	
	public BusTimeModel setEtc(String e){
		etc = e;
		return this;
	}
	
	public String getEtc(){
		return etc;
	}
}
