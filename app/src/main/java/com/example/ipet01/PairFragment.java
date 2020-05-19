package com.example.ipet01;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

import static com.example.ipet01.MainActivity.UID;


public class PairFragment extends Fragment{

    static final String TAG = "MainActivity";
    CardStackLayoutManager manager;
    CardStackAdapter adapter;


    int MixCount_shelter_petID;
    String[] shelter_petID;
    String[] Shelter_petImgLink;
    String[] Shelter_petSpecies;
    String[] Shelter_petGender;
    String[] Shelter_petColor;
    String[]petCardPetID;                  //給卡片記id用
    int countSwipe=-1; //計算滑第幾張卡 從零開始
    long 喜歡的動物數量=0;
    long 不喜歡的動物數量=0;
    long save喜歡的動物數量=0;
    long save不喜歡的動物數量=0;
    String[] save喜歡的動物ID;
    String[] save不喜歡的動物ID;
    boolean ok01;

    String[] 未選擇的動物ID;



    String SaveID;

    String[] card;

    CardStackView cardStackView;




    FirebaseDatabase database = FirebaseDatabase.getInstance();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("pairfragment","onCreateView");
        View view=inflater.inflate(R.layout.pair_fragment,container,false);


        final DatabaseReference swipeDatabaseReference = database.getReference().child("user").child(UID).child("配對");

