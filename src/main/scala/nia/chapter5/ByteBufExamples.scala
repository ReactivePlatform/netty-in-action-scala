/*
 * Copyright 2018 netty.reactiveplatform.xyz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nia.chapter5

import io.netty.buffer._
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.ByteProcessor
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.util.Random
import collection.JavaConverters.iterableAsScalaIterableConverter

import io.netty.channel.DummyChannelHandlerContext.DUMMY_INSTANCE

/**
 * Created by kerr.
 *
 * 代码清单 5-1 支撑数组
 *
 * 代码清单 5-2 访问直接缓冲区的数据
 *
 * 代码清单 5-3 使用 ByteBuffer 的复合缓冲区模式
 *
 * 代码清单 5-4 使用 CompositeByteBuf 的复合缓冲区模式
 *
 * 代码清单 5-5 访问 CompositeByteBuf 中的数据
 *
 * 代码清单 5-6 访问数据
 *
 * 代码清单 5-7 读取所有数据
 *
 * 代码清单 5-8 写数据
 *
 * 代码清单 5-9 使用 ByteBufProcessor 来寻找\r
 *
 * 代码清单 5-10 对 ByteBuf 进行切片
 *
 * 代码清单 5-11 复制一个 ByteBuf
 *
 * 代码清单 5-12 get()和 set()方法的用法
 *
 * 代码清单 5-13 ByteBuf 上的 read()和 write()操作
 *
 * 代码清单 5-14 获取一个到 ByteBufAllocator 的引用
 *
 * 代码清单 5-15 引用计数
 *
 * 代码清单 5-16 释放引用计数的对象
 */
object ByteBufExamples {
  private val random = new Random
  private val BYTE_BUF_FROM_SOMEWHERE = Unpooled.buffer(1024)
  private val CHANNEL_FROM_SOMEWHERE = new NioSocketChannel
  private val CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DUMMY_INSTANCE

  private def handleArray(array: Array[Byte], offset: Int, len: Int): Unit = {
    println(offset)
    println(len)
    println(array(offset))
  }

  /**
   * 代码清单 5-1 支撑数组
   */
  def heapBuffer(): Unit = {
    val heapBuf = BYTE_BUF_FROM_SOMEWHERE //get reference form somewhere
    //检查 ByteBuf 是否有一个支撑数组
    if (heapBuf.hasArray) { //如果有，则获取对该数组的引用
      val array = heapBuf.array
      //计算第一个字节的偏移量
      val offset = heapBuf.arrayOffset + heapBuf.readerIndex
      //获得可读字节数
      val length = heapBuf.readableBytes
      //使用数组、偏移量和长度作为参数调用你的方法
      handleArray(array, offset, length)
    }
  }

  /**
   * 代码清单 5-2 访问直接缓冲区的数据
   */
  def directBuffer(): Unit = {
    val directBuf = BYTE_BUF_FROM_SOMEWHERE
    //检查 ByteBuf 是否由数组支撑。如果不是，则这是一个直接缓冲区
    if (!directBuf.hasArray) { //获取可读字节数
      val length = directBuf.readableBytes
      //分配一个新的数组来保存具有该长度的字节数据
      val array = new Array[Byte](length)
      //将字节复制到该数组
      directBuf.getBytes(directBuf.readerIndex, array)
      handleArray(array, 0, length)
    }
  }

  /**
   * 代码清单 5-3 使用 ByteBuffer 的复合缓冲区模式
   */
  def byteBufferComposite(header: ByteBuffer, body: ByteBuffer): Unit = { // Use an array to hold the message parts
    val message = Array[ByteBuffer](header, body)
    // Create a new ByteBuffer and use copy to merge the header and body
    val message2 = ByteBuffer.allocate(header.remaining + body.remaining)
    message2.put(header)
    message2.put(body)
    message2.flip()
  }

  /**
   * 代码清单 5-4 使用 CompositeByteBuf 的复合缓冲区模式
   */
  def byteBufComposite(): Unit = {
    val messageBuf: CompositeByteBuf = Unpooled.compositeBuffer
    val headerBuf = BYTE_BUF_FROM_SOMEWHERE
    // can be backing or direct
    val bodyBuf = BYTE_BUF_FROM_SOMEWHERE
    //将 ByteBuf 实例追加到 CompositeByteBuf
    messageBuf.addComponents(headerBuf, bodyBuf)
    //...
    //删除位于索引位置为 0（第一个组件）的 ByteBuf
    messageBuf.removeComponent(0) // remove the header

    //循环遍历所有的 ByteBuf 实例
    for (buf: ByteBuf ← messageBuf.asScala) {
      println(buf.toString())
    }
  }

  /**
   * 代码清单 5-5 访问 CompositeByteBuf 中的数据
   */
  def byteBufCompositeArray(): Unit = {
    val compBuf: CompositeByteBuf = Unpooled.compositeBuffer
    val length = compBuf.readableBytes
    //分配一个具有可读字节数长度的新数组
    val array = new Array[Byte](length)
    //将字节读到该数组中
    compBuf.getBytes(compBuf.readerIndex(), array)
    //使用偏移量和长度作为参数使用该数组
    handleArray(array, 0, array.length)
  }

  /**
   * 代码清单 5-6 访问数据
   */
  def byteBufRelativeAccess(): Unit = {
    val buffer = BYTE_BUF_FROM_SOMEWHERE
    var i = 0
    while (i < buffer.capacity) {
      val b = buffer.getByte(i)
      println(b.toChar)
      i += 1
    }
  }

