package phannin.ged2gv;

import java.util.ArrayList;
import java.util.List;

public class Family implements Entity, Comparable<Family>{
	private String id="";
	private String mies="";
	private String vaimo="";
	private List<String> lapset=new ArrayList<String>();
	private boolean mukaan=false;
	private Event vihitty;

	public Family(String id) {
		this.id=id;
	}


	public String getId() {
		return id;
	}


	public String getMies() {
		return mies;
	}

	public void setMies(String mies) {
		this.mies = mies;
	}

	public String getVaimo() {
		return vaimo;
	}

	public void setVaimo(String vaimo) {
		this.vaimo = vaimo;
	}

	public List<String> getLapset() {
		return lapset;
	}

	public void setLapset(List<String> lapset) {
		this.lapset = lapset;
	}



	public Event getVihitty() {
		return vihitty;
	}


	public void setVihitty(Event vihitty) {
		this.vihitty = vihitty;
	}


	public boolean isMukaan() {
		return mukaan;
	}


	public void setMukaan(boolean mukaan) {
		this.mukaan = mukaan;
	}

	private Event currentEvent; 

	@Override
	public void addLine(String line) {
//		1 HUSB @I1415@
//		1 WIFE @I1438@
//		1 CHIL @I1548@
		String[] tokens=line.split(" ",3);
		if (tokens[0].equals("1"))
			currentEvent = null;
		
		if (tokens[0].equals("1") && tokens[1].equals("HUSB")) //
			this.mies = tokens[2];
		if (tokens[0].equals("1") && tokens[1].equals("WIFE")) //
			this.vaimo = tokens[2];
		if (tokens[0].equals("1") && tokens[1].equals("CHIL")) //
			this.lapset.add(tokens[2]);
		if (tokens[0].equals("1") && tokens[1].equals("MARR")) {
			this.vihitty = new Event();
			currentEvent = this.vihitty;
		}
		if (tokens[0].equals("2") && tokens[1].equals("DATE")) {
			if (currentEvent !=null)
				currentEvent.setTime(tokens[2]);
		}


	}
	
	public String getPlainId() {
		String[] tokens=this.id.split("@");
		return tokens[1];
	}


	public String getYear() {
		if (this.vihitty != null)
			return this.vihitty.getYear();
		else
			return "";
	}


	@Override
	public int compareTo(Family o) {
		return o.getYear().compareTo(this.getYear());
	}

}
