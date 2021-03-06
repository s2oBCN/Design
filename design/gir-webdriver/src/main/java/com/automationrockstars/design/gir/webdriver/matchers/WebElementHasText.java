/*******************************************************************************
 * Copyright (c) 2015, 2016 Automation RockStars Ltd.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Automation RockStars - initial API and implementation
 *******************************************************************************/
package com.automationrockstars.design.gir.webdriver.matchers;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.StringContains;
import org.openqa.selenium.WebElement;

public class WebElementHasText extends TypeSafeMatcher<WebElement> {

    private final String textToFind;

    public WebElementHasText(String textToFind) {
        this.textToFind = textToFind;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(textToFind);

    }

    @Override
    protected boolean matchesSafely(WebElement item) {
        return new StringContains(textToFind).matchesSafely(((org.openqa.selenium.WebElement) item).getText());
    }

}
