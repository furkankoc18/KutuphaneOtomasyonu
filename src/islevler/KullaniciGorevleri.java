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
public class KullaniciGorevleri 											// Kullan�c�larla Alakal� ��lemlerin Yap�ld��� Class�m�z
{
		Kullanicibilgileri kullanicibilgileri=new Kullanicibilgileri();   	// kullan�c�bilgileri veritaban�
		Kullanicibean kullanicibean1=new Kullanicibean();					// Kullan�c� ��lemlerinde Kullan�lan De�i�kenlerin Kullan�ld��� Class
		Veritabani veritabani=new Veritabani();								// Veritaban� ��lemlerinin Kullan�ld��� Class

		Date tarih=new Date();												//Server Tarihini Kullanmak ��in
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd");			
	    String strDate = sdf.format(tarih);
	    
	    private static String aramaYazisi="";									// Kullan�c� Araman�n Kullan�ld��� De�i�ken
	    List<Kullanicibilgileri>liste=new ArrayList<Kullanicibilgileri>();		// Kullan�c�bilgilerinin Tutuldu�u Liste
		
		 FacesContext context = FacesContext.getCurrentInstance();
		private Object Gecmisalinanlar;
	    
		
		@PostConstruct
		public void kullanicilariCek()											// Kullan�c�bilgileri Veritaban�ndan Veriler �ekiliyor Ve Listeye Ekliyor
		{
			liste.clear();
			liste=veritabani.entityManager.createNamedQuery("KullanicibilgileriCek").getResultList();
			for (Kullanicibilgileri kullanicibilgileri:liste)
			{
			//	kullanicibilgileri.setUyelik_Tarihi();
			}
			
		}
		

		public void kullaniciKayit()										// Kullan�c� Kay�t Ediyor
		{
			FacesContext context2=FacesContext.getCurrentInstance();
			if(kullanicibean1.getParola().equals(kullanicibean1.getParolaDogrulama()))	// E�er Girilen Parolalar Do�ru �se 
			{
				if(ayniKullaniciVarMi()==0)									// Girilen Bilgilerde Kullan�c� Yok �se return ile 0 d�n�yor
				{
				
				
				kullanicibilgileri.setAd(kullanicibean1.getIsim());
				kullanicibilgileri.setSoyad(kullanicibean1.getSoyisim());
				kullanicibilgileri.setCinsiyet(kullanicibean1.getCinsiyet());
				kullanicibilgileri.setSifre(kullanicibean1.getParola());
				kullanicibilgileri.setTCKimlikNo(kullanicibean1.getTc());
				kullanicibilgileri.setMail(kullanicibean1.getEposta());
				kullanicibilgileri.setUyelik_Tarihi(tarih);
				veritabani.transaction.begin();
					veritabani.entityManager.merge(kullanicibilgileri);	 // Kullan�c� Kayda Giriyor Ve Veritaban�na Kaydediyor
				veritabani.transaction.commit();
				//Kullan�c� Ba�ar�l� �ekilde Kaydedildi 
		         
			        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"BA�ARILI",  "Kullan�c� Ba�ar�l� Bir �ekilde Kaydedildi.." ) );
				}
				else 
				{
			        context2.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"HATA",  "B�yle Bir Kullan�c� Var!!" ) );
			        
				}

					
			}
			else 
			{	
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"HATA",  "Parola Do�rulanmad�!!" ) );

			}
	}
		
		public void kullaniciDuzenleGorunurlukAc(Kullanicibilgileri kullanicibilgileri)  // Se�ilen Kullan�c�n�n G�r�n�rl��� A��l�yor Ve Veritaban�n� G�ncelliyor 
		{
			veritabani.transaction.begin();
				kullanicibilgileri.setGorunurluk(true);
			veritabani.transaction.commit();
		
			
		}
		
		public void kullaniciDuzenle(Kullanicibilgileri kullanicibilgileri)				// Se�ilen Kullan�c�n�n G�r�n�rl��� Kapan�r Ve Girilen veriler g�ncelleniyor
		{
			
			
			String deger=kullanicibilgileri.getSifre();
			veritabani.transaction.begin();
				kullanicibilgileri.setGorunurluk(false);
				kullanicibilgileri.setSifre(deger);
			veritabani.transaction.commit();
			
		}
		
		public void kullaniciSil(Kullanicibilgileri kullanicibilgileri)				// Kullan�c�Bilgileri Veritaban�ndan Se�ilen Kullan�c� Siliniyor
		{
			FacesContext context2=FacesContext.getCurrentInstance();
			if(veritabani.entityManager.createQuery("Select k.barkodAdi from Oduncverilenler k where k.tcno="+kullanicibilgileri.getTCKimlikNo()).getResultList().isEmpty())
			{
			veritabani.transaction.begin();
				veritabani.entityManager.remove(kullanicibilgileri);	
			veritabani.transaction.commit();
			kullanicilariCek(); //Kullan�c� Silindi�inde Datatablenin g�ncellenmesi i�in
			
			//// buraday�m
			if(veritabani.entityManager.createQuery("Select k from Gecmisalinanlar k where k.tc="+kullanicibilgileri.getTCKimlikNo()).getResultList().isEmpty())
			{
				
			}
			else 
			{
			Gecmisalinanlar gecmisalinanlar=new Gecmisalinanlar();
			veritabani.transaction.begin();
				veritabani.entityManager.createQuery("Delete from Gecmisalinanlar k where k.tc="+kullanicibilgileri.getTCKimlikNo()).executeUpdate();
			veritabani.transaction.commit();
				context2.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Ba�ar�l�","Kullan�c� Ba�ar�l� Bir �ekilde Silindi..."));
			}
			////
			
			}
			else 
			{
				
				System.out.println("Bu Kullan�c� Silinemez ��nk� �zerinde Kitap Kay�tl�");
				context2.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Hata","Bu Kullan�c� Silinemez,�zerinde Kitap Var!!"));
			}
			}
		
		public void kullaniciAra(AjaxBehaviorEvent ajaxBehaviorEvent)													// Girilen Veriye G�re Kullan�c� Ar�yor 
		{
			liste.clear();
			List<Kullanicibilgileri>liste2;
			liste2=veritabani.entityManager.createNamedQuery("KullanicibilgileriCek").getResultList();

			if(aramaYazisi.equals(""))											// E�er null giriliyorsa b�t�n kullan�c�lar g�r�nt�leniyor
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
		
		

		public int ayniKullaniciVarMi()										// Girilen Verilere G�re Ayn� Kullan�c� Varm� Ona Bak�yor Ve E�er Ayn� Kullan�c� Varsa return olarak 1 d�n�yor yok ise 0 d�n�yor
		{
			kullanicilariCek();
			for(Kullanicibilgileri kullanicibilgileri:liste)
			{
				if((kullanicibilgileri.getAd().equals(kullanicibean1.getIsim()) && kullanicibilgileri.getSoyad().equals(kullanicibean1.getSoyisim()) ||
						(kullanicibilgileri.getMail().equals(kullanicibean1.getEposta())) ||
						(kullanicibilgileri.getTCKimlikNo()==kullanicibean1.getTc()))
				 )
						{
				
						System.out.println("Ayn� Kullan�c� Mevcuttur!!!");
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
