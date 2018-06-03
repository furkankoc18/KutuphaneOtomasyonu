package islevler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.Part;

import org.primefaces.component.growl.Growl;

import beanlar.Kullanicibean;
import model.Gecmisalinanlar;
import model.Kitaplar;
import model.Kullanicibilgileri;
import model.Veritabani;



@ManagedBean
@RequestScoped
public class KullaniciGorevleri 											// Kullanýcýlarla Alakalý Ýþlemlerin Yapýldýðý Classýmýz
{
		Kullanicibilgileri kullanicibilgileri=new Kullanicibilgileri();   	// kullanýcýbilgileri veritabaný
		Kullanicibean kullanicibean1=new Kullanicibean();					// Kullanýcý Ýþlemlerinde Kullanýlan Deðiþkenlerin Kullanýldýðý Class
		Veritabani veritabani=new Veritabani();								// Veritabaný Ýþlemlerinin Kullanýldýðý Class

		Date tarih=new Date();												//Server Tarihini Kullanmak Ýçin
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");			
	    String strDate = sdf.format(tarih);
	    
	    private static String aramaYazisi="";									// Kullanýcý Aramanýn Kullanýldýðý Deðiþken
	    List<Kullanicibilgileri>liste=new ArrayList<Kullanicibilgileri>();		// Kullanýcýbilgilerinin Tutulduðu Liste
		
		 FacesContext context = FacesContext.getCurrentInstance();
		private Object Gecmisalinanlar;
	    
		
		@PostConstruct
		public void kullanicilariCek()											// Kullanýcýbilgileri Veritabanýndan Veriler Çekiliyor Ve Listeye Ekliyor
		{
			liste.clear();
			liste=veritabani.entityManager.createNamedQuery("KullanicibilgileriCek").getResultList();
			for (Kullanicibilgileri kullanicibilgileri:liste)
			{
			//	kullanicibilgileri.setUyelik_Tarihi();
			}
			
		}
		

