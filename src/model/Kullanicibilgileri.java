package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the kullanicibilgileri database table.
 * 
 */
@Entity
@NamedQuery(name="KullanicibilgileriCek", query="SELECT k FROM Kullanicibilgileri k")			
public class Kullanicibilgileri implements Serializable 							// KullanýcýBilgileri Veritabaný Bilgileri
{		
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int kullaniciId;

	private String ad;

	private String cinsiyet;

	private boolean gorunurluk;

	private String mail;

	private String sifre;

	private String soyad;

	private long TCKimlikNo;

	@Temporal(TemporalType.DATE)
	@Column(name="`Uyelik Tarihi`")
	private Date uyelik_Tarihi;

	public Kullanicibilgileri() {
	}

	public int getKullaniciId() {
		return this.kullaniciId;
	}

	public void setKullaniciId(int kullaniciId) {
		this.kullaniciId = kullaniciId;
	}

	public String getAd() {
		return this.ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

	public String getCinsiyet() {
		return this.cinsiyet;
	}

	public void setCinsiyet(String cinsiyet) {
		this.cinsiyet = cinsiyet;
	}

	public boolean isGorunurluk() {
		return this.gorunurluk;
	}

	public void setGorunurluk(boolean gorunurluk) {
		this.gorunurluk = gorunurluk;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getSifre() {
		return this.sifre;
	}

	public void setSifre(String sifre) {
		this.sifre = sifre;
	}

	public String getSoyad() {
		return this.soyad;
	}

	public void setSoyad(String soyad) {
		this.soyad = soyad;
	}

	public long getTCKimlikNo() {
		return this.TCKimlikNo;
	}

	public void setTCKimlikNo(long TCKimlikNo) {
		this.TCKimlikNo = TCKimlikNo;
	}

	public Date getUyelik_Tarihi() {
		return this.uyelik_Tarihi;
	}

	public void setUyelik_Tarihi(Date uyelik_Tarihi) {
		this.uyelik_Tarihi = uyelik_Tarihi;
	}

}