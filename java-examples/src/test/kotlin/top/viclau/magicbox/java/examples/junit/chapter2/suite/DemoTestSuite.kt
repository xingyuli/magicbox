package top.viclau.magicbox.java.examples.junit.chapter2.suite

import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite

@Suite
@SelectClasses(value = [FooTest::class, BarTest::class])
class DemoTestSuite {
}