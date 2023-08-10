package internship.batch2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name,price,desc;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        name = findViewById(R.id.product_detail_name);
        imageView = findViewById(R.id.product_detail_image);
        price = findViewById(R.id.product_detail_price);
        desc = findViewById(R.id.product_detail_desc);

        name.setText(sp.getString(ConstantSp.PRODUCT_NAME,""));
        imageView.setImageResource(sp.getInt(ConstantSp.PRODUCT_IMAGE,0));
        price.setText(ConstantSp.PRICE_SYMBOL+sp.getString(ConstantSp.PRODUCT_PRICE,""));
        desc.setText(sp.getString(ConstantSp.PRODUCT_DESCRIPTION,""));
    }
}