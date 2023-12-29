#ifndef __RADAR__
#define __RADAR__

#include "Arduino.h"
#include "ServoTimer2.h"
#include "SharedContext.h"
#include "Task.h"
#include "Light.h"
#include "ProximitySensor.h"
#include "MsgService.h"
#include "Logger.h"
#include "AngleIterator.h"

#define ANGLE_TO_PULSE(x) (map(x, MIN_ANGLE, MAX_ANGLE, MIN_PULSE_WIDTH, MAX_PULSE_WIDTH))

class Radar: public Task {
  public:
    Radar(SharedContext *pContext);
    void init(int period);
    void tick();
  private:
    SharedContext* pContext;
    Light* ledConn;
    ProximitySensor* prox;
    ServoTimer2 servo;
    AngleIterator* iterator;
    int pos;
    enum {IDLE, SCANNING, TRACKING} state;
};
#endif
