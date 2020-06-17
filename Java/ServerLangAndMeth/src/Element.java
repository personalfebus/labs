import java.util.ArrayList;
import java.util.PriorityQueue;

public class Element<T> {
    // Создаёт новый элемент со значением x

    private ArrayList<T> var = new ArrayList<>();
    private ArrayList<T> unions = new ArrayList<>();
    private T body;

    public Element(T x){
        this.body = x;
        this.var.add(x);
    }

    // Возвращает значение элемента
    public T x(){
        return this.body;
    }

    // Определяет, принадлежит ли текущий элемент
    // тому же множеству, что и элемент elem
    public boolean equivalent(Element<T> elem) {
        return this.var.contains(elem.x());
    }

    // Объединяет множество, которому принадлежит текущий
    // элемент, с множеством, которому принадлежит
    // элемент elem
    public void union(Element<T> elem)
    {
        // ...
    }
}