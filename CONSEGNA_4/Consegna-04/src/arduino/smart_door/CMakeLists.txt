cmake_minimum_required(VERSION 3.12)
project(smart_door)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(smart_door
        BluetoothService.cpp
        BluetoothService.h
        Button.h
        ButtonImpl.cpp
        ButtonImpl.h
        CommandReaderTask.cpp
        CommandReaderTask.h
        config.h
        DHT.cpp
        DHT.h
        Door.h
        DoorImpl.cpp
        Led.cpp
        Led.h
        LedExt.cpp
        LedExt.h
        LedValueUpdateTask.cpp
        LedValueUpdateTask.h
        Light.h
        LightExt.h
        Logger.cpp
        Logger.h
        Msg.h
        MsgService.cpp
        MsgService.h
        Pir.cpp
        Pir.h
        PresenceSensor.h
        ProximitySensor.h
        Runnable.h
        Scheduler.cpp
        Scheduler.h
        ServoTimer2.cpp
        ServoTimer2.h
        SharedContext.cpp
        SharedContext.h
        Sonar.cpp
        Sonar.h
        Task.h
        TempSensor.cpp
        TempSensor.h
        TempUpdateTask.cpp
        TempUpdateTask.h
        Timer.cpp
        Timer.h)
