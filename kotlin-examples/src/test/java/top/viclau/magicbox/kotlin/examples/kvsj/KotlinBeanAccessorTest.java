/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.kvsj;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KotlinBeanAccessorTest {

    @Test
    public void test() {
        KotlinBean bean = new KotlinBean();

        // property 'name'
        bean.setName("name");
        assertEquals("name", bean.getName());

        // property 'hasName'
        bean.setHasName(true);
        assertTrue(bean.getHasName());

        // property 'isValid'
        bean.setValid(true);
        assertTrue(bean.isValid());

        // property 'active'
        bean.setActive(true);
        assertTrue(bean.getActive());

        assertEquals(90, bean.getAge());
        assertTrue(bean.getHasPressure());
        assertTrue(bean.isOld());
        assertTrue(bean.getFantasy());
    }

}
