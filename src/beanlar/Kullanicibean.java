package beanlar;

public class Kullanicibean  										//Kullanýcý Ýþlemleri Gerçekleþtirmek Ýçin Oluþturulan ManagedBeandir.
{

	private String isim="";										
	private String soyisim="";
	private long tc;
	private String cinsiyet="";
	private String eposta="";
	private String parola="";
	private String parolaDogrulama="";
	private String url="";
	private boolean guncelleme=false;			//Kullanýcý Bilgilerini Güncellemek için Oluþturdum
	
	
	
	public boolean isGuncelleme() {
		return guncelleme;
	}
	public void setGuncelleme(boolean guncelleme) {
		this.guncelleme = guncelleme;
	}
	public String getIsim() {
		return isim;
	}
	public void setIsim(String isim) {
		this.isim = isim;
	}
	public String getSoyisim() {
		return soyisim;
	}
	public void setSoyisim(String soyisim) {
		this.soyisim = soyisim;
	}
	public long getTc() {
		return tc;
	}
	public void setTc(long tc) {
		this.tc = tc;
	}
	public String getCinsiyet() {
		return cinsiyet;
	}
	public void setCinsiyet(String cinsiyet) {
		this.cinsiyet = cinsiyet;
	}
	public String getEposta() {
		return eposta;
	}
	public void setEposta(String eposta) {
		this.eposta = eposta;
	}
	public String getParola() {
		return parola;
	}
	public void setParola(String parola) {
		this.parola = parola;
	}
	public String getParolaDogrulama() {
		return parolaDogrulama;
	}
	public void setParolaDogrulama(String parolaDogrulama) {
		this.parolaDogrulama = parolaDogrulama;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
}
