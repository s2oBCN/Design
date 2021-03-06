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
package com.automationrockstars.gunter;

public enum EventType {

    EXECUTION_START,
    EXECUTION_FINISH,
    TEST_SUITE_START,
    TEST_SUITE_FINISH,
    TEST_CASE_START,
    TEST_CASE_FINISH,
    ACTION,
    TEST_LOG,
    JOB_SCHEDULED,
    TEST_STEP_START,
    TEST_STEP_FINISH,
    ATTACHMENT,
    COMMIT,
    ENVIRONMENT_BROKEN,
    ENVIRONMENT_UNDER_TESTS,
    ENVIRONMENT_WORKING,
    SAMPLE,
    ALL
}
