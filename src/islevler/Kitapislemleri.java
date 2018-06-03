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
public class Kitapislemleri 														// Kitap �le Alakal� ��lemleri Ger�ekle�tirdim(kitapKay�t,Kitap�ek,Ayn�KitapVarm�,KitapSilme,K�t�phaneBo�Mu)
{
	Kitapbean kitapbean1=new Kitapbean();											//KitapBean i�in
	Kitapbilgileri kitapbilgileri=new Kitapbilgileri();								//KitapBilgileri Veritaban�
	Veritabani veritabani=new Veritabani();											// Veritaban� Ba�lant�s� ��in
	List<Kitapbilgileri>kitaplarList=new ArrayList<Kitapbilgileri>();				// Genel Kitaplar�n Listesi
	static List<Kitaplar>tumKitaplar=new ArrayList<Kitaplar>();						// Ayr�nt�l� Kitaplar�n listesi
	Kitaplar kitaplar=new Kitaplar();												// Kitaplar Veritaban� 
	int a;																			// Global De�i�ken Olarak Kulland�m
	Kalankitaplar kalankitaplar=new Kalankitaplar();								// Kalankitaplar Veritaban�
	Oduncverilenler oduncverilenler=new Oduncverilenler();							// �d�n�Verilenler Veritaban�
	private String aramaYazisi="";
	

	FacesContext context=FacesContext.getCurrentInstance();
	Date date=new Date();
	
