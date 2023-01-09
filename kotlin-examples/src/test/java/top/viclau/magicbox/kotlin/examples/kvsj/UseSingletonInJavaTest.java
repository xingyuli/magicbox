/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.kvsj;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class UseSingletonInJavaTest {

    @Test
    public void testCompanionSingleton() {
        CompanionSingleton first = CompanionSingleton.Companion.getInstance();
        CompanionSingleton second = CompanionSingleton.Companion.getInstance();
        assertSame(first, second);
    }

    @Test
    public void testStandardSingleton() {
        StandardSingleton first = StandardSingleton.getInstance();
        StandardSingleton second = StandardSingleton.getInstance();
        assertSame(first, second);
    }

    @Test
    public void testShorthandSingleton() {
        // only access INSTANCE triggers the initialization
        System.out.println(ShorthandSingleton.class);
        System.out.println(ShorthandSingleton.INSTANCE); // print: do some preparation here

        ShorthandSingleton first = ShorthandSingleton.INSTANCE;
        ShorthandSingleton second = ShorthandSingleton.INSTANCE;
        assertSame(first, second);
    }

}
