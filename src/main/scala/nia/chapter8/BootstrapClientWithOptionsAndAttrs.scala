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

import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOption
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.AttributeKey
import java.net.InetSocketAddress
import java.lang.{ Boolean ⇒ JBoolean }

/**
 * 代码清单 8-7 使用属性值
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
class BootstrapClientWithOptionsAndAttrs {
  /**
   * 代码清单 8-7 使用属性值
   */
  def bootstrap(): Unit = { //创建一个 AttributeKey 以标识该属性
    val id: AttributeKey[Integer] = AttributeKey.newInstance("ID")
    //创建一个 Bootstrap 类的实例以创建客户端 Channel 并连接它们
    val bootstrap = new Bootstrap
    //设置 EventLoopGroup，其提供了用以处理 Channel 事件的 EventLoop
    bootstrap.group(new NioEventLoopGroup)
      .channel(classOf[NioSocketChannel])
      .handler(new SimpleChannelInboundHandler[ByteBuf]() {
        @throws[Exception]
        override def channelRegistered(ctx: ChannelHandlerContext): Unit = { //使用 AttributeKey 检索属性以及它的值
          val idValue = ctx.channel.attr(id).get
          // do something with the idValue
        }

        @throws[Exception]
        override protected def channelRead0(channelHandlerContext: ChannelHandlerContext, byteBuf: ByteBuf): Unit = {
          System.out.println("Received data")
        }
      })

    //设置 ChannelOption，其将在 connect()或者bind()方法被调用时被设置到已经创建的 Channel 上
    bootstrap
      .option[JBoolean](ChannelOption.SO_KEEPALIVE, true)
      .option[Integer](ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)

    //存储该 id 属性
    bootstrap.attr[Integer](id, 123456)

    //使用配置好的 Bootstrap 实例连接到远程主机
    val future = bootstrap.connect(new InetSocketAddress("www.manning.com", 80))
    future.syncUninterruptibly
  }
}
