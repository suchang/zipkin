/*
 * Copyright 2012 Twitter Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package com.twitter.zipkin.storm

import backtype.storm.spout.MultiScheme
import com.twitter.tormenta.spout._
import storm.kafka.KafkaConfig
import storm.kafka.trident.{OpaqueTridentKafkaSpout, TridentKafkaConfig}

/**
 * Storm spout to read spans from Kafka
 */
object ZipkinSpout {
  import Serialization._
  def getTridentSpanSpout(
      zkBroker: String,
      zkPath: String,
      kafkaTopic: String,
      scheme: MultiScheme,
      timeOffset: Int = -1) = {
    val kafkaConf =
      new TridentKafkaConfig(
        new KafkaConfig.ZkHosts(zkBroker, zkPath),
        kafkaTopic)
    kafkaConf.forceStartOffsetTime(timeOffset)
    kafkaConf.scheme = scheme
    new OpaqueTridentKafkaSpout(kafkaConf)
  }

  def getTormentaSpanSpout(
      zkHost: String,
      brokerZkPath: String,
      topic: String,
      appID: String,
      zkRoot: String,
      timeOffset: Int = -1) = {
    new KafkaSpout(
      new SpanInjectionScheme(),
      zkHost,
      brokerZkPath,
      topic,
      appID,
      zkRoot,
      timeOffset)
  }

}