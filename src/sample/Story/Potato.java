package sample.Story;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Potato extends Food implements Serializable {
	private static final long serialVersionUID = 6885365571214696713L;
	public Potato(String name){
		super(name);
	}
	public Potato(String name, State state, Location location, int cost){
		super(name,state,location,cost);
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Potato)) return false;
		Potato potato = (Potato) o;
		return  getCost() == potato.getCost() &&
				getThing().equals(potato.getThing()) &&
				getState() == potato.getState() &&
				Objects.equals(getLocation(), potato.getLocation());
	}
	public String getState1(){
		ResourceBundle resourceBundle=ResourceBundle.getBundle("sample.Locales.Nation", Locale.getDefault());
		switch (getState()){
			case GOOD:
				return resourceBundle.getString("GOOD");
			case BAD:
				return resourceBundle.getString("BAD");
		}
		return null;
	}
	@Override
	public int hashCode() {
		return Objects.hash(getThing(), getState(), getCost(), getLocation());
	}
}