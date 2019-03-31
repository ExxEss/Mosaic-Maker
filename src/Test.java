class Animal {
    int number = 10;
    static int age = 20;

    public void eat() {
        System.out.println("Animal eats");
    }

    public static void sleep() {
        System.out.println("Animal sleeps");
    }

    public void run() {
        System.out.println("Animal runs");
    }
}

class Cat extends Animal {
    int number = 20;
    static int age = 10;
    String name = "tomcat";

    public void eat() {
        System.out.println("Cat eats");
    }

    public static void sleep() {
        System.out.println("Cat sleep");
    }

    public void sayMeow() {
        System.out.println("Cat says meow");
    }
}

class Test {
    public static void main(String[] args) {
        Animal animal = new Cat();
        animal.eat();
        animal.sleep();
        animal.run();
        // animal.sayMeow();
        // System.out.println(animal.name);
        System.out.println(animal.number);
        System.out.println(animal.age);

    }
}

