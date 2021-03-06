/*
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at http://www.netbeans.org/cddl.html
 * or http://www.netbeans.org/cddl.txt.
 *
 * When distributing Covered Code, include this CDDL Header Notice in each file
 * and include the License file at http://www.netbeans.org/cddl.txt.
 * If applicable, add the following below the CDDL Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * The Original Software is JSwat. The Initial Developer of the Original
 * Software is Nathan L. Fiedler. Portions created by Nathan L. Fiedler
 * are Copyright (C) 2007-2012. All Rights Reserved.
 *
 * Contributor(s): Nathan L. Fiedler.
 */
package com.bluemarsh.jswat.core.runtime;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the RuntimeEventMulticaster class.
 *
 * @author Nathan Fiedler
 */
public class RuntimeEventMulticasterTest {

    @Test
    public void test_RuntimeEventMulticaster() {
        RuntimeEventMulticaster rem = new RuntimeEventMulticaster();
        Assert.assertNotNull(rem);

        // nothing should happen
        rem.add(null);
        rem.remove(null);

        TestListener l1 = new TestListener();
        rem.add(l1);
        TestListener l2 = new TestListener();
        rem.add(l2);

        Assert.assertEquals(0, l1.added);
        Assert.assertEquals(0, l1.removed);
        Assert.assertEquals(0, l2.added);
        Assert.assertEquals(0, l2.removed);

        JavaRuntime runtime = new DummyRuntime();
        RuntimeEvent sevt = new RuntimeEvent(runtime, RuntimeEventType.ADDED);
        sevt.getType().fireEvent(sevt, rem);
        Assert.assertEquals(1, l1.added);
        Assert.assertEquals(0, l1.removed);
        Assert.assertEquals(1, l2.added);
        Assert.assertEquals(0, l2.removed);

        sevt = new RuntimeEvent(runtime, RuntimeEventType.REMOVED);
        sevt.getType().fireEvent(sevt, rem);
        Assert.assertEquals(1, l1.added);
        Assert.assertEquals(1, l1.removed);
        Assert.assertEquals(1, l2.added);
        Assert.assertEquals(1, l2.removed);

        rem.remove(l1);
        sevt = new RuntimeEvent(runtime, RuntimeEventType.ADDED);
        sevt.getType().fireEvent(sevt, rem);
        Assert.assertEquals(1, l1.added);
        Assert.assertEquals(1, l1.removed);
        Assert.assertEquals(2, l2.added);
        Assert.assertEquals(1, l2.removed);
    }

    private static class TestListener implements RuntimeListener {

        int added;
        int removed;

        @Override
        public void runtimeAdded(RuntimeEvent event) {
            added++;
        }

        @Override
        public void runtimeRemoved(RuntimeEvent event) {
            removed++;
        }
    }
}
