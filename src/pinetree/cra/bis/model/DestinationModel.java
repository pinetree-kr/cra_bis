package pinetree.cra.bis.model;

import java.io.Serializable;

public class DestinationModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 20120813L;
	protected String destinationName;
	protected String destinationSubName;
	protected String destinationType;
	protected String destinationSubType;
	protected String point1;
	protected String point2;
	protected String point3;
	protected String point4;
	protected String point5;
	
	public DestinationModel setDestinationName(String name){
		destinationName = name;
		return this;
	}

	public DestinationModel setDestinationSubName(String name){
		destinationSubName = name;
		return this;
	}

	public DestinationModel setDestinationType(String type){
		destinationType = type;
		return this;
	}
	
	public DestinationModel setDestinationSubType(String type){
		destinationSubType = type;
		return this;
	}

	public DestinationModel setPoint1(String point){
		point1 = point;
		return this;
	}
	
	public DestinationModel setPoint2(String point){
		point2 = point;
		return this;
	}
	
	public DestinationModel setPoint3(String point){
		point3 = point;
		return this;
	}
	
	public DestinationModel setPoint4(String point){
		point4 = point;
		return this;
	}
	
	public DestinationModel setPoint5(String point){
		point5 = point;
		return this;
	}
	
	public String getDestinationType(){
		return destinationType;
	}
	
	public String getDestinationSubType(){
		return destinationSubType;
	}

	public String getDestinationName(){
		return destinationName;
	}

	public String getDestinationSubName(){
		return destinationSubName;
	}
	
	public String getPoint1(){
		return point1;
	}
	
	public String getPoint2(){
		return point2;
	}
	
	public String getPoint3(){
		return point3;
	}
	
	public String getPoint4(){
		return point4;
	}
	
	public String getPoint5(){
		return point5;
	}
}
