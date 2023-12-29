#include "Radar.h"
#include "Led.h"
#include "Sonar.h"
#include "config.h"


Msg* msg;
//long time1;
//long time2;
Radar::Radar(SharedContext *pContext) {
  this->ledConn = new Led(L1_PIN);
  this->prox = new Sonar(SONAR_ECHO_PIN, SONAR_TRIG_PIN);
  this->iterator = new AngleIterator(INITIAL_ANGLE, MIN_ANGLE, MAX_ANGLE);
  this->pContext = pContext;
}

void Radar::init(int period){
  Task::init(period);
  pContext->setStopped(true);
  pContext->setTracking(false);
  state = IDLE;
  servo.attach(SERVO_PWM_PIN);
}

void Radar::tick() {
 // time1 = millis();
  switch(state) {
    case IDLE:
        if (!pContext->isStopped()) {
         // Serial.println("IDLE -> SCANNING");
          ledConn->switchOn();
          state = SCANNING;
        } 
      break;
      case SCANNING:
        if (pContext -> isTracking()) {
        //  Serial.println("SCANNING -> TRACKING");
          state = TRACKING;
        }
        else if(pContext->isStopped()) {
        //  Serial.println("SCANNING -> IDLE");
          servo.write(ANGLE_TO_PULSE(iterator->reset()));
          ledConn->switchOff();
          state = IDLE;
        }
        else {
          pos = iterator->next();
          servo.write(ANGLE_TO_PULSE(pos));              // tell servo to go to position in variable 'pos'
          MsgService.sendMsg(String(pos) + ";" + String(prox->getDistance()));
        }
        break;
      case TRACKING:
          if (!pContext->isTracking()) {
         //   Serial.println("TRACKING -> SCANNING");
            state = SCANNING;
          }
          else if(pContext->isStopped()) {
         //   Serial.println("TRACKING -> IDLE");
            pContext->setTracking(false);
            servo.write(ANGLE_TO_PULSE(iterator->reset()));
            ledConn->switchOff();
            state = IDLE;
          }
          else {
            MsgService.sendMsg(String(pos) + ";" + String(prox->getDistance()));
          }
          break;
      }
    //  time2 = millis();
    //  Serial.println(String(time2 - time1));
}


