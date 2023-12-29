#ifndef __SHAREDCONTEXT__
#define __SHAREDCONTEXT__

#include "ProximitySensor.h"

class SharedContext {
public:
  SharedContext();
  bool isStopped();
  bool isTracking();
  void setStopped(bool value);
  void setTracking(bool value);
  
private:
  volatile bool stopped = true;
  volatile bool tracking = false;
 };

#endif
