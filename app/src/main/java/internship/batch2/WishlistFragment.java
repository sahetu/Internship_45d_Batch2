package internship.batch2;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class WishlistFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<WishlistList> arrayList;

    SharedPreferences sp;
    SQLiteDatabase db;

    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        db = getActivity().openOrCreateDatabase("Internship_Batch2",MODE_PRIVATE,null);
        String tabelQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT INTEGER(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(50),DOB VARCHAR(10))";
        db.execSQL(tabelQuery);

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY AUTOINCREMENT,ORDERID INTEGER(10),USERID INTEGER(10),PRODUCTID INTEGER(10),PRODUCTNAME VARCHAR(200),PRODUCTPRICE VARCHAR(10),PRODUCTIMAGE VARCHAR(100),PRODUCTDESCRIPTION TEXT,PRODUCTQTY INTEGER(10),TOTALPRICE VARCHAR(20))";
        db.execSQL(cartTableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY AUTOINCREMENT,USERID INTEGER(10),PRODUCTID INTEGER(10),PRODUCTNAME VARCHAR(200),PRODUCTPRICE VARCHAR(10),PRODUCTIMAGE VARCHAR(100),PRODUCTDESCRIPTION TEXT)";
        db.execSQL(wishlistTableQuery);

        sp = getActivity().getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        recyclerView = view.findViewById(R.id.wishlist_recyclerview);

        //Display Data In List
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String selectQuery = "SELECT * FROM WISHLIST WHERE USERID='"+sp.getString(ConstantSp.ID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0) {
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()){
                WishlistList list = new WishlistList();
                list.setWishlistId(cursor.getString(0));
                list.setProductId(cursor.getString(2));
                list.setProductName(cursor.getString(3));
                list.setProductImage(cursor.getString(5));
                list.setProductPrice(cursor.getString(4));
                list.setProductDesc(cursor.getString(6));
                arrayList.add(list);
            }
            WishlistAdapter adapter = new WishlistAdapter(getActivity(),arrayList);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }
}