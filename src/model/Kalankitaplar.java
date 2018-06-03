package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the kalankitaplar database table.
 * 
 */
@Entity
@NamedQuery(name="KalankitaplarCek", query="SELECT k FROM Kalankitaplar k")
public class Kalankitaplar implements Serializable 								// KalanKitaplar Veritabýný
{
	private static final long serialVersionUID = 1L;

	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int barkodNo;

	private String kitapAdi;

	private int kitapId;

	public Kalankitaplar() {
	}

	public int getBarkodNo() {
		return this.barkodNo;
	}

	public void setBarkodNo(int barkodNo) {
		this.barkodNo = barkodNo;
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