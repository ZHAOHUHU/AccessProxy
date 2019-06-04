package shenzhen.teamway.test;

import java.util.Objects;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-05-31 09:08
 **/
public class Liianshou {
    public static void main(String[] args) {
        final Person person = new Person();
        final Person person1 = new Person();
        System.out.println(person==person1);
        System.out.println(person.equals(person1));

    }

    static class Person {
        private String
                name;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return name.equals(person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}