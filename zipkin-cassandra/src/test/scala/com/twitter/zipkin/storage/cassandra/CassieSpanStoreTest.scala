/*
 * Copyright 2012 Twitter Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twitter.zipkin.storage.cassandra

import com.twitter.app.App
import com.twitter.cassie.tests.util.FakeCassandra
import com.twitter.zipkin.storage.util.SpanStoreValidator
import com.twitter.zipkin.cassandra.CassieSpanStoreFactory
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CassieSpanStoreTest extends FunSuite {
  object FakeServer extends FakeCassandra
  FakeServer.start()

  object CassieStore extends App with CassieSpanStoreFactory
  CassieStore.main(Array("-zipkin.store.cassie.dest", "127.0.0.1:%d".format(FakeServer.port.get)))

  def newSpanStore = {
    FakeServer.reset()
    CassieStore.newCassandraStore()
  }

  test("validate") {
    // FakeCassandra doesn't honor sort order
    new SpanStoreValidator(newSpanStore, true).validate
  }
}
