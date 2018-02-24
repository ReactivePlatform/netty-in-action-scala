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

package nia.chapter13

import io.netty.bootstrap.Bootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioDatagramChannel
import java.net.InetSocketAddress
import java.lang.{ Boolean ⇒ JBoolean }

/**
 * 代码清单 13-8 LogEventMonitor
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
object LogEventMonitor {

  @throws[Exception]
  def main(args: Array[String]): Unit = {
    if (args.length != 1)
      throw new IllegalArgumentException("Usage: LogEventMonitor <port>")

    //构造一个新的 LogEventMonitor
    val monitor = new LogEventMonitor(new InetSocketAddress(args(0).toInt))
    try {
      val channel = monitor.bind()
      println("LogEventMonitor running")
      channel.closeFuture.sync()
    } finally {
      monitor.stop()
    }
  }
}

class LogEventMonitor(address: InetSocketAddress) {
  val group: EventLoopGroup = new NioEventLoopGroup
  val bootstrap = new Bootstrap
  //引导该 NioDatagramChannel
  bootstrap.group(group)
    .channel(classOf[NioDatagramChannel])
    //设置套接字选项 SO_BROADCAST
    .option[JBoolean](ChannelOption.SO_BROADCAST, true)
    .handler(new ChannelInitializer[Channel]() {
      @throws[Exception]
      override protected def initChannel(channel: Channel): Unit = {
        val pipeline = channel.pipeline
        //将 LogEventDecoder 和 LogEventHandler 添加到 ChannelPipeline 中
        pipeline.addLast(new LogEventDecoder)
        pipeline.addLast(new LogEventHandler)
      }
    }).localAddress(address)

  def bind(): Channel = { //绑定 Channel。注意，DatagramChannel 是无连接的
    bootstrap.bind.syncUninterruptibly.channel
  }

  def stop(): Unit = {
    group.shutdownGracefully
  }
}
