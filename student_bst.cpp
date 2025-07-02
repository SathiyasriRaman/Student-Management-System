#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <iomanip>
using namespace std;

struct Student {
    int roll;
    string name;
    int tamil, english, maths, science, social;
    int total;
    float average;
    string grade;
};

struct Node {
    Student data;
    Node* left;
    Node* right;
};

// -------------------- BST Operations -----------------------

Node* insert(Node* root, Student s) {
    if (!root) {
        Node* newNode = new Node{s, nullptr, nullptr};
        return newNode;
    }
    if (s.roll < root->data.roll)
        root->left = insert(root->left, s);
    else
        root->right = insert(root->right, s);
    return root;
}

Node* search(Node* root, int roll) {
    if (!root || root->data.roll == roll)
        return root;
    if (roll < root->data.roll)
        return search(root->left, roll);
    return search(root->right, roll);
}

Node* findMin(Node* root) {
    while (root->left) root = root->left;
    return root;
}

Node* deleteNode(Node* root, int roll) {
    if (!root) return nullptr;

    if (roll < root->data.roll)
        root->left = deleteNode(root->left, roll);
    else if (roll > root->data.roll)
        root->right = deleteNode(root->right, roll);
    else {
        if (!root->left) {
            Node* temp = root->right;
            delete root;
            return temp;
        } else if (!root->right) {
            Node* temp = root->left;
            delete root;
            return temp;
        }
        Node* temp = findMin(root->right);
        root->data = temp->data;
        root->right = deleteNode(root->right, temp->data.roll);
    }
    return root;
}

void inorder(Node* root) {
    if (!root) return;

    inorder(root->left);
    Student s = root->data;
    cout << left << setw(6) << s.roll << setw(10) << s.name
         << setw(5) << s.tamil << setw(5) << s.english << setw(5) << s.maths
         << setw(5) << s.science << setw(5) << s.social << setw(6) << s.total
         << setw(7) << s.average << setw(3) << s.grade << endl;
    inorder(root->right);
}

// ------------------- File Reading -----------------------

Student parseLine(string line) {
    stringstream ss(line);
    string token;
    Student s;
    getline(ss, token, '|'); s.roll = stoi(token);
    getline(ss, token, '|'); s.name = token;
    getline(ss, token, '|'); s.tamil = stoi(token);
    getline(ss, token, '|'); s.english = stoi(token);
    getline(ss, token, '|'); s.maths = stoi(token);
    getline(ss, token, '|'); s.science = stoi(token);
    getline(ss, token, '|'); s.social = stoi(token);
    getline(ss, token, '|'); s.total = stoi(token);
    getline(ss, token, '|'); s.average = stof(token);
    getline(ss, token, '|'); s.grade = token;
    return s;
}

Node* buildTreeFromFile(const string& filename) {
    ifstream file(filename);
    Node* root = nullptr;
    string line;
    while (getline(file, line)) {
        if (!line.empty()) {
            Student s = parseLine(line);
            root = insert(root, s);
        }
    }
    return root;
}

// ------------------- Menu -----------------------

void printHeader() {
    cout << left << setw(6) << "Roll" << setw(10) << "Name"
         << setw(5) << "Tam" << setw(5) << "Eng" << setw(5) << "Mat"
         << setw(5) << "Sci" << setw(5) << "Soc" << setw(6) << "Tot"
         << setw(7) << "Avg" << setw(3) << "G" << endl;
    cout << "---------------------------------------------------------------\n";
}

void menu(Node*& root) {
    int choice;
    do {
        cout << "\n===== Student BST Menu =====\n";
        cout << "1. Display All (Sorted by Roll No)\n";
        cout << "2. Search by Roll No\n";
        cout << "3. Delete by Roll No\n";
        cout << "0. Exit\n";
        cout << "Enter your choice: ";
        cin >> choice;

        switch (choice) {
            case 1:
                printHeader();
                inorder(root);
                break;

            case 2: {
                int roll;
                cout << "Enter Roll No: ";
                cin >> roll;
                Node* found = search(root, roll);
                if (found) {
                    printHeader();
                    Student s = found->data;
                    cout << left << setw(6) << s.roll << setw(10) << s.name
                         << setw(5) << s.tamil << setw(5) << s.english << setw(5) << s.maths
                         << setw(5) << s.science << setw(5) << s.social << setw(6) << s.total
                         << setw(7) << s.average << setw(3) << s.grade << endl;
                } else {
                    cout << "? Student not found.\n";
                }
                break;
            }

            case 3: {
                int roll;
                cout << "Enter Roll No to delete: ";
                cin >> roll;
                root = deleteNode(root, roll);
                cout << "? Deleted (if exists).\n";
                break;
            }

            case 0:
                cout << "?? Exiting...\n";
                break;

            default:
                cout << "?? Invalid choice!\n";
        }
    } while (choice != 0);
}

// ------------------- Main -----------------------

int main() {
    Node* root = buildTreeFromFile("students.txt");
    menu(root);
    return 0;
}

