package internship.batch2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    ImageView imageView;
    TextView name, price, desc;
    Button buyNow, addCart, addWishlist, removeWishlist;

    SharedPreferences sp;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        db = openOrCreateDatabase("Internship_Batch2", MODE_PRIVATE, null);
        String tabelQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INTEGER(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tabelQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INTEGER(10),USERID INTEGER(10),PRODUCTID INTEGER(10),PRODUCTNAME VARCHAR(200),PRODUCTPRICE VARCHAR(10),PRODUCTIMAGE VARCHAR(100),PRODUCTDESCRIPTION TEXT,PRODUCTQTY INTEGER(10),TOTALPRICE VARCHAR(20))";
        db.execSQL(cartTableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INTEGER(10),PRODUCTID INTEGER(10),PRODUCTNAME VARCHAR(200),PRODUCTPRICE VARCHAR(10),PRODUCTIMAGE VARCHAR(100),PRODUCTDESCRIPTION TEXT)";
        db.execSQL(wishlistTableQuery);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        name = findViewById(R.id.product_detail_name);
        imageView = findViewById(R.id.product_detail_image);
        price = findViewById(R.id.product_detail_price);
        desc = findViewById(R.id.product_detail_desc);

        buyNow = findViewById(R.id.product_detail_buy_now);

        addCart = findViewById(R.id.product_detail_add_cart);

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectQuery = "SELECT * FROM CART WHERE USERID='" + sp.getString(ConstantSp.ID, "") + "' AND PRODUCTID='" + sp.getString(ConstantSp.PRODUCT_ID, "") + "' AND ORDERID='0' ";
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.getCount() > 0) {
                    new CommonMethod(ProductDetailActivity.this, "Product Already Added In Cart");
                } else {
                    int iQty = 3;
                    int iTotalPrice = Integer.parseInt(sp.getString(ConstantSp.PRODUCT_PRICE, "")) * iQty;
                    String insertQuery = "INSERT INTO CART VALUES(NULL,'0','" + sp.getString(ConstantSp.ID, "") + "','" + sp.getString(ConstantSp.PRODUCT_ID, "") + "','" + sp.getString(ConstantSp.PRODUCT_NAME, "") + "','" + sp.getString(ConstantSp.PRODUCT_PRICE, "") + "','" + sp.getInt(ConstantSp.PRODUCT_IMAGE, 0) + "','" + sp.getString(ConstantSp.PRODUCT_DESCRIPTION, "") + "','" + iQty + "','" + iTotalPrice + "')";
                    db.execSQL(insertQuery);
                    new CommonMethod(ProductDetailActivity.this, "Product Added In Cart Successfully");
                }
            }
        });

        addWishlist = findViewById(R.id.product_detail_add_wishlist);
        removeWishlist = findViewById(R.id.product_detail_remove_wishlist);

        String selectQuery = "SELECT * FROM WISHLIST WHERE USERID='" + sp.getString(ConstantSp.ID, "") + "' AND PRODUCTID='" + sp.getString(ConstantSp.PRODUCT_ID, "") + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            addWishlist.setVisibility(View.GONE);
            removeWishlist.setVisibility(View.VISIBLE);
        } else {
            addWishlist.setVisibility(View.VISIBLE);
            removeWishlist.setVisibility(View.GONE);
        }

        removeWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String removeQuery = "DELETE FROM WISHLIST WHERE PRODUCTID='"+sp.getString(ConstantSp.PRODUCT_ID,"")+"' AND USERID='"+sp.getString(ConstantSp.ID,"")+"'";
                db.execSQL(removeQuery);
                new CommonMethod(ProductDetailActivity.this,"Product Removed From Wishlist Successfully");
                addWishlist.setVisibility(View.VISIBLE);
                removeWishlist.setVisibility(View.GONE);
            }
        });

        addWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectQuery = "SELECT * FROM WISHLIST WHERE USERID='" + sp.getString(ConstantSp.ID, "") + "' AND PRODUCTID='" + sp.getString(ConstantSp.PRODUCT_ID, "") + "'";
                Cursor cursor = db.rawQuery(selectQuery, null);
                if (cursor.getCount() > 0) {
                    new CommonMethod(ProductDetailActivity.this, "Product Already Added In Wishlist");
                } else {
                    String insertQuery = "INSERT INTO WISHLIST VALUES(NULL,'" + sp.getString(ConstantSp.ID, "") + "','" + sp.getString(ConstantSp.PRODUCT_ID, "") + "','" + sp.getString(ConstantSp.PRODUCT_NAME, "") + "','" + sp.getString(ConstantSp.PRODUCT_PRICE, "") + "','" + sp.getInt(ConstantSp.PRODUCT_IMAGE, 0) + "','" + sp.getString(ConstantSp.PRODUCT_DESCRIPTION, "") + "')";
                    db.execSQL(insertQuery);
                    new CommonMethod(ProductDetailActivity.this, "Product Added In Wishlist Successfully");

                    addWishlist.setVisibility(View.GONE);
                    removeWishlist.setVisibility(View.VISIBLE);

                }
            }
        });

        name.setText(sp.getString(ConstantSp.PRODUCT_NAME, ""));
        imageView.setImageResource(sp.getInt(ConstantSp.PRODUCT_IMAGE, 0));
        price.setText(ConstantSp.PRICE_SYMBOL + sp.getString(ConstantSp.PRODUCT_PRICE, ""));
        desc.setText(sp.getString(ConstantSp.PRODUCT_DESCRIPTION, ""));

        Checkout.preload(getApplicationContext());
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        co.setKeyID("rzp_test_xsiOz9lYtWKHgF");

        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Online Purchase Order");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://seeklogo.com/images/S/smiley-face-logo-C3882DDA04-seeklogo.com.png");
            options.put("currency", "INR");
            options.put("amount", Integer.parseInt(sp.getString(ConstantSp.PRODUCT_PRICE, "")) * 100);

            JSONObject preFill = new JSONObject();
            preFill.put("email", sp.getString(ConstantSp.EMAIL, ""));
            preFill.put("contact", sp.getString(ConstantSp.CONTACT, ""));

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            Toast.makeText(this, "Payment Successful :\nPayment ID: " + s + "\nPayment Data: " + paymentData.getData(), Toast.LENGTH_SHORT).show();
            Log.d("RESPONSE_SUCCESS", "Payment Successful :\nPayment ID: " + s + "\nPayment Data: " + paymentData.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try {
            Toast.makeText(this, "Payment Failed:\nPayment Data: " + paymentData.getData(), Toast.LENGTH_SHORT).show();
            Log.d("RESPONSE_ERROR", "Payment Failed:\nPayment Data: " + paymentData.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}