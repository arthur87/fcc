configuration <OUTPUT>AppC {}
implementation {
  components MainC, <OUTPUT>C as App, LedsC, new DemoSensorC() as Sensor;
  components new AMSenderC(AM_RADIO_MSG);
  components new AMReceiverC(AM_RADIO_MSG);
  components new TimerMilliC();

  components ActiveMessageC;
  
  App.Boot -> MainC.Boot;
  
  App.Receive -> AMReceiverC;
  App.AMSend -> AMSenderC;
  App.RadioControl -> ActiveMessageC;
  App.Leds -> LedsC;
  App.Loop -> TimerMilliC;
  App.Packet -> AMSenderC;
  App.Read -> Sensor;
}