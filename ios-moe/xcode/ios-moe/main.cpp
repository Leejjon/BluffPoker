#include <MOE/MOE.h>
//#include <string.h>
//#include <alloca.h>

int main(int argc, char *argv[]) {
//    char** argv2 = (char **)alloca(sizeof(char*) * (argc + 1));
//    argv2[0] = argv[0];
//    argv2[1] = strdup("-Xmx24m");
//
//    for (int i = 1; i < argc; ++i) argv2[i + 1] = argv[i];
    return moevm(argc, argv);
}
