#include <stdio.h>
#include "simple_functions.h"

int main(void) {
  printf("%d\n", GetCLangVersion());
  Person person = {
      .id= 1000000,
      .name= "bennyhuo",
      .age= 30
  };
  DumpPerson(&person);

  printf("%lld\n", sizeof(long long));
  printf("%lld\n", sizeof(int));
  return 0;
}
