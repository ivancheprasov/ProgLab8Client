package sample.Story;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Soup extends Food implements Serializable {
	private static final long serialVersionUID = 1741477994777066102L;
	public Soup(String name){
		super(name);
	}
	public Soup(String name, State state, Location location, int cost){
		super(name,state,location,cost);
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Soup)) return false;
		Soup soup = (Soup) o;
		return  getCost() == soup.getCost() &&
				getThing().equals(soup.getThing()) &&
				getState() == soup.getState() &&
				Objects.equals(getLocation(), soup.getLocation());
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