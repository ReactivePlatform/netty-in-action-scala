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

package nia.chapter4

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.CharsetUtil
import java.util.concurrent.Executors

/**
 * 代码清单 4-5 写出到 Channel
 *
 * 代码清单 4-6 从多个线程使用同一个 Channel
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
object ChannelOperationExamples {
  private val CHANNEL_FROM_SOMEWHERE = new NioSocketChannel

  /**
   * 代码清单 4-5 写出到 Channel
   */
  def writingToChannel(): Unit = {
    val channel = CHANNEL_FROM_SOMEWHERE
    // Get the channel reference from somewhere
    //创建持有要写数据的 ByteBuf
    val buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8)
    val cf = channel.writeAndFlush(buf)
    //添加 ChannelFutureListener 以便在写操作完成后接收通知
    cf.addListener(new ChannelFutureListener() {
      override def operationComplete(future: ChannelFuture): Unit = { //写操作完成，并且没有错误发生
        if (future.isSuccess)
          println("Write successful")
        else { //记录错误
          System.err.println("Write error")
          future.cause.printStackTrace()
        }
      }
    })
  }

  /**
   * 代码清单 4-6 从多个线程使用同一个 Channel
   */
  def writingToChannelFromManyThreads(): Unit = {
    val channel = CHANNEL_FROM_SOMEWHERE
    //创建持有要写数据的ByteBuf
    val buf = Unpooled.copiedBuffer("your data", CharsetUtil.UTF_8)
    //创建将数据写到Channel 的 Runnable
    val writer: Runnable = () ⇒ channel.write(buf.duplicate())
    //获取到线程池Executor 的引用
    val executor = Executors.newCachedThreadPool
    //递交写任务给线程池以便在某个线程中执行
    // write in one thread
    executor.execute(writer)
    //递交另一个写任务以便在另一个线程中执行
    // write in another thread
    executor.execute(writer)
    //...
  }
}
