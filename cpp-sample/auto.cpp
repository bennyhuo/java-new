#include <iostream>
#include <map>
#include <vector>

class User {
    static auto i;


};


auto User::i = "hello";


int GetInt() {
    return 1;
}

float GetFloat() {
    return 2.0;
}

class Super {

};

class Sub : public Super {

};

class Sub2: public Super {
    
};

int main() {
    std::cout << "Hello, World!" << std::endl;

    auto example = [](auto x) { return x; };

    std::vector<std::map<std::string, std::vector<int>>> values;
    for (std::vector<std::map<std::string, std::vector<int>>>::iterator i = values.begin(); i < values.end(); ++i) {

    }

    for (auto i = values.begin(); i < values.end(); ++i) {

    }

    std::cout << example(1) << std::endl;
    std::cout << example("Hello") << std::endl;


    auto multiply2 = [](auto i) { return i * 2; };
    multiply2(10);
    multiply2(30.0);

    int a = GetInt();
    float b = GetFloat();
    auto c = a > b ? a : b;
    
    Sub* sub = new Sub();
    Sub2* sub2 = new Sub2();

    auto d = a > b ? sub2 : sub;

    return 0;
}
