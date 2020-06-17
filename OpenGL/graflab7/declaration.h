//
// Created by gleb8 on 4/15/2020.
//
#include <GLFW/glfw3.h>
#include <cmath>

class Parab{
public:
    int AA;
    int BB;
    float tt = 0;
    float tt_step = 0.01;
    int z_cap;
    int z_step;
    int z_count;
    int angle_count;
    float anglestep;
    float *sinuses;
    float *cosinuses;
    float **verticies;
    float **normals;
    float **start_verticies;
    bool first_iterration = true;
    GLuint texlst;
    GLuint lightlst;
    GLuint simplelst;
    explicit Parab(int a, int b, int zc, int zs, float as);
    void draw(bool textureEnable, GLboolean ts, int animationEnable, bool lightningEnable);
    void remakePar(int a, int b, int zc, int zs, float as);
    virtual ~Parab();
};
//
//Parab::Parab(int a, int b, int zc, int zs, float as) {
//    AA = a;
//    BB = b;
//    z_cap = zc;
//    z_step = zs;
//    anglestep = as;
//
//    angle_count = (int)(M_PI * 2 / anglestep);
//    z_count = (int)(z_cap / z_step);
//
//    verticies = new float*[3*angle_count];
//    for (int i = 0; i < 3*angle_count; i++){
//        verticies[i] = new float[z_count];
//    }
//
//    start_verticies = new float*[3*angle_count];
//    for (int i = 0; i < 3*angle_count; i++){
//        start_verticies[i] = new float[z_count];
//    }
//
//    normals = new float*[3*angle_count];
//    for (int i = 0; i < 3*angle_count; i++){
//        normals[i] = new float[z_count];
//    }
//
//    sinuses = new float[angle_count];
//    cosinuses = new float[angle_count];
//
//
//    for (int i = 0; i < z_count; i++){
//        for (int j = 0; j < 3*angle_count; j++){
//            verticies[j][i] = 0;
//            normals[j][i] = 0;
//            start_verticies[j][i] = 0;
//        }
//    }
//
//    float current_angle = 0;
//    for (int i = 0; i < angle_count; i++){
//        sinuses[i] = sinf(current_angle);
//        cosinuses[i] = cosf(current_angle);
////        std::cout << "angle = " << current_angle << "; sin = " << sinuses[i] << "; cos = " << cosinuses[i] << ";\n";
//        current_angle += anglestep;
//    }
//
//    //x = a * cos(t)
//    //y = b * sin(t)
//    float z = z_step;
//    for (int i = 1; i < z_count; i++){
//        //std::cout << "\nZ = " << z << std::endl;
//        float current_BB = BB * sqrtf(z);
//        float current_AA = AA * sqrtf(z);
//        int k = 0;
//
//        for (int j = 0; j < angle_count; j++){
//            verticies[k][i] = current_AA * cosinuses[j];
//            k++;
//            verticies[k][i] = current_BB * sinuses[j];
//            k++;
//            verticies[k][i] = z;
//            k++;
////            std::cout << "Vertex(" << j << "): x = " << verticies[k - 3][i] << "; y = " << verticies[k - 2][i]
////                      << "; z = " << verticies[k - 1][i] << std::endl;
//        }
//
//        z += z_step;
//    }
//
//    //новый цикл для нормалей :)
//    int start = 1;
//
//    for (int i = start; i < z_count - 1; i++){
//        for (int j = 0; j < 3*angle_count; j += 3){
//            if (j >= 3*angle_count - 3){
//                //нормаль в вершине vertex1
//                //A = vertex2 - vertex1
//                //B = vertex4 - vertex1
//                //нормальХ = Ay*Bz - By*Az;
//                //нормальУ = Az*Bx - Bz*Ax;
//                //нормальZ = Ax*By - Bx*Ay;
//                GLfloat Ax = (10 * verticies[j][i + 1]) - (10 * verticies[j][i]);
//                GLfloat Ay = (10 * verticies[j + 1][i + 1]) - (10 * verticies[j + 1][i]);
//                GLfloat Az = (10 * verticies[j + 2][i + 1]) - (10 * verticies[j + 2][i]);
//
//                GLfloat Bx = (10 * verticies[0][i]) - (10 * verticies[j][i]);
//                GLfloat By = (10 * verticies[1][i]) - (10 * verticies[j + 1][i]);
//                GLfloat Bz = (10 * verticies[2][i]) - (10 * verticies[j + 2][i]);
//
//                normals[j][i] = Ay*Bz - By*Az;
//                normals[j + 1][i] = Az*Bx - Bz*Ax;
//                normals[j + 2][i] = Ax*By - Bx*Ay;
//
//                //Изменение ориентации нормалей
//                normals[j][i] *= -1;
//                normals[j + 1][i] *= -1;
//                normals[j + 2][i] *= -1;
//                //std::cout << "Normal = " << "{" << normals[j][i] << ", " << normals[j + 1][i] << ", " << normals[j + 2][i] << "}\n";
//            } else {
//                //нормаль в вершине vertex1
//                //A = vertex2 - vertex1
//                //B = vertex4 - vertex1
//                //нормальХ = Ay*Bz - By*Az;
//                //нормальУ = Az*Bx - Bz*Ax;
//                //нормальZ = Ax*By - Bx*Ay;
//                GLfloat Ax = (10 * verticies[j][i + 1]) - (10 * verticies[j][i]);
//                GLfloat Ay = (10 * verticies[j + 1][i + 1]) - (10 * verticies[j + 1][i]);
//                GLfloat Az = (10 * verticies[j + 2][i + 1]) - (10 * verticies[j + 2][i]);
//
//                GLfloat Bx = (10 * verticies[j + 3][i]) - (10 * verticies[j][i]);
//                GLfloat By = (10 * verticies[j + 4][i]) - (10 * verticies[j + 1][i]);
//                GLfloat Bz = (10 * verticies[j + 5][i]) - (10 * verticies[j + 2][i]);
//
//                normals[j][i] = Ay*Bz - By*Az;
//                normals[j + 1][i] = Az*Bx - Bz*Ax;
//                normals[j + 2][i] = Ax*By - Bx*Ay;
//
//                //Изменение ориентации нормалей
//                normals[j][i] *= -1;
//                normals[j + 1][i] *= -1;
//                normals[j + 2][i] *= -1;
//                //std::cout << "Normal = " << "{" << normals[j][i] << ", " << normals[j + 1][i] << ", " << normals[j + 2][i] << "}\n";
//            }
//        }
//    }
//
//    for (int i = 0; i < z_count; i++) {
//        for (int j = 0; j < 3 * angle_count; j++) {
//            start_verticies[j][i] = verticies[j][i];
//        }
//    }
//
////    glNewList(texlst, GL_COMPILE_AND_EXECUTE);
////    glBegin(GL_QUADS);
////    glTexCoord2f(0.0, 0.0);
////    glVertex3f(500, 500, 0);
////    glTexCoord2f(1.0, 0.0);
////    glVertex3f(600, 500, 0);
////    glTexCoord2f(1.0, 1.0);
////    glVertex3f(600, 600, 0);
////    glTexCoord2f(0.0, 1.0);
////    glVertex3f(500, 600, 0);
////    glEnd();
////    float textureStep = 1.0f / angle_count;
////    float currentStep = 0;
////
////    for (int j = 0; j < 3 * angle_count; j += 3) {
////        glBegin(GL_POLYGON);
////        if (j >= 3 * angle_count - 3) {
////            glTexCoord2f(currentStep, 1.0f);
////            glVertex3f(10*verticies[j][start], 10*verticies[j + 1][start], 10*verticies[j + 2][start] - 700);
////
////            glTexCoord2f(0.5, 0.0f);
////            glVertex3f(0, 0, 0 - 700);
////
////            glTexCoord2f(currentStep + textureStep, 1.0f);
////            glVertex3f(10*verticies[0][start], 10*verticies[1][start], 10*verticies[2][start] - 700);
////        } else {
//////                glNormal3f(normals[j][start], normals[j + 1][start], normals[j + 2][start]);
////            glTexCoord2f(currentStep, 1.0f);
////            glVertex3f(10*verticies[j][start], 10*verticies[j + 1][start], 10*verticies[j + 2][start] - 700);
////
//////                glNormal3f(0, 0, -1);
////            glTexCoord2f(0.5, 0.0f);
////            glVertex3f(0, 0, 0 - 700);
////
//////                glNormal3f(normals[j + 3][start], normals[j + 4][start], normals[j + 5][start]);
////            glTexCoord2f(currentStep + textureStep, 1.0f);
////            glVertex3f(10*verticies[j + 3][start], 10*verticies[j + 4][start], 10*verticies[j + 5][start] - 700);
////        }
////        currentStep += textureStep;
////        glEnd();
////    }
////    currentStep = 0;
////    for (int i = start; i < z_count - 1; i++) {
////        for (int j = 0; j < 3 * angle_count; j += 3) {
////            glBegin(GL_POLYGON);
////            if (j >= 3 * angle_count - 3) {
//////                    glNormal3f(normals[j][i], normals[j + 1][i], normals[j + 2][i]);
////                glTexCoord2f(currentStep, 0.0f);
////                glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 700);
////
//////                    glNormal3f(normals[0][i], normals[1][i], normals[2][i]);
////                glTexCoord2f(currentStep + textureStep, 0.0f);
////                glVertex3f(10 * verticies[0][i], 10 * verticies[1][i], 10 * verticies[2][i] - 700);
////
//////                    glNormal3f(normals[0][i + 1], normals[1][i + 1], normals[2][i + 1]);
////                glTexCoord2f(currentStep + textureStep, 1.0f);
////                glVertex3f(10 * verticies[0][i + 1], 10 * verticies[1][i + 1], 10 * verticies[2][i + 1] - 700);
////
//////                    glNormal3f(normals[j][i + 1], normals[j + 1][i + 1], normals[j + 2][i + 1]);
////                glTexCoord2f(currentStep, 1.0f);
////                glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1], 10 * verticies[j + 2][i + 1] - 700);
////            } else {
//////                    glNormal3f(normals[j][i], normals[j + 1][i], normals[j + 2][i]);
////                glTexCoord2f(currentStep, 0.0f);
////                glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 700);
////
//////                    glNormal3f(normals[j + 3][i], normals[j + 4][i], normals[j + 5][i]);
////                glTexCoord2f(currentStep + textureStep, 0.0f);
////                glVertex3f(10 * verticies[j + 3][i], 10 * verticies[j + 4][i], 10 * verticies[j + 5][i] - 700);
////
//////                    glNormal3f(normals[j + 3][i + 1], normals[j + 4][i + 1], normals[j + 5][i + 1]);
////                glTexCoord2f(currentStep + textureStep, 1.0f);
////                glVertex3f(10 * verticies[j + 3][i + 1], 10 * verticies[j + 4][i + 1],
////                           10 * verticies[j + 5][i + 1] - 700);
////
//////                    glNormal3f(normals[j][i + 1], normals[j + 1][i + 1], normals[j + 2][i + 1]);
////                glTexCoord2f(currentStep, 1.0f);
////                glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1],
////                           10 * verticies[j + 2][i + 1] - 700);
////            }
////            currentStep += textureStep;
////            glEnd();
////        }
////    }
////
////    glBegin(GL_POLYGON);
////    for (int j = 0; j < 3 * angle_count; j += 3) {
////        glTexCoord2f(cosinuses[j / 3], sinuses[j / 3]);
////        glVertex3f(10 * verticies[j][z_count - 1], 10 * verticies[j + 1][z_count - 1], 10 * verticies[j + 2][z_count - 1] - 700);
////    }
////    glEnd();
//   // glEndList();
//
////    glNewList(1, GL_COMPILE_AND_EXECUTE);
////
////    for (int j = 0; j < 3*angle_count; j += 3){
////        glBegin(GL_POLYGON);
////        if (j >= 3*angle_count - 3){
////            glNormal3f(normals[j][start], normals[j + 1][start], normals[j + 2][start]);
////            glVertex3f(10 * verticies[j][start], 10 * verticies[j + 1][start], 10 * verticies[j + 2][start] - 700);
////            glNormal3f(0, 0, -1);
////            glVertex3f(0, 0, 0 - 700);
////            glNormal3f(normals[0][start], normals[1][start], normals[2][start]);
////            glVertex3f(10 * verticies[0][start], 10 * verticies[1][start], 10 * verticies[2][start] - 700);
////        } else {
////            glNormal3f(normals[j][start], normals[j + 1][start], normals[j + 2][start]);
////            glVertex3f(10 * verticies[j][start], 10 * verticies[j + 1][start], 10 * verticies[j + 2][start] - 700);
////            glNormal3f(0, 0, -1);
////            glVertex3f(0, 0, 0 - 700);
////            glNormal3f(normals[j + 3][start], normals[j + 4][start], normals[j + 5][start]);
////            glVertex3f(10 * verticies[j + 3][start], 10 * verticies[j + 4][start],
////                       10 * verticies[j + 5][start] - 700);
////        }
////        glEnd();
////    }
////
////    for (int i = start; i < z_count - 1; i++){
////        for (int j = 0; j < 3*angle_count; j += 3){
////            glBegin(GL_POLYGON);
////            if (j >= 3*angle_count - 3){
////                if (i < z_count - 2) {
////                    glNormal3f(normals[j][i], normals[j + 1][i], normals[j + 2][i]);
////                    glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 700);
////                    glNormal3f(normals[0][i], normals[1][i], normals[2][i]);
////                    glVertex3f(10 * verticies[0][i], 10 * verticies[1][i], 10 * verticies[2][i] - 700);
////                    glNormal3f(normals[0][i + 1], normals[1][i + 1], normals[2][i + 1]);
////                    glVertex3f(10 * verticies[0][i + 1], 10 * verticies[1][i + 1], 10 * verticies[2][i + 1] - 700);
////                    glNormal3f(normals[j][i + 1], normals[j + 1][i + 1], normals[j + 2][i + 1]);
////                    glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1],
////                               10 * verticies[j + 2][i + 1] - 700);
////                } else {
////                    glNormal3f(normals[j][i], normals[j + 1][i], normals[j + 2][i]);
////                    glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 700);
////                    glNormal3f(normals[0][i], normals[1][i], normals[2][i]);
////                    glVertex3f(10 * verticies[0][i], 10 * verticies[1][i], 10 * verticies[2][i] - 700);
////                    glVertex3f(10 * verticies[0][i + 1], 10 * verticies[1][i + 1], 10 * verticies[2][i + 1] - 700);
////                    glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1],
////                               10 * verticies[j + 2][i + 1] - 700);
////                }
////            } else {
////                if (i < z_count - 2){
////                    glNormal3f(normals[j][i], normals[j + 1][i], normals[j + 2][i]);
////                    glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 700);
////                    glNormal3f(normals[j + 3][i], normals[j + 4][i], normals[j + 5][i]);
////                    glVertex3f(10 * verticies[j + 3][i], 10 * verticies[j + 4][i], 10 * verticies[j + 5][i] - 700);
////                    glNormal3f(normals[j + 3][i + 1], normals[j + 4][i + 1], normals[j + 5][i + 1]);
////                    glVertex3f(10 * verticies[j + 3][i + 1], 10 * verticies[j + 4][i + 1],
////                               10 * verticies[j + 5][i + 1] - 700);
////                    glNormal3f(normals[j][i + 1], normals[j + 1][i + 1], normals[j + 2][i + 1]);
////                    glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1],
////                               10 * verticies[j + 2][i + 1] - 700);
////                } else {
////                    glNormal3f(normals[j][i], normals[j + 1][i], normals[j + 2][i]);
////                    glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 700);
////                    glNormal3f(normals[j + 3][i], normals[j + 4][i], normals[j + 5][i]);
////                    glVertex3f(10 * verticies[j + 3][i], 10 * verticies[j + 4][i], 10 * verticies[j + 5][i] - 700);
////                    glVertex3f(10 * verticies[j + 3][i + 1], 10 * verticies[j + 4][i + 1],
////                               10 * verticies[j + 5][i + 1] - 700);
////                    glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1],
////                               10 * verticies[j + 2][i + 1] - 700);
////                }
////            }
////            glEnd();
////        }
////    }
////
////
////    for (int i = start; i < z_count; i++) {
////        glBegin(GL_POLYGON);
////        for (int j = 0; j < 3 * angle_count; j += 3) {
////            glNormal3f(0, 0, 1);
////            glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][z_count - 1] - 700);
////        }
////        glEnd();
////    }
////
////    glEndList();
//}
//
//Parab::~Parab() {
//    for (int i = 0; i < 3*angle_count; i++){
//        delete[] verticies[i];
//    }
//    delete[] verticies;
//    for (int i = 0; i < 3*angle_count; i++){
//        delete[] normals[i];
//    }
//    delete[] normals;
//    for (int i = 0; i < 3*angle_count; i++){
//        delete[] start_verticies[i];
//    }
//    delete[] start_verticies;
//    delete[] sinuses;
//    delete[] cosinuses;
//}
//
//void Parab::remakePar(int a, int b, int zc, int zs, float as) {
//
//    for (int i = 0; i < 3*angle_count; i++){
//        delete[] verticies[i];
//    }
//    delete[] verticies;
//    for (int i = 0; i < 3*angle_count; i++){
//        delete[] normals[i];
//    }
//    delete[] normals;
//    for (int i = 0; i < 3*angle_count; i++){
//        delete[] start_verticies[i];
//    }
//    delete[] start_verticies;
//
//    delete[] sinuses;
//    delete[] cosinuses;
//
//    AA = a;
//    BB = b;
//    z_cap = zc;
//    z_step = zs;
//    anglestep = as;
//
//    angle_count = (int)(M_PI * 2 / anglestep);
//    z_count = (int)(z_cap / z_step);
//
//    verticies = new float*[3*angle_count];
//    for (int i = 0; i < 3*angle_count; i++){
//        verticies[i] = new float[z_count];
//    }
//
//    start_verticies = new float*[3*angle_count];
//    for (int i = 0; i < 3*angle_count; i++){
//        start_verticies[i] = new float[z_count];
//    }
//
//    normals = new float*[3*angle_count];
//    for (int i = 0; i < 3*angle_count; i++){
//        normals[i] = new float[z_count];
//    }
//
//    sinuses = new float[angle_count];
//    cosinuses = new float[angle_count];
//
//
//    for (int i = 0; i < z_count; i++){
//        for (int j = 0; j < 3*angle_count; j++){
//            verticies[j][i] = 0;
//            normals[j][i] = 0;
//            start_verticies[j][i] = 0;
//        }
//    }
//
//    float current_angle = 0;
//    for (int i = 0; i < angle_count; i++){
//        sinuses[i] = sinf(current_angle);
//        cosinuses[i] = cosf(current_angle);
////        std::cout << "angle = " << current_angle << "; sin = " << sinuses[i] << "; cos = " << cosinuses[i] << ";\n";
//        current_angle += anglestep;
//    }
//
//    //x = a * cos(t)
//    //y = b * sin(t)
//    float z = z_step;
//    for (int i = 1; i < z_count; i++){
//        //std::cout << "\nZ = " << z << std::endl;
//        float current_BB = BB * sqrtf(z);
//        float current_AA = AA * sqrtf(z);
//        int k = 0;
//
//        for (int j = 0; j < angle_count; j++){
//            verticies[k][i] = current_AA * cosinuses[j];
//            k++;
//            verticies[k][i] = current_BB * sinuses[j];
//            k++;
//            verticies[k][i] = z;
//            k++;
////            std::cout << "Vertex(" << j << "): x = " << verticies[k - 3][i] << "; y = " << verticies[k - 2][i]
////                      << "; z = " << verticies[k - 1][i] << std::endl;
//        }
//
//        z += z_step;
//    }
//
//    //новый цикл для нормалей :)
//    int start = 1;
//
//    for (int i = start; i < z_count - 1; i++){
//        for (int j = 0; j < 3*angle_count; j += 3){
//            if (j >= 3*angle_count - 3){
//                //нормаль в вершине vertex1
//                //A = vertex2 - vertex1
//                //B = vertex4 - vertex1
//                //нормальХ = Ay*Bz - By*Az;
//                //нормальУ = Az*Bx - Bz*Ax;
//                //нормальZ = Ax*By - Bx*Ay;
//                GLfloat Ax = (10 * verticies[j][i + 1]) - (10 * verticies[j][i]);
//                GLfloat Ay = (10 * verticies[j + 1][i + 1]) - (10 * verticies[j + 1][i]);
//                GLfloat Az = (10 * verticies[j + 2][i + 1]) - (10 * verticies[j + 2][i]);
//
//                GLfloat Bx = (10 * verticies[0][i]) - (10 * verticies[j][i]);
//                GLfloat By = (10 * verticies[1][i]) - (10 * verticies[j + 1][i]);
//                GLfloat Bz = (10 * verticies[2][i]) - (10 * verticies[j + 2][i]);
//
//                normals[j][i] = Ay*Bz - By*Az;
//                normals[j + 1][i] = Az*Bx - Bz*Ax;
//                normals[j + 2][i] = Ax*By - Bx*Ay;
//
//                //Изменение ориентации нормалей
//                normals[j][i] *= -1;
//                normals[j + 1][i] *= -1;
//                normals[j + 2][i] *= -1;
//                //std::cout << "Normal = " << "{" << normals[j][i] << ", " << normals[j + 1][i] << ", " << normals[j + 2][i] << "}\n";
//            } else {
//                //нормаль в вершине vertex1
//                //A = vertex2 - vertex1
//                //B = vertex4 - vertex1
//                //нормальХ = Ay*Bz - By*Az;
//                //нормальУ = Az*Bx - Bz*Ax;
//                //нормальZ = Ax*By - Bx*Ay;
//                GLfloat Ax = (10 * verticies[j][i + 1]) - (10 * verticies[j][i]);
//                GLfloat Ay = (10 * verticies[j + 1][i + 1]) - (10 * verticies[j + 1][i]);
//                GLfloat Az = (10 * verticies[j + 2][i + 1]) - (10 * verticies[j + 2][i]);
//
//                GLfloat Bx = (10 * verticies[j + 3][i]) - (10 * verticies[j][i]);
//                GLfloat By = (10 * verticies[j + 4][i]) - (10 * verticies[j + 1][i]);
//                GLfloat Bz = (10 * verticies[j + 5][i]) - (10 * verticies[j + 2][i]);
//
//                normals[j][i] = Ay*Bz - By*Az;
//                normals[j + 1][i] = Az*Bx - Bz*Ax;
//                normals[j + 2][i] = Ax*By - Bx*Ay;
//
//                //Изменение ориентации нормалей
//                normals[j][i] *= -1;
//                normals[j + 1][i] *= -1;
//                normals[j + 2][i] *= -1;
//                //std::cout << "Normal = " << "{" << normals[j][i] << ", " << normals[j + 1][i] << ", " << normals[j + 2][i] << "}\n";
//            }
//        }
//    }
//
//    for (int i = 0; i < z_count; i++) {
//        for (int j = 0; j < 3 * angle_count; j++) {
//            start_verticies[j][i] = verticies[j][i];
//        }
//    }
//}
//
//void Parab::draw(bool textureEnable, GLboolean ts, int animationEnable, bool lightningEnable) {
//
//    glNewList(texlst, GL_COMPILE_AND_EXECUTE);
//    glBegin(GL_QUADS);
//    glTexCoord2f(0.0, 0.0);
//    glVertex3f(500, 500, 0);
//    glTexCoord2f(1.0, 0.0);
//    glVertex3f(600, 500, 0);
//    glTexCoord2f(1.0, 1.0);
//    glVertex3f(600, 600, 0);
//    glTexCoord2f(0.0, 1.0);
//    glVertex3f(500, 600, 0);
//    glEnd();
//    glEndList();
////    std::cout << texlst << std::endl;
//
////    glColor3f(1.0, 1.0, 1.0);
//    glColor3f(0.878, 0.360, 0.662);
////    glColor3f(0.0, 0.0, 0.3);
////    glBegin(GL_LINE_LOOP);
//    int start = 1;
//
//    //B(t) = P0 * (1 - t)^2 + P1*2*t*(1 - t) + P2 * t^2; t in [0;1];
//    //анимация - отражение отнасительно центра OXY
//    //P0 = начальная поз; P1 = отраж отн OY; P2 = отраж отн OX
//    if (animationEnable == 1){
//        for (int i = 0; i < z_count; i++) {
//            for (int j = 0; j < 3 * angle_count; j += 3) {
//                GLfloat tmp = start_verticies[j][i];
//                verticies[j][i] = tmp * (1 - tt) * (1 - tt) + 2 * tt * (1 - tt) * tmp + (-tmp) * tt * tt;
//                tmp = start_verticies[j + 1][i];
//                verticies[j + 1][i] = tmp * (1 - tt) * (1 - tt) + 2 * tt * (1 - tt) * (-tmp) + (tmp) * tt * tt;
////                tt += tt_step;
////                if ((tt >= 1) || (tt <= 0)){
////                    tt_step *= -1;
////                }
//            }
//        }
////        if ((tt < 1) || (tt > 0)){
////            tt += tt_step;
////        }
//        tt += tt_step;
//
//        if (tt >= 1) {
//            tt = 1;
//            tt_step *= -1;
//        } else if (tt <= 0){
//            tt = 0;
//            tt_step *= -1;
//        }
//
//    } else if (animationEnable == 3) {
//        for (int i = 0; i < z_count; i++) {
//            for (int j = 0; j < 3 * angle_count; j++) {
//                verticies[j][i] = start_verticies[j][i];
//            }
//        }
//        tt = 0;
//        tt_step = 0.01;
//    }
//
//    if (textureEnable){
//        glCallList(texlst);
//        return;
//    }
//
//    if (lightningEnable){
//        glCallList(texlst);
//        return;
//    }
//
//    for (int j = 0; j < 3*angle_count; j += 3){
//        glBegin(GL_POLYGON);
//        if (j >= 3*angle_count - 3){
//            glVertex3f(10 * verticies[j][start], 10 * verticies[j + 1][start], 10 * verticies[j + 2][start] - 700);
//            glVertex3f(0, 0, 0 - 700);
//            glVertex3f(10 * verticies[0][start], 10 * verticies[1][start], 10 * verticies[2][start] - 700);
//        } else {
//            glVertex3f(10 * verticies[j][start], 10 * verticies[j + 1][start], 10 * verticies[j + 2][start] - 700);
//            glVertex3f(0, 0, 0 - 700);
//            glVertex3f(10 * verticies[j + 3][start], 10 * verticies[j + 4][start],
//                       10 * verticies[j + 5][start] - 700);
//        }
//        glEnd();
//    }
//
//    for (int i = start; i < z_count - 1; i++){
//        for (int j = 0; j < 3*angle_count; j += 3){
//            glBegin(GL_POLYGON);
//            if (j >= 3*angle_count - 3){
//                if (i < z_count - 2) {
//                    glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 700);
//                    glVertex3f(10 * verticies[0][i], 10 * verticies[1][i], 10 * verticies[2][i] - 700);
//                    glVertex3f(10 * verticies[0][i + 1], 10 * verticies[1][i + 1], 10 * verticies[2][i + 1] - 700);
//                    glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1],
//                               10 * verticies[j + 2][i + 1] - 700);
//                } else {
//                    glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 700);
//                    glVertex3f(10 * verticies[0][i], 10 * verticies[1][i], 10 * verticies[2][i] - 700);
//                    glVertex3f(10 * verticies[0][i + 1], 10 * verticies[1][i + 1], 10 * verticies[2][i + 1] - 700);
//                    glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1],
//                               10 * verticies[j + 2][i + 1] - 700);
//                }
//            } else {
//                if (i < z_count - 2){
//                    glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 700);
//                    glVertex3f(10 * verticies[j + 3][i], 10 * verticies[j + 4][i], 10 * verticies[j + 5][i] - 700);
//                    glVertex3f(10 * verticies[j + 3][i + 1], 10 * verticies[j + 4][i + 1],
//                               10 * verticies[j + 5][i + 1] - 700);
//                    glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1],
//                               10 * verticies[j + 2][i + 1] - 700);
//                } else {
//                    glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 700);
//                    glVertex3f(10 * verticies[j + 3][i], 10 * verticies[j + 4][i], 10 * verticies[j + 5][i] - 700);
//                    glVertex3f(10 * verticies[j + 3][i + 1], 10 * verticies[j + 4][i + 1],
//                               10 * verticies[j + 5][i + 1] - 700);
//                    glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1],
//                               10 * verticies[j + 2][i + 1] - 700);
//                }
//            }
//            glEnd();
//        }
//    }
//
//
//    for (int i = start; i < z_count; i++) {
//        glBegin(GL_POLYGON);
//        for (int j = 0; j < 3 * angle_count; j += 3) {
//            glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][z_count - 1] - 700);
//        }
//        glEnd();
//    }
//
//
//    //ЦИКЛ ОТРИСОВКИ НОРМАЛЕЙ
////    for (int i = start; i < z_count; i++) {
////        for (int j = 0; j < 3 * angle_count; j += 3) {
////            glBegin(GL_LINE_STRIP);
////            glColor3f(1, 1, 1);
////            glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 700);
////            glVertex3f(10 * verticies[j][i] + normals[j][i] / 10, 10 * verticies[j + 1][i] + normals[j + 1][i] / 10, 10 * verticies[j + 2][i] - 700 + normals[j + 2][i] / 10);
////            glEnd();
////        }
////    }
//
//}
