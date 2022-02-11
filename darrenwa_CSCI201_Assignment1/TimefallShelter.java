import java.io.FileNotFoundException;
import java.util.Collections;

public class TimefallShelter implements Comparable<TimefallShelter> {
	private int chiralFrequency;
	private boolean timefall;
	private String guid;
	private String name;
	private String phone;
	private String address;

	public TimefallShelter() {
		this.chiralFrequency = -1;
		this.timefall = false;
		this.guid = null;
		this.name = null;
		this.phone = null;
		this.address = null;
	}

	public TimefallShelter(int chiralFrequency, boolean timefall, String guid, String name, String phone,
			String address) {
		this.chiralFrequency = chiralFrequency;
		this.timefall = timefall;
		this.guid = guid.trim();
		this.name = name.trim();
		this.phone = phone.trim();
		this.address = address.trim();
	}

	public int getFrequency() {
		return this.chiralFrequency;
	}

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String new_guid) {
		this.guid = new_guid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String new_name) {
		this.name = new_name;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String new_phone) {
		this.phone = new_phone;
	}

	public String getAddy() {
		return this.address;
	}

	public void setAddy(String new_addy) {
		this.address = new_addy;
	}

	public boolean hasTimefall() {
		return this.timefall;
	}

	/**
	 * overriding comparator for sorting
	 */
	@Override
	public int compareTo(TimefallShelter compShelter) {
		/* For Ascending order */
		if (this.chiralFrequency == compShelter.chiralFrequency) {
			return 0;
		} else if (this.chiralFrequency < compShelter.chiralFrequency) {
			return -1;
		}

		return 1;
	}

	/**
	 * String representation of a shelter
	 */
	@Override
	public String toString() {
		return "Shelter information:\n- Chiral frequency: " + this.chiralFrequency + "\n- Timefall: "
				+ (this.timefall ? "Current" : "None") + "\n- GUID: " + this.guid + "\n- Name: " + this.name
				+ "\n- Phone: " + this.phone + "\n- Address: " + this.address + "\n";
	}
}
