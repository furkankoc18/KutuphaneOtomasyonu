package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the kitaplar database table.
 * 
 */
@Entity
@NamedQueries
({
			@NamedQuery(name="TumKitaplarCek", query="SELECT k FROM Kitaplar k"),
			@NamedQuery(name="AktifTumKitaplarCek", query="Select k from Kitaplar k where k.aktifMi=true")

})


public class Kitaplar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int barkodNo;

	private boolean aktifMi=true;

	private String kitapAdi;

	private int kitapId;

	public Kitaplar() {
	}

	public int getBarkodNo() {
		return this.barkodNo;
	}

	public void setBarkodNo(int barkodNo) {
		this.barkodNo = barkodNo;
	}

	public boolean getAktifMi() {
		return this.aktifMi;
	}

	public void setAktifMi(boolean aktifMi) {
		this.aktifMi = aktifMi;
	}

	public String getKitapAdi() {
		return this.kitapAdi;
	}

	public void setKitapAdi(String kitapAdi) {
		this.kitapAdi = kitapAdi;
	}

	public int getKitapId() {
		return this.kitapId;
	}

	public void setKitapId(int kitapId) {
		this.kitapId = kitapId;
	}

}