#include <stdio.h>
#include <unistd.h>


char msg[100] = "uninitialized";

void Child_Process() {
  printf("In child  process at %p: '%s'\n", &msg, msg);
  sleep(2);
  printf("In child  process at %p: '%s'\n", &msg, msg);
}

void Parent_Process() {
  sleep(1);
  strcpy(msg, "I'm a little teapot, short and stout");
  printf("In parent process at %p: '%s'\n", &msg, msg);
}

int main() {
  int pid, stat;

  pid = fork(); // Make a copy of myself and keep running

  if (pid != 0) {
    Child_Process();
  } else {
    Parent_Process();
  }

  wait(&stat);
}
