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

package com.automationrockstars.bmo.event.processor;

import com.automationrockstars.gunter.EventType;
import com.automationrockstars.gunter.events.*;
import org.junit.Before;
import org.junit.Test;

import static com.automationrockstars.asserts.Asserts.assertThat;
import static org.hamcrest.Matchers.*;

public class EventStorageTest {

    private static TestExecutionStart es1 = EventFactory.createExecutionStart("name1");
    private static TestExecutionFinish ef1 = EventFactory.createExecutionFinish(es1, "DONE");
    private static TestExecutionStart es2 = EventFactory.createExecutionStart(String.format("name2-%s", System.currentTimeMillis()));
    private static TestSuiteStart tss2 = EventFactory.createSuiteStart(es2, "suite2");
    private static TestCaseStart tcs2 = EventFactory.createTestCaseStart(tss2, "tc2");
    private static TestStepStart tsts2 = EventFactory.createTestStepStart(tcs2, "step2");
    private static TestStepFinish tstf2 = EventFactory.createTestStepFinish(tsts2, "FAILED");
    private static TestCaseFinish tcf2 = EventFactory.createTestCaseFinish(tcs2, "DONE");
    private static TestSuiteFinish tsf2 = EventFactory.createSuiteFinish(tss2, "DONE");
    private static TestExecutionFinish ef2 = EventFactory.createExecutionFinish(es2, "DONE");
    private EventStorage st = new EventStorage();

    @Before
    public void prepare() {
        st.clean();
        st.store(es1);
        st.store(ef1);
        st.store(es2);
        st.store(tss2);
        st.store(tcs2);
        st.store(tsts2);
        st.store(tstf2);
        st.store(tcf2);
        st.store(tsf2);
        st.store(ef2);
    }

    @Test
    public void should_provideParent() {
        assertThat(st.getParent(ef1), is(equalTo((Event) es1)));
        assertThat(st.getParent(ef2), is(equalTo((Event) es2)));
        assertThat(st.getParent(tcs2), is(equalTo((Event) tss2)));

    }

    //	TODO: 16/05/2017 investigate why results don't match
//	@Test
    public void should_provideChildren() {
        assertThat(st.getChildren(es1), contains((Event) ef1));
        assertThat(st.getChildren(es2), containsInAnyOrder((Event) tss2, (Event) ef2));

        assertThat(st.getAllChildren(es1), contains((Event) ef1));
        assertThat(st.getAllChildren(es2), containsInAnyOrder(
                (Event) ef2,
                (Event) tss2, (Event) tsf2,
                (Event) tcs2, (Event) tcf2,
                (Event) tsts2, (Event) tstf2));

        assertThat(st.getAllChildren(tss2), containsInAnyOrder(
                (Event) tsf2,
                (Event) tcs2, (Event) tcf2,
                (Event) tsts2, (Event) tstf2));

    }

    @Test
    public void should_provideParentOfType() {
        assertThat(st.getParent(ef2, EventType.EXECUTION_START), is(equalTo((Event) es2)));
        assertThat(st.getParent(ef2, EventType.EXECUTION_FINISH), is(nullValue()));
        assertThat(st.getParent(tstf2, EventType.EXECUTION_START), is(equalTo((Event) es2)));
        assertThat(st.getParent(tstf2, EventType.TEST_SUITE_START), is(equalTo((Event) tss2)));
        assertThat(st.getParent(ef1, EventType.EXECUTION_START), is(equalTo((Event) es1)));
    }

    @Test
    public void should_addUnderCondition() {
        assertThat(st.storeIfParentStored(ef2), is(true));
        assertThat(st.storeIfParentStored(es2), is(false));
        Event event = EventFactory.createAction(null, "action", "element");
        assertThat(st.storeIfParentStored(event), is(false));
        assertThat(st.getAll(), not(contains(event)));

    }

    // TODO: 16/05/2017 investigate why cleanTree is removing all events
//	@Test
    public void should_cleanTree() {
        st.clearTree(tcf2);
        assertThat(st.getAll().toList(), hasSize(2));
    }

    @Test
    public void should_remove() {
        Event event = EventFactory.createAction(null, "action", "element");
        st.store(event);
        assertThat("Event not stored", st.has(event));
        st.remove(event);
        assertThat("Event not removed", !st.has(event));
    }

    @Test
    public void should_clean() {
        st.clean();
        assertThat(st.getAll(), is(emptyIterable()));
    }

    // TODO: 16/05/2017 investigate why cleanTree is removing all events
//	@Test
    public void should_provideFullTree() {
        System.out.println(st.getAll());
        st.clearTree(es1);
        System.out.println(st.getAll());
        assertThat(st.getTree(tsf2).toList(), containsInAnyOrder(st.getAll().toList().toArray()));
    }

    @Test
    public void should_provideTestStepFinishFromAllChildren() {
        assertThat(st.getAllChildren(es2, EventType.TEST_STEP_FINISH), contains((Object) tstf2));
    }

    @Test
    public void should_provideTestSuiteStartFromDirechChildren() {
        assertThat(st.getChildren(es2, EventType.TEST_SUITE_START), contains((Object) tss2));
    }

    @Test
    public void should_checkForEventExistense() {
        assertThat("Has unexpected commit", !st.hasEvents(EventType.COMMIT));
        assertThat("Doesnt have test case finish", st.hasEvents(EventType.TEST_CASE_FINISH));
    }

}
