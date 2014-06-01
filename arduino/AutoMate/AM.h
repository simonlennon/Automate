#ifndef DHT_H
#define DHT_H
#if ARDUINO >= 100
 #include "Arduino.h"
#else
 #include "WProgram.h"
#endif

#define MASTERADDR {0,0,0,0} // The master controller
#define PONDADDR {1,1,1,1} // The pond slave working the pond pump and lights etc.

class AM {
 private:


 public:
 AM();
 void debug(String msg);
 void split(char delim, String str, String *str_array);
 void split(char delim, String str, String *str_array, int limit);
 
};
#endif
