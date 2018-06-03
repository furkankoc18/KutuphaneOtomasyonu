package islevler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.component.growl.Growl;

import beanlar.Kitapbean;
import model.Kalankitaplar;
import model.Kitapbilgileri;
import model.Kitaplar;
import model.Kullanicibilgileri;
import model.Oduncverilenler;
import model.Veritabani;

@ManagedBean													
@RequestScoped
public class Kitapislemleri 														// Kitap Ýle Alakalý Ýþlemleri Gerçekleþtirdim(kitapKayýt,KitapÇek,AynýKitapVarmý,KitapSilme,KütüphaneBoþMu)
{
	Kitapbean kitapbean1=new Kitapbean();											//KitapBean için
	Kitapbilgileri kitapbilgileri=new Kitapbilgileri();								//KitapBilgileri Veritabaný
	Veritabani veritabani=new Veritabani();											// Veritabaný Baðlantýsý Ýçin
	List<Kitapbilgileri>kitaplarList=new ArrayList<Kitapbilgileri>();				// Genel Kitaplarýn Listesi
	static List<Kitaplar>tumKitaplar=new ArrayList<Kitaplar>();						// Ayrýntýlý Kitaplarýn listesi
	Kitaplar kitaplar=new Kitaplar();												// Kitaplar Veritabaný 
	int a;																			// Global Deðiþken Olarak Kullandým
	Kalankitaplar kalankitaplar=new Kalankitaplar();								// Kalankitaplar Veritabaný
	Oduncverilenler oduncverilenler=new Oduncverilenler();							// ÖdünçVerilenler Veritabaný
	private String aramaYazisi="";
	

	FacesContext context=FacesContext.getCurrentInstance();
	Date date=new Date();
	
