package beanlar;

import java.util.Date;



public class Kitapbean							// Kitap Ýþlemlerini Gerçekleþtirmek Ýçin Kullandým
{


	private int barkodNo;

	private String kitapAdi;

	private String kitapTuru;

	private int stokSayisi;


	private Date teminTarihi;

	private String yayinEvi;

	private String yazarAdi;

	

	

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
