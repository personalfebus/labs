//
// Created by gleb8 on 04.06.2019.
//

#include "textstats.h"
#include <iostream>
#include <algorithm>

using namespace std;

void get_tokens(
        // [ВХОД] Текстовая строка.
        const string &s,
        // [ВХОД] Множество символов-разделителей.
        const unordered_set<char> &delimiters,
        // [ВЫХОД] Последовательность слов.
        vector<string> &tokens){
    bool word = false;
    string tok = "";
    for (int i = 0; i < s.length(); i++){
        if (((delimiters.count(s[i]) > 0) && (word)) || ((i == s.length() - 1) && (delimiters.count(s[i]) == 0))){
            if ((i == s.length() - 1) && (delimiters.count(s[i]) == 0)) {
                tok += s[i];
                i++;
            }
            word = false;
            //cout << tok << " IS TOKKEN\n";
            tokens.push_back(tok);
        }
        else if (delimiters.count(s[i]) == 0){
            if (!word){
                word = true;
                tok = "";
            }
            if (((int)s[i] > 64) && ((int)s[i] < 91)){
                tok += s[i] + 32;
            }
            else tok += s[i];
        }
    }
}

void get_type_freq(
        // [ВХОД] Последовательность слов.
        const vector<string> &tokens,
        // [ВЫХОД] Частотный словарь
        // (ключи -- слова, значения -- количества вхождений).
        map<string, int> &freqdi){
    for (int i = 0; i < tokens.size(); i++){
        if (freqdi.count(tokens[i]) == 0){
            freqdi.emplace(tokens[i], 1);
        }
        else {
            freqdi.at(tokens[i])++;
        }
    }
}

void get_types(
        // [ВХОД] Последовательность слов.
        const vector<string> &tokens,
        // [ВЫХОД] Список уникальных слов.
        vector<string> &wtypes){
    //cout << "NIGGER\n";
    for (int i = 0; i < tokens.size(); i++){
//        int pos = wtypes.size();
//        auto it = wtypes.begin();
//        for (int j = 0; j < wtypes.size(); j++){
//            if (tokens[i] < wtypes[j]){
//                //cout << tokens[i] << '<' << wtypes[j] << endl;
//                pos = j;
//                break;
//            }
//            else if (tokens[i] == wtypes[j]){
//                pos = -1;
//                break;
//            }
//            it++;
//        }
//        if (pos > -1){
//            wtypes.insert(it, tokens[i]);
//        }
        //cout << tokens[i] << " is on pos = " << pos << '\n';
        bool boo = true;
        for (int j = 0; j < wtypes.size(); j++){
            if (tokens[i] == wtypes[j]){
                boo = false;
                break;
            }
        }
        if (boo){
            wtypes.push_back(tokens[i]);
        }
    }

//    [](string s1, string s2){
//        int min = s1.length();
//        bool xd = true;
//        if (s2.length() < s1.length()) {
//            min = s2.length();
//            xd = false;
//        }
//        for (int i = 0; i < min; i++){
//            if ((int)s1[i] - (int)s2[i] != 0){
//                cout << s1 << " || " << s2 << " --> " << (int)s1[i] - (int)s2[i] << endl;
//                return (int)s2[i] - (int)s1[i];
//            }
//        }
//        if (xd){
//            cout << s1 << " || " << s2 << " --> " << -1 << endl;
//            return -1;
//        }
//        else{
//            cout << s1 << " || " << s2 << " --> " << -1 << endl;
//            return 1;
//        }
//    }

    sort(wtypes.begin(), wtypes.end());
}

void get_x_length_words(
        // [ВХОД] Список уникальных слов.
        const vector<string> &wtypes,
        // [ВХОД] Минимальная длина слова.
        int x,
        // [ВЫХОД] Список слов, длина которых не меньше x.
        vector<string> &words){
    for (int i = 0; i < wtypes.size(); i++){
        if (wtypes[i].length() >= x){
            words.push_back(wtypes[i]);
        }
    }
}

void get_x_freq_words(
        // [ВХОД] Частотный словарь
        const map<string, int> &freqdi,
        // [ВХОД] Минимальное количество вхождений.
        int x,
        // [ВЫХОД] Список слов, встречающихся не меньше x раз.
        vector<string> &words){

    for (auto it = freqdi.begin(); it != freqdi.end(); it++) {
        if (it->second >= x) {
//            auto ter = words.begin();
//            for (int j = 0; j < words.size(); j++){
//                ter++;
//                if (it->first < words[j]){
//                    break;
//                }
//            }
//            words.insert(ter, it->first);
            words.push_back(it->first);
        }
    }
    sort(words.begin(), words.end());
}

void get_words_by_length_dict(
        // [ВХОД] Список уникальных слов.
        const vector<string> &wtypes,
        // [ВЫХОД] Словарь распределения слов по длинам.
        map<int, vector<string> > &lengthdi) {
    for (int i = 0; i < wtypes.size(); i++) {
        if (lengthdi.count(wtypes[i].length()) > 0) {
            auto it = lengthdi.find(wtypes[i].length());
            it->second.push_back(wtypes[i]);
        } else {
            vector<string> tmp;
            tmp.push_back(wtypes[i]);
            lengthdi.emplace(wtypes[i].length(), tmp);
        }
    }
}