	public void kitapKaydet(ActionEvent actionEvent) 		// ActionListener �le Kitaplar� Kaydediyor
	{
		if(kitapbean1.getStokSayisi()<=0)
		{
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"HATA","L�tfen Ge�erli Stok Say�s� Giriniz!"));
		}
		else 
		{
		
			if(ayniKitapVarMi()==0)					// aynikitapvarmi methoduna gidip kitaplar� tar�yor ve ona g�re return d�nd�r�yor e�er return 0 ise �yle bir kitap yok
			{
				
				if(kitapbean1.getTeminTarihi().after(date))
				{
			        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"HATA",  "L�tfen Tekrar Tarih Se�iniz!!" ) );

				}
				else 
				{
				
		
		kitapbilgileri.setBarkodNo(kitapbean1.getBarkodNo());			// KitapBilgileri Veritaban�na  Kitapla �lgili Veiler Giriliyor
		kitapbilgileri.setKitapAdi(kitapbean1.getKitapAdi());
		kitapbilgileri.setKitapTuru(kitapbean1.getKitapTuru());
		kitapbilgileri.setStokSayisi(kitapbean1.getStokSayisi());
		kitapbilgileri.setTeminTarihi(kitapbean1.getTeminTarihi());
		kitapbilgileri.setYayinEvi(kitapbean1.getYayinEvi());
		kitapbilgileri.setYazarAdi(kitapbean1.getYazarAdi());
		veritabani.transaction.begin();
				veritabani.entityManager.persist(kitapbilgileri);		// Transaction ��lemi Yap�l�yor
		veritabani.transaction.commit();
        context.addMessage(null, new FacesMessage("BA�ARILI",  "Kitap Ba�ar�l� Bir �ekilde Kaydedildi..." ) );
        
		
		kitapCek();														// Kitaplar kitaplarListesine kaydediliyor
		for(Kitapbilgileri kitapbilgileri2:kitaplarList)				
		{
			if(kitapbilgileri2.getKitapAdi().equals(kitapbean1.getKitapAdi()))		// Kitapbilgileri Veritaban�na Kaydedilen Kitab�n kitapId'si al�n�yor ve a de�i�kenine atan�yor
				a=kitapbilgileri2.getKitapId();
		}
		for(int i=1;i<=kitapbean1.getStokSayisi();i++)			// Girilen Stok Say�s� kadar kitaplar veritaban�na girilen barkodNo dan ba�lamak �zere kitaplar teker teker Kitaplar Veritaban�na Kaydediliyor.
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
				veritabani.entityManager.persist(kitaplar);			//		// Kitaplar Veritab�na Kay�t ��lemi Ger�ekle�iyor
				veritabani.entityManager.persist(kalankitaplar);	//		// Yeni Kitaplar� kalanKitaplar Veritaban�na ekliyor
			veritabani.transaction.commit();						//
		}
		}
		}
			else 
		        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"HATA",  "B�yle Bir Kitap Mevcut!!" ) );
			
		
		}			
	}
	

	@PostConstruct
	public void kitapCek()
	{
		kitaplarList.clear();
		kitaplarList=veritabani.entityManager.createNamedQuery("KitapbilgileriCek").getResultList();		// Ya�am d�ng�s� ba�lad���nda bu methot �al���yor
		tumKitaplariCek(); 
	}
	
	public void tumKitaplariCek()
	{
		tumKitaplar.clear();																				// kitaplar veritaban�ndan verileri �ekiyor ve sayfada g�steriyor
	//	tumKitaplar=veritabani.entityManager.createNamedQuery("TumKitaplarCek").getResultList();
		tumKitaplar=veritabani.entityManager.createNamedQuery("AktifTumKitaplarCek").getResultList();
	}
	
	
	public int ayniKitapVarMi()								// B�t�n kitaplar� �ekiyor ve ayn� kitab�n olup olmad���na bak�yor ve ona g�re return d�nd�r�yor ayn� kitap varsa 1 de�ilse 0 d�nd�r�yor
	{
		tumKitaplariCek();
		kitapCek();
		for(Kitapbilgileri kitapbilgileri:kitaplarList)							// kitaplar veritab�n�ndaki verileri �ekiyor ve kontrol ediyor
		{
			
			if(
			(kitapbilgileri.getKitapAdi().equals(kitapbean1.getKitapAdi()) && 
			kitapbilgileri.getKitapTuru().equals(kitapbean1.getKitapTuru())) || 
			kitapbilgileri.getBarkodNo()==kitapbean1.getBarkodNo()||
			(kitapbilgileri.getKitapAdi().equals(kitapbean1.getKitapAdi())&&(kitapbilgileri.getYazarAdi().equals(kitapbean1.getYazarAdi())))
			)
			{
				//ayn� kitap var 
				System.out.println("B�yle Bir Kitap Bulunmaktad�r!!!");
				return 1;
			}
		}
		for(Kitaplar kitaplar:tumKitaplar)
		{
			
				for (int i = 1; i <=kitapbean1.getStokSayisi(); i++) 
				{
					if(kitaplar.getBarkodNo()==kitapbean1.getBarkodNo()+i)
					{
						System.out.println("B�yle Bir Kitap Bulunmaktad�r!!!!");
						return 1;
					}
				
				}
			
		}
		return 0;
	}
	
	public void kitapSil(Kitaplar kitaplar)							// T�m kitaplar�n g�r�nt�lendi�i yerde istenilen kitab� siliyor
	{
		kalankitaplar=veritabani.entityManager.find(Kalankitaplar.class, kitaplar.getBarkodNo());//
		if(veritabani.entityManager.find(Oduncverilenler.class, kitaplar.getBarkodNo())!=null) // E�er silinmek istenilen kitap �d�n� olarak verildiyse kitap silinmiyor
		 
		{
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"B�LG�","Bu Kitap �u Anda Kullan�c�da L�tfen Daha Sonra Tekrar Deneyin!"));
			System.out.println("Bu Kitap �d�n�te");			 // Hata Modal ��kmas� Laz�m Bu Kitap �d�n�te Diye

		}
		
		else				
		{
			//
			veritabani.transaction.begin();
			//veritabani.entityManager.remove(kitaplar);		// Silinen kitap kitaplar ve kalankitaplar veritaban�ndan siliniyor
				kitaplar.setAktifMi(false);
				veritabani.entityManager.remove(kalankitaplar);//
		veritabani.transaction.commit();
		
		
		
		for(Kitapbilgileri kitapbilgileri:kitaplarList)
		{
			if(kitaplar.getKitapId()==kitapbilgileri.getKitapId())	
			{	
				if(kitapbilgileri.getStokSayisi()>0)					// Se�ilen kitab�n stok say�s� 0 dan b�y�kse 
				{
				veritabani.transaction.begin();
				kitapbilgileri.setStokSayisi(kitapbilgileri.getStokSayisi()-1);		// Silinen kitap genelkitap�n stok say�s�ndan d���yor
				veritabani.transaction.commit();
					if(kitapbilgileri.getStokSayisi()==0)						// E�er silinen kitaptan sonra stok say�s� 0 oluyorsa kitapbilgileri veritaban�ndan kitap siliniyor
					{
						veritabani.transaction.begin();
							veritabani.entityManager.remove(kitapbilgileri);	// Kitapbilgileri veritaban�ndan kitap siliniyor
						veritabani.transaction.commit();
					}
				}
				else if(kitapbilgileri.getStokSayisi()==0)					//E�er ba�ta kitap say�s� silinenle beraber 0 ise kitapbilgileri veritaban�ndan kitap siliniyor
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

	
	public String kitapVarMi()							// E�er K�t�phanede Hi� Kitap Yoksa Ekrana K�t�phanede Kitap Yok Yazd�r�yor
	{
		kitapCek();
		if(kitaplarList.isEmpty())
		{
			return"K�t�phanede Kitap Yok!!";
		}
		return "";
	}
	
	public String kitapYazar(Kitaplar kitaplar)						//Girilen kitaba g�re kitap yazar�n�n ismi retrun olarak d�nd�r�l�yor
	{
		Kitapbilgileri kitapbilgileri=new Kitapbilgileri();
		kitapbilgileri=veritabani.entityManager.find(Kitapbilgileri.class, kitaplar.getKitapId());
		
		
		
		return kitapbilgileri.getYazarAdi();
	}
	
	
	
	
	
	
	
	

	
	public void kitapAraTumKitaplar(AjaxBehaviorEvent ajaxBehaviorEvent)	// Girilen Veriye G�re Kullan�c� Ar�yor 
	{
		tumKitaplar.clear();
		List<Kitaplar>liste2;
		liste2=veritabani.entityManager.createNamedQuery("AktifTumKitaplarCek").getResultList();
	
	 	if(aramaYazisi.equals(""))											// E�er null giriliyorsa b�t�n kullan�c�lar g�r�nt�leniyor
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

	
	public void kitapAraKitapListele(AjaxBehaviorEvent ajaxBehaviorEvent)													// Girilen Veriye G�re Kullan�c� Ar�yor 
	{
		kitaplarList.clear();
		List<Kitapbilgileri>liste2;
		liste2=veritabani.entityManager.createNamedQuery("KitapbilgileriCek").getResultList();
	
		
		
	 	if(aramaYazisi.equals(""))											// E�er null giriliyorsa b�t�n kullan�c�lar g�r�nt�leniyor
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
