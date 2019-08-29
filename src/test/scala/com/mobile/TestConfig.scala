package com.mobile

import com.typesafe.config.ConfigFactory

trait TestConfig extends Config{
  private val testConfig = ConfigFactory.load("test.conf")

  val baseUrl = testConfig.getString("test.url")
  val wsUrl = testConfig.getString("test.ws")
}