		public void kullaniciKayit()										// Kullanýcý Kayýt Ediyor
		{
			FacesContext context2=FacesContext.getCurrentInstance();
			if(kullanicibean1.getParola().equals(kullanicibean1.getParolaDogrulama()))	// Eðer Girilen Parolalar Doðru Ýse 
			{
				if(ayniKullaniciVarMi()==0)									// Girilen Bilgilerde Kullanýcý Yok Ýse return ile 0 dönüyor
				{
				
				
				kullanicibilgileri.setAd(kullanicibean1.getIsim());
				kullanicibilgileri.setSoyad(kullanicibean1.getSoyisim());
				kullanicibilgileri.setCinsiyet(kullanicibean1.getCinsiyet());
				kullanicibilgileri.setSifre(kullanicibean1.getParola());
				kullanicibilgileri.setTCKimlikNo(kullanicibean1.getTc());
				kullanicibilgileri.setMail(kullanicibean1.getEposta());
				kullanicibilgileri.setUyelik_Tarihi(tarih);
				veritabani.transaction.begin();
					veritabani.entityManager.merge(kullanicibilgileri);	 // Kullanýcý Kayda Giriyor Ve Veritabanýna Kaydediyor
				veritabani.transaction.commit();
				//Kullanýcý Baþarýlý Þekilde Kaydedildi 
		         
			        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"BAÞARILI",  "Kullanýcý Baþarýlý Bir Þekilde Kaydedildi.." ) );
				}
				else 
				{
			        context2.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"HATA",  "Böyle Bir Kullanýcý Var!!" ) );
			        
				}

					
			}
			else 
			{	
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"HATA",  "Parola Doðrulanmadý!!" ) );

			}
	}
		
		public void kullaniciDuzenleGorunurlukAc(Kullanicibilgileri kullanicibilgileri)  // Seçilen Kullanýcýnýn Görünürlüðü Açýlýyor Ve Veritabanýný Güncelliyor 
		{
			veritabani.transaction.begin();
				kullanicibilgileri.setGorunurluk(true);
			veritabani.transaction.commit();
		
			
		}
		
		public void kullaniciDuzenle(Kullanicibilgileri kullanicibilgileri)				// Seçilen Kullanýcýnýn Görünürlüðü Kapanýr Ve Girilen veriler güncelleniyor
		{
			
			
			String deger=kullanicibilgileri.getSifre();
			veritabani.transaction.begin();
				kullanicibilgileri.setGorunurluk(false);
				kullanicibilgileri.setSifre(deger);
			veritabani.transaction.commit();
			
		}
		
		public void kullaniciSil(Kullanicibilgileri kullanicibilgileri)				// KullanýcýBilgileri Veritabanýndan Seçilen Kullanýcý Siliniyor
		{
			FacesContext context2=FacesContext.getCurrentInstance();
			if(veritabani.entityManager.createQuery("Select k.barkodAdi from Oduncverilenler k where k.tcno="+kullanicibilgileri.getTCKimlikNo()).getResultList().isEmpty())
			{
			veritabani.transaction.begin();
				veritabani.entityManager.remove(kullanicibilgileri);	
			veritabani.transaction.commit();
			kullanicilariCek(); //Kullanýcý Silindiðinde Datatablenin güncellenmesi için
			
			//// buradayým
			if(veritabani.entityManager.createQuery("Select k from Gecmisalinanlar k where k.tc="+kullanicibilgileri.getTCKimlikNo()).getResultList().isEmpty())
			{
				
			}
			else 
			{
			Gecmisalinanlar gecmisalinanlar=new Gecmisalinanlar();
			veritabani.transaction.begin();
				veritabani.entityManager.createQuery("Delete from Gecmisalinanlar k where k.tc="+kullanicibilgileri.getTCKimlikNo()).executeUpdate();
			veritabani.transaction.commit();
				context2.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Baþarýlý","Kullanýcý Baþarýlý Bir Þekilde Silindi..."));
			}
			////
			
			}
			else 
			{
				
				System.out.println("Bu Kullanýcý Silinemez Çünkü Üzerinde Kitap Kayýtlý");
				context2.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Hata","Bu Kullanýcý Silinemez,Üzerinde Kitap Var!!"));
			}
			}
		
		public void kullaniciAra(AjaxBehaviorEvent ajaxBehaviorEvent)													// Girilen Veriye Göre Kullanýcý Arýyor 
		{
			liste.clear();
			List<Kullanicibilgileri>liste2;
			liste2=veritabani.entityManager.createNamedQuery("KullanicibilgileriCek").getResultList();

			if(aramaYazisi.equals(""))											// Eðer null giriliyorsa bütün kullanýcýlar görüntüleniyor
			{
				liste.addAll(liste2);
			}
			else if(!aramaYazisi.equals("")) 
			{

				for(int i=0;i<liste2.size();i++)
				{
				if(liste2.get(i).getAd().toLowerCase().startsWith(aramaYazisi.toLowerCase()))
				{
					liste.add(liste2.get(i));


				}
				else if(liste2.get(i).getSoyad().toLowerCase().startsWith(aramaYazisi.toLowerCase()))
				{
					liste.add(liste2.get(i));


				}
				else if((liste2.get(i).getAd().toLowerCase()+" "+liste2.get(i).getSoyad().toLowerCase()).startsWith(aramaYazisi.toLowerCase()))
				{
					liste.add(liste2.get(i));


				}
				else if(Long.toString(liste2.get(i).getTCKimlikNo()).startsWith(aramaYazisi))
				{
					liste.add(liste2.get(i));


				}
				
				else 
				{
					
				}
				
				}
			}
			
				
		}
		
		

		public int ayniKullaniciVarMi()										// Girilen Verilere Göre Ayný Kullanýcý Varmý Ona Bakýyor Ve Eðer Ayný Kullanýcý Varsa return olarak 1 dönüyor yok ise 0 dönüyor
		{
			kullanicilariCek();
			for(Kullanicibilgileri kullanicibilgileri:liste)
			{
				if((kullanicibilgileri.getAd().equals(kullanicibean1.getIsim()) && kullanicibilgileri.getSoyad().equals(kullanicibean1.getSoyisim()) ||
						(kullanicibilgileri.getMail().equals(kullanicibean1.getEposta())) ||
						(kullanicibilgileri.getTCKimlikNo()==kullanicibean1.getTc()))
				 )
						{
				
						System.out.println("Ayný Kullanýcý Mevcuttur!!!");
						return 1;
					
						}
						
			
			}
			
			return 0;
		}
		
		List<Gecmisalinanlar>gecmisList=new ArrayList<Gecmisalinanlar>();
		


		public void kullaniciKitaplari(long tc)
		{
			gecmisList.clear();
			kullanicibean1.setTc(tc);
			
			gecmisList=veritabani.entityManager.createQuery("Select k from Gecmisalinanlar k where k.tc="+tc).getResultList();
			
			
		}
		
		
		
		public String kitapIsmiCek(int barkod)
		{
				if((veritabani.entityManager.createQuery("Select k from Kitaplar k where k.barkodNo="+barkod).getResultList().isEmpty()))
						{
					return "";
						}
				else 
				{
			String isim=veritabani.entityManager.createQuery("Select k.kitapAdi from Kitaplar k where k.barkodNo="+barkod).getSingleResult().toString();
			return isim;
				}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////	GET VE SET METOTLARI
		/////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * GET VE SET METHODLARI
		 */
	
	public List<Gecmisalinanlar> getGecmisList() {
			return gecmisList;
		}


		public void setGecmisList(List<Gecmisalinanlar> gecmisList) {
			this.gecmisList = gecmisList;
		}


	public Kullanicibilgileri getKullanicibilgileri() {
		return kullanicibilgileri;
	}
	public void setKullanicibilgileri(Kullanicibilgileri kullanicibilgileri) {
		this.kullanicibilgileri = kullanicibilgileri;
	}
	public Kullanicibean getKullanicibean1() {
		return kullanicibean1;
	}
	public void setKullanicibean1(Kullanicibean kullanicibean1) {
		this.kullanicibean1 = kullanicibean1;
	}
	public List<Kullanicibilgileri> getListe() {
		return liste;
	}

	public void setListe(List<Kullanicibilgileri> liste) {
		this.liste = liste;
	}

	public String getAramaYazisi() {
		return aramaYazisi;
	}


	public void setAramaYazisi(String aramaYazisi) {
		this.aramaYazisi = aramaYazisi;
	}

	
	
}
