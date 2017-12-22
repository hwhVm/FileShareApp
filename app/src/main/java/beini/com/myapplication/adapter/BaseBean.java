package beini.com.myapplication.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Create by beini  2017/12/22
 */

public class BaseBean<T> {
    private int id;
    private List<T> baseList;

    public BaseBean(@LayoutRes int id, List<T> baseList) {
        this.id = id;
        this.baseList = baseList;
    }

    public int getId() {
        return id;
    }

    public void setId(@LayoutRes int id) {
        this.id = id;
    }

    public List<T> getBaseList() {
        return baseList;
    }

    public void setBaseList(@NonNull List<T> baseList) {
        this.baseList = baseList;
    }
}
