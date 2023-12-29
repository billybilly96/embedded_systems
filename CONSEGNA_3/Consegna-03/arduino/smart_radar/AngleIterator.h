#ifndef __ANGLEITERATOR__
#define __ANGLEITERATOR__

#include "config.h"

#define UPWARDS true
#define DOWNWARDS false

class AngleIterator {
  
  private:
    int initialAngle;
    int currentAngle;
    int minAngle;
    int maxAngle;
    bool direction;

   public:
     AngleIterator(int initialAngle, int minAngle, int maxAngle);
     int next();
     int reset();
     int current();
};


#endif
