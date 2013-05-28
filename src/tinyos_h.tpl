#ifndef APP_H
#define APP_H
 
typedef nx_struct radio_sense_msg {
	nx_uint16_t id;
	nx_uint16_t data1;
	nx_uint16_t data2;
	nx_uint16_t data3;
	nx_uint16_t data4;
	nx_uint16_t data5;
}radio_sense_msg_t;

enum {
  AM_RADIO_MSG = 7,
};
#endif