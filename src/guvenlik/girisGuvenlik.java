package guvenlik;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import islevler.gorevlislemleri;

public class girisGuvenlik implements Filter

{
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("güvenlik");
		
		gorevlislemleri loginBean = (gorevlislemleri)((HttpServletRequest)request).getSession().getAttribute("girdiMi");
		
		
	
		if (loginBean==null)
		{
			String contextPath = ((HttpServletRequest)request).getContextPath();
			System.out.println(contextPath);
			((HttpServletResponse)response).sendRedirect(contextPath + "/yoneticiGiris.xhtml");
		}
		
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
