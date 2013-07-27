#include <WProgram.h>
int __digitalRead(int pin)
{
    pinMode(pin, INPUT);
    return digitalRead(pin);
}

void __digitalWrite(int pin, int value)
{
    pinMode(pin, OUTPUT);
    digitalWrite(pin, value);
}
void __main();

void __main()
{
Serial.begin(9600);
for(;;) {
int v =analogRead(0);
delay(1000);
Serial.print(v);
}
}


int main(void)
{
    init();
    __main();
    return 0;
}
