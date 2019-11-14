package sample.Story;

import sample.Controllers.MainController;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.ResourceBundle;

public interface InteractWithThings extends Serializable {
	String key();
	String place(State state);
	void deadlyThing();
    int getCost();
    Location getLocation();
	String getThing();
    State getState();
	void setLocation(int x, int y);
	void setCost(int cost);
	void setState(State state);
	int compareTo(InteractWithThings thing);
	int compare(InteractWithThings thing1, InteractWithThings thing2);
	void setTime(LocalDateTime time);
	LocalDateTime getTime();
	String getState1();
	String getOptional();
	int getX();
	int getY();
	LocalTime getTime1();
	String getTime2();
	String getOwner();
	void setOwner(String owner);
	default String getType(){
		ResourceBundle resourceBundle=ResourceBundle.getBundle("sample.Locales.Nation", Locale.getDefault());
		String type=this.getClass().toString().replace("class sample.Story.","");
		return resourceBundle.getString(type);
	};
}