        swipeDatabaseReference.addValueEventListener(new ValueEventListener() {  //
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) { //如果有喜歡的動物了
                    喜歡的動物數量 = (dataSnapshot.child("喜歡的動物ID").getChildrenCount());  //這裡是隨時會隨著資料表變動
                    不喜歡的動物數量 = (dataSnapshot.child("不喜歡的動物ID").getChildrenCount());


                    //別理
                    // 喜歡的動物ID=new String[(int)喜歡的動物數量];
                    // for(int i=0;i<喜歡的動物數量;i++){
                    //    喜歡的動物ID[i]=dataSnapshot.child("喜歡的動物ID").child(String.valueOf(i+1)).getValue().toString();
                    // }
                    //  Log.d("aaa",喜歡的動物ID[0]);
                    //  Log.d("aaa",喜歡的動物ID[1]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        swipeDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {//如果有喜歡的動物了
                    save喜歡的動物數量 = (dataSnapshot.child("喜歡的動物ID").getChildrenCount());  //這裡的只會存載入畫面那時的數量,不會變動
                    save不喜歡的動物數量 = (dataSnapshot.child("不喜歡的動物ID").getChildrenCount());
                    //別理
//                    save喜歡的動物ID=new String[(int)save喜歡的動物數量];
//                     for(int i=0;i<save喜歡的動物數量;i++){
//                        save喜歡的動物ID[i]=dataSnapshot.child("喜歡的動物ID").child(String.valueOf(i+1)).getValue().toString();
//                     }
//                      Log.d("aaa",save喜歡的動物ID[0]);
//                      Log.d("aaa",save喜歡的動物ID[1]);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







        DatabaseReference read未選擇=database.getReference().child("user").child(UID).child("配對").child("未選擇");
        DatabaseReference readInfo=database.getReference().child("shelter_pet");


        未選擇的動物ID=new String[10];
        read未選擇.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               // for (int i = 0; i < 10; i++) {
                    未選擇的動物ID[0] = dataSnapshot.child("1").getValue().toString();
                     未選擇的動物ID[1] = dataSnapshot.child("2").getValue().toString();
                未選擇的動物ID[2] = dataSnapshot.child("3").getValue().toString();
                未選擇的動物ID[3] = dataSnapshot.child("4").getValue().toString();
                未選擇的動物ID[4] = dataSnapshot.child("5").getValue().toString();
                未選擇的動物ID[5] = dataSnapshot.child("6").getValue().toString();
                未選擇的動物ID[6] = dataSnapshot.child("7").getValue().toString();
                未選擇的動物ID[7] = dataSnapshot.child("8").getValue().toString();
                未選擇的動物ID[8] = dataSnapshot.child("9").getValue().toString();
                未選擇的動物ID[9] = dataSnapshot.child("10").getValue().toString();
                    Log.d("aaa", 未選擇的動物ID[0]);

                //}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        readInfo.addValueEventListener(new ValueEventListener() { //資料讀取
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                MixCount_shelter_petID = (int) (dataSnapshot.getChildrenCount()); //資料表最大值
                shelter_petID = new String[MixCount_shelter_petID];

//                shelter_petID=new String[MixCount_shelter_petID];
//                for(int i=0;i<MixCount_shelter_petID;i++){  //讓每個卡片不重複
//                    shelter_petID[i]=String.valueOf((int)(Math.random()*MixCount_shelter_petID+1));
//
//                    for(int j=0;j<i;){
//                        if(Integer.parseInt(shelter_petID[i])== Integer.parseInt(shelter_petID[j])){
//                            shelter_petID[i]=String.valueOf((int)(Math.random()* MixCount_shelter_petID+1));
//                            j=0;
//                        }
//                        else j++;
//
//                    }
//                }

                    for(int i=0;i<MixCount_shelter_petID;i++){
                     shelter_petID[i]=未選擇的動物ID[i];
                         }
//

                // Log.d("aaaa",String.valueOf((int)MixCount_shelter_petID));
                // for(String aaa:shelter_petID){
                // Log.d("aaa",aaa);}


                    Shelter_petImgLink = new String[MixCount_shelter_petID];
                Shelter_petSpecies = new String[MixCount_shelter_petID];
                Shelter_petGender = new String[MixCount_shelter_petID];
                Shelter_petColor = new String[MixCount_shelter_petID];
                petCardPetID = new String[MixCount_shelter_petID];


                for (int i = 0; i < shelter_petID.length; i++) {
                    Shelter_petImgLink[i] = dataSnapshot.child(shelter_petID[i]).child("image").getValue().toString();
                    //這邊是從資料庫獲得圖片的網址,並存到link變數
                    Log.d("link", Shelter_petImgLink[i]);  //可以看到網址
                    Shelter_petSpecies[i] = dataSnapshot.child(shelter_petID[i]).child("species").getValue().toString();
                    Shelter_petGender[i] = dataSnapshot.child(shelter_petID[i]).child("gender").getValue().toString();
                    Shelter_petColor[i] = dataSnapshot.child(shelter_petID[i]).child("color").getValue().toString();
                    petCardPetID[i] = dataSnapshot.child(shelter_petID[i]).child("s_id").getValue().toString();
                }

                //先讀資料在設定adapter
                adapter = new CardStackAdapter(addList());
                cardStackView.setLayoutManager(manager);
                cardStackView.setAdapter(adapter);
                cardStackView.setItemAnimator(new DefaultItemAnimator());




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });







        cardStackView =view.findViewById(R.id.card_stack_view);



        manager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {
                Log.d(TAG, "onCardDragging: d=" + direction.name() + " ratio=" + ratio);
            }


