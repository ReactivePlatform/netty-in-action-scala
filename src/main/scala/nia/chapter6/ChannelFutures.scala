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

package nia.chapter6

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import io.netty.channel.socket.nio.NioSocketChannel

/**
 * Created by kerr.
 *
 * 代码清单 6-13 添加 ChannelFutureListener 到 ChannelFuture
 */
object ChannelFutures {
  private val CHANNEL_FROM_SOMEWHERE = new NioSocketChannel
  private val SOME_MSG_FROM_SOMEWHERE = Unpooled.buffer(1024)

  /**
   * 代码清单 6-13 添加 ChannelFutureListener 到 ChannelFuture
   */
  def addingChannelFutureListener(): Unit = {
    val channel = CHANNEL_FROM_SOMEWHERE
    // get reference to pipeline;
    val someMessage = SOME_MSG_FROM_SOMEWHERE
    //...
    val future = channel.write(someMessage)
    future.addListener(new ChannelFutureListener() {
      override def operationComplete(f: ChannelFuture): Unit = {
        if (!f.isSuccess) {
          f.cause.printStackTrace()
          f.channel.close
        }
      }
    })
  }
}
