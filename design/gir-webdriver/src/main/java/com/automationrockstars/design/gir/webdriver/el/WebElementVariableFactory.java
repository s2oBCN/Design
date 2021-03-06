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

package com.automationrockstars.design.gir.webdriver.el;

import org.mvel2.integration.VariableResolver;
import org.mvel2.integration.impl.BaseVariableResolverFactory;
import org.openqa.selenium.WebElement;

public class WebElementVariableFactory extends BaseVariableResolverFactory {

    /**
     *
     */
    private static final long serialVersionUID = 1888208533226010805L;
    private final WebElement element;

    public WebElementVariableFactory(WebElement element) {
        this.element = element;
    }

    public VariableResolver createVariable(String name, Object value) {
        return new WebElementVariableResolver(element, name);
    }

    public VariableResolver createVariable(String name, Object value, Class<?> type) {
        return new WebElementVariableResolver(element, name);
    }

    public boolean isTarget(String name) {
        return true;
    }

    public boolean isResolveable(String name) {
        WebElementVariableResolver resolver = new WebElementVariableResolver(element, name);
        if (resolver.getValue() != null) {
            super.variableResolvers.put(name, resolver);
            return true;
        } else {
            return false;
        }
    }


}
