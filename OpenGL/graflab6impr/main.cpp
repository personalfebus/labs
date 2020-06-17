#include <GLFW/glfw3.h>
#include <iostream>
#include <time.h>
#include <fstream>
#include "declaration.h"

void keyCallback(GLFWwindow *window, int key, int scancode, int action, int mods);
void window_size_callback(GLFWwindow* window, int width, int height);
void DrawCube(GLfloat centerPosX, GLfloat centerPosY, GLfloat centerPosZ, GLfloat edgeLength, bool dynamic);
GLuint LoadTexture(const char *filename );

int SCREEN_WIDTH = 800;
int SCREEN_HEIGHT = 800;

GLfloat DrawCubsDepth = -700;
GLfloat DrawCubsHigh = 200;

GLfloat rotationX = 0.0f;
GLfloat rotationY = 0.0f;
GLfloat shiftX = 0.0f;
GLfloat shiftY = 0.0f;
GLfloat halfScreenWidth = SCREEN_WIDTH / 2;
GLfloat halfScreenHeight = SCREEN_HEIGHT / 2;
int dividers[100];
int div_count = 0;
int div_size = 0;
int drawMode = 0;
int AA = 3;
float BB = 2.0;
int z_cap = 101;
int z_step = 1;
float a_step = 0.1;
Parab *par = new Parab(AA, BB, z_cap, z_step, a_step);
GLfloat *ambientparam = new GLfloat[4];
GLboolean lclmdlvw = GL_FALSE;
GLboolean twoside = GL_TRUE;
bool lightEnable = false;
bool textureEnable = false;
int animationEnable = 0;

// 0 = no animation
// 1 = animation
// 2 = freeze the animation
// 3 = stop animation

//std::ifstream infile("D:\\_IT\\CLionProjects\\graflab6impr\\scene.txt");

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
    div_size = div_count;
    div_count = 0;

    GLFWwindow *window;

    if (!glfwInit()){
        return -1;
    }

    window = glfwCreateWindow( SCREEN_WIDTH, SCREEN_HEIGHT, "LAB 6", NULL, NULL );

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

    //LIGHTNING
    ambientparam[0] = 0.2;
    ambientparam[1] = 0.2;
    ambientparam[2] = 0.2;
    ambientparam[3] = 1.0;

//    GLfloat lightpos[] = {-1.0, 0, 0, 0};
//    GLfloat lightdir[] = {1.0, 0.0, 0.0};
//    glLightfv(GL_LIGHT0, GL_POSITION, lightpos);
//    glLightfv(GL_LIGHT0, GL_SPOT_DIRECTION, lightdir);

    glEnable(GL_DEPTH_TEST);
    glEnable(GL_NORMALIZE);
    glEnable(GL_COLOR_MATERIAL);

    //Глобальные параметры
    glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambientparam);
    glLightModelf(GL_LIGHT_MODEL_LOCAL_VIEWER, lclmdlvw); //GL_FALSE
    glLightModelf(GL_LIGHT_MODEL_TWO_SIDE, twoside); // GL_TRUE

    glViewport(0.0f, 0.0f, screenWidth, screenHeight);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
//    glLoadMatrixf(MAT);
    glOrtho(0, SCREEN_WIDTH, 0, SCREEN_HEIGHT, 0, 2000);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
    float approx_fps = 0;
    int cntr = 0;