  /**
   * 代码清单 5-7 读取所有数据
   */
  def readAllData(): Unit = {
    val buffer = BYTE_BUF_FROM_SOMEWHERE
    while (buffer.isReadable)
      println(buffer.readByte)
  }

  /**
   * 代码清单 5-8 写数据
   */
  def write(): Unit = { // Fills the writable bytes of a buffer with random integers.
    val buffer = BYTE_BUF_FROM_SOMEWHERE
    while (buffer.writableBytes >= 4)
      buffer.writeInt(random.nextInt)
  }

  /**
   * 代码清单 5-9 使用 ByteBufProcessor 来寻找\r
   *
   * use {@link io.netty.buffer.ByteBufProcessor in Netty 4.0.x}
   */
  def byteProcessor(): Unit = {
    val buffer = BYTE_BUF_FROM_SOMEWHERE
    val index = buffer.forEachByte(ByteProcessor.FIND_CR)
  }

  /**
   * 代码清单 5-9 使用 ByteBufProcessor 来寻找\r
   *
   * use {@link io.netty.util.ByteProcessor in Netty 4.1.x}
   */
  def byteBufProcessor(): Unit = {
    val buffer = BYTE_BUF_FROM_SOMEWHERE
    val index = buffer.forEachByte(ByteBufProcessor.FIND_CR)
  }

  /**
   * 代码清单 5-10 对 ByteBuf 进行切片
   */
  def byteBufSlice(): Unit = {
    val utf8 = Charset.forName("UTF-8")
    //创建一个用于保存给定字符串的字节的 ByteBuf
    val buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8)
    //创建该 ByteBuf 从索引 0 开始到索引 15 结束的一个新切片
    val sliced = buf.slice(0, 15)
    //将打印“Netty in Action”
    System.out.println(sliced.toString(utf8))
    //更新索引 0 处的字节
    buf.setByte(0, 'J'.toByte)
    //将会成功，因为数据是共享的，对其中一个所做的更改对另外一个也是可见的
    assert(buf.getByte(0) == sliced.getByte(0))
  }

  /**
   * 代码清单 5-11 复制一个 ByteBuf
   */
  def byteBufCopy(): Unit = {
    val utf8 = Charset.forName("UTF-8")
    //创建 ByteBuf 以保存所提供的字符串的字节
    val buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8)
    //创建该 ByteBuf 从索引 0 开始到索引 15 结束的分段的副本
    val copy = buf.copy(0, 15)
    System.out.println(copy.toString(utf8))
    buf.setByte(0, 'J'.toByte)
    //将会成功，因为数据不是共享的
    assert(buf.getByte(0) != copy.getByte(0))
  }

  /**
   * 代码清单 5-12 get()和 set()方法的用法
   */
  def byteBufSetGet(): Unit = {
    val utf8 = Charset.forName("UTF-8")
    //创建一个新的 ByteBuf以保存给定字符串的字节
    val buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8)
    //打印第一个字符'N'
    System.out.println(buf.getByte(0).toChar)
    //存储当前的 readerIndex 和 writerIndex
    val readerIndex = buf.readerIndex
    val writerIndex = buf.writerIndex
    //将索引 0 处的字 节更新为字符'B'
    buf.setByte(0, 'B'.toByte)
    //打印第一个字符，现在是'B'
    System.out.println(buf.getByte(0).toChar)
    //将会成功，因为这些操作并不会修改相应的索引
    assert(readerIndex == buf.readerIndex)
    assert(writerIndex == buf.writerIndex)
  }

  /**
   * 代码清单 5-13 ByteBuf 上的 read()和 write()操作
   */
  def byteBufWriteRead(): Unit = {
    val utf8 = Charset.forName("UTF-8")
    //创建一个新的 ByteBuf 以保存给定字符串的字节
    val buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8)
    System.out.println(buf.readByte.toChar)
    //存储当前的readerIndex
    val readerIndex = buf.readerIndex
    //存储当前的writerIndex
    val writerIndex = buf.writerIndex
    //将字符 '?'追加到缓冲区
    buf.writeByte('?'.toByte)
    assert(readerIndex == buf.readerIndex)
    //将会成功，因为 writeByte()方法移动了 writerIndex
    assert(writerIndex != buf.writerIndex)
  }

  /**
   * 代码清单 5-14 获取一个到 ByteBufAllocator 的引用
   */
  def obtainingByteBufAllocatorReference(): Unit = {
    val channel = CHANNEL_FROM_SOMEWHERE
    //从 Channel 获取一个到ByteBufAllocator 的引用
    val allocator = channel.alloc()
    val ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE
    //从 ChannelHandlerContext 获取一个到 ByteBufAllocator 的引用
    val allocator2 = ctx.alloc()
  }

  /**
   * 代码清单 5-15 引用计数
   */
  def referenceCounting(): Unit = {
    val channel = CHANNEL_FROM_SOMEWHERE
    //从 Channel 获取ByteBufAllocator
    val allocator = channel.alloc()
    //从 ByteBufAllocator分配一个 ByteBuf
    val buffer = allocator.directBuffer
    //检查引用计数是否为预期的 1
    assert(buffer.refCnt == 1)
  }

  /**
   * 代码清单 5-16 释放引用计数的对象
   */
  def releaseReferenceCountedObject(): Unit = {
    val buffer = BYTE_BUF_FROM_SOMEWHERE
    //减少到该对象的活动引用。当减少到 0 时，该对象被释放，并且该方法返回 true
    val released = buffer.release()
  }
}
