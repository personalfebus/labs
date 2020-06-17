#include <iostream>
#include <fstream>
#include <istream>
#include <vector>

using namespace std;

int main() {
    vector<string> text;
    vector<string> correct;
    vector<double> koef;
    vector<vector<string>> bigrams;
    vector<string> tmp;
    vector<int> chast;
    while (!cin.eof()){
        string s;
        cin >> s;
        if (!s.empty()) {
            text.push_back(s);
            correct.push_back(s);
            koef.push_back(- 1.0);
            chast.push_back(-1);
            for (int i = 0; i < s.length() - 1; i++) {
                string a;
                a += s[i];
                a += s[i + 1];
                //cout << a << ' ';
                tmp.push_back(a);
            }
            if (s.length() == 1) tmp.push_back(s);
            bigrams.push_back(tmp);
            tmp.clear();
        }
    }


    ifstream fs;
    fs.open("D:\\_IT\\CLionProjects\\serv2\\count_big.txt");
    while (!fs.eof()){
        string str1;
        string s;
        int freq = 0;
        fs >> str1;
        if (str1.empty()) break;
        int pos = 0;
        for (int i = 0; i < str1.length(); i++){
            if ((str1[i] == '\t') || (str1[i] == ' ')){
                pos = i + 1;
                break;
            }
            s += str1[i];
        }
        //cout << s << endl;
        string str2;
        fs >> str2;
        for (int i = 0; i < str2.length(); i++){
            if ((str2[i] == '\t') || (str2[i] == ' ')){
                continue;
            }
            freq = freq * 10 + (int)str2[i] - 48;
        }
        //cout << freq << endl;

        vector<string> tmp;
        for (int i = 0; i < s.length() - 1; i++){
            string a;
            a += s[i];
            a += s[i + 1];
            //cout << a << ' ';
            tmp.push_back(a);
        }
        //cout << endl;
        if (s.length() == 1) tmp.push_back(s);

        for (int i = 0; i < text.size(); i++){
            int uni = tmp.size() + bigrams[i].size();
            int over = 0;
            //cout << "START = " << uni << ' ' << over << endl;
            for (int j = 0; j < bigrams[i].size(); j++){
                bool first = true;
                for (int k = 0; k < tmp.size(); k++){
                    if ((bigrams[i][j] == tmp[k]) && (first)){
                        over++;
                        uni--;
                        first = false;
                        for (int g = j - 1; g >= 0; g--){
                            if (bigrams[i][j] == bigrams[i][g]){
                                over--;
                                uni--;
                                break;
                            }
                        }
                    }
                    else if (bigrams[i][j] == tmp[k]){
                        uni--;
                    }
                }
            }
            double curr = (double)over / uni;
            //cout << curr << " from union = " << uni << " and overlap = " << over << endl;
            if (curr > koef[i]){
                //cout <<  text[i] << " and " << str1 << endl;
                //cout << curr << endl;
                correct[i] = s;
                koef[i] = curr;
                chast[i] = freq;
            }
            else if (curr == koef[i]){
                if (freq > chast[i]){
                    correct[i] = s;
                    koef[i] = curr;
                    chast[i] = freq;
                }
                else if (freq == chast[i]){
                    if (str1 < correct[i]){
                        correct[i] = s;
                        koef[i] = curr;
                        chast[i] = freq;
                    }
                }
            }
        }
    }

    for (int i = 0; i < correct.size(); i++){
        cout << correct[i] << ' ';
    }

    return 0;
}