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

package nia.chapter2.echoclient

import io.netty.buffer.{ ByteBuf, Unpooled }
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.{ ChannelHandlerContext, SimpleChannelInboundHandler }
import io.netty.util.CharsetUtil

@Sharable //标记该类的实例可以被多个 Channel 共享
class EchoClientHandler extends SimpleChannelInboundHandler[ByteBuf] {
  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    //当被通知 Channel是活跃的时候，发送一条消息
    ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8))
  }

  override def channelRead0(ctx: ChannelHandlerContext, in: ByteBuf): Unit = {
    //记录已接收消息的转储
    System.out.println("Client received: " + in.toString(CharsetUtil.UTF_8))
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    //在发生异常时，记录错误并关闭Channel
    cause.printStackTrace()
    ctx.close()
  }
}
