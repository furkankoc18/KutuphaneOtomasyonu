package islevler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.message.Message;
import org.w3c.dom.css.DocumentCSS;

import beanlar.Kitapbean;
import beanlar.Kullanicibean;
import model.Gecmisalinanlar;
import model.Kalankitaplar;
import model.Kitapbilgileri;
import model.Kitaplar;
import model.Kullanicibilgileri;
import model.Oduncverilenler;
import model.Veritabani;

@ManagedBean
@SessionScoped
public class Oduncislemler implements Serializable
{
	private static final long serialVersionUID = 1L;
	Kalankitaplar kalankitaplar=new Kalankitaplar();
	Kitaplar kitaplar2=new Kitaplar();
	Veritabani veritabani=new Veritabani();
	Kitapbilgileri kitapbilgileri2=new Kitapbilgileri();
	Oduncverilenler oduncverilenler=new Oduncverilenler();
	Kullanicibilgileri kullanicibilgileri2=new Kullanicibilgileri();
	private  boolean gorunurluk=false;
	HtmlOutputText htmlOutputText;
	static	Kalankitaplar kitapKarsilama=new Kalankitaplar();
	HtmlCommandButton commandButton;
	 Kitapislemleri kitapislemleri=new Kitapislemleri();
 
	List<Kalankitaplar>kalanKitaplarListe=new ArrayList<Kalankitaplar>();
	static	Kullanicibean kullanicibean2=new Kullanicibean();
	Kitapbean kitapbean2=new Kitapbean();
	Date tarih=new Date();
	Gecmisalinanlar gecmisalinanlar=new Gecmisalinanlar();
	private String aramaYazisi="";
	
	public void tcdenBilgileriGetir(AjaxBehaviorEvent ajaxBehaviorEvent)
	{		int a=0;
		List<Kullanicibilgileri>liste=new ArrayList<Kullanicibilgileri>();
		liste=veritabani.entityManager.createNamedQuery("KullanicibilgileriCek").getResultList();
				for(Kullanicibilgileri  kullanicibilgileri:liste)
				{						gorunurluk=true;
					if(kullanicibilgileri.getTCKimlikNo()==kullanicibean2.getTc())
					{		
						commandButton.setRendered(true);

						kullanicibean2.setIsim(kullanicibilgileri.getAd());
						kullanicibean2.setSoyisim(kullanicibilgileri.getSoyad());
						kullanicibean2.setEposta(kullanicibilgileri.getMail());
						a++;
						htmlOutputText.setRendered(false);
					}
				}
				if(a==0)
				{
					gorunurluk=false;
					commandButton.setRendered(false);
					htmlOutputText.setValue("Böyle Bir Kullanýcý Yok");
					htmlOutputText.setRendered(true);
				}
			
	}
	
	public void gorunurlukKapat()
	{
		gorunurluk=false;
		htmlOutputText.setRendered(false);
		commandButton.setRendered(false);
	}
	
