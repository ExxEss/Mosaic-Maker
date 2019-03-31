import java.lang.reflect.Method;

// Programa 1
public class ReflectDemo {
    String name;
    int age;

    public int func(int x) { return x; }

    public static void printMethod() {
        Method[] methods = Test.class.getMethods();

        for (Method method : methods)
            System.out.println(method.getName());
    }

    public static void main(String[] args) {
        printMethod();
    }
}
