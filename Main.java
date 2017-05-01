package Task3;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Main {
    public static void main(String[] args){
        MyClass m = new MyClass();
        m.setCymb('a');
        m.setNumb(11);
        m.setText("Hello");

        try {
            serial(m, "C:\\2.txt");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            m = deSerial(MyClass.class, "C:\\2.txt");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        System.out.println(m);


    }

    static void serial(Object obj, String path) throws IllegalAccessException, IOException {
        Class<?> cls = obj.getClass();
        StringBuilder s = new StringBuilder();
        Field[] field = cls.getDeclaredFields();
        for(Field f: field){
            if(f.isAnnotationPresent(Save.class)) {
                if (Modifier.isPrivate(f.getModifiers())) {
                    f.setAccessible(true);
                }
                s.append(f.getName() + "=");

                if (f.getType() == String.class) {
                    s.append((String) f.get(obj));
                } else if (f.getType() == int.class) {
                    s.append(f.getInt(obj));
                } else if (f.getType() == char.class) {
                    s.append(f.getChar(obj));
                }
                s.append(" ");
            }
        }
        System.out.println(s.toString());

        try(RandomAccessFile raf = new RandomAccessFile(path, "rw")){
            raf.writeUTF(s.toString());
        }

    }
    static <T> T deSerial(Class<T> cls, String path) throws IOException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        String[] arr;
        try(RandomAccessFile raf = new RandomAccessFile(path, "r")){
           arr = raf.readUTF().split(" ");
        }
        T obj = (T) cls.newInstance();
        for(String s: arr){
            String[] ar = s.split("=");
            String name = ar[0];
            String value = ar[1];
            Field f = cls.getDeclaredField(name);
            if(f.isAnnotationPresent(Save.class)){
                if(Modifier.isPrivate(f.getModifiers()))
                    f.setAccessible(true);

                if(f.getType() == String.class)
                    f.set(obj, value);
                if(f.getType() == int.class)
                    f.setInt(obj, Integer.parseInt(value));
                if(f.getType() == char.class)
                    f.setChar(obj, value.charAt(0));
            }
        }
        return obj;
    }

}
