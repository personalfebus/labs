//
// Created by gleb8 on 4/15/2020.
//

#include <cmath>

class Parab{
public:
    int AA;
    int BB;
    int z_cap;
    int z_step;
    int z_count;
    int angle_count;
    float anglestep;
    float *sinuses;
    float *cosinuses;
    float **verticies;
    explicit Parab(int a, int b, int zc, int zs, float as);
    void draw(int drawMode);
    void remakePar(int a, int b, int zc, int zs, float as);
    virtual ~Parab();
};

Parab::Parab(int a, int b, int zc, int zs, float as) {
    AA = a;
    BB = b;
    z_cap = zc;
    z_step = zs;
    anglestep = as;

    angle_count = (int)(M_PI * 2 / anglestep);
    z_count = (int)(z_cap / z_step);

//    std::cout << angle_count << " " << anglestep;

    verticies = new float*[3*angle_count];
    for (int i = 0; i < 3*angle_count; i++){
        verticies[i] = new float[z_count];
    }

    sinuses = new float[angle_count];
    cosinuses = new float[angle_count];


    for (int i = 0; i < z_count; i++){
        for (int j = 0; j < 3*angle_count; j++){
            verticies[j][i] = 0;
        }
    }

    float current_angle = 0;
    for (int i = 0; i < angle_count; i++){
        sinuses[i] = sinf(current_angle);
        cosinuses[i] = cosf(current_angle);
//        std::cout << "angle = " << current_angle << "; sin = " << sinuses[i] << "; cos = " << cosinuses[i] << ";\n";
        current_angle += anglestep;
    }
    //x = a * cos(t)
    //y = b * sin(t)
    float z = z_step;
    for (int i = 1; i < z_count; i++){
        //std::cout << "\nZ = " << z << std::endl;
        float current_BB = BB * sqrtf(z);
        float current_AA = AA * sqrtf(z);
        int k = 0;

        for (int j = 0; j < angle_count; j++){
            verticies[k][i] = current_AA * cosinuses[j];
            k++;
            verticies[k][i] = current_BB * sinuses[j];
            k++;
            verticies[k][i] = z;
            k++;
//            std::cout << "Vertex(" << j << "): x = " << verticies[k - 3][i] << "; y = " << verticies[k - 2][i]
//                      << "; z = " << verticies[k - 1][i] << std::endl;
        }

        z += z_step;
    }
}

Parab::~Parab() {
    for (int i = 0; i < 3*angle_count; i++){
        delete[] verticies[i];
    }
    delete[] verticies;
    delete[] sinuses;
    delete[] cosinuses;
}

void Parab::remakePar(int a, int b, int zc, int zs, float as) {

    for (int i = 0; i < 3*angle_count; i++){
        delete[] verticies[i];
    }
    delete[] verticies;
    delete[] sinuses;
    delete[] cosinuses;

    AA = a;
    BB = b;
    z_cap = zc;
    z_step = zs;
    anglestep = as;

    angle_count = (int)(M_PI * 2 / anglestep);
    z_count = (int)(z_cap / z_step);

//    std::cout << angle_count << " " << anglestep;

    verticies = new float*[3*angle_count];
    for (int i = 0; i < 3*angle_count; i++){
        verticies[i] = new float[z_count];
    }

    sinuses = new float[angle_count];
    cosinuses = new float[angle_count];


    for (int i = 0; i < z_count; i++){
        for (int j = 0; j < 3*angle_count; j++){
            verticies[j][i] = 0;
        }
    }

    float current_angle = 0;
    for (int i = 0; i < angle_count; i++){
        sinuses[i] = sinf(current_angle);
        cosinuses[i] = cosf(current_angle);
//        std::cout << "angle = " << current_angle << "; sin = " << sinuses[i] << "; cos = " << cosinuses[i] << ";\n";
        current_angle += anglestep;
    }
    //x = a * cos(t)
    //y = b * sin(t)
    float z = z_step;
    for (int i = 1; i < z_count; i++){
        //std::cout << "\nZ = " << z << std::endl;
        float current_BB = BB * sqrtf(z);
        float current_AA = AA * sqrtf(z);
        int k = 0;

        for (int j = 0; j < angle_count; j++){
            verticies[k][i] = current_AA * cosinuses[j];
            k++;
            verticies[k][i] = current_BB * sinuses[j];
            k++;
            verticies[k][i] = z;
            k++;
//            std::cout << "Vertex(" << j << "): x = " << verticies[k - 3][i] << "; y = " << verticies[k - 2][i]
//                      << "; z = " << verticies[k - 1][i] << std::endl;
        }

        z += z_step;
    }
}

