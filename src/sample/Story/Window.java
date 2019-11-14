package sample.Story;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class Window extends House implements Serializable {
	private static final long serialVersionUID = 6885365571214696713L;
	public Window(String name){
		super(name);
	}
	public Window(String name, State state, Location location, int cost, boolean openness){
		super(name,state,location,cost,openness);
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Window)) return false;
		Window window = (Window) o;
		return getOpenness() == window.getOpenness() &&
				getCost() == window.getCost() &&
				getThing().equals(window.getThing()) &&
				getState() == window.getState() &&
				Objects.equals(getLocation(), window.getLocation());
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
		return Objects.hash(getThing(), getState(), getOpenness(), getCost(), getLocation());
	}
}