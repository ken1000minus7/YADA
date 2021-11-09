package com.kamikaze.yada.diary;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kamikaze.yada.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiaryHandler {
    User currentUser;
    Context context;

    public DiaryHandler(Context context) {
        this.context=context;
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String ok="";
        if(firebaseUser.getPhotoUrl()!=null) ok=firebaseUser.getPhotoUrl().toString();
        if(currentUser==null) currentUser=new User(firebaseUser.getUid(), firebaseUser.getDisplayName(),ok,new ArrayList<>());
    }

    public void loadData(RecyclerView recyclerView)
    {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot= task.getResult();
                    ArrayList<Diary> diaries= convertToDiary((List<HashMap<String, String>>) documentSnapshot.get("diaries"));
                    currentUser.setDiaries(diaries);
                    recyclerView.setAdapter(new DiaryListRecyclerViewAdapter(context,diaries));
                }
                else
                {
                    Toast.makeText(context, "You failed, failure", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteDiary(int position,RecyclerView recyclerView)
    {
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        DocumentReference documentReference=db.collection("users").document(currentUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
//                    currentUser.getDiaries().add(diary);
                    DocumentSnapshot documentSnapshot=task.getResult();
                    ArrayList<Diary> diaries= convertToDiary((List<HashMap<String, String>>) documentSnapshot.get("diaries"));
                    diaries.remove(position);
                    DiaryListRecyclerViewAdapter adapter=new DiaryListRecyclerViewAdapter(context,diaries);
                    recyclerView.setAdapter(adapter);
                    currentUser.setDiaries(diaries);
                    Log.d("Size", String.valueOf(currentUser.getDiaries().size()));
                    documentReference.update("diaries",diaries).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(context, "Diary deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

    public ArrayList<Diary> getDiaries()
    {
        if(currentUser !=null) return currentUser.getDiaries();
        return new ArrayList<Diary>();
    }

    public Diary getDiary(int position)
    {
        if(currentUser!=null && position<currentUser.getDiaries().size()) return currentUser.getDiaries().get(position);
        return null;
    }

    public void addDiary(Diary diary, RecyclerView recyclerView)
    {
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        DocumentReference documentReference=db.collection("users").document(currentUser.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
//                    currentUser.getDiaries().add(diary);
                    DocumentSnapshot documentSnapshot=task.getResult();
                    ArrayList<Diary> diaries= convertToDiary((List<HashMap<String, String>>) documentSnapshot.get("diaries"));
                    diaries.add(diary);
                    currentUser.setDiaries(diaries);
                    DiaryListRecyclerViewAdapter adapter=new DiaryListRecyclerViewAdapter(context,diaries);
                    recyclerView.setAdapter(adapter);
                    Log.d("Size", String.valueOf(currentUser.getDiaries().size()));
                    documentReference.update("diaries",diaries).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(context, "New diary created successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

    public void updateDiary()
    {

    }

    public ArrayList<Diary> convertToDiary(List<HashMap<String,String>> diaryContent)
    {
        ArrayList<Diary> diaries=new ArrayList<>();
        for(int i=0;i<diaryContent.size();i++)
        {
            diaries.add(new Diary(diaryContent.get(i).get("title"),diaryContent.get(i).get("description"),diaryContent.get(i).get("location"),diaryContent.get(i).get("bgImageUrl")));
        }
        return diaries;
    }
}
