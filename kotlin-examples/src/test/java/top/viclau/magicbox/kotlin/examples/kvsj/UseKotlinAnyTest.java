/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.kotlin.examples.kvsj;

import org.junit.Test;

public class UseKotlinAnyTest {

    @Test
    public void test() {
        // Any -> Object
        UseKotlinAnyKt.doSomething(new Object());
    }

}