	public void kitapKarsilama(Kalankitaplar kitap) 
	{
	kitapKarsilama=kitap;
	}
	public void oduncVer()
	{
		FacesContext context=FacesContext.getCurrentInstance();

		if(veritabani.entityManager.createQuery("Select k.barkodAdi from Oduncverilenler k where k.tcno="+kullanicibean2.getTc()).getResultList().size()>=5)
		{

			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"HATA",kullanicibean2.getTc()+" T.C. Numaralý Kullanýcý 5 Adet Kitap Almýþtýr!!"));

		}
		else 
		{
		veritabani.entityManager.clear();

		oduncverilenler.setBarkodAdi(kitapKarsilama.getBarkodNo());
		oduncverilenler.setTcno(kullanicibean2.getTc());
		oduncverilenler.setOduncverilmetarihi(tarih);

		veritabani.transaction.begin();

			veritabani.entityManager.merge(oduncverilenler);
			

		veritabani.transaction.commit();
	
		Kalankitaplar kalankitaplar=new Kalankitaplar();

		kalankitaplar=veritabani.entityManager.find(Kalankitaplar.class, kitapKarsilama.getBarkodNo());

		
		veritabani.transaction.begin();
			veritabani.entityManager.remove(kalankitaplar);

		veritabani.transaction.commit();
		veritabani.entityManager.clear();
		context.addMessage(null, new FacesMessage("Baþarýlý","Kitap Baþarýlý Bir Þekilde "+kullanicibean2.getTc()+" Ödünç Verildi..."));

		gecmisalinanlar.setBarkodNo(kitapKarsilama.getBarkodNo());
		gecmisalinanlar.setElindeMi(true);
		gecmisalinanlar.setTc(kullanicibean2.getTc());
		gecmisalinanlar.setOduncVerilmeTarihi(tarih);
		
		veritabani.transaction.begin();
			veritabani.entityManager.merge(gecmisalinanlar);
		veritabani.transaction.commit();
		veritabani.entityManager.clear();
		
		gorunurlukKapat();
		}
		cek();
	}
	
	/*
	@PostConstruct
	public void cek()
	{
		kalanKitaplarListe=veritabani.entityManager.createNamedQuery("TumKitaplarCek").getResultList();
		for (int i = 0; i < kalanKitaplarListe.size(); i++) 
		{
			Kalankitaplar kalankitaplar=new Kalankitaplar();
			kalankitaplar.setBarkodNo(kalanKitaplarListe.get(i).getBarkodNo());
			kalankitaplar.setKitapAdi(kalanKitaplarListe.get(i).getKitapAdi());
			kalankitaplar.setKitapId(kalanKitaplarListe.get(i).getKitapId());
		
			veritabani.transaction.begin();
				veritabani.entityManager.merge(kalankitaplar);
			veritabani.transaction.commit();
			
		}
		
		
		
	}
	*/
	@PostConstruct
	public void cek()
	{
		kalanKitaplarListe.clear();
		kalanKitaplarListe=veritabani.entityManager.createNamedQuery("KalankitaplarCek").getResultList();
		
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	List<Oduncverilenler>oduncTeslimList=new ArrayList<Oduncverilenler>();
	List<Long>a=new ArrayList<Long>();
	List<Long>oduncVerilenlerTcList=new ArrayList<Long>();
	List<Kullanicibilgileri>kullaniciBilgileriListOduncVerilenler=new ArrayList<Kullanicibilgileri>();
	
	public void oduncTeslim()
	{
		a.clear();
		oduncVerilenlerTcList.clear();
		a=veritabani.entityManager.createQuery("Select t.tcno from Oduncverilenler t ").getResultList();
		for (int i = 0; i < a.size(); i++) 
		{
			if(a.indexOf(a.get(i))!=a.lastIndexOf(a.get(i)))
			{
				if(!oduncVerilenlerTcList.contains(a.get(i)))
				{
					oduncVerilenlerTcList.add(a.get(i));
				}
			}
			else
			{
				oduncVerilenlerTcList.add(a.get(i));
			}
		}
	}
	
	public String isim(long tc)
	{
		String ad=veritabani.entityManager.createQuery("Select k.ad from Kullanicibilgileri k where k.TCKimlikNo="+tc).getSingleResult().toString();
		return ad;		
	}
	
	public String soyisim(long tc)
	{
		String soyad=veritabani.entityManager.createQuery("Select k.soyad from Kullanicibilgileri k where k.TCKimlikNo="+tc).getSingleResult().toString();
		return soyad;		
	}
	public int oducVerilenAdet(long tc)
	{
		int adet=veritabani.entityManager.createQuery("Select t.tcno from Oduncverilenler t where t.tcno="+tc).getResultList().size();
		return adet;
	}
	
	public void deneme(long tc)
	{
		kullanicibean2.setIsim(	veritabani.entityManager.createQuery("Select k.ad from Kullanicibilgileri k where k.TCKimlikNo="+tc).getSingleResult().toString());
		kullanicibean2.setSoyisim(veritabani.entityManager.createQuery("Select k.soyad from Kullanicibilgileri k where k.TCKimlikNo="+tc).getSingleResult().toString());
		oduncKitapListe(tc);
	}
	
	
private List<String>oduncAlinanlarisim=new ArrayList<String>();
private	List<Integer>oduncAlinanlarBarkodNo=new ArrayList<Integer>();
private List<Date>oduncAlinanlarTarih=new ArrayList<Date>();

	public void oduncKitapListe(long tc)
	{
		oduncAlinanlarBarkodNo=veritabani.entityManager.createQuery("Select k.barkodAdi from Oduncverilenler k where k.tcno="+tc).getResultList();
		for(int i=0;i<oduncAlinanlarBarkodNo.size();i++)
		{
		oduncAlinanlarisim.add(veritabani.entityManager.createQuery("Select k.kitapAdi from Kitaplar k where k.barkodNo="+oduncAlinanlarBarkodNo.get(i)).getSingleResult().toString());
		oduncAlinanlarTarih.add((Date) veritabani.entityManager.createQuery("Select k.oduncverilmetarihi from Oduncverilenler k where k.barkodAdi="+oduncAlinanlarBarkodNo.get(i)).getSingleResult());
		}
	
		
	//	Collections.sort(oduncAlinanlarTarih);
		
	}
	
	List<Gecmisalinanlar>gecmisList=new ArrayList<Gecmisalinanlar>();
	public void teslimEt(int barkod) throws Exception
	{
		FacesContext context=FacesContext.getCurrentInstance();

		Oduncverilenler oduncverilenler=new Oduncverilenler();
		Kalankitaplar kalankitaplar=new Kalankitaplar();
		Kitaplar kitaplar=new Kitaplar();

		oduncverilenler=veritabani.entityManager.find(Oduncverilenler.class, barkod);
		kitaplar=veritabani.entityManager.find(Kitaplar.class, barkod);
		
		
		kalankitaplar.setBarkodNo(barkod);
		kalankitaplar.setKitapAdi(kitaplar.getKitapAdi());
		kalankitaplar.setKitapId(kitaplar.getKitapId());
		
		veritabani.transaction.begin();
			veritabani.entityManager.persist(kalankitaplar);
			veritabani.entityManager.remove(oduncverilenler);
		veritabani.transaction.commit();
	veritabani.entityManager.clear();
	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Baþarýlý","Baþarýlý Bir Þekilde Teslim Edildi.."));
		
	gecmisList=veritabani.entityManager.createNamedQuery("GecmisCek").getResultList();
	for(Gecmisalinanlar gecmisalinanlar:gecmisList)
	{
		if(gecmisalinanlar.getElindeMi()==true && gecmisalinanlar.getBarkodNo()==barkod)
		{
			veritabani.transaction.begin();
			gecmisalinanlar.setGeriVermeTarihi(new Date());
			gecmisalinanlar.setElindeMi(false);
			veritabani.transaction.commit();
			veritabani.entityManager.clear();

		}
	}

	
	
	}
	List<Kalankitaplar>liste2=new ArrayList<Kalankitaplar>();

	public void oduncVermeArama(AjaxBehaviorEvent ajaxBehaviorEvent)
	{
		kalanKitaplarListe.clear();
		liste2.clear();
		liste2=veritabani.entityManager.createNamedQuery("KalankitaplarCek").getResultList();
		
		if(aramaYazisi=="")
		{
			kalanKitaplarListe.addAll(liste2);
		}
		else
		{
			for(int i=0;i<liste2.size();i++)
			{
				if(String.valueOf(liste2.get(i).getBarkodNo()).startsWith(aramaYazisi))
				{
					kalanKitaplarListe.add(liste2.get(i));
				}
				else if(String.valueOf(liste2.get(i).getKitapId()).startsWith(aramaYazisi))
				{
					kalanKitaplarListe.add(liste2.get(i));
				}
				else if(liste2.get(i).getKitapAdi().toUpperCase().startsWith(aramaYazisi.toUpperCase()))
				{
					kalanKitaplarListe.add(liste2.get(i));
				}
			}
			
			
		}
		
	}
	
	
	
/*	public void oduncTeslimArama(AjaxBehaviorEvent ajaxBehaviorEvent)
	{
		List<Oduncverilenler>liste3;
		oduncVerilenlerTcList.clear();
		liste3=veritabani.entityManager.createNamedQuery("OduncverilenlerCek").getResultList();
		
		if(aramaYazisi=="")
		{
			oduncVerilenlerTcList.addAll(liste3);
		}
		else
		{
			for(int i=0;i<liste2.size();i++)
			{
				if(String.valueOf(liste3.get(i).getBarkodAdi()).startsWith(aramaYazisi))
				{
					oduncVerilenlerTcList.add(liste3.get(i));
				}
				else if(String.valueOf(liste3.get(i).getTcno()).startsWith(aramaYazisi))
				{
					oduncVerilenlerTcList.add(liste3.get(i));
				}
				
			}
			
			
		}
		
	}
	*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////////////////////////
	
	
	public List<Long> getOduncVerilenlerTcList() {
		oduncTeslim();
		return oduncVerilenlerTcList;
	}

	public void setOduncVerilenlerTcList(List<Long> oduncVerilenlerTcList) {
		this.oduncVerilenlerTcList = oduncVerilenlerTcList;
	}

	public List<Kalankitaplar> getKalanKitaplarListe() {
		
		return kalanKitaplarListe;
	}

	public void setKalanKitaplarListe(List<Kalankitaplar> kalanKitaplarListe) {
		this.kalanKitaplarListe = kalanKitaplarListe;
	}

	public HtmlOutputText getHtmlOutputText() {
		return htmlOutputText;
	}


	public void setHtmlOutputText(HtmlOutputText htmlOutputText) {
		this.htmlOutputText = htmlOutputText;
	}


	public Kalankitaplar getKitapKarsilama() {
		return kitapKarsilama;
	}

	public void setKitapKarsilama(Kalankitaplar kitapKarsilama) {
		this.kitapKarsilama = kitapKarsilama;

	}

	public  boolean isGorunurluk() {
		return gorunurluk;
	}

	public  void setGorunurluk(boolean gorunurluk) {
		this.gorunurluk = gorunurluk;
	}


	public Kullanicibean getKullanicibean2() {
		return kullanicibean2;
	}
	public void setKullanicibean2(Kullanicibean kullanicibean2) {
		this.kullanicibean2 = kullanicibean2;
	}
	public Kitapbean getKitapbean2() {
		return kitapbean2;
	}
	public void setKitapbean2(Kitapbean kitapbean2) {
		this.kitapbean2 = kitapbean2;
	}

	public HtmlCommandButton getCommandButton() {
		return commandButton;
	}

	public void setCommandButton(HtmlCommandButton commandButton) {
		this.commandButton = commandButton;
	}
	public List<String> getOduncAlinanlarisim() {
		return oduncAlinanlarisim;
	}

	public void setOduncAlinanlarisim(List<String> oduncAlinanlarisim) {
		this.oduncAlinanlarisim = oduncAlinanlarisim;
	}

	public List<Integer> getOduncAlinanlarBarkodNo() {
		return oduncAlinanlarBarkodNo;
	}

	public void setOduncAlinanlarBarkodNo(List<Integer> oduncAlinanlarBarkodNo) {
		this.oduncAlinanlarBarkodNo = oduncAlinanlarBarkodNo;
	}
	
	public List<Date> getOduncAlinanlarTarih() {
		return oduncAlinanlarTarih;
	}

	public void setOduncAlinanlarTarih(List<Date> oduncAlinanlarTarih) {
		this.oduncAlinanlarTarih = oduncAlinanlarTarih;
	}

	public String getAramaYazisi() {
		return aramaYazisi;
	}

	public void setAramaYazisi(String aramaYazisi) {
		this.aramaYazisi = aramaYazisi;
	}

	
}
