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

import io.netty.channel.ChannelHandlerAdapter
import io.netty.channel.ChannelHandlerContext

/**
 * 代码清单 6-9 缓存到 ChannelHandlerContext 的引用
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
class WriteHandler extends ChannelHandlerAdapter {
  private var ctx: ChannelHandlerContext = _

  override def handlerAdded(ctx: ChannelHandlerContext): Unit = {
    //存储到 ChannelHandlerContext的引用以供稍后使用
    this.ctx = ctx
  }

  def send(msg: String): Unit = { //使用之前存储的到 ChannelHandlerContext的引用来发送消息
    ctx.writeAndFlush(msg)
  }
}
