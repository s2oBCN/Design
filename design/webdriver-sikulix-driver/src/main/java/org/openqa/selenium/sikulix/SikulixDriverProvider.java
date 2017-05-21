package org.openqa.selenium.sikulix;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.automationrockstars.base.ConfigLoader;
import com.automationrockstars.design.desktop.driver.SikulixProvider;
import com.automationrockstars.design.gir.webdriver.DriverFactory;
import com.automationrockstars.gir.ui.context.Image;
import com.automationrockstars.gir.ui.context.SearchContextProvider;
import com.google.common.base.Strings;

public class SikulixDriverProvider implements SearchContextProvider{

	@Override
	public boolean canProvide(Class<? extends Annotation> context) {
		return context.equals(Image.class);
	}

	@Override
	public SearchContext provide() {
		if (DriverFactory.canScreenshot()){
			return SikulixProvider.supporting(DriverFactory.getDriver());
		} else if (! Strings.isNullOrEmpty(ConfigLoader.config().getString("grid.url"))){
			URL gridUrl = null;
			try {
				gridUrl = new URL(ConfigLoader.config().getString("grid.url"));
				return new RemoteWebDriver(gridUrl,SikulixDriver.capabilities());
			} catch (MalformedURLException e) {
				throw new RuntimeException(String.format("Grid url %s is wrong", ConfigLoader.config().getString("grid.url")));
			} catch (Throwable e) {
				e.printStackTrace();
				throw e;
			}
			
		} else {
			return new SikulixDriver();		
		}


	}

}
