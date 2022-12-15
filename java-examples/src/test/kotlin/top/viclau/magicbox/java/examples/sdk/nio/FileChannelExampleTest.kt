/*
 * Copyright (c) 2022 Vic Lau
 *
 * Distributed under MIT license.
 * See file LICENSE for detail or copy at https://opensource.org/licenses/MIT
 */

package top.viclau.magicbox.java.examples.sdk.nio

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import top.viclau.magicbox.java.examples.absolutePathOf
import top.viclau.magicbox.java.examples.pathUnderPkg


private const val FILE_CONTENT = """
A value can't be bigger than 512MB.

-------------------------------------------------------------------------------

SET
GET
DEL

RENAME

TTL
EXPIRE
PERSIST

SAVE
BGSAVE

#######################
## list(Linked List) ##
#######################

RPUSH   # puts the new value at the end of the list
LPUSH   # puts the new value at the start of the list

LRANGE  # gives a subset of the list. It takes the index of the first element
        # you want to retrieve as its first parameter and the index of the last
        # element you want to retrieve as its second parameter. A value of -1
        # for the second parameter means to retrieve all elements in the list.

LLEN    # returns the current length of the list       
       
RPOP    # removes the last element from the list and returns it
LPOP    # removes the first element from the list and returns it

#########
## set ##
#########

SADD    # adds the given value to the set
SREM    # removes the given value from the set

SISMEMBER   # tests if the given value is in the set
SMEMBERS    # returns a list of all the members of this set

SUNION  # combines two or more sets and returns the list of all elements
SINTER  # 

################
## sorted set ##
################

ZADD

ZRANGE
ZREVRANGE

ZRANGEBYSCORE
ZREMRANGEBYSCORE    # remove ranges of elements

#######################
## atomic operations ##
#######################

incr
incrby
decr
decrby

##############
##  Pub/Sub ##
##############

SUBSCRIBE
UNSUBSCRIBE
PUBLISH

PSUBSCRIBE
PUNSUBSCRIBE

##########
## TODO ##
##########

test the improvement due to pipeling

RDB persistence # performs point-in-time snapshots of your dataset at specified
                # intervals
                
AOF persistence # logs every write operation received by the server, that will
                # be played again at server startup, reconstructing the
                # original dataset
                
                


"""



class FileChannelExampleTest {

    @Test
    fun testRead() {
        Assertions.assertEquals(read(absolutePathOf(FileChannelExampleTest::class.pathUnderPkg("read.txt"))!!), FILE_CONTENT)
    }

    @Test
    fun testRead2() {
        Assertions.assertEquals(read2(absolutePathOf(FileChannelExampleTest::class.pathUnderPkg("read.txt"))!!), FILE_CONTENT)
    }

    @Disabled("file content is not checked")
    @Test
    fun testWrite() {
        write("Hello World!", toPath = absolutePathOf(FileChannelExampleTest::class.pathUnderPkg("write.txt"))!!)
    }

    @Disabled("file content is not checked")
    @Test
    fun testWriteFromHttpUrl() {
        writeFromHttpUrl("http://www.baidu.com", toPath = absolutePathOf(FileChannelExampleTest::class.pathUnderPkg("write-from-http-url.html"))!!)
    }

}