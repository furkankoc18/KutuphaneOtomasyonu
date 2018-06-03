package islevler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

import javassist.expr.NewArray;
import model.Gorevlibilgileri;
import model.Veritabani;

@ManagedBean
@RequestScoped
public class gorevlislemleri implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String parolaYeniden="";
	Veritabani veritabani=new Veritabani();
	Gorevlibilgileri gorevlibilgileri=new Gorevlibilgileri();
	
	private String gorevliGirisGorevliAdi="";
	private String gorevliGirisGorevliSifre="";
	
	private boolean sayfaGorunurluk=true;
	
	

		public boolean girdiMi=false;
	



	public String gorevliGiris()
	{
		
		List<Gorevlibilgileri>gorevlibilgileris=new ArrayList<Gorevlibilgileri>();
		gorevlibilgileris=veritabani.entityManager.createNamedQuery("GorevlibilgileriCek").getResultList();
		for(Gorevlibilgileri gorevlibilgileri:gorevlibilgileris)
		{
			if(gorevlibilgileri.getGorevliAdi().equals(gorevliGirisGorevliAdi) && gorevlibilgileri.getParola().equals(gorevliGirisGorevliSifre))
			{
				if(gorevlibilgileri.getGorevYetkisi().equals("Admin"))
				{
					sayfaGorunurluk=true;
				}
				else 
					sayfaGorunurluk=false;
				System.out.println("Kullanýcý Bulundu :"+gorevlibilgileri.getGorevliAdi());
				girdiMi=true;
				return "index.xhtml?faces-redirect=true";
			}
		}
		
		return "";
	}
	
	
	
	public void gorevliKaydet(AjaxBehaviorEvent ajaxBehaviorEvent)
	{
		System.out.println("Girdi");
		/*
		veritabani.transaction.begin();
			veritabani.entityManager.persist(gorevlibilgileri);
		veritabani.transaction.commit();
		*/
		
	//	FacesContext context=FacesContext.getCurrentInstance();
	// context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", "Deneme"));
		Gorevlibilgileri gorevlibilgileri2=new Gorevlibilgileri();
		
		System.out.println("parola :"+getParolaYeniden());
		if(gorevlibilgileri.getParola().equals(getParolaYeniden()))
		{
			gorevlibilgileri2=gorevlibilgileri;
			veritabani.entityManager.getTransaction().begin();
				veritabani.entityManager.persist(gorevlibilgileri2);
			veritabani.entityManager.getTransaction().commit();
			//veritabani.entityManager.close();
		}
	}
	
	List<Gorevlibilgileri>gorevlibilgileriList=new ArrayList<Gorevlibilgileri>();
	
	public List<Gorevlibilgileri> gorevliBilgileriCek()
	{
		gorevlibilgileriList=veritabani.entityManager.createNamedQuery("GorevlibilgileriCek").getResultList();
		return gorevlibilgileriList;
	}
	
	public void gorevliSil(Gorevlibilgileri gorevlibilgileri)
	{
		veritabani.entityManager.getTransaction().begin();
			veritabani.entityManager.remove(gorevlibilgileri);
		veritabani.entityManager.getTransaction().commit();
	}
	
	public void gorevliDuzenle(Gorevlibilgileri gorevlibilgileri)
	{
		veritabani.entityManager.getTransaction().begin();
			gorevlibilgileri.setGorunurluk(true);
		veritabani.entityManager.getTransaction().commit();
	}
	public void gorunurkukKapat()
	{
		gorevlibilgileriList.clear();
		gorevlibilgileriList=veritabani.entityManager.createNamedQuery("GorevlibilgileriCek").getResultList();
		
		for(Gorevlibilgileri gorevlibilgileri:gorevlibilgileriList)
		{
			veritabani.entityManager.getTransaction().begin();
				gorevlibilgileri.setGorunurluk(false );
			veritabani.entityManager.getTransaction().commit();
		}
	}
	
	public void  gorevliDuzenleKaydet(Gorevlibilgileri gorevlibilgileri2)
	{
		gorevlibilgileri2.setIsim(gorevlibilgileri.getIsim());
		gorevlibilgileri2.setSoyisim(this.gorevlibilgileri.getSoyisim());
		gorevlibilgileri2.setEposta(this.gorevlibilgileri.getEposta());
		gorevlibilgileri2.setGorevliAdi(this.gorevlibilgileri.getGorevliAdi());
		gorevlibilgileri2.setGorevYetkisi(gorevlibilgileri.getGorevYetkisi());
		gorevlibilgileri2.setTc(gorevlibilgileri.getTc());
		
		veritabani.entityManager.getTransaction().begin();
			veritabani.entityManager.merge(gorevlibilgileri2);
			gorevlibilgileri2.setGorunurluk(false);
		veritabani.entityManager.getTransaction().commit();

	}
	
	
	
	
	
	
	//------------------------------------------------------------------
	//------------------------------------------------------------------


	
	
	public Gorevlibilgileri getGorevlibilgileri() {
		return gorevlibilgileri;
	}

	public void setGorevlibilgileri(Gorevlibilgileri gorevlibilgileri) {
		this.gorevlibilgileri = gorevlibilgileri;
	}
	
	public String getParolaYeniden() {
		return parolaYeniden;
	}

	public void setParolaYeniden(String parolaYeniden) {
		this.parolaYeniden = parolaYeniden;
	}

	public String getGorevliGirisGorevliAdi() {
		return gorevliGirisGorevliAdi;
	}



	public void setGorevliGirisGorevliAdi(String gorevliGirisGorevliAdi) {
		this.gorevliGirisGorevliAdi = gorevliGirisGorevliAdi;
	}



	public String getGorevliGirisGorevliSifre() {
		return gorevliGirisGorevliSifre;
	}



	public void setGorevliGirisGorevliSifre(String gorevliGirisGorevliSifre) {
		this.gorevliGirisGorevliSifre = gorevliGirisGorevliSifre;
	}

	public boolean isGirdiMi() {
		return girdiMi;
	}



	public void setGirdiMi(boolean girdiMi) {
		this.girdiMi = girdiMi;
	}
	
	 

	public boolean isSayfaGorunurluk() {
	return sayfaGorunurluk;
}



public void setSayfaGorunurluk(boolean sayfaGorunurluk) {
	this.sayfaGorunurluk = sayfaGorunurluk;
}

	
	
	
	
	
	
	
}