            @Override
            public void onCardSwiped(Direction direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager.getTopPosition() + " d=" + direction);
                if (direction == Direction.Right){ //卡片往右滑
                    // Toast.makeText(MainActivity.this, "Direction Right", Toast.LENGTH_SHORT).show();
                    countSwipe++;
                    Log.d(" countSwipeRight",String.valueOf(countSwipe));
                    SaveID=card[countSwipe];
                    Log.d(" countSwipeRightPetID",SaveID);

                    swipeDatabaseReference.child("喜歡的動物ID").child(String.valueOf(喜歡的動物數量+1)).setValue(SaveID);
                    swipeDatabaseReference.child("未選擇").child(SaveID).removeValue();


                }
                if (direction == Direction.Top){
                    // Toast.makeText(MainActivity.this, "Direction Top", Toast.LENGTH_SHORT).show();
                    countSwipe++;
                    Log.d(" countSwipeTop",String.valueOf(countSwipe));
                    SaveID=card[countSwipe];
                    Log.d(" countSwipeTopPetID",SaveID);
                }
                if (direction == Direction.Left){
                    // Toast.makeText(MainActivity.this, "Direction Left", Toast.LENGTH_SHORT).show();
                    countSwipe++;
                    Log.d(" countSwipeLeft",String.valueOf(countSwipe));
                    SaveID=card[countSwipe];
                    Log.d(" countSwipeLeftPetID",SaveID);
                    swipeDatabaseReference.child("不喜歡的動物ID").child(String.valueOf(不喜歡的動物數量+1)).setValue(SaveID);
                    swipeDatabaseReference.child("未選擇").child(SaveID).removeValue();

                }
                if (direction == Direction.Bottom){
                    // Toast.makeText(MainActivity, "Direction Bottom", Toast.LENGTH_SHORT).show();
                    countSwipe++;
                    Log.d(" countSwipeBottom",String.valueOf(countSwipe));
                    SaveID=card[countSwipe];
                    Log.d(" countSwipeBottomPetID",SaveID);
                }

                // Paginating
                if (manager.getTopPosition() == adapter.getItemCount() - 5){
                    paginate();
                }

            }

            @Override
            public void onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager.getTopPosition());
            }

            @Override
            public void onCardAppeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }

            @Override
            public void onCardDisappeared(View view, int position) {
                TextView tv = view.findViewById(R.id.item_name);
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.getText());
            }
        });

        manager.setStackFrom(StackFrom.None);
        manager.setVisibleCount(3);
        manager.setTranslationInterval(8.0f);
        manager.setScaleInterval(0.95f);
        manager.setSwipeThreshold(0.3f);
        manager.setMaxDegree(20.0f);
        manager.setDirections(Direction.FREEDOM);
        manager.setCanScrollHorizontal(true);
        manager.setSwipeableMethod(SwipeableMethod.Manual);
        manager.setOverlayInterpolator(new LinearInterpolator());

        return  view;
    }




    private void paginate() {
        List<ItemModel> old = adapter.getItems();
        List<ItemModel> baru = new ArrayList<>(addList());
        CardStackCallback callback = new CardStackCallback(old, baru);
        DiffUtil.DiffResult hasil = DiffUtil.calculateDiff(callback);
        adapter.setItems(baru);
        hasil.dispatchUpdatesTo(adapter);
    }





    private List<ItemModel> addList() {
        List<ItemModel> items = new ArrayList<>();
        card=new String[MixCount_shelter_petID];


        for(int i=0;i<MixCount_shelter_petID-(save喜歡的動物數量+save不喜歡的動物數量);i++) {
            items.add(new ItemModel(Shelter_petImgLink[i], Shelter_petSpecies[i], Shelter_petGender[i], Shelter_petColor[i]));
            card[i]=petCardPetID[i]; //第i張卡 是id多少
        }


        // items.add(new ItemModel(link, "Marpuah", "20", "Malang"));
        // items.add(new ItemModel(link, "Sukijah", "27", "Jonggol"));
        // items.add(new ItemModel(R.drawable.sample4, "Markobar", "19", "Bandung"));
        // items.add(new ItemModel(R.drawable.sample5, "Marmut", "25", "Hutan"));

        // items.add(new ItemModel(R.drawable.sample1, "Markonah", "24", "Jember"));
        // items.add(new ItemModel(R.drawable.sample2, "Marpuah", "20", "Malang"));
        // items.add(new ItemModel(R.drawable.sample3, "Sukijah", "27", "Jonggol"));
        // items.add(new ItemModel(R.drawable.sample4, "Markobar", "19", "Bandung"));
        // items.add(new ItemModel(R.drawable.sample5, "Marmut", "25", "Hutan"));
        return items;
    }

}

