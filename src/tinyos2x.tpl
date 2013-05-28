<TINYOS_MAKE>
COMPONENT=<OUTPUT>AppC
include $(MAKERULES)
</TINYOS_MAKE>
<TINYOS_H>
#ifndef APP_H
#define APP_H

<MESSAGE_CODE>

enum {
  AM_RADIO_MSG = 7,
};
#endif
</TINYOS_H>
<TINYOS_APPC>
configuration <OUTPUT>AppC {}
implementation {
  components MainC, <OUTPUT>C as App, LedsC, new DemoSensorC() as Sensor;
  components new AMSenderC(AM_RADIO_MSG);
  components new AMReceiverC(AM_RADIO_MSG);
  components new TimerMilliC() as Timer0;
  components new TimerMilliC() as Timer1;
  components new TimerMilliC() as Timer2;
  components new TimerMilliC() as Timer3;
  components new TimerMilliC() as Timer4;

  components ActiveMessageC;
  
  App.Boot -> MainC.Boot;
  
  App.Receive -> AMReceiverC;
  App.AMSend -> AMSenderC;
  App.AMControl -> ActiveMessageC;
  App.Leds -> LedsC;
  App.Timer0 -> Timer0;
  App.Timer1 -> Timer1;
  App.Timer2 -> Timer2;
  App.Timer3 -> Timer3;
  App.Timer4 -> Timer4;
  App.Packet -> AMSenderC;
  App.Read -> Sensor;
}
</TINYOS_APPC>
<TINYOS_C>
#include "Timer.h"
#include "<OUTPUT>.h"
 
module <OUTPUT>C @safe() {
  uses {
    interface Leds;
    interface Boot;
    interface Receive;
    interface AMSend;
    interface Timer<TMilli> as Timer0;
    interface Timer<TMilli> as Timer1;
    interface Timer<TMilli> as Timer2;
    interface Timer<TMilli> as Timer3;
    interface Timer<TMilli> as Timer4;
    interface SplitControl as AMControl;
    interface Packet;
    interface Read<uint16_t>;
  }
}
implementation {
	message_t _packet;
	
	/**
	 * entry point
	 */
	<FUNC_CODE>
	<MAIN_CODE>
}
</TINYOS_C>