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

import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

/**
 * 代码清单 6-11 @Sharable 的错误用法
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
//使用注解@Sharable标注
@Sharable
class UnsharableHandler extends ChannelInboundHandlerAdapter {
  private var count = 0

  override def channelRead(ctx: ChannelHandlerContext, msg: Any): Unit = {
    //将 count 字段的值加 1
    count += 1
    //记录方法调用，并转发给下一个ChannelHandler
    System.out.println("inboundBufferUpdated(...) called the " + count + " time")
    ctx.fireChannelRead(msg)
  }
}
