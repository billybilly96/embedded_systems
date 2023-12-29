#include "GarageDoorTask.h"
#include "Pir.h"
#include "LedExt.h"
#include "MsgService.h"


GarageDoorTask::GarageDoorTask(SharedContext *pContext, PresenceSensor *sensor, LightExt *light, Button* forceClose) {
  this->light = light;
  this->detector = sensor;
  this->pContext = pContext;
  this->forceClose = forceClose;
  //count = 0;
}

void GarageDoorTask::init(int period) {
  Task::init(period);
  currentIntensity = 0;
  state = OPEN_WAIT;
}

void GarageDoorTask::openDoor() {
  cmdOpen = true;
}

void GarageDoorTask::tick() {
  switch(state) {
      case OPEN_WAIT:
          
          if(cmdOpen ) {  
            //Serial.println("qui");          
            cmdOpen = false;
            pContext->setDoorClosed(false);
            light->switchOn();
            state = OPENING_DOOR;
          }
          break;
      case OPENING_DOOR:                 
          count++;
          currentIntensity += 12;
          light->setIntensity(currentIntensity);
          if (count > 20){
            //Serial.println("Trans_OD");
            count = 0;
            state = DOOR_OPEN;
          }
          break;
       case DOOR_OPEN:
          count++;
          if(detector->isDetected()) {
            //Serial.println("CAR_DETECTED");
            pContext->setCarAssistance(true);
            count = 0;
            MsgService.sendMsg("Welcome Home.");
            state = CAR_IN;
          }
          else if(count > 100) {
            count = 0;
            state = CLOSING_DOOR;
            MsgService.sendMsg(".::Car OnBoard PC::.");        
          }
          break;
        case CAR_IN:
          // Serial.println(state);
          if (forceClose->isPressed() && pContext->isDistanceOK()) {
         //   Serial.println("FORCE_CLOSE_OK");
            pContext->setDoorClosed(true);
            state = CLOSING_DOOR;
          }
          else if (pContext->isDoorClosed()) {
            state = CLOSING_DOOR;
          }
          break;
        case CLOSING_DOOR:
          //Serial.println("lillo");
          count++;
          currentIntensity -= 12;
          light->setIntensity(currentIntensity);
          if (count > 20) {
            light->switchOff();
            count = 0;
            state = OPEN_WAIT;
          }
          break;
  }
}