//    glEnable(GL_TEXTURE_2D);
    GLuint texture = LoadTexture("D:\\_IT\\CLionProjects\\graflab6impr\\wood-texture1.bmp");
    while (!glfwWindowShouldClose(window)){
        double time1 = glfwGetTime();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // | GL_DEPTH_BUFFER_BIT
//        glLoadIdentity();

        if (textureEnable) {
            glEnable(GL_TEXTURE_2D);
        }
        glPushMatrix();
        glLoadIdentity();
        glTranslatef(halfScreenWidth + shiftX, halfScreenHeight + shiftY, -700);
        glRotatef(rotationX, 1, 0, 0);
        glRotatef(rotationY, 0, 1, 0);
        par->draw(textureEnable, twoside, animationEnable);
        if (animationEnable == 3){
            animationEnable = 0;
        }
        glTranslatef(-halfScreenWidth, -halfScreenHeight, 700);
        glPopMatrix();
        glDisable(GL_TEXTURE_2D);

        //STATIC CUBES
        glPushMatrix();
        glLoadIdentity();
        glTranslatef(50, 50, -700);
        glRotatef(30, 1, 0, 0);
        glRotatef(30, 0, 1, 0);
        glTranslatef(-50, -50, 700);
        DrawCube(50, 50, DrawCubsDepth, 50, false);
        glPopMatrix();

//        glPushMatrix();
//        glLoadIdentity();
//        glTranslatef(screenWidth - 50, 50, -700);
//        glRotatef(30, 1, 0, 0);
//        glRotatef(30, 0, 1, 0);
//        glTranslatef(50 - screenWidth, -50, 700);
//        DrawCube(screenWidth - 50, 50, DrawCubsDepth, 50, false);
//        glPopMatrix();

//        glPushMatrix();
//        glLoadIdentity();
//        glTranslatef(50, screenHeight - 50, -700);
//        glRotatef(30, 1, 0, 0);
//        glRotatef(30, 0, 1, 0);
//        glTranslatef(-50, 50 - screenHeight, 700);
//        DrawCube(50, screenHeight - 50, DrawCubsDepth, 50, false);
//        glPopMatrix();

//        glPushMatrix();
//        glLoadIdentity();
//        glTranslatef(screenWidth - 50, screenHeight - 50, -700);
//        glRotatef(30, 1, 0, 0);
//        glRotatef(30, 0, 1, 0);
//        glTranslatef(50 - screenWidth, 50 - screenHeight, 700);
//        DrawCube(screenWidth - 50, screenHeight - 50, DrawCubsDepth, 50, false);
//        glPopMatrix();

//        DrawCube(50, 50, DrawCubsDepth, 50, false);
//        DrawCube(screenWidth - 50, 50, DrawCubsDepth, 50, false);
//        DrawCube(50, screenHeight - 50, DrawCubsDepth, 50, false);
//        DrawCube(screenWidth - 50, screenHeight - 50, DrawCubsDepth, 50, false);

        double time2 = glfwGetTime();
        float curr_fps = 1/(time2 - time1);
        approx_fps = (approx_fps + curr_fps) / 2;
        if (cntr % 100 == 0) {
            std::cout << "time1 = " << time1 << ";" << "time2 = " << time2 << "; FPS = " << approx_fps  << " with z_cap = " << z_cap << '\n';
            approx_fps = 0;
        }
//        std::cout << "FPS = " << (int)(1/(time2 - time1)) << " with z_cap = " << z_cap << '\n';
        cntr++;

        glfwSwapBuffers(window);
        glfwPollEvents();
    }
    delete par;
    glfwTerminate();
    return 0;
}

GLuint LoadTexture(const char *filename ){
//    glClear(GL_COLOR_BUFFER_BIT);

    // Data read from the header of the BMP file
    unsigned char header[54]; // Each BMP file begins by a 54-bytes header
    unsigned int dataPos;     // Position in the file where the actual data begins
    unsigned int width, height;
    unsigned int imageSize;   // = width*height*3
// Actual RGB data
    unsigned char *data;

    // Open the file
    FILE * file = fopen(filename,"rb");
    if (!file){printf("Image could not be opened\n"); return 0;}

    if ( fread(header, 1, 54, file)!=54 ){ // If not 54 bytes read : problem
        printf("Not a correct BMP file\n");
        return false;
    }

    if ( header[0]!='B' || header[1]!='M' ){
        printf("Not a correct BMP file\n");
        return 0;
    }

    // Read ints from the byte array
    dataPos    = *(int*)&(header[0x0A]);
    imageSize  = *(int*)&(header[0x22]);
    width      = *(int*)&(header[0x12]);
    height     = *(int*)&(header[0x16]);

    // Some BMP files are misformatted, guess missing information
    if (imageSize==0)    imageSize=width*height*3; // 3 : one byte for each Red, Green and Blue component
    if (dataPos==0)      dataPos=54; // The BMP header is done that way

    // Create a buffer
    data = new unsigned char [imageSize];

// Read the actual data from the file into the buffer
    fread(data,1,imageSize,file);

//Everything is in memory now, the file can be closed
    fclose(file);
//    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    // Create one OpenGL texture
    GLuint textureID;
    glGenTextures(1, &textureID);

// "Bind" the newly created texture : all future texture functions will modify this texture
    glBindTexture(GL_TEXTURE_2D, textureID);

// Give the image to OpenGL
    glTexImage2D(GL_TEXTURE_2D, 0,GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);

//    glEnable(GL_TEXTURE_2D);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

    glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
    glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPLACE);
