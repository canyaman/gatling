#Gatling

## Setup
Install java8

## Configure

During 100 seconds ramping up 200 connection per second
Total 20k concurrent websocket connection

Set enviroment variables
```
export TEST_BASE_URL=http://localhost:8080
export TEST_WS_URL=ws://localhost:8080
export TEST_DURATION=100s
export TEST_USER=200
```

## Run

Run test
```console
./sbt gatling:test
```