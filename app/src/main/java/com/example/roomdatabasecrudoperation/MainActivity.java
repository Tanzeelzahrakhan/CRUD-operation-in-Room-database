package com.example.roomdatabasecrudoperation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.roomdatabasecrudoperation.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
RvAdapter rvAdapter;
private NoteViewModel noteViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       binding= ActivityMainBinding.inflate(getLayoutInflater());
       setContentView(binding.getRoot());
      noteViewModel=new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                      .get(NoteViewModel.class);
       binding.floating.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(MainActivity.this,DataInsertActivity.class);
               intent.putExtra("type","addMode");
               startActivityForResult(intent,1);


           }
       });
       binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
       binding.recyclerView.setHasFixedSize(true);
       rvAdapter=new RvAdapter();
       binding.recyclerView.setAdapter(rvAdapter);
       noteViewModel.getAllNote().observe(this, new Observer<List<Note>>() {
           @Override
           public void onChanged(List<Note> notes) {
               rvAdapter.submitList(notes);
           }
       });
       new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
           @Override
           public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
               return false;
           }

           @Override
           public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
               if (direction==ItemTouchHelper.RIGHT){
                   noteViewModel.delete(rvAdapter.getNote(viewHolder.getAdapterPosition()));
                   Toast.makeText(MainActivity.this, "Note Delete", Toast.LENGTH_SHORT).show();

               }
               else {
                 Intent intent=new Intent(MainActivity.this,DataInsertActivity.class);
                 intent.putExtra("type","update");
                 intent.putExtra("title",rvAdapter.getNote(viewHolder.getAdapterPosition()).getTittle());
                 intent.putExtra("desc",rvAdapter.getNote(viewHolder.getAdapterPosition()).getDescrip());
                 intent.putExtra("id",rvAdapter.getNote(viewHolder.getAdapterPosition()).getId());
                 startActivityForResult(intent,2);
               }


           }
       }).attachToRecyclerView(binding.recyclerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            String title=data.getStringExtra("title");
            String desc=data.getStringExtra("desc");
            Note note=new Note(title,desc);
            noteViewModel.insert(note);
            Toast.makeText(this, "note Added", Toast.LENGTH_SHORT).show();
        }
        else if (requestCode==2){
            String title=data.getStringExtra("title");
            String desc=data.getStringExtra("desc");
            Note note=new Note(title,desc);
            note.setId(data.getIntExtra("id",0));
            noteViewModel.update(note);
            Toast.makeText(this, "note updated", Toast.LENGTH_SHORT).show();

        }
    }

}