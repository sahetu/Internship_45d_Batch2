package internship.batch2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {

    Context context;
    String[] nameArray;
    int[] imageArray;

    public ProductAdapter(Context context, String[] nameArray, int[] imageArray) {
        this.context = context;
        this.nameArray = nameArray;
        this.imageArray = imageArray;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.custom_product_name);
            image = itemView.findViewById(R.id.custom_product_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.image.setImageResource(imageArray[position]);
        holder.name.setText(nameArray[position]);
    }

    @Override
    public int getItemCount() {
        return nameArray.length;
    }
}
