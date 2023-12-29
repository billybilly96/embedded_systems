#ifndef __POTENTIOMETER__
#define __POTENTIOMETER__
class Potentiometer {
  public:
    Potentiometer(int potPin);
    int readValue();
  private:
    int potPin;
};
#endif
