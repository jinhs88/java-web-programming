package core.nmvc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.mvc.SimpleControllerHandlerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import core.mvc.SlippHandlerAdapter;

public class SpringMvcController extends HttpServlet {
	private static final long serialVersionUID = -5704524104722380068L;
	private static final Logger logger = LoggerFactory.getLogger(SpringMvcController.class);
	
	private ConfigurableWebApplicationContext context;
	private BeanNameUrlHandlerMapping handlerMapping;
	
	private List<HandlerAdapter> handlerAdapters = new ArrayList<HandlerAdapter>();
	
	@Override
	public void init() throws ServletException {
		context = new XmlWebApplicationContext();
		context.setConfigLocation(configPath());
		context.setServletContext(getServletContext());
		context.setServletConfig(getServletConfig());
		context.refresh();
		
		handlerMapping = new BeanNameUrlHandlerMapping();
		handlerMapping.setApplicationContext(context);
		
		initHandlerAdapters();
	}
	
	private void initHandlerAdapters() {
		handlerAdapters.add(new SimpleControllerHandlerAdapter());
		handlerAdapters.add(new SlippHandlerAdapter());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
		
		try {
			HandlerExecutionChain hec = handlerMapping.getHandler(request);
			logger.debug("Handler : {}", hec.getHandler().getClass());
			
			ModelAndView mav = null;
			for (HandlerAdapter each : this.handlerAdapters) {
				if (each.supports(hec.getHandler())) {
					mav = each.handle(request, response, hec.getHandler());
				}
			}
			
			if (mav.getView() != null) {
				View view = mav.getView();
				view.render(mav.getModel(), request, response);
				return;
			}
			
			InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
			viewResolver.setApplicationContext(context);
			viewResolver.setSuffix(".jsp");
			LocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
			View view = viewResolver.resolveViewName(mav.getViewName(), localeResolver.resolveLocale(request));
			view.render(mav.getModel(), request, response);
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		}
	}
	
	private String configPath() {
		return "WEB-INF" + File.separator + getServletName() + "-servlet.xml"; 
	}
}