	public void kitapKaydet(ActionEvent actionEvent) 		// ActionListener Ýle Kitaplarý Kaydediyor
	{
		if(kitapbean1.getStokSayisi()<=0)
		{
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"HATA","Lütfen Geçerli Stok Sayýsý Giriniz!"));
		}
		else 
		{
		
			if(ayniKitapVarMi()==0)					// aynikitapvarmi methoduna gidip kitaplarý tarýyor ve ona göre return döndürüyor eðer return 0 ise öyle bir kitap yok
			{
				
				if(kitapbean1.getTeminTarihi().after(date))
				{
			        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"HATA",  "Lütfen Tekrar Tarih Seçiniz!!" ) );

				}
				else 
				{
				
		
		kitapbilgileri.setBarkodNo(kitapbean1.getBarkodNo());			// KitapBilgileri Veritabanýna  Kitapla Ýlgili Veiler Giriliyor
		kitapbilgileri.setKitapAdi(kitapbean1.getKitapAdi());
		kitapbilgileri.setKitapTuru(kitapbean1.getKitapTuru());
		kitapbilgileri.setStokSayisi(kitapbean1.getStokSayisi());
		kitapbilgileri.setTeminTarihi(kitapbean1.getTeminTarihi());
		kitapbilgileri.setYayinEvi(kitapbean1.getYayinEvi());
		kitapbilgileri.setYazarAdi(kitapbean1.getYazarAdi());
		veritabani.transaction.begin();
				veritabani.entityManager.persist(kitapbilgileri);		// Transaction Ýþlemi Yapýlýyor
		veritabani.transaction.commit();
        context.addMessage(null, new FacesMessage("BAÞARILI",  "Kitap Baþarýlý Bir Þekilde Kaydedildi..." ) );
        
		
		kitapCek();														// Kitaplar kitaplarListesine kaydediliyor
		for(Kitapbilgileri kitapbilgileri2:kitaplarList)				
		{
			if(kitapbilgileri2.getKitapAdi().equals(kitapbean1.getKitapAdi()))		// Kitapbilgileri Veritabanýna Kaydedilen Kitabýn kitapId'si alýnýyor ve a deðiþkenine atanýyor
				a=kitapbilgileri2.getKitapId();
		}
		for(int i=1;i<=kitapbean1.getStokSayisi();i++)			// Girilen Stok Sayýsý kadar kitaplar veritabanýna girilen barkodNo dan baþlamak üzere kitaplar teker teker Kitaplar Veritabanýna Kaydediliyor.
		{	
			Kitaplar  kitaplar =new Kitaplar();
			Kalankitaplar kalankitaplar=new Kalankitaplar();
			kitaplar.setBarkodNo(kitapbean1.getBarkodNo()+i);
			kitaplar.setKitapAdi(kitapbean1.getKitapAdi());
			kitaplar.setKitapId(a);
			kitaplar.setAktifMi(true);
			kalankitaplar.setBarkodNo(kitapbean1.getBarkodNo()+i);	//
			kalankitaplar.setKitapAdi(kitapbean1.getKitapAdi());	//
			kalankitaplar.setKitapId(a);							//
			veritabani.transaction.begin();							//
				veritabani.entityManager.persist(kitaplar);			//		// Kitaplar Veritabýna Kayýt Ýþlemi Gerçekleþiyor
				veritabani.entityManager.persist(kalankitaplar);	//		// Yeni Kitaplarý kalanKitaplar Veritabanýna ekliyor
			veritabani.transaction.commit();						//
		}
		}
		}
			else 
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"HATA",  "Böyle Bir Kitap Mevcut!!" ) );
			
		
		}			
	}
	

	@PostConstruct
	public void kitapCek()
	{
		kitaplarList.clear();
		kitaplarList=veritabani.entityManager.createNamedQuery("KitapbilgileriCek").getResultList();		// Yaþam döngüsü baþladýðýnda bu methot çalýþýyor
		tumKitaplariCek(); 
	}
	
	public void tumKitaplariCek()
	{
		tumKitaplar.clear();																				// kitaplar veritabanýndan verileri çekiyor ve sayfada gösteriyor
	//	tumKitaplar=veritabani.entityManager.createNamedQuery("TumKitaplarCek").getResultList();
		tumKitaplar=veritabani.entityManager.createNamedQuery("AktifTumKitaplarCek").getResultList();
	}
	
	
	public int ayniKitapVarMi()								// Bütün kitaplarý çekiyor ve ayný kitabýn olup olmadýðýna bakýyor ve ona göre return döndürüyor ayný kitap varsa 1 deðilse 0 döndürüyor
	{
		tumKitaplariCek();
		kitapCek();
		for(Kitapbilgileri kitapbilgileri:kitaplarList)							// kitaplar veritabýnýndaki verileri çekiyor ve kontrol ediyor
		{
			
			if(
			(kitapbilgileri.getKitapAdi().equals(kitapbean1.getKitapAdi()) && 
			kitapbilgileri.getKitapTuru().equals(kitapbean1.getKitapTuru())) || 
			kitapbilgileri.getBarkodNo()==kitapbean1.getBarkodNo()||
			(kitapbilgileri.getKitapAdi().equals(kitapbean1.getKitapAdi())&&(kitapbilgileri.getYazarAdi().equals(kitapbean1.getYazarAdi())))
			)
			{
				//ayný kitap var 
				System.out.println("Böyle Bir Kitap Bulunmaktadýr!!!");
				return 1;
			}
		}
		for(Kitaplar kitaplar:tumKitaplar)
		{
			
				for (int i = 1; i <=kitapbean1.getStokSayisi(); i++) 
				{
					if(kitaplar.getBarkodNo()==kitapbean1.getBarkodNo()+i)
					{
						System.out.println("Böyle Bir Kitap Bulunmaktadýr!!!!");
						return 1;
					}
				
				}
			
		}
		return 0;
	}
	
	public void kitapSil(Kitaplar kitaplar)							// Tüm kitaplarýn görüntülendiði yerde istenilen kitabý siliyor
	{
		kalankitaplar=veritabani.entityManager.find(Kalankitaplar.class, kitaplar.getBarkodNo());//
		if(veritabani.entityManager.find(Oduncverilenler.class, kitaplar.getBarkodNo())!=null) // Eðer silinmek istenilen kitap ödünç olarak verildiyse kitap silinmiyor
		 
		{
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"BÝLGÝ","Bu Kitap Þu Anda Kullanýcýda Lütfen Daha Sonra Tekrar Deneyin!"));
			System.out.println("Bu Kitap Ödünçte");			 // Hata Modal Çýkmasý Lazým Bu Kitap Ödünçte Diye

		}
		
		else				
		{
			//
			veritabani.transaction.begin();
			//veritabani.entityManager.remove(kitaplar);		// Silinen kitap kitaplar ve kalankitaplar veritabanýndan siliniyor
				kitaplar.setAktifMi(false);
				veritabani.entityManager.remove(kalankitaplar);//
		veritabani.transaction.commit();
		
		
		
		for(Kitapbilgileri kitapbilgileri:kitaplarList)
		{
			if(kitaplar.getKitapId()==kitapbilgileri.getKitapId())	
			{	
				if(kitapbilgileri.getStokSayisi()>0)					// Seçilen kitabýn stok sayýsý 0 dan büyükse 
				{
				veritabani.transaction.begin();
				kitapbilgileri.setStokSayisi(kitapbilgileri.getStokSayisi()-1);		// Silinen kitap genelkitapýn stok sayýsýndan düþüyor
				veritabani.transaction.commit();
					if(kitapbilgileri.getStokSayisi()==0)						// Eðer silinen kitaptan sonra stok sayýsý 0 oluyorsa kitapbilgileri veritabanýndan kitap siliniyor
					{
						veritabani.transaction.begin();
							veritabani.entityManager.remove(kitapbilgileri);	// Kitapbilgileri veritabanýndan kitap siliniyor
						veritabani.transaction.commit();
					}
				}
				else if(kitapbilgileri.getStokSayisi()==0)					//Eðer baþta kitap sayýsý silinenle beraber 0 ise kitapbilgileri veritabanýndan kitap siliniyor
				{
					veritabani.transaction.begin();
						veritabani.entityManager.remove(kitapbilgileri);
					veritabani.transaction.commit();
				}
			}
			
		}	
		}
		
		tumKitaplariCek();
	
	}

	
	public String kitapVarMi()							// Eðer Kütüphanede Hiç Kitap Yoksa Ekrana Kütüphanede Kitap Yok Yazdýrýyor
	{
		kitapCek();
		if(kitaplarList.isEmpty())
		{
			return"Kütüphanede Kitap Yok!!";
		}
		return "";
	}
	
	public String kitapYazar(Kitaplar kitaplar)						//Girilen kitaba göre kitap yazarýnýn ismi retrun olarak döndürülüyor
	{
		Kitapbilgileri kitapbilgileri=new Kitapbilgileri();
		kitapbilgileri=veritabani.entityManager.find(Kitapbilgileri.class, kitaplar.getKitapId());
		
		
		
		return kitapbilgileri.getYazarAdi();
	}
	
	
	
	
	
	
	
	

	
	public void kitapAraTumKitaplar(AjaxBehaviorEvent ajaxBehaviorEvent)	// Girilen Veriye Göre Kullanýcý Arýyor 
	{
		tumKitaplar.clear();
		List<Kitaplar>liste2;
		liste2=veritabani.entityManager.createNamedQuery("AktifTumKitaplarCek").getResultList();
	
	 	if(aramaYazisi.equals(""))											// Eðer null giriliyorsa bütün kullanýcýlar görüntüleniyor
		{

			tumKitaplar.addAll(liste2);
		}

	 	else if(!aramaYazisi.equals("")) 
		{


			for(int i=0;i<liste2.size();i++)
			{
				if(liste2.get(i).getKitapAdi().toLowerCase().startsWith(aramaYazisi.toLowerCase()))
				{
				tumKitaplar.add(liste2.get(i));
				}
						
				else if (String.valueOf(liste2.get(i).getBarkodNo()).startsWith(aramaYazisi)) 
				{
					tumKitaplar.add(liste2.get(i));
				}
				else if(String.valueOf(liste2.get(i).getKitapId()).startsWith(aramaYazisi))
				{
					tumKitaplar.add(liste2.get(i));
				}
				
			}
		}
		
		
			
	}

	
	public void kitapAraKitapListele(AjaxBehaviorEvent ajaxBehaviorEvent)													// Girilen Veriye Göre Kullanýcý Arýyor 
	{
		kitaplarList.clear();
		List<Kitapbilgileri>liste2;
		liste2=veritabani.entityManager.createNamedQuery("KitapbilgileriCek").getResultList();
	
		
		
	 	if(aramaYazisi.equals(""))											// Eðer null giriliyorsa bütün kullanýcýlar görüntüleniyor
		{

	 		kitaplarList.addAll(liste2);
		}

	 	else if(!aramaYazisi.equals("")) 
		{


			for(int i=0;i<liste2.size();i++)
			{
				if(liste2.get(i).getKitapAdi().toLowerCase().startsWith(aramaYazisi.toLowerCase()))
				{
					kitaplarList.add(liste2.get(i));
				}
						
				else if (String.valueOf(liste2.get(i).getKitapId()).startsWith(aramaYazisi)) 
				{
					kitaplarList.add(liste2.get(i));
					
				} 
				else if(liste2.get(i).getYazarAdi().toUpperCase().startsWith(aramaYazisi.toUpperCase()))
				{
					kitaplarList.add(liste2.get(i));
				}
				else if(liste2.get(i).getYayinEvi().toUpperCase().startsWith(aramaYazisi.toUpperCase()))
				{
					kitaplarList.add(liste2.get(i));
				}
				else if(String.valueOf(liste2.get(i).getBarkodNo()).startsWith(aramaYazisi))
				{
					kitaplarList.add(liste2.get(i));
				}
				else if(liste2.get(i).getKitapTuru().toUpperCase().startsWith(aramaYazisi.toUpperCase()))
				{
					kitaplarList.add(liste2.get(i));
				}
				
			}
		}
	
	}
	
	
	
	
	
	
	
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////
	///	
	///				POJOLAR-GETTER VE SETTER
	///
	//////////////////////////////////////////////////////////////////////////////////
	
	public List<Kitaplar> getTumKitaplar() {
		return tumKitaplar;
	}

	public void setTumKitaplar(List<Kitaplar> tumKitaplar) {
		this.tumKitaplar = tumKitaplar;
	}

	public Kitaplar getKitaplar() {
		return kitaplar;
	}

	public void setKitaplar(Kitaplar kitaplar) {
		this.kitaplar = kitaplar;
	}
	
	public List<Kitapbilgileri> getKitaplarList() {
		
		return kitaplarList;
	}

	public void setKitaplarList(List<Kitapbilgileri> kitaplarList) {
		this.kitaplarList = kitaplarList;
	}

	public Kitapbean getKitapbean1() {
		return kitapbean1;
	}

	public void setKitapbean1(Kitapbean kitapbean1) {
		this.kitapbean1 = kitapbean1;
	}

	
	public String getAramaYazisi() {
		return aramaYazisi;
	}

	public void setAramaYazisi(String aramaYazisi) {
		this.aramaYazisi = aramaYazisi;
	}
	
	
}
