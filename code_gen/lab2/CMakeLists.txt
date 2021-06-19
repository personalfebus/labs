cmake_minimum_required(VERSION 3.17)
project(opt_lab2)

set(CMAKE_CXX_STANDARD 14)

add_executable(opt_lab2 main.cpp)

find_package(LLVM REQUIRED CONFIG)
find_package(ZLIB REQUIRED)
include_directories(${LLVM_INCLUDE_DIRS})
add_definitions(${LLVM_DEFINITIONS})

llvm_map_components_to_libnames(llvm_libs support core irreader)

target_link_libraries(opt_lab2 ${llvm_libs})