//    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
//    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);


//    glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_REPEAT);



//    glBindTexture(GL_TEXTURE_2D, textureID);
//    glEnd();
    delete[] data;
    return textureID;
}

void keyCallback(GLFWwindow *window, int key, int scancode, int action, int mods){
    const GLfloat rotationSpeed = 5;
    const GLfloat shiftingSpeed = 15;
    std::fstream infile("D:\\_IT\\CLionProjects\\graflab6impr\\scene.txt");
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
            case GLFW_KEY_1:
                z_cap *= 2;
                dividers[div_size] = z_cap;
                div_size++;
                par->remakePar(AA, BB, z_cap, z_step, a_step);
                break;
            case GLFW_KEY_2:
                z_cap /= 2;
                div_size--;
                dividers[div_size] = 0;
                par->remakePar(AA, BB, z_cap, z_step, a_step);
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
            case GLFW_KEY_Z:
                ambientparam[0] -= 0.05;
                ambientparam[1] -= 0.05;
                ambientparam[2] -= 0.05;
                std::cout << ambientparam[0] << "-" << std::endl;
                glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambientparam);
                break;
            case GLFW_KEY_X:
                ambientparam[0] += 0.05;
                ambientparam[1] += 0.05;
                ambientparam[2] += 0.05;
                glLightModelfv(GL_LIGHT_MODEL_AMBIENT, ambientparam);
                std::cout << ambientparam[0] << "+" << std::endl;
                break;
            case GLFW_KEY_C:
                if (lclmdlvw == GL_TRUE){
                    lclmdlvw = GL_FALSE;
                    glLightModelf(GL_LIGHT_MODEL_LOCAL_VIEWER, lclmdlvw); //GL_FALSE
                } else {
                    lclmdlvw = GL_TRUE;
                    glLightModelf(GL_LIGHT_MODEL_LOCAL_VIEWER, lclmdlvw); //GL_FALSE
                }
                break;
            case GLFW_KEY_V:
                if (twoside == GL_TRUE){
                    twoside = GL_FALSE;
                    glLightModelf(GL_LIGHT_MODEL_TWO_SIDE, twoside); // GL_TRUE
                } else {
                    twoside = GL_TRUE;
                    glLightModelf(GL_LIGHT_MODEL_TWO_SIDE, twoside); // GL_TRUE
                }
                break;
            case GLFW_KEY_E:
                if (lightEnable){
                    lightEnable = false;
                    glDisable(GL_LIGHT0);
                    glDisable(GL_LIGHTING);
                } else {
                    lightEnable = true;
                    glEnable(GL_LIGHTING);
                    glEnable(GL_LIGHT0);
                }
                break;
            case GLFW_KEY_T:
                if (textureEnable){
                    textureEnable = false;
                } else {
                    textureEnable = true;
                }
                break;
            case GLFW_KEY_O:
                if (animationEnable == 0){
                    animationEnable = 1;
                } else {
                    //par->stopAnimation;
                    animationEnable = 3;
                }
                break;
            case GLFW_KEY_P:
                if (animationEnable == 1){
                    animationEnable = 2;
                } else if (animationEnable == 2){
                    animationEnable = 1;
                }
                break;
            case GLFW_KEY_F:
                for( std::string line; getline( infile, line ); ){
                    int pos = line.find('=');
                    std::string val = line.substr(pos + 1);
                    std::string name = line.substr(0, pos);
//                    std::cout << "value =" << val << "|key =" << name << "|\n";

                    if (name == "lightEnable"){
                        int nig = val[0] - 48;
                        //std::cout << val << "\n";

                        if (nig) {
                            lightEnable = true;
                        } else {
                            lightEnable = false;
                        }

                        if (lightEnable){
                            glEnable(GL_LIGHTING);
                            glEnable(GL_LIGHT0);
                        } else {
                            glDisable(GL_LIGHT0);
                            glDisable(GL_LIGHTING);
                        }
                    } else if (name == "textureEnable"){
//                        std::cout << (bool)(val[0]);
//                        textureEnable = (val[0]);
                        int nig = val[0] - 48;
                        std::cout << val << "\n";

                        if (nig) {
                            textureEnable = true;
                        } else {
                            textureEnable = false;
                        }
                    } else if (name == "shiftX") {
                        unsigned int start = 0;
                        if (val[0] == '-') start++;
                        float num = 0;
                        for (unsigned int i = start; i < val.length(); i++){
                            num = num*10 + (int)val[i] - 48;
                        }
                        if (start) num *= -1;
                        shiftX = num;
//                        std::cout << num << std::endl;
                    } else if (name == "shiftY"){
                        unsigned int start = 0;
                        if (val[0] == '-') start++;
                        float num = 0;
                        for (unsigned int i = start; i < val.length(); i++){
                            num = num*10 + (int)val[i] - 48;
                        }
                        if (start) num *= -1;
                        shiftY = num;
                    } else if (name == "rotationX"){
                        unsigned int start = 0;
                        if (val[0] == '-') start++;
                        float num = 0;
                        for (unsigned int i = start; i < val.length(); i++){
                            num = num*10 + (int)val[i] - 48;
                        }
                        if (start) num *= -1;
                        rotationX = num;
                    } else if (name == "rotationY"){
                        unsigned int start = 0;
                        if (val[0] == '-') start++;
                        float num = 0;
                        for (unsigned int i = start; i < val.length(); i++){
                            num = num*10 + (int)val[i] - 48;
                        }
                        if (start) num *= -1;
                        rotationY = num;
                    } else if (name == "animationEnable"){
                        unsigned int start = 0;
                        if (val[0] == '-') start++;
                        float num = 0;
                        for (unsigned int i = start; i < val.length(); i++){
                            num = num*10 + (int)val[i] - 48;
                        }
                        if (start) num *= -1;
                        animationEnable = num;
                    }
                }
                break;
            case GLFW_KEY_G:
                infile.clear();
