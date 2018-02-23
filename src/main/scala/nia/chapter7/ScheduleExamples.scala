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

import io.netty.channel.socket.nio.NioSocketChannel
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * 代码清单 7-2 使用 ScheduledExecutorService 调度任务
 *
 * 代码清单 7-3 使用 EventLoop 调度任务
 *
 * 代码清单 7-4 使用 EventLoop 调度周期性的任务
 *
 * 代码清单 7-5 使用 ScheduledFuture 取消任务
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
object ScheduleExamples {
  private val CHANNEL_FROM_SOMEWHERE = new NioSocketChannel

  /**
   * 代码清单 7-2 使用 ScheduledExecutorService 调度任务
   */
  def schedule(): Unit = { //创建一个其线程池具有 10 个线程的ScheduledExecutorService
    val executor = Executors.newScheduledThreadPool(10)
    val future = executor.schedule(
      new Runnable() {
      override def run(): Unit = {
        //该任务要打印的消息
        println("Now it is 60 seconds later")
      } //调度任务在从现在开始的 60 秒之后执行
    }, //创建一个 Runnable，以供调度稍后执行
      60, TimeUnit.SECONDS)
    //...
    //一旦调度任务执行完成，就关闭 ScheduledExecutorService 以释放资源
    executor.shutdown()
  }

  /**
   * 代码清单 7-3 使用 EventLoop 调度任务
   */
  def scheduleViaEventLoop(): Unit = {
    val ch = CHANNEL_FROM_SOMEWHERE
    // get reference from somewhere
    val future = ch.eventLoop.schedule(
      new Runnable() {
      override def run(): Unit = { //要执行的代码
        System.out.println("60 seconds later")
      }
    }, //创建一个 Runnable以供调度稍后执行
      60, TimeUnit.SECONDS)
  }

  /**
   * 代码清单 7-4 使用 EventLoop 调度周期性的任务
   */
  def scheduleFixedViaEventLoop(): Unit = {
    val ch = CHANNEL_FROM_SOMEWHERE
    val future = ch.eventLoop.scheduleAtFixedRate(new Runnable() {
      override def run(): Unit = { //这将一直运行，直到 ScheduledFuture 被取消
        System.out.println("Run every 60 seconds")
      } //调度在 60 秒之后，并且以后每间隔 60 秒运行
    }, 60, 60, TimeUnit.SECONDS)
  }

  /**
   * 代码清单 7-5 使用 ScheduledFuture 取消任务
   */
  def cancelingTaskUsingScheduledFuture(): Unit = {
    val ch = CHANNEL_FROM_SOMEWHERE
    //调度任务，并获得所返回的ScheduledFuture
    val future = ch.eventLoop.scheduleAtFixedRate(new Runnable() {
      override def run(): Unit = {
        System.out.println("Run every 60 seconds")
      }
    }, 60, 60, TimeUnit.SECONDS)
    // Some other code that runs...
    val mayInterruptIfRunning = false
    //取消该任务，防止它再次运行
    future.cancel(mayInterruptIfRunning)
  }
}
