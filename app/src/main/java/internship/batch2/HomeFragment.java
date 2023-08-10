package internship.batch2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView;

    String[] nameArray = {"Bread","Butter","Cloth","Makeup Kit","Gift"};
    String[] priceArray = {"50","120","2000","4000","1000"};
    int[] imageArray = {R.drawable.bread,R.drawable.butter,R.drawable.cloth,R.drawable.makup_kit,R.drawable.upcoming_load};

    String[] descArray = {
            "Bread, baked food product made of flour or meal that is moistened, kneaded, and sometimes fermented. A major food since prehistoric times, it has been made in various forms using a variety of ingredients and methods throughout the world.",
            "Butter, a yellow-to-white solid emulsion of fat globules, water, and inorganic salts produced by churning the cream from cows' milk. Butter has long been used as a spread and as a cooking fat. It is an important edible fat in northern Europe, North America, and other places where cattle are the primary dairy animals.",
            "Piece of cloth - a separate part consisting of fabric. piece of material. bib - top part of an apron; covering the chest. chamois cloth - a piece of chamois used for washing windows or cars. dishcloth, dishrag - a cloth for washing dishes.",
            "What does a makeup kit consist of? Ideally, a complete makeup kit consists of a moisturizer, skin tint (like foundation or tinted primer), concealer, lip product, bronzer, blush, and mascara.",
            "Hold out your hands and close your eyes! I hope this small gift will make your birthday even more joyful and brighter!\n" +
                    "Best wishes! I have something special for you on this special day, and I hope you like it. Enjoy this gift!"
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.home_recyclerview);

        //Display Data In List
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Display Data In Grid
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //Display Data In Horizontal
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ProductAdapter adapter = new ProductAdapter(getActivity(),nameArray,imageArray,priceArray,descArray);
        recyclerView.setAdapter(adapter);

        return view;
    }
}