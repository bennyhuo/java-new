cmake_minimum_required(VERSION 3.15)
project(SimpleNativeLibrary)

set(CMAKE_C_STANDARD 11)

set(CMAKE_INSTALL_PREFIX "${CMAKE_SOURCE_DIR}/../lib")

if (MSVC)
    set(LIBNAME libsimple)
else ()
    set(LIBNAME simple)
endif ()

add_executable(simple_library main.c simple_functions.c)

add_library(${LIBNAME} SHARED simple_functions.c)

install(TARGETS ${LIBNAME}
        RUNTIME DESTINATION bin)
