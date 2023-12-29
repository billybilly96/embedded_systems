cmake_minimum_required(VERSION 3.12)
project(arduino)

set(CMAKE_CXX_STANDARD 14)

include_directories(smart_radar)

add_executable(arduino
        smart_radar/AngleIterator.cpp
        smart_radar/AngleIterator.h
        smart_radar/CommandReaderTask.cpp
        smart_radar/CommandReaderTask.h
        smart_radar/config.h
        smart_radar/Led.cpp
        smart_radar/Led.h
        smart_radar/LedExt.cpp
        smart_radar/LedExt.h
        smart_radar/Light.h
        smart_radar/LightExt.h
        smart_radar/Logger.cpp
        smart_radar/Logger.h
        smart_radar/MsgService.cpp
        smart_radar/MsgService.h
        smart_radar/Potentiometer.cpp
        smart_radar/Potentiometer.h
        smart_radar/ProximitySensor.h
        smart_radar/Radar.cpp
        smart_radar/Radar.h
        smart_radar/Runnable.h
        smart_radar/Scheduler.cpp
        smart_radar/Scheduler.h
        smart_radar/ServoTimer2.cpp
        smart_radar/ServoTimer2.h
        smart_radar/SharedContext.cpp
        smart_radar/SharedContext.h
        smart_radar/Sonar.cpp
        smart_radar/Sonar.h
        smart_radar/Task.h
        smart_radar/Timer.cpp
        smart_radar/Timer.h)
