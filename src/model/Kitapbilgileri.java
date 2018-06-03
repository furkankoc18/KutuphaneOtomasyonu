package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the kitapbilgileri database table.
 * 
 */
@Entity
@NamedQuery(name="KitapbilgileriCek", query="SELECT k FROM Kitapbilgileri k")
public class Kitapbilgileri implements Serializable						// Genel Kitaplarýn Tutulduðu Veritabaný
{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int kitapId;

	private int barkodNo;

	private String kitapAdi;

	private String kitapTuru;

	private int stokSayisi;

	@Temporal(TemporalType.DATE)
	private Date teminTarihi;

	private String yayinEvi;

	private String yazarAdi;

	public Kitapbilgileri() {
	}

	public int getKitapId() {
		return this.kitapId;
	}

	public void setKitapId(int kitapId) {
		this.kitapId = kitapId;
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

	public String getKitapTuru() {
		return this.kitapTuru;
	}

	public void setKitapTuru(String kitapTuru) {
		this.kitapTuru = kitapTuru;
	}

	public int getStokSayisi() {
		return this.stokSayisi;
	}

	public void setStokSayisi(int stokSayisi) {
		this.stokSayisi = stokSayisi;
	}

	public Date getTeminTarihi() {
		return this.teminTarihi;
	}

	public void setTeminTarihi(Date teminTarihi) {
		this.teminTarihi = teminTarihi;
	}

	public String getYayinEvi() {
		return this.yayinEvi;
	}

	public void setYayinEvi(String yayinEvi) {
		this.yayinEvi = yayinEvi;
	}

	public String getYazarAdi() {
		return this.yazarAdi;
	}

	public void setYazarAdi(String yazarAdi) {
		this.yazarAdi = yazarAdi;
	}

}