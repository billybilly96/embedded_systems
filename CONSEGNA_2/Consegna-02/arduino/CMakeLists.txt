cmake_minimum_required(VERSION 3.12)
project(arduino)

set(CMAKE_CXX_STANDARD 14)

include_directories(smart_garage)

add_executable(arduino
        smart_garage/Button.h
        smart_garage/ButtonImpl.cpp
        smart_garage/ButtonImpl.h
        smart_garage/CommandReaderTask.cpp
        smart_garage/CommandReaderTask.h
        smart_garage/config.h
        smart_garage/GarageDoorTask.cpp
        smart_garage/GarageDoorTask.h
        smart_garage/Led.cpp
        smart_garage/Led.h
        smart_garage/LedExt.cpp
        smart_garage/LedExt.h
        smart_garage/Light.h
        smart_garage/LightExt.h
        smart_garage/MsgService.cpp
        smart_garage/MsgService.h
        smart_garage/ParkingAssistanceTask.cpp
        smart_garage/ParkingAssistanceTask.h
        smart_garage/Pir.cpp
        smart_garage/Pir.h
        smart_garage/PresenceSensor.h
        smart_garage/ProximitySensor.h
        smart_garage/Runnable.h
        smart_garage/Scheduler.cpp
        smart_garage/Scheduler.h
        smart_garage/SharedContext.cpp
        smart_garage/SharedContext.h
        smart_garage/Sonar.cpp
        smart_garage/Sonar.h
        smart_garage/Task.h
        smart_garage/Timer.cpp
        smart_garage/Timer.h)
