#include <GLFW/glfw3.h>
#include <iostream>
#include <time.h>
#include "declaration.h"

void keyCallback(GLFWwindow *window, int key, int scancode, int action, int mods);
void window_size_callback(GLFWwindow* window, int width, int height);

int SCREEN_WIDTH = 800;
int SCREEN_HEIGHT = 800;
GLfloat rotationX = 0.0f;
GLfloat rotationY = 0.0f;
GLfloat shiftX = 0.0f;
GLfloat shiftY = 0.0f;
GLfloat halfScreenWidth = SCREEN_WIDTH / 2;
GLfloat halfScreenHeight = SCREEN_HEIGHT / 2;
int dividers[100];
int div_count = 0;
int drawMode = 0;
int AA = 3;
float BB = 2.0;
int z_cap = 101;
int z_step = 1;
float a_step = 0.1;
Parab *par = new Parab(AA, BB, z_cap, z_step, a_step);

//counter = 5
// 0123 4567 891011 12131415

GLfloat MAT[16] = {
        1, 0.0, 0.0, 0.8, //0.8
        0.0, 1, 0.0, 0.1, //0.1
        0.0, 0.0, 0.2, -0.5, //-0.5
        0.0, 0.0, 0.0, 1
};

int main(){
    for (int i = 0; i < 100; i++){
        dividers[i] = 0;
    }

    for (int i = 1; i < z_cap - 1; i++){
        if ((z_cap - 1) % i == 0) {
            dividers[div_count] = i;
//            std::cout << dividers[div_count] << std::endl;
            div_count++;
        }
    }

    div_count = 0;

    GLFWwindow *window;

    if (!glfwInit()){
        return -1;
    }

    window = glfwCreateWindow( SCREEN_WIDTH, SCREEN_HEIGHT, "LAB 3", NULL, NULL );

    glfwSetKeyCallback(window, keyCallback);
    glfwSetWindowSizeCallback(window, window_size_callback);
    glfwSetInputMode(window, GLFW_STICKY_KEYS, 1);

    int screenWidth, screenHeight;
    glfwGetFramebufferSize(window, &screenWidth, &screenHeight);

    if (!window){
        glfwTerminate();
        return -1;
    }

    glfwMakeContextCurrent(window);

    glEnable(GL_DEPTH_TEST);

    glViewport(0.0f, 0.0f, screenWidth, screenHeight);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
//    glLoadMatrixf(MAT);
    glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, 0, 2000);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();




    while (!glfwWindowShouldClose(window)){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // | GL_DEPTH_BUFFER_BIT
//        glLoadIdentity();

        glPushMatrix();
        glLoadIdentity();
        glTranslatef(halfScreenWidth + shiftX, halfScreenHeight + shiftY, -500);
        glRotatef(rotationX, 1, 0, 0);
        glRotatef(rotationY, 0, 1, 0);
        par->draw(drawMode);
        glTranslatef(-halfScreenWidth, -halfScreenHeight, 500);
        glPopMatrix();

        glfwSwapBuffers(window);
        glfwPollEvents();
    }
    delete par;
    glfwTerminate();
    return 0;
}



void keyCallback(GLFWwindow *window, int key, int scancode, int action, int mods){
    const GLfloat rotationSpeed = 5;
    const GLfloat shiftingSpeed = 15;
    if (action == GLFW_PRESS || action == GLFW_REPEAT){
        switch (key){
            case GLFW_KEY_UP:
                rotationX -= rotationSpeed;
                break;
            case GLFW_KEY_DOWN:
                rotationX += rotationSpeed;
                break;
            case GLFW_KEY_RIGHT:
                rotationY += rotationSpeed;
                break;
            case GLFW_KEY_LEFT:
                rotationY -= rotationSpeed;
                break;
            case GLFW_KEY_EQUAL:
                a_step += 0.1;
                par->remakePar(AA, BB, z_cap, z_step, a_step);
                break;
            case GLFW_KEY_MINUS:
                a_step -= 0.1;
                if (a_step < 0.1) a_step = 0.1;
                par->remakePar(AA, BB, z_cap, z_step, a_step);
                break;
            case GLFW_KEY_9:
                if (div_count > 0) {
                    div_count--;
                    z_step = dividers[div_count];
                    par->remakePar(AA, BB, z_cap, z_step, a_step);
                }
                break;
            case GLFW_KEY_0:
                if (dividers[div_count + 1] != 0) {
                    div_count++;
                    z_step = dividers[div_count];
                    par->remakePar(AA, BB, z_cap, z_step, a_step);
                }
                break;
            case GLFW_KEY_W:
                shiftY += shiftingSpeed;
                break;
            case GLFW_KEY_S:
                shiftY -= shiftingSpeed;
                break;
            case GLFW_KEY_A:
                shiftX -= shiftingSpeed;
                break;
            case GLFW_KEY_D:
                shiftX += shiftingSpeed;
                break;
            case GLFW_KEY_L:
                rotationX = 0;
                rotationY = 0;
                shiftX = 0;
                shiftY = 0;
                break;
            case GLFW_KEY_ESCAPE:
                glfwSetWindowShouldClose(window, GLFW_TRUE);
                break;
            default:
                drawMode = (drawMode + 1) % 2;
                //printf("%d\n", drawMode);
        }
    }
}


void window_size_callback(GLFWwindow* window, int width, int height){
    std::cout << "height = " << height << " == " << width << " = width\n";
    if (width == height) {
        std::cout << "square area\n";
        halfScreenHeight = height / 2;
        halfScreenWidth = width / 2;
        glViewport(0, 0, width, height);
    }
    halfScreenHeight = height / 2;
    halfScreenWidth = width / 2;
}