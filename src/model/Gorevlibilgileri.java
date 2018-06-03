package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the gorevlibilgileri database table.
 * 
 */
@Entity
@NamedQuery(name="GorevlibilgileriCek", query="SELECT g FROM Gorevlibilgileri g")
public class Gorevlibilgileri implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int gorevliId;

	private boolean gorunurluk=false;


	private String eposta;

	private String gorevliAdi;

	private String gorevYetkisi;

	private String isim;

	private String parola;

	private String soyisim;

	private long tc;

	public Gorevlibilgileri() {
	}

	public int getGorevliId() {
		return this.gorevliId;
	}

	public void setGorevliId(int gorevliId) {
		this.gorevliId = gorevliId;
	}

	

	public String getEposta() {
		return this.eposta;
	}

	public void setEposta(String eposta) {
		this.eposta = eposta;
	}

	public String getGorevliAdi() {
		return this.gorevliAdi;
	}

	public void setGorevliAdi(String gorevliAdi) {
		this.gorevliAdi = gorevliAdi;
	}

	public String getGorevYetkisi() {
		return this.gorevYetkisi;
	}

	public void setGorevYetkisi(String gorevYetkisi) {
		this.gorevYetkisi = gorevYetkisi;
	}

	public String getIsim() {
		return this.isim;
	}

	public void setIsim(String isim) {
		this.isim = isim;
	}

	public String getParola() {
		return this.parola;
	}

	public void setParola(String parola) {
		this.parola = parola;
	}

	public String getSoyisim() {
		return this.soyisim;
	}

	public void setSoyisim(String soyisim) {
		this.soyisim = soyisim;
	}

	public long getTc() {
		return this.tc;
	}

	public void setTc(long tc) {
		this.tc = tc;
	}
	public boolean isGorunurluk() {
		return gorunurluk;
	}

	public void setGorunurluk(boolean gorunurluk) {
		this.gorunurluk = gorunurluk;
	}


}