#include "ServoTimer2.h"
#include "CommandReaderTask.h"
#include "Radar.h"
#include "Scheduler.h"
#include "MsgService.h"
#include "Potentiometer.h"
#include "config.h"

#define ANGULAR_VELOCITY(x) (BASE_ANGULAR_VELOCITY * map(x, 0, 1023, 8, 1))

Scheduler sched;

void setup() {
  
  sched.init(12);
  SharedContext* pContext = new SharedContext();
  Potentiometer* pot = new Potentiometer(POT_PIN);
  MsgService.init();
  
  Radar* radar = new Radar(pContext);
  radar->init(ANGULAR_VELOCITY(pot->readValue()));
  sched.addTask(radar);
  
  CommandReaderTask* reader = new CommandReaderTask(pContext);
  reader->init(12);
  sched.addTask(reader);
}

void loop() {
  sched.schedule();
}
