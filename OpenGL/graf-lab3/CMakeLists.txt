cmake_minimum_required(VERSION 3.15)
project(graf_lab3)

set(CMAKE_CXX_STANDARD 14)

add_executable(graf_lab3 main.cpp linmath.h declaration.h)

include_directories(D:/_IT/glfw-3.3.2.bin.WIN32/include/GLFW)
find_package(OpenGL REQUIRED)
target_link_libraries(graf_lab3 libglfw3.a OpenGL::GL)