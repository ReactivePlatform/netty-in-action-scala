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
import io.netty.channel.DummyChannelPipeline.DUMMY_INSTANCE

/**
 * 代码清单 6-5 修改 ChannelPipeline
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
object ModifyChannelPipeline {
  private val CHANNEL_PIPELINE_FROM_SOMEWHERE = DUMMY_INSTANCE

  /**
   * 代码清单 6-5 修改 ChannelPipeline
   */
  def modifyPipeline(): Unit = {
    val pipeline = CHANNEL_PIPELINE_FROM_SOMEWHERE
    // get reference to pipeline;
    //创建一个 FirstHandler 的实例
    val firstHandler = new FirstHandler
    //将该实例作为"handler1"添加到ChannelPipeline 中
    pipeline.addLast("handler1", firstHandler)
    //将一个 SecondHandler的实例作为"handler2"添加到 ChannelPipeline的第一个槽中。这意味着它将被放置在已有的"handler1"之前
    pipeline.addFirst("handler2", new SecondHandler)
    //将一个 ThirdHandler 的实例作为"handler3"添加到 ChannelPipeline 的最后一个槽中
    pipeline.addLast("handler3", new ThirdHandler)
    //...
    //通过名称移除"handler3"
    pipeline.remove("handler3")
    //通过引用移除FirstHandler（它是唯一的，所以不需要它的名称）
    pipeline.remove(firstHandler)
    //将 SecondHandler("handler2")替换为 FourthHandler:"handler4"
    pipeline.replace("handler2", "handler4", new FourthHandler)
  }

  final private class FirstHandler extends ChannelHandlerAdapter {}

  final private class SecondHandler extends ChannelHandlerAdapter {}

  final private class ThirdHandler extends ChannelHandlerAdapter {}

  final private class FourthHandler extends ChannelHandlerAdapter {}

}

