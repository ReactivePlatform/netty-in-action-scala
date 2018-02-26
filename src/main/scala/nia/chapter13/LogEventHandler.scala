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

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

/**
 * 代码清单 13-7 LogEventHandler
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
//扩展 SimpleChannelInboundHandler 以处理 LogEvent 消息
class LogEventHandler extends SimpleChannelInboundHandler[LogEvent] {
  @throws[Exception]
  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    //当异常发生时，打印栈跟踪信息，并关闭对应的 Channel
    cause.printStackTrace()
    ctx.close()
  }

  @throws[Exception]
  override def channelRead0(ctx: ChannelHandlerContext, event: LogEvent): Unit = {
    //打印 LogEvent 的数据
    println(event)
  }
}
