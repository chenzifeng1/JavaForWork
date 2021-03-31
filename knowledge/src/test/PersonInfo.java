package test;

import java.util.List;

/**
 * @Author: czf
 * @Description:
 * @Date: 2021-03-31 14:23
 * @Version: 1.0
 **/
public class PersonInfo {

    int num;
    List<Person> personList;

//    static PersonInfo toObject(String json){
//        return new Gson().fromJson(json,PersonInfo.class);
//    }

    static class Person{
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public Person() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }
}
