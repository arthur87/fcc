#include "Timer.h"
#include "<OUTPUT>.h"
 
module <OUTPUT>C @safe() {
  uses {
    interface Leds;
    interface Boot;
    interface Receive;
    interface AMSend;
    interface Timer<TMilli> as Loop;
    interface Packet;
    interface Read<uint16_t>;
    interface SplitControl as RadioControl;    
  }
}
implementation {
	message_t packet;
	uint16_t _Loop_start(uint16_t ms);
	uint16_t _Read_read();
	uint16_t _Send_send(uint16_t data1, uint16_t data2, uint16_t data3, uint16_t data4, uint16_t data5);
	uint16_t _Leds_led(uint16_t pin, uint16_t val);
		
	<MAIN_CODE>
	uint16_t _Loop_start(uint16_t ms)
	{
		call Loop.startPeriodic(ms);
		return 0;
	}
	uint16_t _Read_read()
	{
		call Read.read();
		return 0;
	}
	uint16_t _Send_send(uint16_t data1, uint16_t data2, uint16_t data3, uint16_t data4, uint16_t data5)
	{
		radio_sense_msg_t* rsm;
		rsm = (radio_sense_msg_t*)call Packet.getPayload(&packet, sizeof(radio_sense_msg_t));
		rsm->id = TOS_NODE_ID;
		rsm->data1 = data1;
		rsm->data1 = data2;
		rsm->data1 = data3;
		rsm->data1 = data4;
		rsm->data1 = data5;
		call AMSend.send(AM_BROADCAST_ADDR, &packet, sizeof(radio_sense_msg_t));
		return 0;
	}
	uint16_t _Leds_led(uint16_t pin, uint16_t val)
	{
		if(pin == 0 && val == 0) {
			call Leds.led0On();
		}else if(pin == 0 && val == 1) {
			call Leds.led0Off();
		}else if(pin == 0 && val == 2) {
			call Leds.led0Toggle();
		}else if(pin == 1 && val == 0) {
			call Leds.led1On();
		}else if(pin == 1 && val == 1) {
			call Leds.led1Off();
		}else if(pin == 1 && val == 2) {
			call Leds.led1Toggle();
		}else if(pin == 2 && val == 0) {
			call Leds.led2On();
		}else if(pin == 2 && val == 1) {
			call Leds.led2Off();
		}else if(pin == 2 && val == 2) {
			call Leds.led2Toggle();
		}
		return 0;
	}
	event void Boot.booted()
	{
		call RadioControl.start();
	}
	
	event void RadioControl.startDone(error_t _err)
	{
		if(_err == SUCCESS)
			/**
			 * entry point
			 */
			Setup();
	}
	event void RadioControl.stopDone(error_t _err)
	{
	}
	
	event void AMSend.sendDone(message_t* _bufPtr, error_t _err)
	{
	}
	
	/*
	event void Loop.fired() {
	
	}
	event void Read.readDone(error_t _result, uint16_t _data) {
	}
	event message_t* Receive.receive(message_t* _bufPtr, void* _payload, uint8_t _len) {
	}
	*/
}