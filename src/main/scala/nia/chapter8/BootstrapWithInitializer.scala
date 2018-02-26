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

package nia.chapter8

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import java.net.InetSocketAddress

/**
 * 代码清单 8-6 引导和使用 ChannelInitializer
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
class BootstrapWithInitializer {
  /**
   * 代码清单 8-6 引导和使用 ChannelInitializer
   */
  @throws[InterruptedException]
  def bootstrap(): Unit = {
    //创建 ServerBootstrap 以创建和绑定新的 Channel
    val bootstrap = new ServerBootstrap

    //设置 EventLoopGroup，其将提供用以处理 Channel 事件的 EventLoop
    bootstrap.group(new NioEventLoopGroup, new NioEventLoopGroup)
      .channel(classOf[NioServerSocketChannel]) //指定 Channel 的实现
      //注册一个 ChannelInitializerImpl 的实例来设置 ChannelPipeline
      .childHandler(new ChannelInitializerImpl)

    //绑定到地址
    val future = bootstrap.bind(new InetSocketAddress(8080))
    future.sync
  }

  //用以设置 ChannelPipeline 的自定义 ChannelInitializerImpl 实现
  final private[chapter8] class ChannelInitializerImpl extends ChannelInitializer[Channel] {
    //将所需的 ChannelHandler 添加到 ChannelPipeline
    @throws[Exception]
    override protected def initChannel(ch: Channel): Unit = {
      val pipeline = ch.pipeline
      pipeline.addLast(new HttpClientCodec)
      pipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE))
    }
  }
}