//                    rotationX = 0.0f;
//                    rotationY = 0.0f;
//                    shiftX = 0.0f;
//                    shiftY = 0.0f;
//                    lclmdlvw = GL_FALSE;
//                    twoside = GL_TRUE;
//                    lightEnable = false;
//                    textureEnable = false;
//                    animationEnable = 0;
                infile << "rotationX=" << rotationX << std::endl;
                infile << "rotationY=" << rotationY << std::endl;
                infile << "shiftX=" << shiftX << std::endl;
                infile << "shiftY=" << shiftY << std::endl;
                infile << "lightEnable=" << lightEnable << std::endl;
                infile << "textureEnable=" << textureEnable << std::endl;
                infile << "animationEnable=" << animationEnable << std::endl;
                infile.close();
                break;
            default:
                drawMode = (drawMode + 1) % 2;
                if (drawMode){
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                } else {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                }
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

void DrawCube(GLfloat centerPosX, GLfloat centerPosY, GLfloat centerPosZ, GLfloat edgeLength, bool dynamic){
    GLfloat halfSideLength = edgeLength * 0.5f;
    GLfloat vertices[] =
            {
                    // front face
                    centerPosX - halfSideLength, centerPosY + halfSideLength, centerPosZ + halfSideLength, // top left
                    centerPosX + halfSideLength, centerPosY + halfSideLength, centerPosZ + halfSideLength, // top right
                    centerPosX + halfSideLength, centerPosY - halfSideLength, centerPosZ + halfSideLength, // bottom right
                    centerPosX - halfSideLength, centerPosY - halfSideLength, centerPosZ + halfSideLength, // bottom left

                    // back face
                    centerPosX - halfSideLength, centerPosY + halfSideLength, centerPosZ - halfSideLength, // top left
                    centerPosX + halfSideLength, centerPosY + halfSideLength, centerPosZ - halfSideLength, // top right
                    centerPosX + halfSideLength, centerPosY - halfSideLength, centerPosZ - halfSideLength, // bottom right
                    centerPosX - halfSideLength, centerPosY - halfSideLength, centerPosZ - halfSideLength, // bottom left

                    // left face
                    centerPosX - halfSideLength, centerPosY + halfSideLength, centerPosZ + halfSideLength, // top left
                    centerPosX - halfSideLength, centerPosY + halfSideLength, centerPosZ - halfSideLength, // top right
                    centerPosX - halfSideLength, centerPosY - halfSideLength, centerPosZ - halfSideLength, // bottom right
                    centerPosX - halfSideLength, centerPosY - halfSideLength, centerPosZ + halfSideLength, // bottom left

                    // right face
                    centerPosX + halfSideLength, centerPosY + halfSideLength, centerPosZ + halfSideLength, // top left
                    centerPosX + halfSideLength, centerPosY + halfSideLength, centerPosZ - halfSideLength, // top right
                    centerPosX + halfSideLength, centerPosY - halfSideLength, centerPosZ - halfSideLength, // bottom right
                    centerPosX + halfSideLength, centerPosY - halfSideLength, centerPosZ + halfSideLength, // bottom left

                    // top face
                    centerPosX - halfSideLength, centerPosY + halfSideLength, centerPosZ + halfSideLength, // top left
                    centerPosX - halfSideLength, centerPosY + halfSideLength, centerPosZ - halfSideLength, // top right
                    centerPosX + halfSideLength, centerPosY + halfSideLength, centerPosZ - halfSideLength, // bottom right
                    centerPosX + halfSideLength, centerPosY + halfSideLength, centerPosZ + halfSideLength, // bottom left

                    // bottom face
                    centerPosX - halfSideLength, centerPosY - halfSideLength, centerPosZ + halfSideLength, // top left
                    centerPosX - halfSideLength, centerPosY - halfSideLength, centerPosZ - halfSideLength, // top right
                    centerPosX + halfSideLength, centerPosY - halfSideLength, centerPosZ - halfSideLength, // bottom right
                    centerPosX + halfSideLength, centerPosY - halfSideLength, centerPosZ + halfSideLength  // bottom left
            };

    glEnableClientState( GL_VERTEX_ARRAY );
    glVertexPointer(3, GL_FLOAT, 0, vertices);

    //1
    glColor3f(1.0, 0.0, 0.0);
    glNormal3f(0.0, 0.0, -1.0);
    glDrawArrays(GL_QUADS, 0, 4);


    //2
    glColor3f(0.0, 1.0, 0.0);
    glNormal3f(0.0, 0.0, 1.0);
    glDrawArrays(GL_QUADS, 4, 4);

    //3
    glColor3f(0.0, 0.0, 1.0);
    glNormal3f(-1.0, 0.0, 0.0);
    glDrawArrays(GL_QUADS, 8, 4);

    //4
    glColor3f(1.0, 1.0, 0.15);
    glNormal3f(1.0, 0.0, 0.0);
    glDrawArrays(GL_QUADS, 12, 4);

    //5
    glColor3f(1.0, 0.15, 0.777);
    glNormal3f(0.0, -1.0, 0.0);
    glDrawArrays(GL_QUADS, 16, 4);

    //6
    glColor3f(0.15, 1.0, 0.85);
    glNormal3f(0.0, 1.0, 0.0);
    glDrawArrays(GL_QUADS, 20, 4);

    glDisableClientState(GL_VERTEX_ARRAY);
}