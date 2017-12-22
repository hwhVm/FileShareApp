package beini.com.myapplication.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import beini.com.myapplication.R;
import beini.com.myapplication.bean.SharedFileBean;

/**
 * Create by beini  2017/12/22
 */

public class FileAdapter extends BaseAdapter {

    List<SharedFileBean> fileList;

    public FileAdapter(BaseBean<SharedFileBean> baseBean) {
        super(baseBean);
        this.fileList = baseBean.getBaseList();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        getTextView((ViewHolder) holder, R.id.text_file_name).setText(fileList.get(position).getFileName());
    }
}
