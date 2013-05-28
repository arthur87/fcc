#include <WProgram.h>

int _Loop_start(float ms);
int _IO_digitalWrite(int pin, int val);
int _IO_digitalRead(int pin);
int _IO_analogWrite(int pin, int val);
int _IO_analogRead(int pin);

<MAIN_CODE>

int main(void)
{
    init();
    Setup();
    return 0;
}
int _Loop_start(float ms)
{
    while(1) {
        Loop();
        delay(ms);
    }
    return 0;
}
int _IO_digitalWrite(int pin, int val)
{
    pinMode(pin, OUTPUT);
    digitalWrite(pin, val);
    return 0;
}
int _IO_digitalRead(int pin)
{
    pinMode(pin, INPUT);
    return digitalRead(pin);
}
int _IO_analogWrite(int pin, int val)
{
    analogWrite(pin, val);
    return 0;
}
int _IO_analogRead(int pin)
{
    return analogRead(pin);
}