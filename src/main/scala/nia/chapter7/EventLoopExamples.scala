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

package nia.chapter7

import java.util.Collections
import collection.JavaConverters.asScalaBufferConverter

/**
 * 代码清单 7-1 在事件循环中执行任务
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
object EventLoopExamples {
  /**
   * 代码清单 7-1 在事件循环中执行任务
   */
  def executeTaskInEventLoop(): Unit = {
    val terminated = true
    //...
    while (!terminated) { //阻塞，直到有事件已经就绪可被运行
      val readyEvents = blockUntilEventsReady
      for (ev: Runnable ← readyEvents.asScala) { //循环遍历，并处理所有的事件
        ev.run()
      }
    }
  }

  private def blockUntilEventsReady = Collections.singletonList[Runnable](new Runnable() {
    override def run(): Unit = {
      try {
        Thread.sleep(1000)
      } catch {
        case e: InterruptedException ⇒
          e.printStackTrace()
      }
    }
  })
}
