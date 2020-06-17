cmake_minimum_required(VERSION 3.15)
project(graflab6impr)

set(CMAKE_CXX_STANDARD 14)

add_executable(graflab6impr main.cpp declaration.h declaration.h)

include_directories(D:/_IT/glfw-3.3.2.bin.WIN32/include/GLFW)
find_package(OpenGL REQUIRED)
target_link_libraries(graflab6impr libglfw3.a OpenGL::GL)