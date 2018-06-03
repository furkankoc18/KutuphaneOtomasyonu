package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the oduncverilenler database table.
 * 
 */
@Entity
@NamedQuery(name="OduncverilenlerCek", query="SELECT o FROM Oduncverilenler o")			// ÖdünçVerilenler Veritabaný
public class Oduncverilenler implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int barkodAdi;

	@Temporal(TemporalType.DATE)
	private Date oduncverilmetarihi;

	private long tcno;

	public Oduncverilenler() {
	}

	public int getBarkodAdi() {
		return this.barkodAdi;
	}

	public void setBarkodAdi(int barkodAdi) {
		this.barkodAdi = barkodAdi;
	}

	public Date getOduncverilmetarihi() {
		return this.oduncverilmetarihi;
	}

	public void setOduncverilmetarihi(Date oduncverilmetarihi) {
		this.oduncverilmetarihi = oduncverilmetarihi;
	}

	public long getTcno() {
		return this.tcno;
	}

	public void setTcno(long tcno) {
		this.tcno = tcno;
	}

}