void Parab::draw(int drawMode) {
//    glColor3f(1.0, 1.0, 1.0);

    glColor3f(0.878, 0.360, 0.662);
//    glColor3f(0.0, 0.0, 0.3);
//    glBegin(GL_LINE_LOOP);

    int start = 1;
    for (int j = 0; j < 3*angle_count; j += 3){
        if (drawMode){
            glBegin(GL_POLYGON);
        } else {
            glBegin(GL_LINE_STRIP);
        }
        if (j >= 3*angle_count - 3){
            glVertex3f(10*verticies[j][start], 10*verticies[j + 1][start], 10*verticies[j + 2][start] - 500);
            glVertex3f(10*verticies[0][start], 10*verticies[1][start], 10*verticies[2][start] - 500);
            glVertex3f(0, 0, 0 - 500);
        } else {
            glVertex3f(10*verticies[j][start], 10*verticies[j + 1][start], 10*verticies[j + 2][start] - 500);
            glVertex3f(10*verticies[j + 3][start], 10*verticies[j + 4][start], 10*verticies[j + 5][start] - 500);
            glVertex3f(0, 0, 0 - 500);
        }
        glEnd();
    }

    for (int i = start; i < z_count - 1; i++){
        for (int j = 0; j < 3*angle_count; j += 3){
            if (j >= 3*angle_count - 3){
                //std::cout << "est";
                if (drawMode){
                    glBegin(GL_POLYGON);
                } else {
                    glBegin(GL_LINE_LOOP);
                }
                glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 500);
                glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1], 10 * verticies[j + 2][i + 1] - 500);
                glVertex3f(10 * verticies[0][i + 1], 10 * verticies[1][i + 1], 10 * verticies[2][i + 1] - 500);
                glVertex3f(10 * verticies[0][i], 10 * verticies[1][i], 10 * verticies[2][i] - 500);
                glEnd();
            } else {
                if (drawMode){
                    glBegin(GL_POLYGON);
                } else {
                    glBegin(GL_LINE_STRIP);
                }
                glVertex3f(10 * verticies[j][i], 10 * verticies[j + 1][i], 10 * verticies[j + 2][i] - 500);
                glVertex3f(10 * verticies[j][i + 1], 10 * verticies[j + 1][i + 1], 10 * verticies[j + 2][i + 1] - 500);
                glVertex3f(10 * verticies[j + 3][i + 1], 10 * verticies[j + 4][i + 1], 10 * verticies[j + 5][i + 1] - 500);
                glVertex3f(10 * verticies[j + 3][i], 10 * verticies[j + 4][i], 10 * verticies[j + 5][i] - 500);
                glEnd();
            }
        }
    }

//    glColor3f(1.0, 1.0, 1.0);
//    glBegin(GL_LINE_STRIP);
//    glVertex3f(10 * verticies[0][z_count - 2], 10 * verticies[1][z_count - 2], 10 * verticies[2][z_count - 2] - 500);
//    glVertex3f(10 * verticies[93][z_count - 2], 10 * verticies[94][z_count - 2], 10 * verticies[95][z_count - 2] - 500);
//    glVertex3f(10 * verticies[160][z_count - 2], 10 * verticies[161][z_count - 2], 10 * verticies[162][z_count - 2] - 500);
//    glEnd();
//    std::cout << "middle = " << middle << std::endl;

//    if (!drawMode) {
//        for (int i = 0; i < 3 * (angle_count / 2); i += 3) {
//            //std::cout << "a ";
//            glColor3f(1.0, 1.0, 1.0);
//            glBegin(GL_LINE_STRIP);
//            glVertex3f(10 * verticies[i][z_count - 1], 10 * verticies[i + 1][z_count - 1],
//                       10 * verticies[i + 2][z_count - 1] - 500);
//            glVertex3f(10 * verticies[3 * angle_count - 1 - i - 2][z_count - 1],
//                       10 * verticies[3 * angle_count - 1 - i - 1][z_count - 1],
//                       10 * verticies[3 * angle_count - 1 - i][z_count - 1] - 500);
//            glEnd();
//        }
//    } else {
//        glColor3f(1.0, 1.0, 1.0);
//        glBegin(GL_POLYGON);
//        for (int j = 0; j < 3*angle_count; j += 3){
//            glVertex3f(10 * verticies[j][z_count - 1], 10 * verticies[j + 1][z_count - 1], 10 * verticies[j + 2][z_count - 1] - 500);
//        }
//        glEnd();
//    }
}