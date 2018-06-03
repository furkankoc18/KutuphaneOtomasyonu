package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the gecmisalinanlar database table.
 * 
 */
@Entity
@NamedQuery(name="GecmisCek", query="SELECT g FROM Gecmisalinanlar g")
public class Gecmisalinanlar implements Serializable 
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="` id`")
	private int _id;

	private int barkodNo;

	private boolean elindeMi;

	@Temporal(TemporalType.DATE)
	private Date geriVermeTarihi;

	@Temporal(TemporalType.DATE)
	private Date oduncVerilmeTarihi;

	private long tc;

	public Gecmisalinanlar() {
	}

	public int get_id() {
		return this._id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getBarkodNo() {
		return this.barkodNo;
	}

	public void setBarkodNo(int barkodNo) {
		this.barkodNo = barkodNo;
	}

	public boolean getElindeMi() {
		return this.elindeMi;
	}

	public void setElindeMi(boolean elindeMi) {
		this.elindeMi = elindeMi;
	}

	public Date getGeriVermeTarihi() {
		return this.geriVermeTarihi;
	}

	public void setGeriVermeTarihi(Date geriVermeTarihi) {
		this.geriVermeTarihi = geriVermeTarihi;
	}

	public Date getOduncVerilmeTarihi() {
		return this.oduncVerilmeTarihi;
	}

	public void setOduncVerilmeTarihi(Date oduncVerilmeTarihi) {
		this.oduncVerilmeTarihi = oduncVerilmeTarihi;
	}

	public long getTc() {
		return this.tc;
	}

	public void setTc(long tc) {
		this.tc = tc;
	}

}