package sample.Story;
import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class CakeLayer extends Food implements Serializable {
	private static final long serialVersionUID = 6885365571214696713L;
	public CakeLayer(String name){
		super(name);
	}
	public CakeLayer(String name, State state, Location location, int cost){
		super(name,state,location,cost);
	}
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CakeLayer)) return false;
		CakeLayer cakeLayer = (CakeLayer) o;
		return  getCost() == cakeLayer.getCost() &&
				getThing().equals(cakeLayer.getThing()) &&
				getState() == cakeLayer.getState() &&
				Objects.equals(getLocation(), cakeLayer.getLocation());
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