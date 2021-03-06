/*
 * <!--
 *     Copyright (c) 2015-2019 Automation RockStars Ltd.
 *     All rights reserved. This program and the accompanying materials
 *     are made available under the terms of the Apache License v2.0
 *     which accompanies this distribution, and is available at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Contributors:
 *         Automation RockStars
 *  -->
 */

package com.automationrockstars.design.desktop.driver;

import com.automationrockstars.base.ConfigLoader;
import com.automationrockstars.design.desktop.driver.internal.ImageCache;
import com.automationrockstars.design.gir.webdriver.DriverFactory;
import com.automationrockstars.design.gir.webdriver.GridUtils;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.server.DriverProvider;
import org.openqa.selenium.sikulix.SikulixDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;


public class SikulixProvider implements DriverProvider {

    public static final String DRIVER_NAME = "sikulix";
    private static final Map<WebDriver, WebDriver> supported = Maps.newHashMap();

    public static Capabilities driverCapabilities() {
        return new DesiredCapabilities(DRIVER_NAME, "", Platform.WINDOWS);
    }

    public static synchronized WebDriver supporting(WebDriver toSupport) {
        if (supported.containsKey(toSupport)) {
            return supported.get(toSupport);

        } else {
            String gridUrl = ConfigLoader.config().getString("grid.url");
            if (DriverFactory.unwrap(toSupport) instanceof RemoteWebDriver && !Strings.isNullOrEmpty(gridUrl)) {
                String node = GridUtils.getNode(gridUrl, toSupport);
                try {
                    final WebDriver sikulix = new RemoteWebDriver(new URL(node + "/wd/hub"), driverCapabilities());
                    supported.put(toSupport, sikulix);
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

                        @Override
                        public void run() {
                            sikulix.close();
                            sikulix.quit();
                        }
                    }));
                    ImageCache.syncRemote(sikulix);
                    return sikulix;
                } catch (MalformedURLException e) {
                    Throwables.propagate(e);
                    return null;
                }
            } else return new SikulixDriver();
        }
    }

    @Override
    public Capabilities getProvidedCapabilities() {
        return driverCapabilities();
    }


    public boolean canCreateDriverInstances() {
        return true;
    }

    @Override
    public boolean canCreateDriverInstanceFor(Capabilities capabilities) {
        return capabilities.getBrowserName().equals(DRIVER_NAME) && (capabilities.getPlatform().is(Platform.WINDOWS) || capabilities.getPlatform().is(Platform.ANY));
    }

    @Override
    public WebDriver newInstance(Capabilities capabilities) {
        return new SikulixDriver();
    }

}
