package com.example.mybooks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class AddBookActivity extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("books/");

    Button btnChooseImage, btnSubmitStory;
    EditText editTextTitle, editTextContent, editTextGenre, editTextAuhor, editTextYear;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    String id = UUID.randomUUID().toString();



    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        btnChooseImage = (Button)findViewById(R.id.ButtonChoosePic);
        btnSubmitStory = (Button)findViewById(R.id.ButtonSubmitStory);
        editTextContent = (EditText)findViewById(R.id.editTextContent);
        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextGenre = (EditText)findViewById(R.id.editTextGenre);
        editTextAuhor = (EditText)findViewById(R.id.editTextAuthorName);
        editTextYear = (EditText)findViewById(R.id.editTextYear);

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnSubmitStory.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString();
                String content = editTextContent.getText().toString();
                String genre = editTextGenre.getText().toString();
                String author = editTextAuhor.getText().toString();
                String year = editTextYear.getText().toString();
                if(title.matches("")){
                    Toast.makeText(getApplicationContext(), "Введите название книги", Toast.LENGTH_LONG).show();
                }else if(content.matches("")){
                    Toast.makeText(getApplicationContext(), "Напишите описание книги", Toast.LENGTH_LONG).show();
                }else{
                    myRef.child(id).child("title").setValue(title);
                    myRef.child(id).child("content").setValue(content);
                    myRef.child(id).child("genre").setValue(genre);
                    myRef.child(id).child("author").setValue(author);
                    myRef.child(id).child("year").setValue(year);
                    uploadImage();
                }
            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
        }
    }
    private void uploadImage() {
        Intent intent = new Intent(this, MainActivity.class);

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Загрузка...");
            progressDialog.show();

            StorageReference ref = storageReference.child("books/images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;
                                    String stringUrl = downloadUrl.toString();
                                    myRef.child(id).child("imgUrl").setValue(stringUrl);
                                }
                            });
                            progressDialog.dismiss();
                            Toast.makeText(AddBookActivity.this, "Загружено", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddBookActivity.this, "Не удалось "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Загружено "+(int)progress+"%");
                        }
                    });
        }else {
            myRef.child(id).child("imgUrl").setValue("null");
            startActivity(intent);
        }
    }


}