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
import io.netty.channel.DummyChannelPipeline
import io.netty.util.CharsetUtil
import io.netty.channel.DummyChannelHandlerContext.DUMMY_INSTANCE

/**
 * Created by kerr.
 *
 * 代码清单 6-6 从 ChannelHandlerContext 访问 Channel
 *
 * 代码清单 6-7 通过 ChannelHandlerContext 访问 ChannelPipeline
 *
 * Listing 6.8 Calling ChannelHandlerContext write()
 */
object WriteHandlers {
  private val CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE = DUMMY_INSTANCE
  private val CHANNEL_PIPELINE_FROM_SOMEWHERE = DummyChannelPipeline.DUMMY_INSTANCE

  /**
   * 代码清单 6-6 从 ChannelHandlerContext 访问 Channel
   */
  def writeViaChannel(): Unit = {
    val ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE
    //get reference form somewhere
    //获取到与 ChannelHandlerContext相关联的 Channel 的引用
    val channel = ctx.channel
    //通过 Channel 写入缓冲区
    channel.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8))
  }

  /**
   * 代码清单 6-7 通过 ChannelHandlerContext 访问 ChannelPipeline
   */
  def writeViaChannelPipeline(): Unit = {
    val ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE
    //获取到与 ChannelHandlerContext相关联的 ChannelPipeline 的引用
    val pipeline = ctx.pipeline
    //通过 ChannelPipeline写入缓冲区
    pipeline.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8))
  }

  /**
   * 代码清单 6-8 调用 ChannelHandlerContext 的 write()方法
   */
  def writeViaChannelHandlerContext(): Unit = { //获取到 ChannelHandlerContext 的引用
    val ctx = CHANNEL_HANDLER_CONTEXT_FROM_SOMEWHERE //get reference form somewhere;
    //write()方法将把缓冲区数据发送到下一个 ChannelHandler
    ctx.write(Unpooled.copiedBuffer("Netty in Action", CharsetUtil.UTF_8))
  }
}
